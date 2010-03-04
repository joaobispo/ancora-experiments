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

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import org.ancora.IrForDynamicMapping.Operation;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.MicroBlaze.Instructions.InstructionName;

/**
 * Receives a list of MicroBlaze Instructions and returns a list of IR 
 * Operations.
 *
 * @author Joao Bispo
 */
public class MbToIrParser {

   public static List<Operation> parseInstructions(List<Instruction> instructions) {
      // Start index
      int index = 0;
      // List of Operations
      List<Operation> operations = new LinkedList<Operation>();

      while(index < instructions.size()) {
         index = parseIntruction(instructions, operations, index);
      }

      return operations;
   }

   public static int parseIntruction(List<Instruction> instructions, List<Operation> operations, int index) {
      InstructionName instructionName = instructions.get(index).getOperation();
      switch(instructionName) {
         case add:
            return ArithmeticAndLogic.parseIntegerOpWithCarry(instructions, operations, index);
         case addc:
            return ArithmeticAndLogic.parseIntegerOpWithCarry(instructions, operations, index);
         case addk:
            return ArithmeticAndLogic.parseIntegerOpWithCarry(instructions, operations, index);
         case addkc:
            return ArithmeticAndLogic.parseIntegerOpWithCarry(instructions, operations, index);
         case addi:
            return ArithmeticAndLogic.parseIntegerOpWithCarry(instructions, operations, index);
         case addic:
            return ArithmeticAndLogic.parseIntegerOpWithCarry(instructions, operations, index);
         case addik:
            return ArithmeticAndLogic.parseIntegerOpWithCarry(instructions, operations, index);
         case addikc:
            return ArithmeticAndLogic.parseIntegerOpWithCarry(instructions, operations, index);

         case rsub:
            return ArithmeticAndLogic.parseIntegerOpWithCarry(instructions, operations, index);
         case rsubc:
            return ArithmeticAndLogic.parseIntegerOpWithCarry(instructions, operations, index);
         case rsubk:
            return ArithmeticAndLogic.parseIntegerOpWithCarry(instructions, operations, index);
         case rsubkc:
            return ArithmeticAndLogic.parseIntegerOpWithCarry(instructions, operations, index);
         case rsubi:
            return ArithmeticAndLogic.parseIntegerOpWithCarry(instructions, operations, index);
         case rsubic:
            return ArithmeticAndLogic.parseIntegerOpWithCarry(instructions, operations, index);
         case rsubik:
            return ArithmeticAndLogic.parseIntegerOpWithCarry(instructions, operations, index);
         case rsubikc:
            return ArithmeticAndLogic.parseIntegerOpWithCarry(instructions, operations, index);

         case mul:
            return ArithmeticAndLogic.parseIntegerBinaryOperation(instructions, operations, index);
         case muli:
            return ArithmeticAndLogic.parseIntegerBinaryOperation(instructions, operations, index);
         case idiv:
            return ArithmeticAndLogic.parseIntegerBinaryOperation(instructions, operations, index);
         case idivu:
            return ArithmeticAndLogic.parseIntegerBinaryOperation(instructions, operations, index);
         case and:
            return ArithmeticAndLogic.parseIntegerBinaryOperation(instructions, operations, index);
         case andi:
            return ArithmeticAndLogic.parseIntegerBinaryOperation(instructions, operations, index);
         case andn:
            return ArithmeticAndLogic.parseIntegerBinaryOperation(instructions, operations, index);
         case andni:
            return ArithmeticAndLogic.parseIntegerBinaryOperation(instructions, operations, index);
         case or:
            return ArithmeticAndLogic.parseIntegerBinaryOperation(instructions, operations, index);
         case ori:
            return ArithmeticAndLogic.parseIntegerBinaryOperation(instructions, operations, index);
         case xor:
            return ArithmeticAndLogic.parseIntegerBinaryOperation(instructions, operations, index);
         case xori:
            return ArithmeticAndLogic.parseIntegerBinaryOperation(instructions, operations, index);
         case bsrl:
            return ArithmeticAndLogic.parseIntegerBinaryOperation(instructions, operations, index);
         case bsrli:
            return ArithmeticAndLogic.parseIntegerBinaryOperation(instructions, operations, index);
         case bsra:
            return ArithmeticAndLogic.parseIntegerBinaryOperation(instructions, operations, index);
         case bsrai:
            return ArithmeticAndLogic.parseIntegerBinaryOperation(instructions, operations, index);
         case bsll:
            return ArithmeticAndLogic.parseIntegerBinaryOperation(instructions, operations, index);
         case bslli:
            return ArithmeticAndLogic.parseIntegerBinaryOperation(instructions, operations, index);
         case cmp:
            return ArithmeticAndLogic.parseIntegerBinaryOperation(instructions, operations, index);
         case cmpu:
            return ArithmeticAndLogic.parseIntegerBinaryOperation(instructions, operations, index);

         case sext8:
            return ArithmeticAndLogic.parseIntegerUnaryOperation(instructions, operations, index);
         case sext16:
            return ArithmeticAndLogic.parseIntegerUnaryOperation(instructions, operations, index);
         case sra:
            return ArithmeticAndLogic.parseIntegerUnaryOperation(instructions, operations, index);
         case srl:
            return ArithmeticAndLogic.parseIntegerUnaryOperation(instructions, operations, index);

         case br:
            return Branches.parseUnconditionalBranch(instructions, operations, index);
         case bri:
            return Branches.parseUnconditionalBranch(instructions, operations, index);
         case brd:
            return Branches.parseUnconditionalBranch(instructions, operations, index);
         case brid:
            return Branches.parseUnconditionalBranch(instructions, operations, index);
         case brld:
            return Branches.parseUnconditionalBranch(instructions, operations, index);
         case brlid:
            return Branches.parseUnconditionalBranch(instructions, operations, index);
         case bra:
            return Branches.parseUnconditionalBranch(instructions, operations, index);
         case brai:
            return Branches.parseUnconditionalBranch(instructions, operations, index);
         case brad:
            return Branches.parseUnconditionalBranch(instructions, operations, index);
         case braid:
            return Branches.parseUnconditionalBranch(instructions, operations, index);
         case brald:
            return Branches.parseUnconditionalBranch(instructions, operations, index);
         case bralid:
            return Branches.parseUnconditionalBranch(instructions, operations, index);

         case beq:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case beqd:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case beqi:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case beqid:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case bge:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case bged:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case bgei:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case bgeid:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case bgt:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case bgtd:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case bgti:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case bgtid:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case ble:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case bled:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case blei:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case bleid:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case blt:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case bltd:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case blti:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case bltid:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case bne:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case bned:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case bnei:
            return Branches.parseConditionalBranch(instructions, operations, index);
         case bneid:
            return Branches.parseConditionalBranch(instructions, operations, index);
           
         case rtsd:
            return Branches.parseReturn(instructions, operations, index);

         case lw:
            return Memory.parseMemoryOperation(instructions, operations, index);
         case lwi:
            return Memory.parseMemoryOperation(instructions, operations, index);
         case lbu:
            return Memory.parseMemoryOperation(instructions, operations, index);
         case lbui:
            return Memory.parseMemoryOperation(instructions, operations, index);
         case lhu:
            return Memory.parseMemoryOperation(instructions, operations, index);
         case lhui:
            return Memory.parseMemoryOperation(instructions, operations, index);
         case sw:
            return Memory.parseMemoryOperation(instructions, operations, index);
         case swi:
            return Memory.parseMemoryOperation(instructions, operations, index);
         case sb:
            return Memory.parseMemoryOperation(instructions, operations, index);
         case sbi:
            return Memory.parseMemoryOperation(instructions, operations, index);
         case sh:
            return Memory.parseMemoryOperation(instructions, operations, index);
         case shi:
            return Memory.parseMemoryOperation(instructions, operations, index);

         case imm:
            return ArithmeticAndLogic.parseImm(instructions, operations, index);


         default:
            Logger.getLogger(MbToIrParser.class.getName()).
                    warning("Parsing for instruction '"+instructionName+"' not implemented.");
            index++;
            return index;
      }
   }

}
