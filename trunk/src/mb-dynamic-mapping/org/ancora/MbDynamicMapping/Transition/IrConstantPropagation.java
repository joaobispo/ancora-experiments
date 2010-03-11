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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.ancora.IrForDynamicMapping.InputIndex;
import org.ancora.IrForDynamicMapping.Operand;
import org.ancora.IrForDynamicMapping.Operation;
import org.ancora.IrForDynamicMapping.OperationName;
import org.ancora.IrForDynamicMapping.OutputIndex;
import org.ancora.MicroBlazeToIr.GeneralParsing;
import org.ancora.MicroBlazeToIr.Optimizations;

/**
 *
 * @author Joao Bispo
 */
public class IrConstantPropagation {

   public IrConstantPropagation() {
      registerValues = new Hashtable<String, Integer>();
      optimizedOperations = new LinkedList<Operation>();
   }

   public void clearOptimizedOperations() {
      optimizedOperations = new LinkedList<Operation>();
   }

   public void reset() {
      registerValues = new Hashtable<String, Integer>();
      optimizedOperations = new LinkedList<Operation>();
   }

   public List<Operation> getOptimizedOperations() {
      return optimizedOperations;
   }



   public void acceptOperation(Operation operation) {
      // Make a copy
      Operation newOperation = operation.copy();

      // 1. Check if inputs can be substituted by literals
      newOperation = substituteInputs(newOperation);
      // 2. Check if operation can be resolved
      List<Operation> newOperations;
      newOperations = resolveLiterals(newOperation);
      // 3. Check output of move operations
      for(Operation tempOperation : newOperations) {
         fillRegisterValuesTable(tempOperation);
      }
      // 4. Detect code spilling for register 15
      /*
      for(Operation tempOperation : newOperations) {
         removeCodeSpillingAdditions(tempOperation);
       }*/

      // final. Add operation to optimized list
      for(Operation tempOperation : newOperations) {
         optimizedOperations.add(tempOperation);
      }

   }

   /**
    * Checks the table to see if there is a literal value associated with any
    * of its inputs registers.
    *
    * @param newOperation
    * @return
    */
   private Operation substituteInputs(Operation newOperation) {
      // Check each input
      for(int i=0; i<newOperation.getInputs().length; i++) {
         Operand operand = newOperation.getInput(i);
         // Check if it is not null
         if(operand == null) {
            continue;
         }

         // Check if it is a register
         if(operand.getOpType() != Operand.OpType.register) {
            continue;
         }

         // Check if there is a literal for this register
         Integer value = registerValues.get(operand.getValue());
         if(value == null) {
            continue;
         }

         // There is a literal value. Substitute register operand with literal
         newOperation.setInput(i, GeneralParsing.newMbLiteralOperand(value));
      }

      return newOperation;
   }


   private List<Operation> resolveLiterals(Operation newOperation) {
      // Check if is binary operation with carry
      if(GeneralParsing.isCarryOperationOpt.contains(newOperation.getOperation())) {
         return Optimizations.computeLiteralsOfCarryOperation(newOperation);
      }

      // Check if is a simple binary operation
      if(GeneralParsing.isBinaryOperationOpt.contains(newOperation.getOperation())) {
         return Optimizations.computeLiteralsOfBinaryOperation(newOperation);
      }

      // Could not be optimized
      List<Operation> newOperations = new ArrayList<Operation>();
      newOperations.add(newOperation);
      return newOperations;
   }


   private void fillRegisterValuesTable(Operation operation) {
      // Check if it is a move operation
      if(operation.getOperation() == OperationName.move) {
         Operand outReg = operation.getOutput(OutputIndex.firstResult);
         Operand inLiteral = operation.getInput(InputIndex.firstOperand);
         // Associate the literal input with the register output
         registerValues.put(outReg.getValue(), inLiteral.getValueAsIntegerLiteral());
      }
      // For any other case, check if the output register is written again
      else {
         for(Operand output : operation.getOutputs()) {
            if(output == null) {
               continue;
            }

            // If register of this output is present on the table, remove it
            // from the table
            if(output.getOpType() == Operand.OpType.register) {
               String registerName = output.getValue();
               if(registerValues.containsKey(registerName)) {
                  registerValues.remove(registerName);
               }
            }
         }
      }
   }

   private void removeCodeSpillingAdditions(Operation operation) {
      // Check if it is a store of a literal to register 15
      OperationName opName = operation.getOperation();
      boolean isStore = opName == OperationName.store_16 || opName == OperationName.store_32 ||
              opName == OperationName.store_8;
      // Get operand to be stored to see if source is register 15?
      //Operand eg15 = operation.getInput(InputIndex.)

      if(isStore) {

      }


   }

   /**
    * INSTANCE VARIABLES
    */
   private Map<String, Integer> registerValues;

   private List<Operation> optimizedOperations;






   
}
