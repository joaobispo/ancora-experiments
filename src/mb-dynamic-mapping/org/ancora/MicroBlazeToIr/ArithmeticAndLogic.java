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
public class ArithmeticAndLogic {

   public static int parseIntegerOpWithCarry(List<Instruction> instructions, List<Operation> operations, int index) {
      Instruction instruction = instructions.get(index);
      InstructionName instructionName = instruction.getOperation();

      OperationName operationName = getCarryOperationName(instructionName);
      Operation operation = Operation.newOperation(operationName);


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

      // Check carries
      if(hasCarryIn.contains(instructionName)) {
         operation.setInput(InputIndex.carry, GeneralParsing.newMbCarryRegisterOperand());
      }
      if(hasCarryOut.contains(instructionName)) {
         operation.setOutput(OutputIndex.carry, GeneralParsing.newMbCarryRegisterOperand());
      }
      

      //
      // At this point, optimizations can be made
      //
      List<Operation> optimizedOperations = Optimizations.computeLiteralsOfCarryOperation(operation);
      

      // Update index - read one MicroBlaze instruction
      index++;
      // Update operation list
      operations.addAll(optimizedOperations);

      return index;
   }

   public static int parseIntegerBinaryOperation(List<Instruction> instructions, List<Operation> operations, int index) {
      Instruction instruction = instructions.get(index);
      InstructionName instructionName = instruction.getOperation();

      OperationName operationName = getBinaryOperationName(instructionName);
      Operation operation = Operation.newOperation(operationName);

      // Set Address
      operation.setAddress(instruction.getAddress());


      // Set inputs
      List<Operand> inputs = GeneralParsing.parseReadType1(instruction);
      operation.setInput(InputIndex.firstOperand, inputs.get(0));
      operation.setInput(InputIndex.secondOperand, inputs.get(1));

      // Set Output
      List<Operand> outputs = GeneralParsing.parseWriteType1(instruction);
      operation.setOutput(OutputIndex.firstResult, outputs.get(0));


      //
      // At this point, optimizations can be made
      //
      List<Operation> optimizedOperations = Optimizations.computeLiteralsOfBinaryOperation(operation);


      // Update index - read one MicroBlaze instruction
      index++;
      // Update operation list
      operations.addAll(optimizedOperations);

      return index;
   }

   public static int parseIntegerUnaryOperation(List<Instruction> instructions, List<Operation> operations, int index) {
      Instruction instruction = instructions.get(index);
      InstructionName instructionName = instruction.getOperation();

      OperationName operationName = getUnaryOperationName(instructionName);
      Operation operation = Operation.newOperation(operationName);

      // Set Address
      operation.setAddress(instruction.getAddress());


      // Set inputs
      List<Operand> inputs = GeneralParsing.parseReadType1(instruction);
      operation.setInput(InputIndex.firstOperand, inputs.get(0));

      // Check for special case srl and sra
      if(instructionName == InstructionName.sra || instructionName == InstructionName.srl) {
         operation.setInput(InputIndex.secondOperand, GeneralParsing.newMbLiteralOperand(1));
         // Add write to carry operation
         Operation carryOperation = Operation.newOperation(OperationName.move_bit);
         carryOperation.setAddress(instruction.getAddress());
         carryOperation.setInput(InputIndex.firstOperand, inputs.get(0));
         carryOperation.setInput(InputIndex.source_bit, GeneralParsing.newMbLiteralOperand(31));
         carryOperation.setInput(InputIndex.destination_bit, GeneralParsing.newMbLiteralOperand(0));
         carryOperation.setOutput(OutputIndex.firstResult, GeneralParsing.newMbCarryRegisterOperand());
         operations.add(carryOperation);
      }

      // Set Output
      List<Operand> outputs = GeneralParsing.parseWriteType1(instruction);
      operation.setOutput(OutputIndex.firstResult, outputs.get(0));


      //
      // At this point, optimizations can be made
      //
      List<Operation> optimizedOperations = Optimizations.computeLiteralsOfBinaryOperation(operation);


      // Update index - read one MicroBlaze instruction
      index++;
      // Update operation list
      operations.addAll(optimizedOperations);

      return index;
   }


