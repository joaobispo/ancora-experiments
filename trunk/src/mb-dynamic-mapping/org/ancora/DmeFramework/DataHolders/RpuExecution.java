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

import org.ancora.DmeFramework.Interfaces.MapperMonitor;

/**
 * Holds data from a RPU execution.
 *
 * @author Joao Bispo
 */
public class RpuExecution {

   public RpuExecution(MapperMonitor monitor, int iterations, int mappingHash) {
      this.monitor = monitor;
      this.iterations = iterations;
      this.mappingHash = mappingHash;
   }

   public int getIterations() {
      return iterations;
   }

   public int getMappingHash() {
      return mappingHash;
   }

   public MapperMonitor getMonitor() {
      return monitor;
   }

   

   /**
    * INSTANCE VARIABLES
    */
   private MapperMonitor monitor;
   private int iterations;
   private int mappingHash;
}
