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
      mergedFlow = false;
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
    * Appends the elements of the given instruction flow to the end of this
    * instruction flow.
    *
    * @param instructionFlow
    */
   public void addInstructionFlow(InstructionFlow instructionFlow) {
      for(int i=0; i<instructionFlow.addresses.size(); i++) {
         addresses.add(instructionFlow.addresses.get(i));
         instructionCount.add(instructionFlow.instructionCount.get(i));
      }

      mergedFlow = true;
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

   /**
    * Returns the number of basic blocks of this flow
    * @return
    */
   public int getSize() {
      return addresses.size();
   }

   public long getAddress(int index) {
      return addresses.get(index);
   }

   public boolean isMergedFlow() {
      return mergedFlow;
   }

   public int getRecurrence() {
      return recurrence;
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

      String prefix1 = "";
      if(mergedFlow) {
         prefix1 = "Merged;";
      }

//      String prefix2 = "TotalInst:"+(flowInstructions*recurrence) +
//                        ";Recurrence:"+ recurrence +
//                        ";Inst:" + flowInstructions;
      String prefix2 = "TotalInst:"+(flowInstructions);

      return "(" + prefix1 + prefix2 + ") " + builder.toString();
   }

   /**
    * Returns a String representing Java code which implements an array with
    * the values of the addresses.
    * 
    * @return
    */
   public String getAddressesArray() {
      StringBuilder builder = new StringBuilder();

      String prefix = "int[] a = {";
      String sufix = "};";

      builder.append(prefix);
      // Append first value
      if(addresses.size() > 0) {
         builder.append(addresses.get(0));
      }

      for(int i=1; i<addresses.size(); i++) {
         builder.append(", ");
         builder.append(addresses.get(i));
      }

      builder.append(sufix);

      return builder.toString();
   }

   /**
    * Returns true if the two instruction flows have the same sequence of
    * addresses.
    *
    * @param instructionFlow
    * @return
    */
   public boolean compare(InstructionFlow instructionFlow) {
      return instructionFlow.addresses.equals(this.addresses);
   }

   ///
   // INSTANCE VARIABLES
   ///
   final private List<Long> addresses;
   final private List<Integer> instructionCount;
   private int recurrence;
   private boolean mergedFlow;
}