   public static int parseImm(List<Instruction> instructions, List<Operation> operations, int index) {
      Instruction instruction = instructions.get(index);

      // Get next operation and change IMM value
      Instruction typeBInst = instructions.get(index+1);

      int upperImm = instruction.getImmediate();
      int typeBImm = typeBInst.getImmediate();
      // Fuse IMM with typeB
      int newInt = GeneralParsing.fuseInt(upperImm, typeBImm);
      // Save changes
      typeBInst.setImmediate(newInt);
      instructions.set(index+1, typeBInst);
      // Update index - read one MicroBlaze instruction
      index++;

      return index;
   }

   private static OperationName getCarryOperationName(InstructionName instructionName) {
      switch(instructionName) {
         case add:
            return OperationName.integer_add;
         case addc:
            return OperationName.integer_add;
         case addk:
            return OperationName.integer_add;
         case addkc:
            return OperationName.integer_add;
         case addi:
            return OperationName.integer_add;
         case addic:
            return OperationName.integer_add;
         case addik:
            return OperationName.integer_add;
         case addikc:
            return OperationName.integer_add;
         case rsub:
            return OperationName.integer_rsub;
         case rsubc:
            return OperationName.integer_rsub;
         case rsubk:
            return OperationName.integer_rsub;
         case rsubkc:
            return OperationName.integer_rsub;
         case rsubi:
            return OperationName.integer_rsub;
         case rsubic:
            return OperationName.integer_rsub;
         case rsubik:
            return OperationName.integer_rsub;
         case rsubikc:
            return OperationName.integer_rsub;
         default:
            Logger.getLogger(ArithmeticAndLogic.class.getName()).
                    warning("Method not defined for instruction '"+instructionName+"'");
            return null;
      }
   }

   private static OperationName getBinaryOperationName(InstructionName instructionName) {
      switch (instructionName) {
         case mul:
            return OperationName.integer_mul;
         case muli:
            return OperationName.integer_mul;
         case idiv:
            return OperationName.integer_div;
         case idivu:
            return OperationName.integer_div_unsigned;
         case and:
            return OperationName.integer_and;
         case andi:
            return OperationName.integer_and;
         case andn:
            return OperationName.integer_and_not;
         case andni:
            return OperationName.integer_and_not;
         case or:
            return OperationName.integer_or;
         case ori:
            return OperationName.integer_or;
         case xor:
            return OperationName.integer_xor;
         case xori:
            return OperationName.integer_xor;
         case bsrl:
            return OperationName.integer_srl;
         case bsrli:
            return OperationName.integer_srl;
         case bsra:
            return OperationName.integer_sra;
         case bsrai:
            return OperationName.integer_sra;
         case bsll:
            return OperationName.integer_sll;
         case bslli:
            return OperationName.integer_sll;
         case cmp:
            return OperationName.integer_compare_msb;
         case cmpu:
            return OperationName.integer_compare_msb_unsigned;
         default:
            Logger.getLogger(ArithmeticAndLogic.class.getName()).
                    warning("Method not defined for instruction '"+instructionName+"'");
            return null;
      }
   }

   private static OperationName getUnaryOperationName(InstructionName instructionName) {
      switch (instructionName) {
         case sext8:
            return OperationName.integer_sext_8;
         case sext16:
            return OperationName.integer_sext_16;
         case sra:
            return OperationName.integer_sra;
         case srl:
            return OperationName.integer_srl;
         default:
            Logger.getLogger(ArithmeticAndLogic.class.getName()).
                    warning("Method not defined for instruction '" + instructionName + "'");
            return null;
      }
   }



   /**
    * Which Instructions have Carry Out
    */
   private static final EnumSet<InstructionName> hasCarryOut = EnumSet.of(
           InstructionName.add,
           InstructionName.addc,
           InstructionName.addi,
           InstructionName.addic,
           InstructionName.rsub,
           InstructionName.rsubc,
           InstructionName.rsubi,
           InstructionName.rsubic);

   private static final EnumSet<InstructionName> hasCarryIn = EnumSet.of(
           InstructionName.addc,
           InstructionName.addkc,
           InstructionName.addic,
           InstructionName.addikc,
           InstructionName.rsubc,
           InstructionName.rsubkc,
           InstructionName.rsubic,
           InstructionName.rsubikc
           );







 


}
