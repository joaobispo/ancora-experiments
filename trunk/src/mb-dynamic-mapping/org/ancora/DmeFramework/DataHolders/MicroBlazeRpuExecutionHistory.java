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

package org.ancora.DmeFramework.DataHolders;

import java.util.ArrayList;
import java.util.List;
import org.ancora.DmeFramework.Interfaces.MapperMonitor;
import org.ancora.DmeFramework.DataHolders.Interface.MapperToCycles;

/**
 * Records the history of executions.
 *
 * @author Joao Bispo
 */
public class MicroBlazeRpuExecutionHistory {

   public MicroBlazeRpuExecutionHistory(MapperToCycles mapper2cycles) {
      this.executionCycles = new ArrayList<Integer>();
      this.executionTypes = new ArrayList<CycleType>();
      this.mapper2cycles = mapper2cycles;
   }


   public void addMicroBlazeExecution(int cycles) {
      // Check if last added execution is of type MicroBlaze
      boolean isMicroBlazeType = lastAddedCycleIsOfType(CycleType.MicroBlaze);

      // If it is, increment last cycle variable
      if(isMicroBlazeType) {
         int index = executionCycles.size()-1;
         int newCycles = executionCycles.get(index) + cycles;
         executionCycles.set(index, newCycles);
         return;
      }

      // Is not MicroBlaze type
      // Create new entry
      executionCycles.add(cycles);
      executionTypes.add(CycleType.MicroBlaze);
   }

   public void addRpuExecution(MapperMonitor monitor) {
      // Delegate updates to the MapperToCycles
      mapper2cycles.getExecutionSteps(monitor, executionCycles, executionTypes);
   }

   public int getTotalCycles() {
      int acc = 0;

      for(Integer i : executionCycles) {
         acc += i;
      }

      return acc;
   }

   private boolean lastAddedCycleIsOfType(CycleType cycleType) {
      // Check if there list is empty
      if(executionTypes.size() == 0) {
         return false;
      }

      // Get last type and compare
      int index = executionTypes.size()-1;
      if(executionTypes.get(index) == cycleType) {
         return true;
      } else {
         return false;
      }
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      for(int i=0; i<executionCycles.size(); i++) {
         builder.append(executionTypes.get(i));
         builder.append(":");
         builder.append(executionCycles.get(i));
         builder.append("\n");
      }

      return builder.toString();
   }

   public List<Integer> getExecutionCycles() {
      return executionCycles;
   }

   public List<CycleType> getExecutionTypes() {
      return executionTypes;
   }



   /**
    * INSTANCE VARIABLES
    */
   private List<Integer> executionCycles;
   private List<CycleType> executionTypes;
   private MapperToCycles mapper2cycles;



   public enum CycleType {
      MicroBlaze,
      RPU,
      Communication
   }
}
