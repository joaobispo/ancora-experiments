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
import org.ancora.DmeFramework.DataHolders.Interface.MapperToSteps;

/**
 * Records the history of executions.
 *
 * @author Joao Bispo
 */
public class MicroBlazeRpuExecutionHistory {

   public MicroBlazeRpuExecutionHistory(MapperToSteps mapper2steps) {
      this.executionSteps = new ArrayList<Integer>();
      this.executionTypes = new ArrayList<StepType>();
      this.mapper2steps = mapper2steps;
   }


   public void addMicroBlazeExecution(int cycles) {
      // Check if last added execution is of type MicroBlaze
      boolean isMicroBlazeType = lastAddedStepIsOfType(StepType.MicroBlaze);

      // If it is, increment last cycle variable
      if(isMicroBlazeType) {
         int index = executionSteps.size()-1;
         int newCycles = executionSteps.get(index) + cycles;
         executionSteps.set(index, newCycles);
         return;
      }

      // Is not MicroBlaze type
      // Create new entry
      executionSteps.add(cycles);
      executionTypes.add(StepType.MicroBlaze);
   }

   public void addRpuExecution(MapperMonitor monitor) {
      // Delegate updates to the MapperToSteps
      mapper2steps.getExecutionSteps(monitor, executionSteps, executionTypes);
   }

   public int getSteps(StepType type) {
       int acc = 0;
      for(int i=0; i<executionSteps.size(); i++) {
         if(executionTypes.get(i) == type) {
            acc += executionSteps.get(i);
         }
      }

      return acc;
   }

   public int getTotalSteps() {
      int acc = 0;

      for(Integer i : executionSteps) {
         acc += i;
      }

      return acc;
   }

   private boolean lastAddedStepIsOfType(StepType stepType) {
      // Check if there list is empty
      if(executionTypes.size() == 0) {
         return false;
      }

      // Get last type and compare
      int index = executionTypes.size()-1;
      if(executionTypes.get(index) == stepType) {
         return true;
      } else {
         return false;
      }
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      for(int i=0; i<executionSteps.size(); i++) {
         builder.append(executionTypes.get(i));
         builder.append(":");
         builder.append(executionSteps.get(i));
         builder.append("\n");
      }

      return builder.toString();
   }

   public List<Integer> getExecutionSteps() {
      return executionSteps;
   }

   public List<StepType> getExecutionTypes() {
      return executionTypes;
   }



   /**
    * INSTANCE VARIABLES
    */
   private List<Integer> executionSteps;
   private List<StepType> executionTypes;
   private MapperToSteps mapper2steps;



   public enum StepType {
      MicroBlaze,
      RPU,
      Communication
   }
}
