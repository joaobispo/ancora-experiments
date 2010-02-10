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

package org.ancora.MicroBlaze;

import java.util.Arrays;
import org.ancora.MicroBlaze.Instructions.BranchInstruction;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.MicroBlaze.Instructions.InstructionWithDelaySlot;
import org.ancora.SharedLibrary.MicroBlazePackage.ParseUtils;

/**
 * Methods for operations related to MicroBlaze.
 *
 * @author Joao Bispo
 */
public class MicroBlazeUtils {

   /**
    * Parses the registers and immediate value from a String, and stores them
    * in an array, in the format [r1, r2, r3, imm]. If any of the elements is
    * not present, its corresponding value will be null.
    *
    * <p>Assumes that the arguments are separated by commas; Registers start
    * with an 'r'.
    *
    * <p>Example
    * <br>Input: r6, r6, 4
    * <br>Output: [6,6,null,4]
    *
    * @param registerString String containing the register portion of the trace
    * instruction.
    * @return an array of Integers of size 4. The first three elements always
    * refer to register numbers, and the last element to an immediate value.
    */
   public static Integer[] parseRegisters(String registerString) {
      // Split String
      String[] unparsedRegisters = registerString.split(",");

      int registerCounter = 0;
      Integer[] basicRegisters = new Integer[3];
      Integer immediate = null;

      for (int i = 0; i < unparsedRegisters.length; i++) {
         String regString = unparsedRegisters[i].trim();
         if (regString.startsWith("r")) {
            String registerNumber = regString.substring(1);
            basicRegisters[registerCounter] = ParseUtils.parseInt(registerNumber);
            registerCounter++;
         } else {
            immediate = ParseUtils.parseInt(regString);
         }
      }

      Integer[] registers = Arrays.copyOf(basicRegisters, basicRegisters.length + 1);
      int immIndex = registers.length - 1;
      registers[immIndex] = immediate;

      //        Instruction.buildRegisterArray(basicRegisters[0],
      //        basicRegisters[1], basicRegisters[2], immediate);

      return registers;
   }

   /**
    * Parses the given String with a Trace Instruction into a Instruction object.
    * 
    * @param traceInstruction String representing a MicroBlaze
    * Trace instruction.
    * @return a MicroBlaze Instruction.
    */
   public static Instruction parseTraceInstruction(String traceInstruction) {
      /// Split the trace instruction in parts
      int whiteSpaceIndex = ParseUtils.indexOfFirstWhiteSpace(traceInstruction);
      String addressString = traceInstruction.substring(0, whiteSpaceIndex);

      traceInstruction = traceInstruction.substring(whiteSpaceIndex).trim();
      whiteSpaceIndex = ParseUtils.indexOfFirstWhiteSpace(traceInstruction);
      String operationString = traceInstruction.substring(0, whiteSpaceIndex);

      String registersString = traceInstruction.substring(whiteSpaceIndex).trim();


      /// Parse Instruction Address
      // Remove prefix
      addressString = addressString.substring("0x".length());
      // Get Instruction Address
      int instructionAddress = Integer.valueOf(addressString, 16);

      /// Parse Registers
      Integer[] registers = parseRegisters(registersString);

      // Reorder registers according to write, read and immediate
      registers = Instruction.buildRegisterArray(operationString, registers);

      // Build Instruction
      Instruction instruction = new Instruction(instructionAddress, operationString,
              registers);

      return instruction;

   }

   /**
    * @param operation the operation name (add, lw...)
    * @return true if the given MicroBlaze operation is a branch. False otherwise.
    */
   public static boolean isBranch(String operation) {
      boolean isBranch = true;
      try{
         BranchInstruction.valueOf(operation);
      } catch (IllegalArgumentException ex) {
         isBranch = false;
      }

      return isBranch;
   }

   /**
    * @param operation the operation name (add, lw...)
    * @return true if the given MicroBlaze operation has a delay slot.
    */
   public static boolean hasDelaySlot(String operation) {
      boolean hasDelaySlot = true;
      try{
         InstructionWithDelaySlot.valueOf(operation);
      } catch (IllegalArgumentException ex) {
         hasDelaySlot = false;
      }

      return hasDelaySlot;
   }

  
   /**
    * DEFINITIONS
    */

   /**
    * The maximum number of registers a MicroBlaze trace instruction can have.
    */
   private static int MAX_REGISTERS = 3;
}
