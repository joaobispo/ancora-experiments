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

package org.ancora.MbDynamicMapping.Partitioners;

import java.util.ArrayList;
import java.util.List;
import org.ancora.MbDynamicMapping.Interface.InstructionBlock;
import org.ancora.MbDynamicMapping.Interface.Partitioner;
import org.ancora.MbDynamicMapping.Options.PartitionerName;
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
      blockId = 0;
   }



   @Override
   public String getName() {
      return PartitionerName.basicblock.getName();
   }

   @Override
   public void accept(Instruction instruction) {
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
      iBlock.setId(blockId);
      blockId++;
      // Send Instruction Block  to listeners
      noticeListeners(iBlock);
      // Clean current instructions
      currentInstructions = new ArrayList<Instruction>();
   }

   @Override
   public void flush() {
      flushListeners();
   }

   /**
    * INSTANCE VARIABLES
    */
    private List<Instruction> currentInstructions;
    private long blockId;
    // State
   private boolean previousInstructionHadDelaySlot;

}
