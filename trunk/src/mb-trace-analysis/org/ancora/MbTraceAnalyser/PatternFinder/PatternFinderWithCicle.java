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

package org.ancora.MbTraceAnalyser.PatternFinder;

import org.ancora.MbTraceAnalyser.DataObjects.TraceFlow;
import org.ancora.MbTraceAnalyser.DataObjects.InstructionFlow;
import org.ancora.MbTraceAnalyser.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.BitUtils;
import org.ancora.SharedLibrary.DataStructures.RotatingQueue;
import org.ancora.SharedLibrary.IoUtils2;

/**
 *
 * @author Joao Bispo
 */
public class PatternFinderWithCicle {

   private static void saveOutput(List<Integer> hashPattern, int matchSize, File outputFile, Map<Integer, InstructionFlow> table) {
      // Found loop?
      boolean foundLoop;
      if (matchSize > 1) {
         foundLoop = true;
      } else {
         foundLoop = false;
      }

      if (foundLoop) {
         String prefix = "---- Loop with " + matchSize + " iterations ----\n";
         IoUtils2.append(outputFile, prefix);
      }

      for (Integer hash : hashPattern) {
         saveOutput(hash, outputFile, table);
         totalInstructions += (matchSize-1)*table.get(hash).getTotalInstructions();

         if(foundLoop) {
            instructionsInLoops += (matchSize)*table.get(hash).getTotalInstructions();
            totalLoops += 1;
            totalIterations += matchSize;
         }
      }

      if (foundLoop) {
         String sufix = "---- End Loop  ----\n";
         IoUtils2.append(outputFile, sufix);
      }
   }


   private static void saveOutput(Integer hash, File outputFile, Map<Integer, InstructionFlow> table) {
      InstructionFlow flow = table.get(hash);
      IoUtils2.append(outputFile, flow.toString()+"\n");
      totalInstructions += flow.getTotalInstructions();
   }

   private static String buildStats() {
      float rationInstLoopsTotalInsts = (float) instructionsInLoops / (float) totalInstructions;
      float instPerLoop = (float) instructionsInLoops / (float) totalLoops;
      float loopSize = instPerLoop / (float) totalIterations;
      float iterationsPerLoop = (float) totalIterations / (float) totalLoops;
      
      StringBuilder builder = new StringBuilder();
      
      builder.append("Total Instructions:"+totalInstructions+"\n");
      builder.append("Ratio Inst in Loop / Total Inst:"+rationInstLoopsTotalInsts+"\n");
      builder.append("Instructions per loop:"+instPerLoop+"\n");
      builder.append("Average Loop Size:"+loopSize+"\n");
      builder.append("Iterations per loop:"+iterationsPerLoop+"\n");
      builder.append("Number of loop:"+totalLoops);

      return builder.toString();
//      float rationInstLoopsTotalInsts = (float) instructionsInLoops / (float) totalInstructions;
//      System.out.println("Total Instructions:"+totalInstructions);
//      System.out.println("Ratio Inst in Loop / Total Inst:"+rationInstLoopsTotalInsts);
   }

   private static void saveStats(File outputFile) {
      /*
      float rationInstLoopsTotalInsts = (float) instructionsInLoops / (float) totalInstructions;
      float instPerLoop = (float) totalInstructions / (float) totalLoops;
      float interationsPerLoop = (float) totalIterations / (float) totalLoops;

      StringBuilder builder = new StringBuilder();

      builder.append("Total Instructions:"+totalInstructions);
      builder.append("Ratio Inst in Loop / Total Inst:"+rationInstLoopsTotalInsts);
      */
      IoUtils2.append(outputFile, "\n"+buildStats());
   }

   public PatternFinderWithCicle(int patternSize) {
     // Check Pattern Size
      if(patternSize > 32) {
         Logger.getLogger(PatternFinderWithCicle.class.getName()).
                 warning("Maximum window size is 32 at this momment. Setting size" +
                 " to 32.");
         patternSize = 32;
      }

      windowSize = patternSize + 1;
      matchQueues = new int[windowSize - 1];
      queue = new RotatingQueue<Integer>(windowSize);

      // Initiallize Queue
      for(int i=0; i<windowSize; i++) {
         queue.insertElement(0);
      }
   }

