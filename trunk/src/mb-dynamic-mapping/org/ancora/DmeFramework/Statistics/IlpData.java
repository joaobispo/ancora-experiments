/*
 *  Copyright 2010 Ancora Research Group.
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

package org.ancora.DmeFramework.Statistics;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ancora.DmeFramework.DataHolders.InstructionBlock;
import org.ancora.DmeFramework.System.MicroBlazeRpuMonitor;

/**
 *
 * @author Joao Bispo
 */
class IlpData {

   IlpData(MicroBlazeRpuMonitor monitor) {
      // Initialize Values
      blockFrequencyMax = Integer.MIN_VALUE;
      blockInstructionsMax = Integer.MIN_VALUE;
      blockIterationsMax = Integer.MIN_VALUE;
      
      blockFrequencyMin = Integer.MAX_VALUE;
      blockInstructionsMin = Integer.MAX_VALUE;
      blockIterationsMin = Integer.MAX_VALUE;

      // Calculate values

      int numberOfBlockSequences = monitor.getInstructionBlockPartitions().size();

      //Set<Integer> seenBlocks = new HashSet<Integer>();
            
      //int totalIterations = 0;
      int totalInstructions = 0;
      int totalBlocks = 0;
      Map<Integer,Integer> blockFrequency = new Hashtable<Integer,Integer>();
      for(InstructionBlock block : monitor.getInstructionBlockPartitions()) {
         //System.out.println(block);

         // Number of Instructions per block
         blockInstructionsMax = Math.max(blockInstructionsMax, block.getInstructions().length);
         blockInstructionsMin = Math.min(blockInstructionsMin, block.getInstructions().length);
         
         // Block Iterations
         blockIterationsMax = Math.max(blockIterationsMax, block.getIterations());
         blockIterationsMin = Math.min(blockIterationsMin, block.getIterations());

         // Total Instructions in trace
         totalInstructions += block.getTotalInstructions();

         // Total Blocks in trace
         totalBlocks += block.getIterations();

         // Build table for Frequency of InstructionBlocks
         int blockHash = block.getHash();
         Integer value = blockFrequency.get(blockHash);
         if(value == null) {
            blockFrequency.put(blockHash, block.getIterations());
         } else {
            blockFrequency.put(blockHash, value+block.getIterations());
         }

      }

      int numberOfDifferentBlocks = blockFrequency.size();
      // Calculate block frequencies
      for(Integer frequency : blockFrequency.values()) {
         blockFrequencyMax = Math.max(blockFrequencyMax, frequency);
         blockFrequencyMin = Math.min(blockFrequencyMin, frequency);
      }

      
      
      // Calculate averages
      blockInstructionsAvg = (double) totalInstructions / (double) totalBlocks;
      blockFrequencyAvg = (double) totalBlocks / (double) numberOfDifferentBlocks;
      blockIterationsAvg = (double) totalBlocks / (double) numberOfBlockSequences;
      // Number of Block Sequences == Size of the sequence of blocks
      
   }

   public double getBlockFrequencyAvg() {
      return blockFrequencyAvg;
   }

   public int getBlockFrequencyMax() {
      return blockFrequencyMax;
   }

   public int getBlockFrequencyMin() {
      return blockFrequencyMin;
   }

   public double getBlockInstructionsAvg() {
      return blockInstructionsAvg;
   }

   public int getBlockInstructionsMax() {
      return blockInstructionsMax;
   }

   public int getBlockInstructionsMin() {
      return blockInstructionsMin;
   }

   public double getBlockIterationsAvg() {
      return blockIterationsAvg;
   }

   public int getBlockIterationsMax() {
      return blockIterationsMax;
   }

   public int getBlockIterationsMin() {
      return blockIterationsMin;
   }

   

   private double blockInstructionsAvg;
   private int blockInstructionsMax;
   private int blockInstructionsMin;

   private double blockFrequencyAvg;
   private int blockFrequencyMax;
   private int blockFrequencyMin;

   private double blockIterationsAvg;
   private int blockIterationsMax;
   private int blockIterationsMin;

}
