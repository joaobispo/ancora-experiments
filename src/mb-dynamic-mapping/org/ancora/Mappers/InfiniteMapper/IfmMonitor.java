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

package org.ancora.Mappers.InfiniteMapper;


/**
 *
 * @author Joao Bispo
 */
public class IfmMonitor {

   public IfmMonitor() {
      totalCcaLines = 0;
      totalInstructions = 0;
      totalMappedInstructions = 0;
      totalLiveIn = 0;
      totalLiveOut = 0;
      totalMappedBlocks = 0;
      totalMoveInstructions = 0;
      //totalIlp = 0;
      maxIlp = 0;
   maxMappedLines = 0;
   maxLiveIn = 0;
   maxLiveOut = 0;

   immInstructions = 0;
   }

 

   public String getStats() {
      StringBuilder builder = new StringBuilder();

      builder.append("Total cycles:"+getTotalCycles()+"\n");
      //builder.append("Total Move Operations:"+totalMoveInstructions);
      builder.append("Averages:\n");
      builder.append("Live-Ins Per Block (Average):"+getLiveInPerBlockAverage()+"\n");
      builder.append("Live-Outs Per Block (Average):"+getLiveOutPerBlockAverage()+"\n");
      builder.append("Mapped Lines Per Block (Average):"+getMappedLinesPerBlockAverage()+"\n");
      //builder.append("ILP Per Block (Average):"+getIlpPerBlockAverage()+"\n");
      builder.append("ILP (Average):"+getIlpPerBlockAverage2()+"\n");
      builder.append("Speed-Up (Average):"+getSpeedupAverage()+"\n");
      builder.append("Maximums:\n");
      builder.append("Live-Ins Per Block (Max):"+maxLiveIn+"\n");
      builder.append("Live-Outs Per Block (Max):"+maxLiveOut+"\n");
      builder.append("Mapped Lines Per Block (Max):"+maxMappedLines+"\n");
      builder.append("ILP (Max):"+maxIlp+"\n");
      if(totalMoveInstructions > 0) {
         builder.append("Special:\n");
         builder.append("MOVE Instructions (Average):"+getMovesPerBlockAverage()+"\n");
         builder.append("Not Move Instructions (Average):"+getInstructionsPerBlockAverage()+"\n");
         builder.append("Normal Instructions per Move Inst. (Average):"+getNormalInstructionsPerMoveInstructions()+"\n");
      }

      return builder.toString();
   }

   public float getLiveInPerBlockAverage() {
      return (float)totalLiveIn / (float)totalMappedBlocks;
   }

   public float getLiveOutPerBlockAverage() {
      return (float)totalLiveOut / (float)totalMappedBlocks;
   }

   public float getMappedLinesPerBlockAverage() {
      return (float)totalCcaLines / (float)totalMappedBlocks;
   }

   /*
   public float getIlpPerBlockAverage() {
      return totalIlp / (float)totalMappedBlocks;
   }
    */

   public float getIlpPerBlockAverage2() {
      //return (float)totalMappedInstructions / (float)totalCcaLines;
      return (float)getMappedNotMoveInstructions() / (float)totalCcaLines;
   }

   public float getSpeedupAverage() {
      //System.out.println("Total Inst:"+totalInstructions);
      //System.out.println("Total CCALines:"+totalCcaLines);
      return  (float)totalInstructions/(float)totalCcaLines;
   }

   public float getMovesPerBlockAverage() {
      return (float)totalMoveInstructions / (float)totalMappedBlocks;
   }

   public float getInstructionsPerBlockAverage() {
      //return (float)totalMappedInstructions / (float) totalMappedBlocks;
      return (float)getMappedNotMoveInstructions() / (float) totalMappedBlocks;
   }

   public int getMappedNotMoveInstructions() {
      //return totalMappedInstructions-totalMoveInstructions;
      return totalMappedInstructions;
   }

   public float getNormalInstructionsPerMoveInstructions() {
      return (float)getMappedNotMoveInstructions()/(float)totalMoveInstructions;
   }

   public int getTotalCycles() {
      return totalCcaLines;
   }

   /**
    * INSTANCE VARIABLES
    */
   public int totalLiveIn;
   public int totalLiveOut;
   public int totalInstructions;
   public int totalMappedBlocks;
   public int totalMappedInstructions;
   public int totalCcaLines;
   public int totalMoveInstructions;
   //public float totalIlp;

   public float maxIlp;
   public int maxMappedLines;
   public int maxLiveIn;
   public int maxLiveOut;

   public int immInstructions;
}
