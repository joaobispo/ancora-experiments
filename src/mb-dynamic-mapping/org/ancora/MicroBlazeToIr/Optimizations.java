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

package org.ancora.MicroBlazeToIr;

import java.util.ArrayList;
import java.util.List;
import org.ancora.IrForDynamicMapping.InputIndex;
import org.ancora.IrForDynamicMapping.Operand;
import org.ancora.IrForDynamicMapping.Operation;
import org.ancora.IrForDynamicMapping.OperationName;
import org.ancora.IrForDynamicMapping.OutputIndex;

/**
 *
 * @author Joao Bispo
 */
public class Optimizations {

   /**
    * Optimizes integer_add operations.
    * 
    * @param operation
    * @return
    */
   public static List<Operation> computeIntegerAddLiterals(Operation operation) {
      List<Operation> operations = new ArrayList<Operation>();

      // Check if optimization is enabled
      if (!GeneralParsing.COMPUTE_LITERALS) {
         operations.add(operation);
         return operations;
      }


      // Try to sum the inputs
      // No need to worry about input carry, it is always a register
      Operand inputSum = GeneralParsing.integerSumOperands(operation.getInputs());

      // Check if sum was possible
      if(inputSum == null) {
         operations.add(operation);
         return operations;
      }

      // Add move operation equivalent to sum
      Operation newOperation = Operation.newOperation(OperationName.move);
      newOperation.setInput(InputIndex.firstOperand, inputSum);
      newOperation.setOutput(OutputIndex.firstResult, operation.getOutput(OutputIndex.firstResult));
      operations.add(newOperation);

      // Check if there is an output carry
      Operand carryOut = operation.getOutput(OutputIndex.carry);

      // Calculate carry if different than null
      if(carryOut != null) {
         int firstOperand = operation.getInput(InputIndex.firstOperand).getValueAsIntegerLiteral();
         int secondOperand =  operation.getInput(InputIndex.secondOperand).getValueAsIntegerLiteral();
         int carry = GeneralParsing.getCarryOutAdd(firstOperand, secondOperand, 0);
         Operation carryOperation = Operation.newOperation(OperationName.move);
         carryOperation.setInput(InputIndex.firstOperand, new Operand(Operand.OpType.literal, Integer.toString(carry), 1));
         carryOperation.setOutput(OutputIndex.firstResult, carryOut);

         operations.add(carryOperation);
      }

      return operations;
   }

}