   public static void patternFinder(TraceFlow traceFlow, File outputFile, int windowSize) {

      List<Integer> hashes = traceFlow.getFlow();
      Map<Integer, InstructionFlow> table = traceFlow.getSequenceTable();
      PatternFinderWithCicle patternFinder = new PatternFinderWithCicle(windowSize);
      resetStats();

      PatternFinderState state = PatternFinderState.LOOKING_FOR_PATTERN;

      List<Integer> hashPattern = new ArrayList<Integer>();
      int builtPatternSize = 1;
      int repetitions = 1;
      int matchingIndex = 0;

      int insts = 0;

      for(Integer hash : hashes) {
         int patternSize = patternFinder.findPattern(hash);
         System.out.println("pattern size: "+patternSize+" for hash "+hash);
         //System.out.println("Value of matchsize before switch:"+repetitions);
         switch(state) {
            
            case LOOKING_FOR_PATTERN:
               saveOutput(hash, outputFile, table);
               if(patternSize != 0) {
                  // Found pattern! Next hash may start a pattern
                  //System.out.println("Found Pattern.");
                  state = PatternFinderState.BUILDING_PATTERN;
                  builtPatternSize = patternSize;
                  hashPattern = new ArrayList<Integer>(patternSize);
               }
               break;

            case BUILDING_PATTERN:
               // Add hash to pattern.
               hashPattern.add(hash);

               //System.out.println("Building pattern... "+hashPattern.size()+" of "+builtPatternSize);
               //Check if this is the last element to add to pattern
               if(hashPattern.size() == builtPatternSize) {
                  state = PatternFinderState.MATCHING_PATTERN;
                  repetitions = 1;
                  matchingIndex = 0;
                  //System.out.println("Build Pattern:"+hashPattern);
               }
               break;

            case MATCHING_PATTERN:
               //System.out.println("MatchingIndex:"+matchingIndex);
               //System.out.println("Matching: list("+hashPattern.get(matchingIndex)+") vs hash("+hash+")");
               //boolean mismatch = hashPattern.get(matchingIndex) != hash;
               //System.out.println("mismatch:"+mismatch);

               //boolean mismatch2 = hashPattern.get(matchingIndex).intValue() != hash;
               //System.out.println("mismatch2:"+mismatch2);
               // Check if we are still inside the pattern
               if(hashPattern.get(matchingIndex).intValue() != hash) {
                  //System.out.println("Pattern Broke: MatchSize "+repetitions);
                  // Pattern broke
                  // Save found patterns
                  saveOutput(hashPattern, repetitions, outputFile, table);
                  // Save patterns until matchingIndex
                  saveOutput(hashPattern.subList(0, matchingIndex), 1, outputFile, table);
                  // Save single superblock
                  saveOutput(hash, outputFile, table);

                  state = PatternFinderState.LOOKING_FOR_PATTERN;
               }
               else {
                  matchingIndex += 1;
                  // Check matching index to know if we got to the end of the pattern
                  if (matchingIndex == hashPattern.size()) {
                     repetitions = repetitions + 1;
                     matchingIndex = 0;
                     //System.out.println("matchsize was incremented");
                  }
               }
               break;

         }
         //System.out.println("Value of matchsize after switch:"+repetitions);

         // Count instructions in each hash, for checking
         //insts += table.get(hash).getTotalInstructions();
      }

      // Show Stats
      System.out.println(buildStats());
      saveStats(outputFile);
      //System.out.println("Total Instructions (checker):"+insts);
   }

