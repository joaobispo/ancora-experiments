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
import java.util.EnumSet;
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
public class Memory {
/*
   public static int parseLoadOperation(List<Instruction> instructions, List<Operation> operations, int index) {
      throw new UnsupportedOperationException("Not yet implemented");
   }
*/

   public static int parseMemoryOperation(List<Instruction> instructions, List<Operation> operations, int index) {
      // Get Instruction
      Instruction instruction = instructions.get(index);
      // Create new operations list
      List<Operation> newOperations = new ArrayList<Operation>();
      // Get operands
      List<Operand> operands = GeneralParsing.parseReadType1(instruction);
      Operand firstOperand = operands.get(0);
      Operand secondOperand = operands.get(1);
      Operand address = GeneralParsing.parseWriteType1(instruction).get(0);

      OperationName operationName = getMemoryOperation(instruction.getOperation());

      Operand tempRegister = GeneralParsing.newMbRegisterOperand("t1");

      Operation addOp = Operation.newOperation(OperationName.integer_add);
      addOp.setInput(InputIndex.firstOperand, firstOperand);
      addOp.setInput(InputIndex.secondOperand, secondOperand);
      addOp.setOutput(OutputIndex.firstResult, tempRegister);

      // Optimize operations
      List<Operation> optimizedOperations = Optimizations.computeLiteralsOfCarryOperation(addOp);
      newOperations.addAll(optimizedOperations);

      Operation memOp = Operation.newOperation(operationName);

      if(isStore.contains(operationName)) {
         memOp.setInput(InputIndex.firstOperand, address);
         memOp.setInput(InputIndex.secondOperand, tempRegister);
      } else {
         memOp.setInput(InputIndex.firstOperand, tempRegister);
         memOp.setOutput(OutputIndex.firstResult, address);
      }

      newOperations.add(memOp);

      // Update index
      index++;
      // Add operations
      for(Operation operation : newOperations) {
         operation.setAddress(instruction.getAddress());
         operations.add(operation);
      }

      return index;
   }

   private static OperationName getMemoryOperation(InstructionName instructionName) {
      switch (instructionName) {
         case lw:
            return OperationName.load_32;
         case lwi:
            return OperationName.load_32;
         case lbu:
            return OperationName.load_8;
         case lbui:
            return OperationName.load_8;
         case lhu:
            return OperationName.load_16;
         case lhui:
            return OperationName.load_16;
         case sw:
            return OperationName.store_32;
         case swi:
            return OperationName.store_32;
         case sb:
            return OperationName.store_8;
         case sbi:
            return OperationName.store_8;
         case sh:
            return OperationName.store_16;
         case shi:
            return OperationName.store_16;
         default:
            Logger.getLogger(Branches.class.getName()).
                    warning("Method not defined for instruction '" + instructionName + "'");
            return null;

      }
   }

      public static final EnumSet<OperationName> isStore = EnumSet.of(
           OperationName.store_8,
           OperationName.store_16,
           OperationName.store_32,
           OperationName.store_add_8,
           OperationName.store_add_16,
           OperationName.store_add_32
      );

      public static final EnumSet<OperationName> isMemoryOperation = EnumSet.of(
           OperationName.load_8,
           OperationName.load_16,
           OperationName.load_32,
           OperationName.load_add_8,
           OperationName.load_add_16,
           OperationName.load_add_32,
           OperationName.store_8,
           OperationName.store_16,
           OperationName.store_32,
           OperationName.store_add_8,
           OperationName.store_add_16,
           OperationName.store_add_32
      );
}
