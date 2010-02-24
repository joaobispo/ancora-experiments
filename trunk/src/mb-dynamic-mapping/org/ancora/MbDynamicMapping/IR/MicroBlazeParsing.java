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

package org.ancora.MbDynamicMapping.IR;

import java.util.logging.Logger;
import org.ancora.MbDynamicMapping.Interface.InstructionWindow;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.SharedLibrary.MbDynamicMapping.MicroBlaze.InstructionName;
import org.ancora.SharedLibrary.MbDynamicMapping.MicroBlaze.MicroBlazeParam;

/**
 * Methods related to parsing MicroBlaze instructions to the 
 * Intermediate Representation.
 *
 * @author Joao Bispo
 */
public class MicroBlazeParsing {

   public static void parseOperandsType1(Instruction mbInstruction, Operation operation) {
      int registerSize = MicroBlazeParam.REGISTER_SIZE_BITS;

      // Parse read registers
      Integer[] readRegs = mbInstruction.getReadRegisters();
      for(Integer reg : readRegs) {
         Operand newData = null;
         // Check if is r0. If yes, this is considered as literal
         if(reg == 0) {
            newData = new Operand(Operand.OpType.literal, Integer.toString(0), registerSize);
         }
         //Add as a register
         else {
            newData = new Operand(Operand.OpType.register, Operand.registerAsString(reg), registerSize);
         }

         operation.getInputs().add(newData);
      }

      // Parse immediate and sign-extend it
      Integer immReg = mbInstruction.getImmediate();
      if(immReg != null) {
         operation.getInputs().add(new Operand(Operand.OpType.literal, Integer.toString(immReg), registerSize));
      }

      // Parse output
      Integer writeReg = mbInstruction.getWriteRegister();
      if(writeReg != null) {
         operation.getOutputs().add(new Operand(Operand.OpType.register, Operand.registerAsString(writeReg), registerSize));
      }
   }

   public static Operand buildCarryRegister() {
      return new Operand(Operand.OpType.register, CARRY_REGISTER_NAME, 1);
   }

   public static Operation parseInstruction(Instruction mbInstruction, InstructionWindow instWindow) {
      // Get InstructionName enum
      InstructionName instructionName = InstructionName.getEnum(mbInstruction.getOperation());
      Operation operation = new Operation();

      if(instructionName == null) {
         return operation;
      }

      // Set Address
      operation.setAddress(mbInstruction.getAddress());

      switch(instructionName) {
         case add:
            operation.setOperation(OperationName.integer_add);
            parseOperandsType1(mbInstruction, operation);
            operation.getOutputs().add(buildCarryRegister());
            return operation;
         case addc:
            operation.setOperation(OperationName.integer_add);
            parseOperandsType1(mbInstruction, operation);
            operation.getInputs().add(buildCarryRegister());
            operation.getOutputs().add(buildCarryRegister());
            return operation;
         case addk:
            operation.setOperation(OperationName.integer_add);
            parseOperandsType1(mbInstruction, operation);
            return operation;
         case addkc:
            operation.setOperation(OperationName.integer_add);
            parseOperandsType1(mbInstruction, operation);
            operation.getInputs().add(buildCarryRegister());
            return operation;
         case addi:
            operation.setOperation(OperationName.integer_add);
            parseOperandsType1(mbInstruction, operation);
            operation.getOutputs().add(buildCarryRegister());
            return operation;
         case addic:
            operation.setOperation(OperationName.integer_add);
            parseOperandsType1(mbInstruction, operation);
            operation.getInputs().add(buildCarryRegister());
            operation.getOutputs().add(buildCarryRegister());
            return operation;
         case addik:
            operation.setOperation(OperationName.integer_add);
            parseOperandsType1(mbInstruction, operation);
            return operation;
         case addikc:
            operation.setOperation(OperationName.integer_add);
            parseOperandsType1(mbInstruction, operation);
            operation.getInputs().add(buildCarryRegister());
            return operation;
         default:
            Logger.getLogger(MicroBlazeParsing.class.getName()).
                    warning("No parsing yet for instruction '"+instructionName+"'");
            return operation;
      }

   }

   /**
    * INSTANCE VARIABLES
    */
   public static final String CARRY_REGISTER_NAME = "MSR_C";
}
