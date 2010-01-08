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

package org.ancora.MbTraceAnalyser.DataObjects;

import org.ancora.SharedLibrary.BitUtils;

/**
 * Entity which represents a set of sequential instructions until the first
 * branch, inclusive.
 *
 * <p>Each BasicBlock is represented by the address of its first instruction and
 * the number of instructions it contains.
 *
 * @author Joao Bispo
 */
public class BasicBlock {

   public BasicBlock(int startAddress) {
      this.startAddress = startAddress;
      this.instructionCount = 1;
   }

   public void incrementInstructionCount() {
      instructionCount++;
   }

   public int getStartAddress() {
      return startAddress;
   }

   public int getInstructionCount() {
      return instructionCount;
   }

   public boolean compare(BasicBlock basicBlock) {
      boolean firstTest = basicBlock.startAddress == startAddress;
      boolean secondTest = basicBlock.instructionCount == instructionCount;

      if(firstTest && secondTest) {
         return true;
      } else {
         return false;
      }
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      String hexString = Integer.toHexString(startAddress);
      hexString = BitUtils.padHexString(hexString, Integer.SIZE / 4);
      builder.append(hexString);
      builder.append("(");
      builder.append(instructionCount);
      builder.append(")");

      return builder.toString();
   }



   /**
    * INSTANCE VARIABLES
    */
   private final int startAddress;
   private int instructionCount;
}
