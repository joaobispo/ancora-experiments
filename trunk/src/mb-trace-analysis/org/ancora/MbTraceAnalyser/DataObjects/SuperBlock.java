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

package org.ancora.MbTraceAnalyser.DataObjects;

import java.util.ArrayList;
import java.util.List;
import org.ancora.SharedLibrary.BitUtils;

/**
 * Entity which represents a set of BasicBlocks which only have forward
 * branches to the next BasicBlock.
 *
 * Each SuperBlock is represented by a sequence of BasicBlocks.
 *
 * @author Joao Bispo
 */
public class SuperBlock {

   public SuperBlock() {
      basicBlocks = new ArrayList<BasicBlock>();
   }


   /**
    * Add a new BasicBlock to the SuperBlock
    * 
    * @param basicBlock
    */
   public void addBasicBlock(BasicBlock basicBlock) {
      basicBlocks.add(basicBlock);
   }

   /**
    * Appends the elements of the given instruction flow to the end of this
    * instruction flow.
    *
    * @param instructionFlow
    */
   /*
   public void addInstructionFlow(SuperBlock instructionFlow) {
      for(int i=0; i<instructionFlow.addresses.size(); i++) {
         addresses.add(instructionFlow.addresses.get(i));
         instructionCount.add(instructionFlow.instructionCount.get(i));
      }

      mergedFlow = true;
   }
    */

   /**
    * Increments the instruction counter of the current address block.
    */
   /*
   public void incrementInstruction() {
      int index = instructionCount.size()-1;
      int value = instructionCount.get(index) + 1;
      instructionCount.set(index, value);
   }
    */

   /**
    * 
    * @param address
    * @param position
    * @return
    */
   /*
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
    */

   /**
    * @return the number of basic blocks of this SuperBlock.
    */
   public int getBasicBlockCount() {
      return basicBlocks.size();
   }

   /**
    *
    * @param position
    * @return the address of the first instruction of the specified BasicBlock.
    */
   public int getBasicBlockAddress(int position) {
      return basicBlocks.get(position).getStartAddress();
   }


   /**
    *
    * @return the total number of instructions inside this SuperBlock.
    */
   public int getTotalInstructions() {
      int totalInstructions = 0;
      for(BasicBlock basicBlock : basicBlocks) {
         totalInstructions += basicBlock.getInstructionCount();
      }

     return totalInstructions;
   }

   @Override

   public String toString() {
      StringBuilder builder = new StringBuilder();

      int flowInstructions = 0;
      for(int i=0; i<basicBlocks.size(); i++) {
         builder.append(basicBlocks.get(i).toString());
         builder.append(" -> ");

         flowInstructions += basicBlocks.get(i).getInstructionCount();
      }

      String prefix1 = "";
      /*
      if(mergedFlow) {
         prefix1 = "Merged;";
      }
       */

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
   /*
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
    */

   /**
    * Returns true if the two instruction flows have the same sequence of
    * addresses.
    *
    * @param instructionFlow
    * @return
    */
   public boolean compare(SuperBlock superBlock) {
      // Check sizes first
      List<BasicBlock> secondList = superBlock.basicBlocks;
      if(secondList.size() != basicBlocks.size()) {
         return false;
      }

      for(int i=0; i<basicBlocks.size(); i++) {
         boolean basicBlocksAreEqual = basicBlocks.get(i).compare(secondList.get(i));
         if(!basicBlocksAreEqual) {
            return false;
         }
      }

      return true;
   }





   ///
   // INSTANCE VARIABLES
   ///
   final private List<BasicBlock> basicBlocks;

}
