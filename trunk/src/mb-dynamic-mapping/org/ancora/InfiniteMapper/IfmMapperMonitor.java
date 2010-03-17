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

package org.ancora.InfiniteMapper;

import org.ancora.DmeFramework.Interfaces.MapperMonitor;

/**
 *
 * @author Joao Bispo
 */
public class IfmMapperMonitor implements MapperMonitor {

   @Override
   public int getMaxIlp() {
      return maxIlp;
   }

   @Override
   public int getLiveIn() {
      return liveIn;
   }

   @Override
   public int getLiveOut() {
      return liveOut;
   }

   @Override
   public int getMappedOperations() {
      return mappedOperations;
   }

   @Override
   public int getMappedElements() {
      return mappedElements;
   }

   @Override
   public int getCycles() {
      return cycles;
   }

   @Override
   public MapperMonitor copy() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   /**
    * INSTANCE VARIABLES
    */
   int liveIn;
   int liveOut;
   int mappedOperations;
   int mappedElements;
   int cycles;
   int maxIlp;
}
