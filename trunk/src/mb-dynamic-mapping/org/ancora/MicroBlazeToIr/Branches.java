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
         newOperations = generateUnconditionalRegisterBranch(operand, pc, nextPC, absoluteBranch);
         // OTher
         //newOperations = null;
         //System.out.println("UNCONDITION REGISTER NOT DONE");
      }
     

      // Update index
      index++;
      // Add operations
      for(Operation operation : newOperations) {
         operation.setAddress(instruction.getAddress());
         operations.add(operation);
      }

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

   private static List<Operation> generateUnconditionalRegisterBranch(Operand operand, int pc, int nextPC, boolean absoluteBranch) {
      List<Operation> newOperations = new ArrayList<Operation>();
      String tempReg2 = "t2";
      String tempReg1 = "t1";

      // If absolute, add sum operation
      if(!absoluteBranch) {
         Operation operation = Operation.newOperation(OperationName.integer_add);
         operation.setInput(InputIndex.firstOperand, GeneralParsing.newMbLiteralOperand(pc));
         operation.setInput(InputIndex.secondOperand, operand);
         operation.setOutput(OutputIndex.firstResult, GeneralParsing.newMbRegisterOperand(tempReg2));

         newOperations.add(operation);
      }

      // Set Operands
      Operand exitAddress;
      if(!absoluteBranch) {
         exitAddress = GeneralParsing.newMbRegisterOperand(tempReg2);
      } else {
         exitAddress = operand;
      }
      Operand temporaryOperand = GeneralParsing.newMbRegisterOperand(tempReg1);

      // Build rsub
      Operation operation1 = Operation.newOperation(OperationName.integer_rsub);
      operation1.setInput(InputIndex.firstOperand, GeneralParsing.newMbLiteralOperand(nextPC));
      operation1.setInput(InputIndex.secondOperand, exitAddress);
      operation1.setOutput(OutputIndex.firstResult, temporaryOperand);

      // Build exit
      Operation operation2 = Operation.newOperation(OperationName.exit_not_equal);
      operation2.setInput(InputIndex.firstOperand, temporaryOperand);
      operation2.setInput(InputIndex.secondOperand, exitAddress);

      // Add operations
      newOperations.add(operation1);
      newOperations.add(operation2);

      return newOperations;
   }

   public static int parseConditionalBranch(List<Instruction> instructions, List<Operation> operations, int index) {
      // Check if instruction has delay slot
      index = GeneralParsing.delaySlotCheck(instructions, operations, index);

       // Get Instruction
      Instruction instruction = instructions.get(index);

      // Get PC
      int pc = instruction.getAddress();

      // Calculate next_pc
      int nextPC;
      if(index < instructions.size()-1) {
         nextPC = instructions.get(index+1).getAddress();
      } else {
         nextPC = instructions.get(0).getAddress();
      }

      // Check if next pc is an offset of 4
      boolean nextPcIsOffset4 = (nextPC - pc == 4);

      // Get Operands
      List<Operand> operands = GeneralParsing.parseReadType1(instruction);
      Operand conditionOperandRa = operands.get(0);
      Operand jumpOffset = operands.get(1);


      List<Operation> newOperations;
      if(nextPcIsOffset4) {
         // Get Operation name
         OperationName operationName = getSynonymOperation(instruction);
         newOperations = generateConditionalBranchOffset4(operationName, conditionOperandRa, jumpOffset, pc);
      } else {
           // Get Operation name
         OperationName operationName = getAntonymOperation(instruction);
         newOperations = generateConditionalBranchOtherOffset(operationName, conditionOperandRa, jumpOffset, pc, nextPC);
      }

      // Update index
      index++;
      // Add operations
      for(Operation operation : newOperations) {
         operation.setAddress(instruction.getAddress());
         operations.add(operation);
      }

      return index;
   }


   private static List<Operation> generateConditionalBranchOffset4(OperationName operationName, Operand conditionOperandRa, Operand jumpOffset, int pc) {
      List<Operation> newOperations = new ArrayList<Operation>();
      String tempRegister = "t1";

      // Is offset literal?
      boolean isJumpOffsetLiteral = jumpOffset.getOpType() == Operand.OpType.literal;

      // Calculate jump address
      Operand jumpAddress;
      if(isJumpOffsetLiteral) {
         int jumpAddressValue = pc + jumpOffset.getValueAsIntegerLiteral();
         jumpAddress = GeneralParsing.newMbLiteralOperand(jumpAddressValue);
      } else {
         jumpAddress = GeneralParsing.newMbRegisterOperand(tempRegister);
      }

      // Check if integer_add is needed
      if(isJumpOffsetLiteral) {
         Operation operation = Operation.newOperation(OperationName.integer_add);
         operation.setInput(InputIndex.firstOperand, GeneralParsing.newMbLiteralOperand(pc));
         operation.setInput(InputIndex.secondOperand, jumpOffset);
         operation.setOutput(OutputIndex.firstResult, GeneralParsing.newMbRegisterOperand(tempRegister));

         newOperations.add(operation);
      }

      // Add exit operation
      Operation operation = Operation.newOperation(operationName);
      operation.setInput(InputIndex.firstOperand, conditionOperandRa);
      operation.setInput(InputIndex.secondOperand, jumpAddress);

      newOperations.add(operation);

      return newOperations;
   }


   private static List<Operation> generateConditionalBranchOtherOffset(OperationName operationName, Operand conditionOperandRa, Operand jumpOffset, int pc, int nextPC) {
      List<Operation> newOperations = new ArrayList<Operation>();
      String tempReg1 = "t1";
      String tempReg2 = "t2";

      int jumpAddress = pc + jumpOffset.getValueAsIntegerLiteral();

      // Add first exit
      Operation operation = Operation.newOperation(operationName);
      operation.setInput(InputIndex.firstOperand, conditionOperandRa);
      operation.setInput(InputIndex.secondOperand, GeneralParsing.newMbLiteralOperand(pc+4));

      newOperations.add(operation);

      // Check if jumpOffset is literal
      if(jumpOffset.getOpType() == Operand.OpType.literal) {
         // Check if Jump Address is equal to nextPC    
         if(!(jumpAddress == nextPC)) {
            Operation exitOperation = Operation.newOperation(OperationName.exit);
            exitOperation.setInput(InputIndex.firstOperand, GeneralParsing.newMbLiteralOperand(jumpAddress));

            newOperations.add(exitOperation);
         }

         return newOperations;
      }

      // From here on, we know that jumpOffset is a Register
      Operand temp1 = GeneralParsing.newMbRegisterOperand(tempReg1);
      Operand temp2 = GeneralParsing.newMbRegisterOperand(tempReg2);

      // Integer_Add Operation
      operation = Operation.newOperation(OperationName.integer_add);
      operation.setInput(InputIndex.firstOperand, GeneralParsing.newMbLiteralOperand(pc));
      operation.setInput(InputIndex.secondOperand, jumpOffset);
      operation.setOutput(OutputIndex.firstResult, temp1);

      newOperations.add(operation);

      // Integer_Rsub Operation
      operation = Operation.newOperation(OperationName.integer_rsub);
      operation.setInput(InputIndex.firstOperand, temp1);
      operation.setInput(InputIndex.secondOperand, GeneralParsing.newMbLiteralOperand(nextPC));
      operation.setOutput(OutputIndex.firstResult, temp2);

      newOperations.add(operation);

      // Add final exit
      operation = Operation.newOperation(operationName);
      operation.setInput(InputIndex.firstOperand, temp2);
      operation.setInput(InputIndex.secondOperand, GeneralParsing.newMbLiteralOperand(jumpAddress));

      newOperations.add(operation);

      return newOperations;
   }

   public static int parseReturn(List<Instruction> instructions, List<Operation> operations, int index) {
      // Check if instruction has delay slot
      index = GeneralParsing.delaySlotCheck(instructions, operations, index);

      // Get Instruction
      Instruction instruction = instructions.get(index);

      // Get PC
      int pc = instruction.getAddress();

      // Calculate next_pc
      int nextPC;
      if(index < instructions.size()-1) {
         nextPC = instructions.get(index+1).getAddress();
      } else {
         nextPC = instructions.get(0).getAddress();
      }

      // Get operands
      List<Operand> operands = GeneralParsing.parseReadType1(instruction);

      List<Operation> newOperations = new ArrayList<Operation>();
      Operand returnAddress = operands.get(0);
      Operand jumpOffset = operands.get(1);

      boolean isLiteral = returnAddress.getOpType() == Operand.OpType.literal;
      if(isLiteral) {
         int jumpAddress = returnAddress.getValueAsIntegerLiteral() + jumpOffset.getValueAsIntegerLiteral();
         // Check nextPC
         boolean jumpEqualsNextPc = nextPC == jumpAddress;
         if(!jumpEqualsNextPc) {
            Operation newOperation = Operation.newOperation(OperationName.exit);
            newOperation.setInput(InputIndex.firstOperand, GeneralParsing.newMbLiteralOperand(jumpAddress));
            newOperations.add(newOperation);
         }
      } else {
         String tempReg1 = "t1";
         String tempReg2 = "t2";
         Operand temp1 = GeneralParsing.newMbRegisterOperand(tempReg1);
         Operand temp2 = GeneralParsing.newMbRegisterOperand(tempReg2);

         Operation integerAddOp = Operation.newOperation(OperationName.integer_add);
         integerAddOp.setInput(InputIndex.firstOperand, returnAddress);
         integerAddOp.setInput(InputIndex.secondOperand, jumpOffset);
         integerAddOp.setOutput(OutputIndex.firstResult, temp1);
         newOperations.add(integerAddOp);

         Operation integerRsubOp = Operation.newOperation(OperationName.integer_rsub);
         integerRsubOp.setInput(InputIndex.firstOperand, temp1);
         integerRsubOp.setInput(InputIndex.secondOperand, GeneralParsing.newMbLiteralOperand(nextPC));
         integerRsubOp.setOutput(OutputIndex.firstResult, temp2);
         newOperations.add(integerRsubOp);

         Operation exitOp = Operation.newOperation(OperationName.exit_not_equal);
         exitOp.setInput(InputIndex.firstOperand, temp2);
         exitOp.setInput(InputIndex.secondOperand, temp1);
         newOperations.add(exitOp);

         // OTher
         //newOperations = null;
         //System.out.println("UNCONDITION REGISTER NOT DONE");
      }


      // Update index
      index++;
      // Add operations
      for(Operation operation : newOperations) {
         operation.setAddress(instruction.getAddress());
         operations.add(operation);
      }

      return index;
   }

   private static OperationName getSynonymOperation(Instruction instruction) {
      switch(instruction.getOperation()) {
         case beq:
            return OperationName.exit_equal;
         case beqd:
            return OperationName.exit_equal;
         case beqi:
            return OperationName.exit_equal;
         case beqid:
            return OperationName.exit_equal;
         case bge:
            return OperationName.exit_greater_or_equal;
         case bged:
            return OperationName.exit_greater_or_equal;
         case bgei:
            return OperationName.exit_greater_or_equal;
         case bgeid:
            return OperationName.exit_greater_or_equal;
         case bgt:
            return OperationName.exit_greater;
         case bgtd:
            return OperationName.exit_greater;
         case bgti:
            return OperationName.exit_greater;
         case bgtid:
            return OperationName.exit_greater;
         case ble:
            return OperationName.exit_less_or_equal;
         case bled:
            return OperationName.exit_less_or_equal;
         case blei:
            return OperationName.exit_less_or_equal;
         case bleid:
            return OperationName.exit_less_or_equal;
         case blt:
            return OperationName.exit_less;
         case bltd:
            return OperationName.exit_less;
         case blti:
            return OperationName.exit_less;
         case bltid:
            return OperationName.exit_less;
         case bne:
            return OperationName.exit_not_equal;
         case bned:
            return OperationName.exit_not_equal;
         case bnei:
            return OperationName.exit_not_equal;
         case bneid:
            return OperationName.exit_not_equal;
         default:
            Logger.getLogger(Branches.class.getName()).
                    warning("Method not defined for instruction '"+instruction.getOperation()+"'");
            return null;
      }
   }

   private static OperationName getAntonymOperation(Instruction instruction) {
      switch(instruction.getOperation()) {
         case beq:
            return OperationName.exit_not_equal;
         case beqd:
            return OperationName.exit_not_equal;
         case beqi:
            return OperationName.exit_not_equal;
         case beqid:
            return OperationName.exit_not_equal;
         case bge:
            return OperationName.exit_less;
         case bged:
            return OperationName.exit_less;
         case bgei:
            return OperationName.exit_less;
         case bgeid:
            return OperationName.exit_less;
         case bgt:
            return OperationName.exit_less_or_equal;
         case bgtd:
            return OperationName.exit_less_or_equal;
         case bgti:
            return OperationName.exit_less_or_equal;
         case bgtid:
            return OperationName.exit_less_or_equal;
         case ble:
            return OperationName.exit_greater;
         case bled:
            return OperationName.exit_greater;
         case blei:
            return OperationName.exit_greater;
         case bleid:
            return OperationName.exit_greater;
         case blt:
            return OperationName.exit_greater_or_equal;
         case bltd:
            return OperationName.exit_greater_or_equal;
         case blti:
            return OperationName.exit_greater_or_equal;
         case bltid:
            return OperationName.exit_greater_or_equal;
         case bne:
            return OperationName.exit_equal;
         case bned:
            return OperationName.exit_equal;
         case bnei:
            return OperationName.exit_equal;
         case bneid:
            return OperationName.exit_equal;
         default:
            Logger.getLogger(Branches.class.getName()).
                    warning("Method not defined for instruction '"+instruction.getOperation()+"'");
            return null;
      }
   }





}
