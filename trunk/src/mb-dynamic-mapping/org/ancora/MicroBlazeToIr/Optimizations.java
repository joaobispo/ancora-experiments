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
import java.util.logging.Logger;
import org.ancora.IrForDynamicMapping.InputIndex;
import org.ancora.IrForDynamicMapping.Operand;
import org.ancora.IrForDynamicMapping.Operation;
import org.ancora.IrForDynamicMapping.OperationName;
import org.ancora.IrForDynamicMapping.OutputIndex;
import org.ancora.SharedLibrary.BitUtils;

/**
 *
 * @author Joao Bispo
 */
public class Optimizations {

   /**
    * Optimizes operations with carries (integer_add, integer_sub).
    * 
    * @param operation
    * @return
    */
   public static List<Operation> computeLiteralsOfCarryOperation(Operation operation) {
      List<Operation> operations = new ArrayList<Operation>();

     if(GeneralParsing.REMOVE_WRITES_TO_LITERALS) {
         // Check if output is a literal
         if(operation.getOutput(OutputIndex.firstResult).getOpType() == Operand.OpType.literal) {
            return operations;
         }
      }

      // Check if optimization is enabled
      if (!GeneralParsing.COMPUTE_LITERALS) {
         operations.add(operation);
         return operations;
      }


      // Check if inputs are literals
      boolean areOperandsLiteral = GeneralParsing.areOperandsLiterals(operation.getInputs());

      if (!areOperandsLiteral) {
         operations.add(operation);
         return operations;
      }

      // Get computed value
      // No need to worry about input carry, it is always a register, so at this point
      // there is no carry
      int input1 = operation.getInput(InputIndex.firstOperand).getValueAsIntegerLiteral();
      int input2 = operation.getInput(InputIndex.secondOperand).getValueAsIntegerLiteral();
      int computedValue = computeLiteral(operation.getOperation(), input1, input2);

      // Try to sum the inputs
      // No need to worry about input carry, it is always a register
      //Operand computedOperand = GeneralParsing.integerSumOperands(operation.getInputs());
      Operand computedOperand = GeneralParsing.newMbLiteralOperand(computedValue);

      // Add move operation equivalent to sum
      Operation newOperation = Operation.newOperation(OperationName.move);
      newOperation.setAddress(operation.getAddress());
      newOperation.setInput(InputIndex.firstOperand, computedOperand);
      newOperation.setOutput(OutputIndex.firstResult, operation.getOutput(OutputIndex.firstResult));
      operations.add(newOperation);

      // Check if there is an output carry
      Operand carryOut = operation.getOutput(OutputIndex.carry);

      // Calculate carry if different than null
      if(carryOut != null) {
         //int firstOperand = operation.getInput(InputIndex.firstOperand).getValueAsIntegerLiteral();
         //int secondOperand =  operation.getInput(InputIndex.secondOperand).getValueAsIntegerLiteral();
         int carry = computeCarry(operation.getOperation(), input1, input2);
                 //GeneralParsing.getCarryOutAdd(firstOperand, secondOperand, 0);
         Operation carryOperation = Operation.newOperation(OperationName.move);
         carryOperation.setAddress(operation.getAddress());
         carryOperation.setInput(InputIndex.firstOperand, new Operand(Operand.OpType.literal, Integer.toString(carry), 1));
         carryOperation.setOutput(OutputIndex.firstResult, carryOut);

         operations.add(carryOperation);
      }

      return operations;
   }

   public static List<Operation> computeLiteralsOfBinaryOperation(Operation operation) {
      List<Operation> operations = new ArrayList<Operation>();

      if(GeneralParsing.REMOVE_WRITES_TO_LITERALS) {
         // Check if output is a literal
         if(operation.getOutput(OutputIndex.firstResult).getOpType() == Operand.OpType.literal) {
            return operations;
         }
      }

      // Check if optimization is enabled
      if (!GeneralParsing.COMPUTE_LITERALS) {
         operations.add(operation);
         return operations;
      }

      // Check if they are not Sext16 or sext8 operation
      if(operation.getOperation() == OperationName.integer_sext_8 || operation.getOperation() == OperationName.integer_sext_16) {
         operations.add(operation);
         return operations;
      }

      // Check if inputs are literals
      boolean areOperandsLiteral = GeneralParsing.areOperandsLiterals(operation.getInputs());

      if (!areOperandsLiteral) {
         operations.add(operation);
         return operations;
      }

      // Get computed value
      int input1 = operation.getInput(InputIndex.firstOperand).getValueAsIntegerLiteral();
      int input2 = operation.getInput(InputIndex.secondOperand).getValueAsIntegerLiteral();
      int computedValue = computeLiteral(operation.getOperation(), input1, input2);

      // Try to sum the inputs
      // No need to worry about input carry, it is always a register
      //Operand computedOperand = GeneralParsing.integerSumOperands(operation.getInputs());
      Operand computedOperand = GeneralParsing.newMbLiteralOperand(computedValue);

      // Add move operation equivalent to the operation
      Operation newOperation = Operation.newOperation(OperationName.move);
      newOperation.setAddress(operation.getAddress());
      newOperation.setInput(InputIndex.firstOperand, computedOperand);
      newOperation.setOutput(OutputIndex.firstResult, operation.getOutput(OutputIndex.firstResult));
      operations.add(newOperation);

      return operations;
   }

   public static int computeLiteral(OperationName operationName, int input1, int input2) {
      switch(operationName) {
         case integer_add:
            return input1 + input2;
         case integer_rsub:
            return input2 - input1;
         case integer_and:
            return input1 & input2;
         case integer_and_not:
            return input1 & ~input2;
         case integer_compare_msb:
            int result = input2 + ~input1 + 1;
            boolean aBiggerThanB = input1 > input2;
            // Change MSB to reflect relation
            if (aBiggerThanB) {
               return BitUtils.setBit(31, result);
            } else {
               return GeneralParsing.clearBit(31, result);
            }
         case integer_compare_msb_unsigned:
            result = input2 + ~input1 + 1;
            aBiggerThanB = GeneralParsing.unsignedComp(input1, input2);
            // Change MSB to reflect relation
            if (aBiggerThanB) {
               return BitUtils.setBit(31, result);
            } else {
               return GeneralParsing.clearBit(31, result);
            }
         case integer_div:
            return input2/input1;
         case integer_div_unsigned:
            return GeneralParsing.unsignedDiv(input2, input1);
         case integer_mul:
            return input1 * input2;
         case integer_or:
            return input1 | input2;
         case integer_sll:
            return input1 << input2;
         case integer_sra:
            return input1 >> input2;
         case integer_srl:
            return input1 >>> input2;
         case integer_xor:
            return input1 ^ input2;

         default:
            Logger.getLogger(Optimizations.class.getName()).
                    warning("Method not defined for instruction '"+operationName+"'");
            return 0;
      }
   }

   private static int computeCarry(OperationName operationName, int input1, int input2) {
      switch(operationName) {
         case integer_add:
            return GeneralParsing.getCarryOutAdd(input1, input2, 0);
         case integer_rsub:
             return GeneralParsing.getCarryOutRsub(input1, input2, 0);
         default:
            Logger.getLogger(Optimizations.class.getName()).
                    warning("Method not defined for instruction '"+operationName+"'");
            return 0;
      }
   }





}
