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
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.MicroBlaze.MicroBlazeUtils;

/**
 * Transforms MicroBlaze branch instructions.
 *
 * @author Joao Bispo
 */
public class Branches {

   public static int parseUnconditionalBranch(List<Instruction> instructions, List<Operation> operations, int index) { 
      // Check if instruction has delay slot
      index = GeneralParsing.delaySlotCheck(instructions, operations, index);
      
      // Get Instruction
      Instruction instruction = instructions.get(index);

      // Check if instruction performs linking
      GeneralParsing.performsLinkingCheck(instruction, operations);

      // Get PC
      int pc = instruction.getAddress();

      // Calculate next_pc
      int nextPC;
      if(index < instructions.size()-1) {
         nextPC = instructions.get(index+1).getAddress();
      } else {
         nextPC = instructions.get(0).getAddress();
      }

      // Check if is a branch to absolute value
      boolean absoluteBranch = org.ancora.SharedLibrary.MbDynamicMapping.
              MicroBlazeUtils.unconditionalBranchWithAbsoluteValue(instruction.getOperation());

      // Get first operand
      List<Operand> operands = GeneralParsing.parseReadType1(instruction);

      List<Operation> newOperations;
      Operand operand = operands.get(0);
      boolean isLiteral = operand.getOpType() == Operand.OpType.literal;
      if(isLiteral) {
          newOperations = generateUnconditionalLiteralBranch(operand.getValueAsIntegerLiteral(), pc, nextPC, absoluteBranch);
      } else {
         // OTher
         newOperations = null;
         System.out.println("UNCONDITION REGISTER NOT DONE");
      }
     

      // Update index
      index++;
      // Add operations
      operations.addAll(newOperations);

      return index;
   }


   private static List<Operation> generateUnconditionalLiteralBranch(int operandValue, int pc, int nextPC, boolean absoluteBranch) {
      List<Operation> newOperations = new ArrayList<Operation>();
      // Literal operand


      int exitAddress;
      if (absoluteBranch) {
         exitAddress = operandValue;
      } else {
         exitAddress = pc + operandValue;
      }

      int exitCondition = exitAddress - nextPC;

      // If exit condition is 0, it needs no operation
      if (exitCondition == 0) {
         return newOperations;
      }

      // Build Operation
      Operation exitOp = Operation.newOperation(OperationName.exit);
      exitOp.setInput(InputIndex.firstOperand, GeneralParsing.newMbLiteralOperand(exitAddress));
      newOperations.add(exitOp);

      return newOperations;

   }

}
