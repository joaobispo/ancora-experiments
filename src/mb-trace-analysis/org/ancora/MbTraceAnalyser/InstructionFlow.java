/*
 *  Copyright 2009 Ancora Research Group.
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

package org.ancora.MbTraceAnalyser;

import java.util.ArrayList;
import java.util.List;
import org.ancora.SharedLibrary.BitUtils;

/**
 * Records the flow of forward branches, how many instructions each branch had
 * and how many times in a row the same flow was executed.
 *
 * @author Joao Bispo
 */
public class InstructionFlow {

   public InstructionFlow() {
      addresses = new ArrayList<Long>();
      instructionCount = new ArrayList<Integer>();
      recurrence = 1;
   }


   /**
    * Add a new address to the flow of instructions
    * 
    * @param address
    */
   public void addAddress(long address) {
      addresses.add(address);
      instructionCount.add(0);
   }

   /**
    * Increments the instruction counter of the current address block.
    */
   public void incrementInstruction() {
      int index = instructionCount.size()-1;
      int value = instructionCount.get(index) + 1;
      instructionCount.set(index, value);
   }

   /**
    * 
    * @param address
    * @param position
    * @return
    */
   public boolean isSameAddress(long address, int position) {
      // Check if position is not out of bounds
      if(position >= addresses.size() ) {
         return false;
      }

      if(addresses.get(position) == address) {
         return true;
      } else {
         return false;
      }

   }

   public int getSize() {
      return addresses.size();
   }

   public long getAddress(int index) {
      return addresses.get(index);
   }

   public void incrementRecurrence() {
      recurrence++;
   }

   public int getTotalInstructions() {
      int flowInstructions = 0;
      for(int i=0; i<addresses.size(); i++) {
         flowInstructions += instructionCount.get(i);
      }

      return recurrence*flowInstructions;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      int flowInstructions = 0;
      for(int i=0; i<addresses.size(); i++) {
         String hexString = Long.toHexString(addresses.get(i));
         hexString = BitUtils.padHexString(hexString, 8);
         builder.append(hexString);
         builder.append("(");
         builder.append(instructionCount.get(i));
         builder.append(") -> ");

         flowInstructions += instructionCount.get(i);
      }

      String prefix = "(TotalInst:"+(flowInstructions*recurrence) +
                        ";Recurrence:"+ recurrence +
                        ";Inst:" + flowInstructions + ")";

      return prefix + builder.toString() + "\n";
   }



   ///
   // INSTANCE VARIABLES
   ///
   final private List<Long> addresses;
   final private List<Integer> instructionCount;
   private int recurrence;
}
