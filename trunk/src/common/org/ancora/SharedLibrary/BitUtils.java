/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.SharedLibrary;

/**
 * Methods for bit manipulation.
 *
 * @author Joao Bispo
 */
public class BitUtils {

    /**
    * Pads the string with 0x and zeros on the left until it has the
    * requested size.
    *
    * @param hexNumber
    * @param size
    * @return
    */
   public static String padHexString(String hexNumber, int size) {
       int stringSize = hexNumber.length();

       if(stringSize >= size) {
           return hexNumber;
       }

       int numZeros = size - stringSize;
       StringBuilder builder = new StringBuilder(numZeros+HEX_PREFIX.length());
       builder.append(HEX_PREFIX);
       for(int i=0; i<numZeros; i++) {
           builder.append(ZERO);
       }

       return builder.toString() + hexNumber;
   }

   
   ///
   // CONSTANTS
   ///
   private static final String ZERO = "0";
   private static final String HEX_PREFIX = "0x";
}
