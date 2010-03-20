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

import java.util.List;
import org.ancora.DmeFramework.DataHolders.Interface.MapperToSteps;
import org.ancora.DmeFramework.DataHolders.MicroBlazeRpuExecutionHistory.StepType;
import org.ancora.DmeFramework.DataHolders.RpuExecution;

/**
 *
 * @author Joao Bispo
 */
public class SimpleRpuCommModel implements MapperToSteps {

   /**
    * Adds three blocks:
    * <br>1 Comm block with cycles equal to the number of live-ins;
    * <br>1 Rpu block with cycles equal to the number of the mapper cycles;
    * <br>1 Comm block with cycles equal to the number of live-outs;
    *
    * <p>The number of iterations will be multiplied by the number of cycles of the RPU,
    * but not by the number of cycles of the live-ins or live-outs. This simulates
    * a situation where there is communication only at the beginning and end of a loop.
    *
    * @param monitor
    * @param executionCycles
    * @param executionTypes
    */
   @Override
   public void getExecutionSteps(RpuExecution rpuExecution, List<Integer> executionCycles, List<StepType> executionTypes) {
      executionTypes.add(StepType.Communication);
      executionCycles.add(rpuExecution.getLiveIn());

      executionTypes.add(StepType.RPU);
      executionCycles.add(rpuExecution.getTotalCycles());

      executionTypes.add(StepType.Communication);
      executionCycles.add(rpuExecution.getLiveOut());
   }

}
