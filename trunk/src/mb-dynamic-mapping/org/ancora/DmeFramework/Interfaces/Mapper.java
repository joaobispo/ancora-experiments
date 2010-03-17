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

package org.ancora.DmeFramework.Interfaces;

import java.util.List;
import org.ancora.IrForDynamicMapping.Operation;

/**
 * Maps a sequence of Intermediate Representation operations to custom hardware.
 *
 * @author Joao Bispo
 */
public interface Mapper {

   /**
    * @return the name of this mapper
    */
   String getName();

   /**
    * @return the monitor for this mapper
    */
   MapperMonitor getMonitor();

   /**
    * Clears the current mapping and maps the given sequence of operations.
    * 
    * @param operations
    */
   void mapOperations(List<Operation> operations);

   /**
    * Simulates execution of the mapped operations. Use this method to gather
    * data for the monitor.
    *
    * @param numberOfExecutions
    */
   //void execute(int numberOfExecutions);

   boolean hasMappingFailed();
}
