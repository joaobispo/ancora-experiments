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

package org.ancora.Partitioners.BasicBlock;

import java.util.ArrayList;
import java.util.List;
import org.ancora.DmeFramework.DataHolders.InstructionBlock;
import org.ancora.DmeFramework.Interfaces.Partitioner;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.MicroBlaze.MicroBlazeUtils;

/**
 * Partitions incoming MicroBlaze Instructions into BasicBlocks to be mapped
 * in hardware. Inspects the content of the instruction to determine the
 * limits of the BasicBlock.
 *
 * @author Joao Bispo
 */
public class BasicBlock extends Partitioner {

   public BasicBlock() {
      currentInstructions = new ArrayList<Instruction>();
      previousInstructionHadDelaySlot = false;
   }



   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public void accept(Instruction instruction) {
/*
      if(instruction.getAddress() == 472) {
         System.out.println("Current Inst:");
         System.out.println(currentInstructions);
         System.out.println("Previous had delay slot? -> "+previousInstructionHadDelaySlot);
      }

*/

      // Add instruction to current block of instructions
      currentInstructions.add(instruction);

      // Check if previous instructions had delay slot.
      // If positive, add current instruction and finish the basic block.
      if(previousInstructionHadDelaySlot) {
         // Clear flag
         previousInstructionHadDelaySlot = false;
         completeBasicBlock();
         return;
      }

      // Check if instruction is a branch
      if(MicroBlazeUtils.isBranch(instruction.getOperation())) {
         // Check if it has a delay slot
         if(MicroBlazeUtils.hasDelaySlot(instruction.getOperation())) {
            // Set flag and wait for next instruction
            previousInstructionHadDelaySlot = true;
         } else {
            // Complete BasicBlock
            completeBasicBlock();
            return;
         }
      }
   }


   private void completeBasicBlock() {
      // Build Instruction Block 
      InstructionBlock iBlock = new InstructionBlock(true, currentInstructions);

      // Basic Block can be identified by the address of its first instruction
      int hash = currentInstructions.get(0).getAddress();
      iBlock.setHash(hash);

      // Send Instruction Block  to listeners
      System.out.println("--BasicBlock Begin--");
      System.out.println(iBlock);
      System.out.println("--BasicBlock End--");
      noticeListeners(iBlock);
      // Clean current instructions
      currentInstructions = new ArrayList<Instruction>();
      //counter++;
   }

   @Override
   public void flush() {
      if(currentInstructions.size() > 0) {
         completeBasicBlock();
      }
      //System.out.println("Sent "+counter+" BasicBlocks.");
      flushListeners();
   }

   /**
    * INSTANCE VARIABLES
    */
    private List<Instruction> currentInstructions;

    // State
   private boolean previousInstructionHadDelaySlot;

   public static final String NAME = "BasicBlock";

   //int counter;

}
