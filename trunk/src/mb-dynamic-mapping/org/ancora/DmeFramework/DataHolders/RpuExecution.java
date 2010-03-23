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

   public RpuExecution(MapperMonitor monitor, InstructionBlock block, int iterations, int mappingHash) {
      this.monitor = monitor;
      this.instructionBlock = block;
      this.iterations = iterations;
      this.mappingHash = mappingHash;
   }

   public int getIterations() {
      return iterations;
   }

   public int getMappingHash() {
      return mappingHash;
   }

   public InstructionBlock getInstructionBlock() {
      return instructionBlock;
   }

   

   /*
   public MapperMonitor getMonitor() {
      return monitor;
   }
    */

   /**
    * @return for how many cycles executed, multiplied by the number of iterations.
    */
   public int getTotalCycles() {
      return monitor.getCycles() * iterations;
   }

    /**
    * @return number of mapped operations, multiplied by the number of iterations.
    */
   public int getTotalMappedOperations() {
      return monitor.getMappedOperations() * iterations;
   }

   /**
    * @return number of used elements, multiplied by the number of iterations
    */
   public int getTotalMappedElements() {
      return monitor.getMappedElements() * iterations;
   }

   public int getLiveIn() {
      return monitor.getLiveIn();
   }

   public int getLiveOut() {
      return monitor.getLiveOut();
   }
   
   public int getMaxIlp() {
      return monitor.getMaxIlp();
   }

   /**
    * INSTANCE VARIABLES
    */
   private MapperMonitor monitor;
   private InstructionBlock instructionBlock;
   private int iterations;
   private int mappingHash;
}
