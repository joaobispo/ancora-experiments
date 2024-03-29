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

package org.ancora.DmeFramework.System;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.ancora.DmeFramework.DataHolders.InstructionBlock;
import org.ancora.DmeFramework.DataHolders.MicroBlazeRpuExecutionHistory;
import org.ancora.DmeFramework.DataHolders.RpuExecution;
import org.ancora.DmeFramework.Interfaces.MapperMonitor;

/**
 * Collects data for a MicroBlaze-RPU system.
 *
 * @author Joao Bispo
 */
public class MicroBlazeRpuMonitor {

   public MicroBlazeRpuMonitor(float traceCpi) {
      this.traceCpi = traceCpi;
      this.rpuExecutions = new ArrayList<RpuExecution>();
      this.instructionBlockPartitions = new ArrayList<InstructionBlock>();

      totalMicroBlazeInstructions = 0;
      executionHistory = new MicroBlazeRpuExecutionHistory(new SimpleRpuCommModel());

   }

   public void incrementTotalMicroBlazeInstructions(int instructions) {
      totalMicroBlazeInstructions += instructions;
   }

   public void addMicroBlazeExecution(InstructionBlock instructionBlock) {
      // Add block to list
      instructionBlockPartitions.add(instructionBlock);
      
      
      // Check
      if(instructionBlock.mapToHardware()) {
         Logger.getLogger(MicroBlazeRpuMonitor.class.getName()).
                 warning("InstructionBlock is meant for the RPU, not the MicroBlaze.");
         return;
      }


      int microBlazeInstructions = instructionBlock.getTotalInstructions();

      // Update MicroBlaze Instructions
      //microblazeExecutedInstructions += microBlazeInstructions;

      // Build ExecutionHistory
      //float microBlazeCycles = StatsUtils.microBlazeCycles(microBlazeInstructions, traceCpi);
      //int microBlazeCyclesInt = (int)microBlazeCycles;
      //System.out.println("MicroBlazeCycles:"+microBlazeCycles);
      //System.out.println("MicroBlazeCyclesInt:"+microBlazeCyclesInt);
      executionHistory.addMicroBlazeExecution(microBlazeInstructions);
   }

   public void addRpuExecution(InstructionBlock instructionBlock, MapperMonitor mapperMonitor) {
      // Add block to list
      instructionBlockPartitions.add(instructionBlock);

      // Check
      if(!instructionBlock.mapToHardware()) {
         Logger.getLogger(MicroBlazeRpuMonitor.class.getName()).
                 warning("InstructionBlock is meant for the MicroBlaze, not the RPU.");
         return;
      }

      // Build an RpuExecution
      int iterations = instructionBlock.getIterations();
      int hash = instructionBlock.getHash();
      // WARNING
      // Using directly mapperMonitor instead of copying it.
      // It works, if the Mapper creates a new Monitor for each mapping, instead
      // of reusing the same monitor object.
      RpuExecution newExec = new RpuExecution(mapperMonitor, instructionBlock, iterations, hash);
      // Add execution
      rpuExecutions.add(newExec);

      // Build ExecutionHistory
      executionHistory.addRpuExecution(newExec);
   }

   public MicroBlazeRpuExecutionHistory getExecutionHistory() {
      return executionHistory;
   }

   public int getTotalMicroblazeInstructions() {
      return totalMicroBlazeInstructions;
   }

   public List<RpuExecution> getRpuExecutions() {
      return rpuExecutions;
   }

   public float getTraceCpi() {
      return traceCpi;
   }

   public int executedOperationsOnRpu() {
      int acc = 0;
      for(RpuExecution exec : rpuExecutions) {
         //acc+=exec.getMonitor().getMappedOperations();
         acc+=exec.getTotalMappedOperations();
      }
      return acc;
   }

   public int cyclesOfRpu() {
      int acc = 0;
      for(RpuExecution exec : rpuExecutions) {
         //acc+=exec.getMonitor().getCycles();
         acc+=exec.getTotalCycles();
      }
      return acc;
   }

   public int getLiveInsOutsOfRpu() {
      int acc = 0;
      for(RpuExecution exec : rpuExecutions) {
         //acc+=exec.getMonitor().getLiveIn();
         //acc+=exec.getMonitor().getLiveOut();
         acc+=exec.getLiveIn();
         acc+=exec.getLiveOut();
      }
      return acc;
   }

   public int getMaxLiveIn() {
      int max = 0;
      for(RpuExecution exec : rpuExecutions) {
         max = Math.max(max, exec.getLiveIn());
      }
      return max;
   }

   public int getMaxLiveOut() {
      int max = 0;
      for(RpuExecution exec : rpuExecutions) {
         max = Math.max(max, exec.getLiveOut());
      }
      return max;
   }

   public double getMaxIlp() {
      double max = 0;
      for(RpuExecution exec : rpuExecutions) {
         max = Math.max(max, exec.getMaxIlp());
      }
      return max;
   }

   public int getMaxCycles() {
      int max = 0;
      for(RpuExecution exec : rpuExecutions) {
         max = Math.max(max, exec.getTotalCycles());
      }
      return max;
   }

   public int getTotalMappedOperations() {
       int acc = 0;
      for(RpuExecution exec : rpuExecutions) {
         //acc += exec.getMonitor().getMappedOperations();
         acc += exec.getTotalMappedOperations();
      }
      return acc;
   }

   public List<InstructionBlock> getInstructionBlockPartitions() {
      return instructionBlockPartitions;
   }


   /**
    * INSTANCE VARIABLES
    */
   private float traceCpi;
   private int totalMicroBlazeInstructions;
   private List<RpuExecution> rpuExecutions;
   private MicroBlazeRpuExecutionHistory executionHistory;
   private List<InstructionBlock> instructionBlockPartitions;
}
