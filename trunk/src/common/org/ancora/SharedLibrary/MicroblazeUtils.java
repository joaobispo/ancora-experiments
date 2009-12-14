/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.SharedLibrary;

/**
 * Methods for Microblaze-related programs
 *
 * @author Joao Bispo
 */
public class MicroblazeUtils {

   /**
    * Extracts the memory address from the given trace instruction.
    *
    * <p>The method assumes that the address are at the beginning of the String
    * and that they start with "0x" and have 8 hexadecimal digits.
    *
    * @param traceInstruction a trace instruction
    * @return a string with an hexadecimal representation of the memory
    */
   public static String getTraceInstructionAddress(String traceInstruction) {
      final int beginIndex = "0x".length();
      final int endIndex = "0x".length() + 8;

      return traceInstruction.substring(beginIndex, endIndex);
   }
}
