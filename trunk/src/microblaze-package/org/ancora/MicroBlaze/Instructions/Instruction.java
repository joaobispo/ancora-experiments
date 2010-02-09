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

import java.util.logging.Logger;
import org.ancora.MicroBlaze.Trace.TraceRegisters;
import org.ancora.ShareLibrary.BitUtils;

/**
 * Represents a parsed MicroBlaze Instruction.
 *
 * @author Joao Bispo
 */
public class Instruction {

   public Instruction(int address, String operation, Integer[] registers) {
      this.address = address;
      this.operation = operation;
      this.registers = registers;
      
   }

   /**
    * Builds the Register Array, by assigning values to writeRegister,
    * readRegisters and Immediate, according to a given instruction name.
    *
    * <p>Example:
    * <br>Integer[] parsedRegisters = {1,null,null,100};
    * <br>Integer[] registers = buildRegisterArray(parsedRegisters);
    *
    * <p>Null values can be used for registers which does not have a value.
    *
    * @param reg1 value of register 1
    * @param reg2 value of register 2
    * @param reg3 value of register 3
    * @param immediate value the immediate
    * @return an array of Integers, with the values of the registers correctly
    * ordered, as in the enum Intruction.Register.
    */
   /*
   public static Integer[] buildRegisterArray(Integer writeReg, Integer readRegA, Integer readRegB, Integer immediate) {
      Integer[] registers = new Integer[Register.values().length];

      registers[Register.writeRegister.ordinal()] = writeReg;
      registers[Register.readRegister1.ordinal()] = readRegA;
      registers[Register.readRegister2.ordinal()] = readRegB;
      registers[Register.immediate.ordinal()] = immediate;

      return registers;
   }
   */


   /**
    * Builds the Register Array, by assigning values to writeRegister,
    * readRegisters and Immediate, according to a given instruction name and the
    * values parsed in a trace instruction.
    *
    * <p>Example:
    * <br>Integer[] parsedRegisters = {1,2,null,null};
    * <br>Integer[] registers = buildRegisterArray("beq",parsedRegisters);
    * <br>registers -> {null, 1, 2, null}
    *
    * <p>Null values can be used for registers which does not have a value.
    *
    * @param operation name of the operation
    * @param traceIntegers array of integers from the parsed trace
    * @return an array of Integers, with the values of the registers correctly
    * ordered, as in the enum Intruction.Register.
    */
   public static Integer[] buildRegisterArray(String operation, Integer[] traceIntegers) {
      Integer[] registers = new Integer[Register.values().length];

      // Get properties of registers
      TraceRegisters.Property[] properties = TraceRegisters.getProperties(operation);
      int readCounter = 0;
      for (int i = 0; i < properties.length; i++) {
         Register register = null;
         switch (properties[i]) {
            case read:
               if (readCounter == 0) {
                  register = Register.readRegister1;
                  readCounter++;
               } else {
                  register = Register.readRegister2;
               }
               break;
            case write:
               register = Register.writeRegister;
               break;
            default:
               Logger.getLogger(Instruction.class.getName()).
                       warning(TraceRegisters.class.getName()+
                       " not supported:'"+properties[i].name()+"'.");
               break;
         }

         registers[register.ordinal()] = traceIntegers[i];
      }

      // Immediate is always in the last position of the array
      int immIndex = traceIntegers.length - 1;
      registers[Register.immediate.ordinal()] = traceIntegers[immIndex];

      //registers[Register.writeRegister.ordinal()] = writeReg;
      //registers[Register.readRegister1.ordinal()] = readRegA;
      //registers[Register.readRegister2.ordinal()] = readRegB;
      //registers[Register.immediate.ordinal()] = immediate;

      return registers;
   }

   public int getAddress() {
      return address;
   }

   public String getOperation() {
      return operation;
   }

   private Integer getRegister(Register register) {
      return registers[register.ordinal()];
   }

   /**
    * @return the read registers of this instruction.
    */
   public Integer[] getReadRegisters() {
      Integer reg1 = getRegister(Register.readRegister1);
      if (reg1 == null) {
         return new Integer[0];
      }

      Integer reg2 = getRegister(Register.readRegister2);
      if (reg2 == null) {
         Integer[] result = new Integer[1];
         result[0] = reg1;
         return result;
      }

      Integer[] result = new Integer[2];
      result[0] = reg1;
      result[1] = reg2;
      return result;

   }

   /**
    * @return the write register of this instruction.
    */
   public Integer getWriteRegister() {
      Integer reg1 = getRegister(Register.writeRegister);
      if (reg1 == null) {
         return null;
      } else {
         return reg1;
      }
   }

   /**
    * @return the Immediate value of this instruction.
    */
   public Integer getImmediate() {
      Integer imm = getRegister(Register.immediate);
      if (imm == null) {
         return null;
      } else {
         return imm;
      }
   }



   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      builder.append(BitUtils.padHexString(Integer.toHexString(address), 8));
      builder.append(" ");
      builder.append(operation);
      //builder.append(" ");

      for (Register register : Register.values()) {
         Integer value = registers[register.ordinal()];
         if (value != null) {
            builder.append(" ");
            builder.append(register.getRegName());
            builder.append("=");
            builder.append(value);
         }
      }

      /*
      int counter = 0;
      boolean firstNonNull = false;
      while(!firstNonNull) {
         if(registers[counter] != null) {
            if(Register.immediate.ordinal() != counter) {
               builder.append("r");
            }
            builder.append(registers[counter]);
            firstNonNull = true;
         }
         counter++;
      }

      while (counter < registers.length) {
         if (registers[counter] != null) {
            builder.append(", ");
            if (Register.immediate.ordinal() != counter) {
               builder.append("r");
            }
            builder.append(registers[counter]);
         }
         counter++;
      }
       */

      return builder.toString();
   }



   public enum Register {
      writeRegister("WriteReg"),
      readRegister1("ReadReg1"),
      readRegister2("ReadReg2"),
      immediate("Imm");

      private Register(String regName) {
         this.regName = regName;
      }

      public String getRegName() {
         return regName;
      }

      /**
       * Instance Variables
       */
      final private String regName;
   }

   /**
    * INSTANCE VARIABLES
    */
   private final int address;
   private final String operation;
   private final Integer[] registers;
}
