/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.SharedLibrary.MicroBlazePackage;


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

   /**
    * Pads the string with zeros on the left until it has the requested size.
    *
    * @param binaryNumber
    * @param size
    * @return
    */
   public static String padBinaryString(String binaryNumber, int size) {
       int stringSize = binaryNumber.length();
       if(stringSize >= size) {
           return binaryNumber;
       }

       int numZeros = size - stringSize;
       StringBuilder builder = new StringBuilder(numZeros);
       for(int i=0; i<numZeros; i++) {
           builder.append(ZERO);
       }

       return builder.toString() + binaryNumber;
   }

   /**
    * Gets the a single bit of the target.
    *
    * @param position
    * @param target
    * @return
    */
   public static int getBit(int position, int target) {
      return (target >>> position) & MASK_BIT_1;
   }

   /**
    * Returns 16 bits from the long number.
    * 
    * @param data
    * @param offset one of 0 to 3
    * @return
    */
   public static int get16BitsAligned(long data, int offset) {
      // Normalize offset
      offset = offset%4;
      //System.out.println("offset:"+offset);
      // Align the mask
      long mask = MASK_16_BITS << 16*offset;
      //System.out.println("Mask:"+Long.toHexString(mask));
      //System.out.println("Data:"+Long.toHexString(data));

      // Get the bits
      long result = data & mask;

      // Put bits in position
      return (int) (result >>> (16*offset));
   }

   /**
    * Paul Hsieh's Hash Function.
    *
    * @param data data to hash
    * @param dataLength length of the data, in bytes
    * @param hashedValue previous value of the hash. If it is the start of the
    * method, used the length of the data (ex.: 8 bytes).
    * @return
    */
   public static int superFastHash(long data, int hash) {
      int tmp;
      //int rem;

      //if (len <= 0) {
      //   return 0;
      //}

      //rem = len & 3;
      //len >>= 2;

      //Main Loop
      for (int i = 0; i < 4; i += 2) {
         // Get lower 16 bits
         hash += BitUtils.get16BitsAligned(data, i);
         // Calculate some random value with second-lower 16 bits
         tmp = (BitUtils.get16BitsAligned(data, i + 1) << 11) ^ hash;
         hash = (hash << 16) ^ tmp;
         // At this point, it would advance the data, but since it is restricted
         // to longs (64-bit values), it is unnecessary).
         hash += hash >> 11;
      }

      // Handle end cases //
      // There are no end cases, main loop is done in chuncks of 32 bits.

      // Force "avalanching" of final 127 bits //
      hash ^= hash << 3;
      hash += hash >> 5;
      hash ^= hash << 4;
      hash += hash >> 17;
      hash ^= hash << 25;
      hash += hash >> 6;

      return hash;
   }
   /**
    * Paul Hsieh's Hash Function.
    *
    * @param data data to hash
    * @param dataLength length of the data, in bytes
    * @param hashedValue previous value of the hash. If it is the start of the
    * method, used the length of the data (ex.: 8 bytes).
    * @return
    */
   public static int superFastHash(int data, int hash) {
      int tmp;
      //int rem;

      //if (len <= 0) {
      //   return 0;
      //}

      //rem = len & 3;
      //len >>= 2;

      //Main Loop
      for (int i = 0; i < 2; i += 2) {
         // Get lower 16 bits
         hash += BitUtils.get16BitsAligned(data, i);
         // Calculate some random value with second-lower 16 bits
         tmp = (BitUtils.get16BitsAligned(data, i + 1) << 11) ^ hash;
         hash = (hash << 16) ^ tmp;
         // At this point, it would advance the data, but since it is restricted
         // to longs (64-bit values), it is unnecessary).
         hash += hash >> 11;
      }

      // Handle end cases //
      // There are no end cases, main loop is done in chuncks of 32 bits.

      // Force "avalanching" of final 127 bits //
      hash ^= hash << 3;
      hash += hash >> 5;
      hash ^= hash << 4;
      hash += hash >> 17;
      hash ^= hash << 25;
      hash += hash >> 6;

      return hash;
   }

   /**
    * Sets a specific bit of an int.
    *
    * @param bit the bit to set. The least significant bit is bit 0
    * @param target the integer where the bit will be set
    * @return the updated value of the target
    */
   public static int setBit(int bit, int target) {
      // Create mask
      int mask = 1 << bit;
      // Set bit
      return target | mask;
   }


   ///
   // CONSTANTS
   ///
   private static final String ZERO = "0";
   private static final String HEX_PREFIX = "0x";
   private static final long MASK_16_BITS = 0xFFFFL;
   private static final int MASK_BIT_1 = 0x1;
}
