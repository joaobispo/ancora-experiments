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

import org.ancora.DmeFramework.DataHolders.MicroBlazeRpuExecutionHistory;
import org.ancora.DmeFramework.DataHolders.MicroBlazeRpuExecutionHistory.StepType;
import org.ancora.DmeFramework.System.MicroBlazeRpuMonitor;

/**
 *
 * @author Joao Bispo
 */
public class MicroBlazeRpuDataProcess {

   public MicroBlazeRpuDataProcess(MicroBlazeRpuMonitor monitor) {
      this.monitor = monitor;
      this.history = monitor.getExecutionHistory();
   }

   /*
   public void showStats() {
      System.out.println("Trace CPI:"+monitor.getTraceCpi());
   }
    */
   /**
    * Shows the difference of MicroBlaze cycles between the value of SystemMonitor
    * (accumulated float values) and the value of ExecutionHistory (accumulated
    * int values).
    */
   public void showDiffMbCyclesSysHis() {
      int sysCycles = (int) StatsUtils.microBlazeCycles(monitor.getTotalMicroblazeInstructions(), monitor.getTraceCpi());
      int hisCycles = (int) (calcHistoryMbCycles()*monitor.getTraceCpi());

      System.out.println("MB SystemMonitor cycles:"+sysCycles);
      System.out.println("MB ExecutionHist cycles:"+hisCycles);
   }

   public int calcHistoryMbCycles() {
      int acc = 0;
      for(int i=0; i<history.getExecutionTypes().size(); i++) {
         StepType type = history.getExecutionTypes().get(i);

         if(type == StepType.MicroBlaze) {
            acc += history.getExecutionSteps().get(i);
         }
      }

      return acc;
   }

   public void showSpeedup() {
      int totalMbInstructions = monitor.getTotalMicroblazeInstructions();
      float cpi = monitor.getTraceCpi();
      float cyclesMbWithoutMap = StatsUtils.microBlazeCycles(totalMbInstructions, cpi);

      int executedMbInstructions = history.getSteps(StepType.MicroBlaze);
      float cyclesMbWithMap = StatsUtils.microBlazeCycles(executedMbInstructions, cpi);

      //Assume each communication and rpu step is equivalent to a cycle
      //int commSteps = history.getSteps(StepType.Communication);
      //int commCycles = StatsUtils.communicationCycles(commSteps);
      int commSteps = monitor.getLiveInsOutsOfRpu();
      // Write communication optimized for iterations
      //int commSteps = history.


      //int rpuSteps = history.getSteps(StepType.RPU);
      int rpuSteps = monitor.cyclesOfRpu();

      float mappedCycles = (float)commSteps + (float) rpuSteps + cyclesMbWithMap;

      float speedup = cyclesMbWithoutMap / mappedCycles;

      System.out.println("Cycles before Mapping:"+cyclesMbWithoutMap);
      System.out.println("Cycles after Mapping:"+mappedCycles);
      System.out.println("Communication Cycles:"+commSteps);
      System.out.println("RPU Cycles:"+rpuSteps);
      System.out.println("Mb Cycles:"+cyclesMbWithMap);
      System.out.println("Speed-Up:"+speedup);
   }

   public void showIlp() {
      int executedOperations = monitor.executedOperationsOnRpu();
      int cyclesOnRpu = monitor.cyclesOfRpu();

      float ilp = (float) executedOperations / (float) cyclesOnRpu;
      System.out.println("ILP:"+ilp);
   }
   /**
    * INSTANCE VARIABLES
    */
   private MicroBlazeRpuMonitor monitor;
   MicroBlazeRpuExecutionHistory history;

}
