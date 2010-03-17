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

package org.ancora.DMExplorer.Plugins.Dummies;

import java.util.List;
import org.ancora.DmeFramework.Interfaces.MapperMonitor;
import org.ancora.IrForDynamicMapping.Operation;

/**
 *
 * @author Joao Bispo
 */
public class DummyMapperMonitor implements MapperMonitor {

   public DummyMapperMonitor() {
      mappedOperations = 0;
   }



   @Override
   public double getIlp() {
      return 1;
   }

   @Override
   public int getLiveIn() {
      return 2;
   }

   @Override
   public int getLiveOut() {
      return 1;
   }

   @Override
   public int getMappedOperations() {
      return mappedOperations;
   }

   @Override
   public int getMappedElements() {
      return mappedOperations;
   }

   @Override
   public int getCycles() {
      return mappedOperations;
   }

   @Override
   public MapperMonitor copy() {
      DummyMapperMonitor dummy = new DummyMapperMonitor();

      dummy.mappedOperations = this.mappedOperations;

      return dummy;
   }

   void processOperationList(List<Operation> operations) {
      mappedOperations += operations.size();
   }

   private int mappedOperations;


}
