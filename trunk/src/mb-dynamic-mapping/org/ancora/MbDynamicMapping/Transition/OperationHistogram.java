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

package org.ancora.MbDynamicMapping.Transition;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.ancora.IrForDynamicMapping.Operation;
import org.ancora.IrForDynamicMapping.OperationName;

/**
 *
 * @author Joao Bispo
 */
public class OperationHistogram {

   public OperationHistogram() {
      operationHistogram = new EnumMap<OperationName, Integer>(OperationName.class);
   }

   public void addOperations(List<Operation> operations) {
       for(Operation operation : operations) {
         addOperation(operation);
      }
   }

   public void addOperation(Operation operation) {
      OperationName opName = operation.getOperation();

      Integer value = operationHistogram.get(opName);
      if (value == null) {
         value = 0;
      }
      value++;
      operationHistogram.put(opName, value);
   }

   public int getNumberOperations(OperationName opName) {
     Integer value = operationHistogram.get(opName);
     if(value == null) {
        return 0;
     } else {
        return value;
     }
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      // Show Histogram
      int counter = 0;
      for(OperationName opName : operationHistogram.keySet()) {
         int value = operationHistogram.get(opName);
         //System.out.println(opName+":"+value);
         counter+=value;
      }

      // Count number of mem operations
      int memOperations = 0;
      memOperations += getNumberOperations(OperationName.load_8);
      memOperations += getNumberOperations(OperationName.load_16);
      memOperations += getNumberOperations(OperationName.load_32);
      memOperations += getNumberOperations(OperationName.store_8);
      memOperations += getNumberOperations(OperationName.store_16);
      memOperations += getNumberOperations(OperationName.store_32);

      // Count number of moves
      int moveOperations = 0;
      moveOperations += getNumberOperations(OperationName.move);
      moveOperations += getNumberOperations(OperationName.move_bit);

      builder.append("Total Operations:"+counter+"\n");
      builder.append("Mem Operations:"+memOperations+"\n");
      builder.append("Move Operations:"+moveOperations+"\n");
      //builder.append("Total without mem adds:"+(counter-memOperations)+"\n");
      //builder.append("Total without mem adds and moves:"+(counter-memOperations-moveOperations)+"\n");
      builder.append("Total without moves:"+(counter-moveOperations)+"\n");

     return builder.toString();
   }

      /**
       * INSTANCE VARIABLES
       */
   private Map<OperationName, Integer> operationHistogram;

}