   /**
    * By iteratively feeding this method, it will try to find patterns in the
    * given numbers. If a pattern is found, returns the size of the pattern.
    * 
    * @param number
    * @return
    */
   public int findPattern(int number) {
      // Insert new element
      queue.insertElement(number);

      // Compare first element with all other elements and store result on
      // match queues
      for (int i = 0; i < windowSize - 1; i++) {
         // Shift match queue
         matchQueues[i] <<= 1;
         // Calculate match
         if (number == queue.getElement(i + 1)) {
            // We have a match. Set the bit.
            matchQueues[i] = BitUtils.setBit(0, matchQueues[i]);
         } else {
            // Reset queue
            matchQueues[i] = 0;
         }
      }

      // Check if there is a pattern
      // Look if bit position is set, only first encountered matters
      int bitIndex = windowSize - 1 - 1;
      for (int i = 0; i < matchQueues.length; i++) {
         //if (BitUtils.getBit(bitIndex, matchQueues[i]) > 0) {
         // Instead of using the same bitIndex for every queue, look at the bit
         // the same size as the pattern
         if (BitUtils.getBit(i, matchQueues[i]) > 0) {
            // There was a match!
            return i + 1;
         }
      }


      return 0;
   }

   public static void findPatterns(List<Integer> hashes) {
      // It works up to 33, right now.
      int windowSize = 9+1;

      // State Queues
      int[] matchQueues = new int[windowSize-1];

      // Initiallize Queue
      RotatingQueue<Integer> queue = new RotatingQueue<Integer>(windowSize);
      for(int i=0; i<windowSize; i++) {
         queue.insertElement(0);
      }

      // Get list of hashes, where we want to find patterns
      //List<Integer> hashes = traceFlow.getFlow();
      int hashIndex = 0;
      int hash = hashes.get(hashIndex);
      hashIndex++;

      while(hashIndex < hashes.size()) {
         queue.insertElement(hash);

         // Compare first element with all other elements and store result on
         // match queues
         for(int i=0; i<windowSize-1; i++) {
            // Shift match queue
            matchQueues[i] <<= 1;
            // Calculate match
            if(hash == queue.getElement(i+1)) {
               // We have a match. Set the bit.
               matchQueues[i] = BitUtils.setBit(0, matchQueues[i]);
            } else {
               // Reset queue
               matchQueues[i] = 0;
            }
         }

            // Check if there is a pattern
            // Look if bit position is set, only first encountered matters
            int bitIndex = windowSize-1-1;
            for(int i=0; i<matchQueues.length; i++) {
               if (BitUtils.getBit(bitIndex, matchQueues[i]) > 0) {
                  // There was a match!
                  System.out.println("Match of size " + (i + 1) + " at iteration " + hashIndex);
                  // Stop Looking
                  break;
                  // Reset queues
                  //matchQueues = new int[windowSize-1];
               }
            }


         // Show Match Queues
         /*
         System.out.println("---Iteration:"+hashIndex+"-----");
         for(int i=0; i<matchQueues.length; i++) {
            String binaryString = Integer.toBinaryString(matchQueues[i]);
            binaryString = BitUtils.padBinaryString(binaryString, windowSize-1);
            System.out.println("Queue "+(i+1)+":"+binaryString);
         }
          */
         

         // Advance to next element
         hash = hashes.get(hashIndex);
         hashIndex += 1;
      }
   }


//    private boolean saveOutput(List<InstructionFlow> instructionFlows, File outputFile) {
     // Gathers statistics
       /*
      if (instructionFlows.size() > 1) {

         instructionsInLoops += (instructionFlow.getTotalInstructions());
         loopsIterations += instructionFlow.getRecurrence();
         numberOfLoops++;
      }
        */

      // Test addresses of instruction flow, to check if they all are forward jumps.
      /*
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
       */
//      totalInstructions += instructionFlow.getTotalInstructions();
//      return IoUtils2.append(outputFile, instructionFlow.toString());
//   }

   private static void resetStats() {
      totalInstructions = 0;
      instructionsInLoops = 0;
      totalLoops = 0;
      totalIterations = 0;
   }

   enum PatternFinderState {
      LOOKING_FOR_PATTERN,
      BUILDING_PATTERN,
      MATCHING_PATTERN;
   }

   // INSTANCE VARIABLES
   private int[] matchQueues;
   private RotatingQueue<Integer> queue;
   private int windowSize;

   // Stats
   private static int totalInstructions;
   private static int instructionsInLoops;
   private static int totalLoops;
   private static int totalIterations;
}
