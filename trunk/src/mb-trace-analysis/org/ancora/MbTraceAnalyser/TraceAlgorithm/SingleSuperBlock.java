/*
 *  Copyright 2009 Ancora Research Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.ancora.MbTraceAnalyser.TraceAlgorithm;

import org.ancora.MbTraceAnalyser.DataObjects.TraceFlow;
import org.ancora.MbTraceAnalyser.DataObjects.InstructionFlow;
import org.ancora.MbTraceAnalyser.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.BitUtils;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.IoUtils2;
import org.ancora.SharedLibrary.ParseUtils2;

/**
 * Builds a trace-flow from a trace.
 *
 * @author Joao Bispo
 */
public class SingleSuperBlock {

   private static void reset() {
      totalInstructions = 0;
      instructionsInLoops = 0;
      loopsIterations = 0;
      numberOfLoops = 0;
   }

   private static TraceFlow exit(TraceFlow result) {
      reset();
      return  result;
   }


   private static boolean saveOutput(InstructionFlow instructionFlow, File outputFile) {
     // Gathers statistics
      if (instructionFlow.getRecurrence() > 1) {
         
         instructionsInLoops += (instructionFlow.getTotalInstructions());
         loopsIterations += instructionFlow.getRecurrence();
         numberOfLoops++;
      }

      // Test addresses of instruction flow, to check if they all are forward jumps.
      for(int i=1; i<instructionFlow.getSize(); i++) {
         long firstInst = instructionFlow.getAddress(i-1);
         long secondInst = instructionFlow.getAddress(i);

         if(firstInst > secondInst) {
            String firstInstString = ParseUtils2.toHexString(firstInst, 8);
            String secondInstString = ParseUtils2.toHexString(secondInst, 8);

            Logger.getLogger(SingleSuperBlock.class.getName()).
                    warning("Violated Forward Branch. "+firstInstString+"->"+secondInstString);
         }
      }
      totalInstructions += instructionFlow.getTotalInstructions();
      return IoUtils2.append(outputFile, instructionFlow.toString());
   }

   /**
    * Builds a TraceFlow from the trace in traceFile.
    *
    * @param traceFile the trace file
    * @param outputFile the output file
    * @return a TraceFlow if operation is successful, null otherwise.
    */
   public static TraceFlow doTraceFlow(File traceFile, File outputFile) {
      // Open trace file
      MicroblazeTraceReader reader = MicroblazeTraceReader.createTraceReader(traceFile);

      // Delete contents of outputfile
      IoUtils.write(outputFile, "");

      Map<Integer, InstructionFlow> sequenceTable = new HashMap<Integer, InstructionFlow>();

      InstructionFlow currentTrace = new InstructionFlow();
      MicroblazeTraceInstruction currentInst = reader.nextInstruction();
      MicroblazeTraceInstruction nextInst = reader.nextInstruction();

      long firstInstructionAddress = currentInst.getInstructionAddress();
      currentTrace.addAddress(firstInstructionAddress);
      int hash = BitUtils.superFastHash(firstInstructionAddress, HASH_INITIAL_VALUE); // Number of bytes of instructions (32 bits)

      List<Integer> superblockFlow = new ArrayList<Integer>();

      while(nextInst != null) {
         currentTrace.incrementInstruction();

         // Check if instruction is a branch
         if(currentInst.isBranch()) {
            long nextInstAddress = nextInst.getInstructionAddress();

            // Check if branch has a delay slot
            if(currentInst.hasDelaySlot()) {
               currentTrace.incrementInstruction();
               nextInst = reader.nextInstruction();
               nextInstAddress = nextInst.getInstructionAddress();
            }

            // Check if instruction is a forward branch
            boolean forwardBranch = currentInst.getInstructionAddress() < nextInstAddress;

            if(forwardBranch) {
               // Add address to current trace
               currentTrace.addAddress(nextInstAddress);

               // Recalculate the hash
               hash = BitUtils.superFastHash(nextInstAddress, hash);

            }
            else { // Is a backward branch
               // Check collisions in hash
               updateTable(currentTrace, hash, sequenceTable);

               // Save superblock to file
               //saveOutput(currentTrace, outputFile);

               // Save superblock in superblock list
               superblockFlow.add(hash);

               currentTrace = new InstructionFlow();
               currentTrace.addAddress(nextInstAddress);
               // Reset hash
               hash = hash = BitUtils.superFastHash(nextInstAddress, HASH_INITIAL_VALUE);
            }

         }



         currentInst = nextInst;
         nextInst = reader.nextInstruction();
      }

      // Last Instruction
      currentTrace.incrementInstruction();

      if (currentTrace != null) {
         // Save current trace to file
         updateTable(currentTrace, hash, sequenceTable);
         // Save superblock in superblock list
         superblockFlow.add(hash);
      }

      // Build TraceFlow
      return exit(new TraceFlow(sequenceTable, superblockFlow));
   }

   /**
    * Updates table, and checks if there is a collision.
    *
    * @param currentTrace
    * @param hash
    * @param sequenceTable
    */
   private static void updateTable(InstructionFlow currentTrace, int hash, Map<Integer, InstructionFlow> sequenceTable) {
      // Check if hash is already in table
      boolean containsSequence = sequenceTable.containsKey(hash);

      // If does not contain sequence, add it to the table and return
      if(!containsSequence) {
         sequenceTable.put(hash, currentTrace);
         return;
      }

      // Check if sequence in table is the same as the one to put inside
      InstructionFlow tableInstructionFlow = sequenceTable.get(hash);

      boolean areEqual = tableInstructionFlow.compare(currentTrace);
      if(!areEqual) {
         // There was a collision
         Logger.getLogger(SingleSuperBlock.class.getName()).
                 warning("There was a hash collision between instruction flows:\n" +
                 "Previous flow:"+tableInstructionFlow.getAddressesArray() + "\n" +
                 "Current flow:"+currentTrace.getAddressesArray() + "\n" +
                 "Hash:"+hash
                 );

      }

   }

   ///
   // INSTANCE VARIABLE
   ///

   // Statistics
   private static int totalInstructions = 0;
   private static int instructionsInLoops = 0;
   private static int loopsIterations = 0;
   private static int numberOfLoops = 0;

   // Constants
   private static final int HASH_INITIAL_VALUE = 4;


}
