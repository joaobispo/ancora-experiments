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

package org.ancora.MicroBlaze.Instructions;

/**
 * Represents a parsed MicroBlaze Instruction.
 *
 * @author Joao Bispo
 */
public class Instruction {

   public Instruction(int address, String operation, Integer reg1, Integer reg2, Integer reg3, Integer immediate) {
      this.address = address;
      this.operation = operation;
      this.registers = new Integer[Register.values().length];

      this.registers[Register.register1.ordinal()] = reg1;
      this.registers[Register.register2.ordinal()] = reg2;
      this.registers[Register.register3.ordinal()] = reg3;
      this.registers[Register.immediate.ordinal()] = immediate;

      /*
      this.reg1 = reg1;
      this.reg2 = reg2;
      this.reg3 = reg3;
      this.immediate = immediate;
       */
   }

   public int getAddress() {
      return address;
   }

   public String getOperation() {
      return operation;
   }

   public Integer getRegister(Register register) {
      return registers[register.ordinal()];
   }

   /*
   public Integer getReg1() {
      return reg1;
   }

   public Integer getReg2() {
      return reg2;
   }

   public Integer getReg3() {
      return reg3;
   }

   public Integer getImmediate() {
      return immediate;
   }
*/

   public enum Register {
      register1,
      register2,
      register3,
      immediate
   }

   /**
    * INSTANCE VARIABLES
    */
   private final int address;
   private final String operation;
   private final Integer[] registers;
   /*
   private final Integer reg1;
   private final Integer reg2;
   private final Integer reg3;
   private final Integer immediate;
    */
}
