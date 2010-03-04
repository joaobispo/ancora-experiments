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
import org.ancora.IrForDynamicMapping.Operand.OpType;
import org.ancora.IrForDynamicMapping.Operation;
import org.ancora.IrForDynamicMapping.OperationName;
import org.ancora.IrForDynamicMapping.OutputIndex;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.MicroBlaze.Instructions.InstructionName;
import org.ancora.MicroBlaze.MicroBlazeUtils;

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
         Operand newData;
         // Check if is r0. If yes, this is considered as literal
         if(writeReg == 0) {
            newData = new Operand(Operand.OpType.literal, Integer.toString(0), registerSize);
         } else {
            newData = new Operand(Operand.OpType.register, Operand.registerAsString(writeReg), registerSize);
         }

         operands.add(newData);
      }

      return operands;
   }

   public static Operand newMbCarryRegisterOperand() {
      return new Operand(Operand.OpType.register, CARRY_REGISTER_NAME, 1);
   }


   public static Operand newMbLiteralOperand(int value) {
      return new Operand(Operand.OpType.literal, Integer.toString(value), REGISTER_SIZE_BITS);
   }

   public static Operand newMbRegisterOperand(String register) {
      return new Operand(Operand.OpType.register, register, REGISTER_SIZE_BITS);
   }

   /**
    *
    * @param operation
    * @return true if all inputs of the operation are literals. False otherwise
    */
   public static boolean areOperandsLiterals(Operand[] operands) {
      for (Operand operand : operands) {
         if (operand == null) {
            continue;
         }

         boolean isLiteral = operand.getOpType() == OpType.literal;
         if (!isLiteral) {
            return false;
         }

      }
      
      return true;
   }

   /**
    * The output operand will have as many bits as the operand with the most bits.
    * @param inputs
    * @return a literal operand with the integer sum of the given operands, or 
    * null if at least one of the operands was not a literal.
    */
   public static Operand integerSumOperands(Operand[] operands) {
      // Check if operands are literals
      if(!areOperandsLiterals(operands)) {
         return null;
      }

      int sum = 0;
      int numBits = 0;
      for(Operand operand : operands) {
         if(operand == null) {
            continue;
         }
         
         sum += operand.getValueAsIntegerLiteral();
         numBits = Math.max(numBits, operand.getBitSize());
      }
      return new Operand(OpType.literal, Integer.toString(sum), numBits);
   }

    /**
     * Calculates the carryOut of the sum of rA with rB and carry.
     * Operation is rA + rB + carry.
     *
     * @param rA
     * @param rB
     * @param carry the carry from the previous operation. Should be 0 or 1.
     * @return 1 if there is carry out, or 0 if not.
     */
    public static int getCarryOutAdd(int rA, int rB, int carry) {
        if(carry != 0 && carry != 1) {
            Logger.getLogger(GeneralParsing.class.getName()).
                    warning("Carry is different than 0 or 1 ("+
                    carry+")");
        }

        //System.out.println("rA:"+Integer.toBinaryString(rA));
        //System.out.println("rB:"+Integer.toBinaryString(rB));

        // Extend operands to long and mask them
        long lRa = rA & MASK_32_BITS;
        long lRb = rB & MASK_32_BITS;
        // Carry must be 0 or 1, it shouldn't need to be masked.
        long lCarry = carry;


        //System.out.println("lRa:"+Long.toBinaryString(lRa));
        //System.out.println("lRb:"+Long.toBinaryString(lRb));

        // Do the summation
        long result = lRa + lRb + lCarry;

        //System.out.println("Result:"+Long.toBinaryString(result));

        // Get the carry bit
        int carryOut = (int) ((result & MASK_BIT_33) >>> 32);
        return carryOut;
    }

    /**
     * Calculates the carryOut of the reverse subtraction of rA with rB and
     * carry. Operation is rB + ~rA + carry.
     *
     * @param rA
     * @param rB
     * @param carry the carry from the previous operation. Should be 0 or 1.
     * @return 1 if there is carry out, or 0 if not.
     */
    public static int getCarryOutRsub(int rA, int rB, int carry) {
        if(carry != 0 && carry != 1) {
            Logger.getLogger(GeneralParsing.class.getName()).
                    warning("Carry is different than 0 or 1 ("+
                    carry+")");
        }

        //System.out.println("rA:"+Integer.toBinaryString(rA));
        //System.out.println("rB:"+Integer.toBinaryString(rB));

        // Extend operands to long and mask them
        long lRa = rA & MASK_32_BITS;
        long lRb = rB & MASK_32_BITS;
        // Carry must be 0 or 1, it shouldn't need to be masked.
        long lCarry = carry;


        //System.out.println("lRa:"+Long.toBinaryString(lRa));
        //System.out.println("lRb:"+Long.toBinaryString(lRb));

        // Do the summation
        long result = lRb + ~lRa + lCarry;

        //System.out.println("Result:"+Long.toBinaryString(result));

        // Get the carry bit
        int carryOut = (int) ((result & MASK_BIT_33) >>> 32);
        return carryOut;
    }


   public static <E> void swap(List<E> list, int index1, int index2) {
      E tmp = list.get(index1);
      list.set(index1, list.get(index2));
      list.set(index2, tmp);
   }

   public static int delaySlotCheck(List<Instruction> instructions, List<Operation> operations, int index) {
      Instruction instruction = instructions.get(index);
      if (MicroBlazeUtils.hasDelaySlot(instruction.getOperation())) {
         // Move next instruction to the place of this instruction
         GeneralParsing.swap(instructions, index, index + 1);
         // Parse delayed instruction
         return MbToIrParser.parseIntruction(instructions, operations, index);
      }

      return index;

   }

   public static void performsLinkingCheck(Instruction instruction, List<Operation> operations) {
      boolean performsLink = org.ancora.SharedLibrary.MbDynamicMapping.MicroBlazeUtils.performsLinking(instruction.getOperation());
      if(performsLink) {
         // Get output register
         List<Operand> outputs = parseWriteType1(instruction);
         // Build input operand
         Operand inputLiteral = new Operand(OpType.literal, Integer.toString(instruction.getAddress()), REGISTER_SIZE_BITS);

         Operation moveOperation = Operation.newOperation(OperationName.move);
         moveOperation.setAddress(instruction.getAddress());
         moveOperation.setInput(InputIndex.firstOperand, inputLiteral);
         moveOperation.setOutput(OutputIndex.firstResult, outputs.get(0));

         operations.add(moveOperation);
      }
   }

       /**
     * Returns true if a is greater than b.
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean unsignedComp(int a, int b) {
            // Unsigned Comparison
            long longA = a & MASK_32_BITS;
            long longB = b & MASK_32_BITS;
            return longA > longB;
    }

   /**
    * Clears a specific bit of an int.
    *
    * @param bit the bit to clear. The least significant bit is bit 0
    * @param target the integer where the bit will be cleared
    * @return the updated value of the target
    */
   public static int clearBit(int bit, int target) {
      // Create mask
      int mask = 1 << bit;
      // Clear bit
      return target & ~mask;
   }

    /**
     * Performs a 32-bit unsigned division.
     *
     * @param a
     * @param b
     * @return
     */
    public static int unsignedDiv(int a, int b) {
         final long la = a & MASK_32_BITS;
         final long lb = b & MASK_32_BITS;

         return (int) (la / lb);
    }

       /**
    * Fuses the lower 16 bits of two ints.
    *
    * TODO: Verify correcteness.
    * <p>Ex.:
    * upper16 = 1001
    * lower16 = 101
    * result = 00000000000010010000000000000101
    *
    * @param upper16
    * @param lower16
    * @return
    */
   public static int fuseInt(int upper16, int lower16){
      // Mask the 16 bits of each one
      upper16 = upper16 & Integer.parseInt("0000FFFF", 16);
      lower16 = lower16 & Integer.parseInt("0000FFFF", 16);
      // Shift Upper16
      upper16 = upper16 << 16;
      // Merge
      return upper16 | lower16;
   }

   /**
    * DEFINITIONS
    */
    private static final long MASK_32_BITS = 0xFFFFFFFFL;
    private static final long MASK_BIT_33 = 0x100000000L;
   /**
    * Size in bits of a MicroBlaze Register
    */
   public static final int REGISTER_SIZE_BITS = 32;
   //public static final int IMMEDIATE_SIZE_BITS = 16;
   /**
    * Name given to the Carry Register
    */
   public static final String CARRY_REGISTER_NAME = "msr_c";

   public static boolean COMPUTE_LITERALS = true;
   public static boolean REMOVE_WRITES_TO_LITERALS = true;








}
