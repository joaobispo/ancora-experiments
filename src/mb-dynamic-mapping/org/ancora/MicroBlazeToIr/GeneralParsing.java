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
import org.ancora.IrForDynamicMapping.Operand;
import org.ancora.MicroBlaze.Instructions.Instruction;

/**
 * Utility methods for parsing the MicroBlaze Instructions to the Intermediate
 * Representation.
 *
 * @author Joao Bispo
 */
public class GeneralParsing {

   /**
    * Does an operand parsing of type one to the read registers and immediate value.
    * @param instruction
    * @return
    */
   public static List<Operand> parseReadType1(Instruction instruction) {
      int registerSize = REGISTER_SIZE_BITS;
      List<Operand> operands = new ArrayList<Operand>();

      // Parse read registers
      Integer[] readRegs = instruction.getReadRegisters();
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

         operands.add(newData);
      }

      // Parse immediate and sign-extend it
      Integer immReg = instruction.getImmediate();
      if(immReg != null) {
         operands.add(new Operand(Operand.OpType.literal, Integer.toString(immReg), registerSize));
      }

      return operands;
   }

   /**
    * Does an operand parsing of type one to the write register.
    * @param instruction
    * @return
    */
   public static List<Operand> parseWriteType1(Instruction instruction) {
      int registerSize = REGISTER_SIZE_BITS;
      List<Operand> operands = new ArrayList<Operand>();

      // Parse output
      Integer writeReg = instruction.getWriteRegister();
      if(writeReg != null) {
         operands.add(new Operand(Operand.OpType.register, Operand.registerAsString(writeReg), registerSize));
      }

      return operands;
   }

   public static Operand buildCarryRegister() {
      return new Operand(Operand.OpType.register, CARRY_REGISTER_NAME, 1);
   }

   /**
    * DEFINITIONS
    */
   /**
    * Size in bits of a MicroBlaze Register
    */
   public static final int REGISTER_SIZE_BITS = 32;
   //public static final int IMMEDIATE_SIZE_BITS = 16;
   /**
    * Name given to the Carry Register
    */
   public static final String CARRY_REGISTER_NAME = "msr_c";

   public static final boolean COMPUTE_LITERALS = true;
}
