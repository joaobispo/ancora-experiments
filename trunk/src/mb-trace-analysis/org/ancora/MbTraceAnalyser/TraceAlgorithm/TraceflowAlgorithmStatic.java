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

import org.ancora.MbTraceAnalyser.*;
import java.io.File;
import org.ancora.SharedLibrary.IoUtils;

/**
 * Builds a trace-flow from a trace.
 *
 * @author Joao Bispo
 */
public class TraceflowAlgorithmStatic {

   private static void reset() {
      totalInstructions = 0;
      outputFile = null;

      recurringTrace = null;
      currentTrace = null;

      currentInst = null;
      nextInst = null;
   }

   private static boolean exit(boolean result) {
      reset();
      return result;
   }

   private static void saveRecurringFlow() {
      boolean result = saveOutput(currentTrace);
      recurringTrace = null;
   }

   private static void saveCurrentFlow() {
      boolean result = saveOutput(currentTrace);
      currentTrace = null;
   }

   private static boolean saveOutput(InstructionFlow instructionFlow) {
      totalInstructions += instructionFlow.getTotalInstructions();
      return IoUtils.append(outputFile, instructionFlow.toString());
   }

   /**
    * Builds the trace-flow from the trace in traceFile, writes the results to
    * outputFile.
    *
    * @param traceFile the trace file
    * @param outputFile the output file
    * @return true if operation is successful, false otherwise.
    */
public static boolean doTraceFlow(File traceFile, File outputFile) {
   // Open trace file
   MicroblazeTraceReader reader = MicroblazeTraceReader.createTraceReader(traceFile);

   // Delete contents of outputfile
   IoUtils.write(outputFile, "");

   InstructionFlow recurringTrace = null;
      InstructionFlow currentTrace = new InstructionFlow();
      MicroblazeTraceInstruction currentInst = reader.nextInstruction();
      MicroblazeTraceInstruction nextInst = reader.nextInstruction();

      currentTrace.addAddress(currentInst.getInstructionAddress());

      while(nextInst != null) {
         currentTrace.incrementInstruction();

         // Check if instruction is a branch
         if(currentInst.isBranch()) {
            long nextInstAddress = nextInst.getInstructionAddress();
            // Check if instruction is a forward branch
            boolean forwardBranch = currentInst.getInstructionAddress() < nextInstAddress;
            if(forwardBranch) {
               // Add address to current trace
               currentTrace.addAddress(nextInstAddress);

               // Check recurrence trace
               if(recurringTrace != null) {
                  // check if current trace does not follow recurrent trace
                  int index = currentTrace.getSize() - 1;
                  if(!recurringTrace.isSameAddress(nextInstAddress, index)) {
                     // Save recurring trace to file
                     saveOutput(recurringTrace);
                     // Reset recurring trace
                     recurringTrace = null;
                  }
               }
            }
            else { // Is a backward branch
               // Check if next inst address is the same as the address of
               // the current trace
               boolean sameStartAddress = nextInstAddress == currentTrace.getAddress(0);
               if(sameStartAddress) {
                  if(recurringTrace == null) {
                     recurringTrace = currentTrace;
                  } else {
                     recurringTrace.incrementRecurrence();
                  }
               }
               else {
                  if(recurringTrace != null) {
                     // Save recurring trace to file
                     totalInstructions += recurringTrace.getTotalInstructions();
                     IoUtils.append(outputFile, recurringTrace.toString());
                     recurringTrace = null;
                  }
                  // Save current trace to file
                  totalInstructions += currentTrace.getTotalInstructions();
                  IoUtils.append(outputFile, currentTrace.toString());
                  currentTrace = null;
               }

               currentTrace = new InstructionFlow();
               currentTrace.addAddress(nextInstAddress);
            }

         }



         currentInst = nextInst;
         nextInst = reader.nextInstruction();
      }

      if(recurringTrace != null) {
         totalInstructions += recurringTrace.getTotalInstructions();
         IoUtils.append(outputFile, recurringTrace.toString());
      }

      if(currentTrace != null) {
         totalInstructions += currentTrace.getTotalInstructions();
         IoUtils.append(outputFile, currentTrace.toString());
      }



   return false;
}

   ///
   // INSTANCE VARIABLE
   ///
   private static int totalInstructions = 0;

   private static InstructionFlow recurringTrace = null;
   private static InstructionFlow currentTrace = null;
   private static MicroblazeTraceInstruction currentInst = null;
   private static MicroblazeTraceInstruction nextInst = null;

   private static File outputFile = null;

}
