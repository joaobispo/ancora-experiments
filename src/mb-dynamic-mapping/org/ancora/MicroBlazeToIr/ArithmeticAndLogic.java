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

import java.util.List;
import java.util.logging.Logger;
import org.ancora.IrForDynamicMapping.InputIndex;
import org.ancora.IrForDynamicMapping.Operand;
import org.ancora.IrForDynamicMapping.Operation;
import org.ancora.IrForDynamicMapping.OperationName;
import org.ancora.IrForDynamicMapping.OutputIndex;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.MicroBlaze.Instructions.InstructionName;

/**
 *
 * @author Joao Bispo
 */
public class ArithmeticAndLogic {

   public static int parseIntegerAdd(List<Instruction> instructions, List<Operation> operations, int index) {
      Instruction instruction = instructions.get(index);
      InstructionName instructionName = instruction.getOperation();

      Operation operation = Operation.newOperation(OperationName.integer_add);

      //OperationName operationName = OperationName.integer_add;
      //Operation operation = new Operation(operationName.getNumInputs(), operationName.getNumOutputs());
      // Set Operation
      //operation.setOperation(operationName);

      // Set Address
      operation.setAddress(instruction.getAddress());
      

      // Set inputs
      List<Operand> inputs = GeneralParsing.parseReadType1(instruction);
      operation.setInput(InputIndex.firstOperand, inputs.get(0));
      operation.setInput(InputIndex.secondOperand, inputs.get(1));

      // Set Output
      List<Operand> outputs = GeneralParsing.parseWriteType1(instruction);
      operation.setOutput(OutputIndex.firstResult, outputs.get(0));

      switch(instructionName) {
         case add:
            operation.setOutput(OutputIndex.carry, GeneralParsing.newMbCarryRegisterOperand());
            break;
         case addc:
            operation.setInput(InputIndex.carry, GeneralParsing.newMbCarryRegisterOperand());
            operation.setOutput(OutputIndex.carry, GeneralParsing.newMbCarryRegisterOperand());
            break;
         case addk:
            // Do Nothing
            break;
         case addkc:
            operation.setInput(InputIndex.carry, GeneralParsing.newMbCarryRegisterOperand());
            break;
         case addi:
            operation.setOutput(OutputIndex.carry, GeneralParsing.newMbCarryRegisterOperand());
            break;
         case addic:
            operation.setInput(InputIndex.carry, GeneralParsing.newMbCarryRegisterOperand());
            operation.setOutput(OutputIndex.carry, GeneralParsing.newMbCarryRegisterOperand());
            break;
         case addik:
            // Do Nothing
            break;
         case addikc:
            operation.setInput(InputIndex.carry, GeneralParsing.newMbCarryRegisterOperand());
            break;
         default:
            Logger.getLogger(ArithmeticAndLogic.class.getName()).
                    warning("MicroBlaze Instruction '"+instructionName+"' does" +
                    "not translate to an '"+operation.getOperation()+"'");
            break;
      }

      //
      // At this point, optimizations can be made
      //
      List<Operation> optimizedOperations = Optimizations.computeIntegerAddLiterals(operation);
      

      // Update index - read one MicroBlaze instruction
      index++;
      // Update operation list
      operations.addAll(optimizedOperations);

      return index;
   }

}
