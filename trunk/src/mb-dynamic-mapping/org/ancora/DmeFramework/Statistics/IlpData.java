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

import java.util.Hashtable;
import java.util.Map;
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
      mappingInstructionsMax = Integer.MIN_VALUE;
      
      blockFrequencyMin = Integer.MAX_VALUE;
      blockInstructionsMin = Integer.MAX_VALUE;
      blockIterationsMin = Integer.MAX_VALUE;
      mappingInstructionsMin = Integer.MAX_VALUE;

      // Calculate values

      int totalBlocks = monitor.getInstructionBlockPartitions().size();

      //Set<Integer> seenBlocks = new HashSet<Integer>();
            
      //int totalIterations = 0;
      int totalInstructions = 0;
      int totalMappableInstructions = 0;
      int totalIterations = 0;

      int weigthedInstructionsPerIteration = 0;


      Map<Integer,Integer> blockFrequency = new Hashtable<Integer,Integer>();
      for(InstructionBlock block : monitor.getInstructionBlockPartitions()) {
         //System.out.println(block);

         // Number of Instructions per block
         blockInstructionsMax = Math.max(blockInstructionsMax, block.getTotalInstructions());
         blockInstructionsMin = Math.min(blockInstructionsMin, block.getTotalInstructions());

         // Number of Mappable Instructions per block
         mappingInstructionsMax = Math.max(mappingInstructionsMax, block.getInstructions().length);
         mappingInstructionsMin = Math.min(mappingInstructionsMin, block.getInstructions().length);

         weigthedInstructionsPerIteration += block.getInstructions().length * block.getIterations();

         // Block Iterations
         blockIterationsMax = Math.max(blockIterationsMax, block.getIterations());
         blockIterationsMin = Math.min(blockIterationsMin, block.getIterations());

         // Total Instructions in trace
         totalInstructions += block.getTotalInstructions();

         // Total Mappable Instructions
         totalMappableInstructions += block.getInstructions().length;

         // Total Blocks in trace
         totalIterations += block.getIterations();

         // Build table for Frequency of InstructionBlocks
         int blockHash = block.getHash();
         Integer value = blockFrequency.get(blockHash);
         if(value == null) {
            //blockFrequency.put(blockHash, block.getIterations());
            blockFrequency.put(blockHash, 1);
         } else {
            //blockFrequency.put(blockHash, value+block.getIterations());
            blockFrequency.put(blockHash, value+1);
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

      // FPL10_V7 Consecutive Appearences
      //blockIterationsAvg = (double) totalIterations / (double) totalBlocks;
      
      // FPL10_V8 Weigthed Average
      blockIterationsAvg = (double) weigthedInstructionsPerIteration / (double) totalMappableInstructions;

      // Number of Block Sequences == Size of the sequence of blocks
      //mappingInstructionsAvg = (double) totalMappableInstructions / (double) totalBlocks;

      // FPL10_V7 Avg Block Size (should be called avg iteration size?)
      mappingInstructionsAvg = (double) totalInstructions / (double) totalIterations;
      
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

   public double getMappingInstructionsAvg() {
      return mappingInstructionsAvg;
   }

   public int getMappingInstructionsMax() {
      return mappingInstructionsMax;
   }

   public int getMappingInstructionsMin() {
      return mappingInstructionsMin;
   }

   /**
    * Calculated as the #total instructions / #total iterations
    * @return
    */
   public double getBlockSizeAvg() {
      return blockSizeAvg;
   }

   

   private double blockInstructionsAvg;
   private int blockInstructionsMax;
   private int blockInstructionsMin;

   private double mappingInstructionsAvg;
   private int mappingInstructionsMax;
   private int mappingInstructionsMin;

   private double blockFrequencyAvg;
   private int blockFrequencyMax;
   private int blockFrequencyMin;

   private double blockIterationsAvg;
   private int blockIterationsMax;
   private int blockIterationsMin;

   private double blockSizeAvg;

}
