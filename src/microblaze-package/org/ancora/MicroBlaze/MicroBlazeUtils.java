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

import org.ancora.MicroBlaze.Instructions.BranchInstruction;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.MicroBlaze.Instructions.InstructionWithDelaySlot;
import org.ancora.ShareLibrary.ParseUtils2;
import org.ancora.SharedLibrary.ParseUtils;

/**
 * Methods for operations related to MicroBlaze.
 *
 * @author Joao Bispo
 */
public class MicroBlazeUtils {

   /**
    * Parses the registers and immediate value from a String, and stores them
    * in an array. thegiven String array. Returns the number of arguments found.
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
    * @return an array of Integers compatible with the constructor of
    * Instruction.
    */
    public static Integer[] parseRegisters(String registerString) {
      // Split String
      String[] unparsedRegisters = registerString.split(",");

      int registerCounter = 0;
      Integer[] basicRegisters = new Integer[3];
      Integer immediate = null;

      for(int i=0; i<unparsedRegisters.length; i++) {
         String regString = unparsedRegisters[i].trim();
         if(regString.startsWith("r")) {
            String registerNumber = regString.substring(1);
            basicRegisters[registerCounter] = ParseUtils.parseInt(registerNumber);
            registerCounter++;
         } else {
            immediate = ParseUtils.parseInt(regString);
         }
      }

      Integer[] registers = Instruction.buildRegisterArray(basicRegisters[0],
              basicRegisters[1], basicRegisters[2], immediate);

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
      int whiteSpaceIndex = ParseUtils2.indexOfFirstWhiteSpace(traceInstruction);
      String addressString = traceInstruction.substring(0, whiteSpaceIndex);

      traceInstruction = traceInstruction.substring(whiteSpaceIndex).trim();
      whiteSpaceIndex = ParseUtils2.indexOfFirstWhiteSpace(traceInstruction);
      String operationString = traceInstruction.substring(0, whiteSpaceIndex);

      String registersString = traceInstruction.substring(whiteSpaceIndex).trim();


      /// Parse Instruction Address
      // Remove prefix
      addressString = addressString.substring("0x".length());
      // Get Instruction Address
      int instructionAddress = Integer.valueOf(addressString, 16);

      /// Parse Registers
      Integer[] registers = parseRegisters(registersString);

      // Build Instruction
      Instruction instruction = new Instruction(instructionAddress, operationString,
              registers);

      return instruction;
/*
      // Get Instruction Address
      String addressString = MicroblazeUtils.getTraceInstructionAddress(instruction);
      int address = Integer.valueOf(addressString, 16);


      // Cut the memory address
      String tempInstruction = instruction.substring(12);

      // Get Instruction without address
      String instructionProper = tempInstruction.trim();

      // Find first space. This will cut the operation name
      int endIndex = tempInstruction.indexOf(' ');
      String opName = tempInstruction.substring(0, endIndex);

      // Is Branch?
      InstructionBuilder instBuilder = InstructionBuilder.valueOf(opName);
      boolean isBranch = instBuilder.isBranch();

      boolean hasDelaySlot = instBuilder.hasDelaySlot();
*/

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
    * Extracts the register from an instruction string and stores them in the
    * given String array. Returns the number of arguments found.
    *
    * <p>Assumes that the arguments are separated by commas and start with r.
    * @param instruction instruction to parse
    * @param results String array to hold the parsed arguments
    * @return the number of arguments found
    */
   /*
   public static int parseRegisters(String instruction, String[] results) {
      int numArgs = 0;

      // Check the first comma
      int beginIndex = 0;
      int indexOfComma = instruction.indexOf(COMMA);

      boolean hasCommas = indexOfComma != -1;
      while (hasCommas) {
         // Extract Argument
         String otherArg = instruction.substring(beginIndex, indexOfComma);
         results[numArgs] = otherArg.trim();
         numArgs++;

         // Update Indexes
         beginIndex = indexOfComma + 1;
         indexOfComma = instruction.indexOf(COMMA, beginIndex);

         hasCommas = indexOfComma != -1;
      }

      // Extract last argument
      String otherArg = instruction.substring(beginIndex);
      results[numArgs] = otherArg.trim();
      numArgs++;


      return numArgs;
   }
    */

   /**
    * DEFINITIONS
    */

   /**
    * The maximum number of registers a MicroBlaze trace instruction can have.
    */
   private static int MAX_REGISTERS = 3;
}
