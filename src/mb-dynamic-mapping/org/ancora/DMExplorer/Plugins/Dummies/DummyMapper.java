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
import org.ancora.DMExplorer.Plugins.MapperName;
import org.ancora.DmeFramework.Interfaces.Mapper;
import org.ancora.DmeFramework.Interfaces.MapperMonitor;
import org.ancora.IrForDynamicMapping.Operation;


/**
 *
 * @author Joao Bispo
 */
public class DummyMapper implements Mapper {

   public DummyMapper() {
      shownMessage = false;
   }



   @Override
   public String getName() {
      return MapperName.dummyrpu.getName();
   }

   @Override
   public MapperMonitor getMonitor() {
      return dummyMonitor;
   }

   @Override
   public void mapOperations(List<Operation> operations) {
      if (!shownMessage) {
         System.out.println("No Mapping being done.");
         System.out.println("Using a Dummy MappingMonitor.");
         shownMessage = true;
      }

      DummyMapperMonitor dummy = new DummyMapperMonitor();
      dummy.processOperationList(operations);

      // Update monitor
      dummyMonitor = dummy;
   }

      @Override
   public boolean hasMappingFailed() {
      return false;
   }

   /**
    * INSTANCE VARIABLES
    */
   //private int totalInstructions;
   private boolean shownMessage;
   private MapperMonitor dummyMonitor;







}
