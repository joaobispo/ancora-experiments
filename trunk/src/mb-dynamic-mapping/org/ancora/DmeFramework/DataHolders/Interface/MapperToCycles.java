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

package org.ancora.DmeFramework.DataHolders.Interface;

import java.util.List;
import org.ancora.DmeFramework.DataHolders.MicroBlazeRpuExecutionHistory.CycleType;
import org.ancora.DmeFramework.Interfaces.MapperMonitor;

/**
 * Transform the data in an MapperMonitor into ExecutionSteps
 *
 * @author Joao Bispo
 */
public interface MapperToCycles {

   void getExecutionSteps(MapperMonitor monitor, List<Integer> executionCycles,
           List<CycleType> executionTypes);
}
