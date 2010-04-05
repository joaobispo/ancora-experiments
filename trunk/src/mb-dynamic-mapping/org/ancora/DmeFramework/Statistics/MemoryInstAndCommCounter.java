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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;
import org.ancora.DmeFramework.DataHolders.InstructionBlock;
import org.ancora.DmeFramework.DataHolders.RpuExecution;
import org.ancora.DmeFramework.System.MicroBlazeRpuMonitor;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.MicroBlaze.Instructions.InstructionName;
import org.ancora.SharedLibrary.IoUtils;

/**
 *
 * @author Joao Bispo
 */
public class MemoryInstAndCommCounter {

   public MemoryInstAndCommCounter() {
      dataSets = new ArrayList<CoverageData>();
   }

   public void addData(String trace, String partition, MicroBlazeRpuMonitor monitor) {
      // Create new object to store the data
      CoverageData data = new CoverageData(trace, partition);

      // Prepare list with RpuExecutions
      //List<InstructionBlock> instructionBlocks = new ArrayList(monitor.getInstructionBlockPartitions());

      // Build list with iterations
      //SortedSet<Integer> iterationsSet = new TreeSet<Integer>();
      List<InstructionBlock> iBlocks = monitor.getInstructionBlockPartitions();
      List<RpuExecution> rpuExecs = monitor.getRpuExecutions();

      // Check sizes
      if(iBlocks.size() != rpuExecs.size()) {
         Logger.getLogger(MemoryInstAndCommCounter.class.getName()).
                 warning("Size of InstructionBlock list ("+iBlocks.size()+") different than size " +
                 "of RpuExecutionList ("+rpuExecs.size()+").");
         return;
      }


      for(int i=0; i<iBlocks.size(); i++) {
         data.addBlock(iBlocks.get(i), rpuExecs.get(i));
      }

      dataSets.add(data);

   }

   public void saveData(File memAndCommFile) {
      StringBuilder builder = new StringBuilder();

      // Empty column + categories
      for(Category cat : Category.values()) {
        builder.append(",");
         builder.append(cat.toString());
      }
      builder.append("\n");

      for(CoverageData dataSet : dataSets) {
         builder.append(dataSet.getName());

         String[] resultsArray = dataSet.getResultsArray();
         for(String result : resultsArray) {
            builder.append(",");
            builder.append(result);
         }
         builder.append("\n");
      }

      IoUtils.write(memAndCommFile, builder.toString());
   }

   enum Category {
      loads,
      stores,
      totalMem,
      totalInst,
      memOverInst,
      liveIns,
      liveOuts,
      totalComm
   }

 
   /**
    * INSTANCE VARIABLES
    */
   private List<CoverageData> dataSets;


   public static final EnumSet<InstructionName> isLoad = EnumSet.of(
              InstructionName.lhu,
              InstructionName.lhui,
              InstructionName.lw,
              InstructionName.lwi,
              InstructionName.lbu,
              InstructionName.lbui);

   public static final EnumSet<InstructionName> isStore = EnumSet.of(
              InstructionName.sb,
              InstructionName.sbi,
              InstructionName.sh,
              InstructionName.shi,
              InstructionName.sw,
              InstructionName.swi);




   class CoverageData {

      public CoverageData(String trace, String partitioner) {
         this.trace = trace;
         this.partitioner = partitioner;
         totalInstructions = 0;
         totalLoads = 0;
         totalStores = 0;

         liveIns = new ArrayList<Integer>();
         liveOuts = new ArrayList<Integer>();
      }

      private void addBlock(InstructionBlock block, RpuExecution rpuExecution) {
         int blockInstructions = block.getTotalInstructions();

         totalInstructions += blockInstructions;
         for(Instruction inst : block.getInstructions()) {
            if(isLoad.contains(inst.getOperation())) {
               totalLoads+=block.getIterations();
               continue;
            }
            
            if(isStore.contains(inst.getOperation())) {
               totalStores+=block.getIterations();
               continue;
            }
         }

         liveIns.add(rpuExecution.getLiveIn());
         liveOuts.add(rpuExecution.getLiveOut());

      }


      String getName() {
         return trace+"-"+partitioner;
      }


   private double listAverage(List<Integer> sizes) {
      int acc = 0;
      for(Integer value : sizes) {
         acc += value;
      }

      return (double)acc / (double)sizes.size();
   }

      private String[] getResultsArray() {
         String[] results = new String[Category.values().length];

         int totalMem = totalLoads+totalStores;
         double memOverInst = (double) totalMem / (double) totalInstructions;
         double liveInsAvg = listAverage(liveIns);
         double liveOutsAvg = listAverage(liveOuts);
         
         
         double totalComm = liveInsAvg+liveOutsAvg;

         results[Category.loads.ordinal()] = String.valueOf(totalLoads);
         results[Category.stores.ordinal()] = String.valueOf(totalStores);
         results[Category.totalMem.ordinal()] = String.valueOf(totalMem);
         results[Category.totalInst.ordinal()] = String.valueOf(totalInstructions);
         results[Category.memOverInst.ordinal()] = String.valueOf(memOverInst);

         results[Category.liveIns.ordinal()] = String.valueOf(liveInsAvg);
         results[Category.liveOuts.ordinal()] = String.valueOf(liveOutsAvg);
         results[Category.totalComm.ordinal()] = String.valueOf(totalComm);


         return results;
      }




      // INSTANCE VARIABLES
      String trace;
      String partitioner;
      int totalInstructions;
      int totalLoads;
      int totalStores;

      List<Integer> liveIns;
      List<Integer> liveOuts;

   }

}
