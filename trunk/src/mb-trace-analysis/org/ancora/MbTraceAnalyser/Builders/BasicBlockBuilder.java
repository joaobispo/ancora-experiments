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

package org.ancora.MbTraceAnalyser.Builders;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.ancora.MbTraceAnalyser.DataObjects.BasicBlock;
import org.ancora.MbTraceAnalyser.Interfaces.BasicBlockConsumer;
import org.ancora.MbTraceAnalyser.MicroblazeTraceInstruction;

/**
 * Builds BasicBlocks, iteratively.
 *
 * @author Joao Bispo
 */
public class BasicBlockBuilder {

   public BasicBlockBuilder() {
      basicBlockConsumers = new ArrayList<BasicBlockConsumer>();
      currentBasicBlock = null;

      previousInstructionHadDelaySlot = false;

      // Settings
      countDummyInstructions = true;
   }

   public void addBasicBlockConsumer(BasicBlockConsumer basicBlockConsumer) {
      basicBlockConsumers.add(basicBlockConsumer);
   }

   public void consumeTraceInstruction(MicroblazeTraceInstruction traceInstruction) {
      // Check if previous instruction has delay slot
      if(previousInstructionHadDelaySlot) {
         previousInstructionHadDelaySlot = false;

         boolean isDummyInstruction = traceInstruction.getInstruction().equals(DUMMY_INSTRUCTION);

         boolean dontCountInstruction = !countDummyInstructions && isDummyInstruction;

         if(!dontCountInstruction) {
            currentBasicBlock.incrementInstructionCount();
         }

         /*
         if(countDelaySlotInstructions) {
            currentBasicBlock.incrementInstructionCount();
         }
          */

         // Check if delay slot instruction is dummy instruction
         /*
         if(!traceInstruction.getInstruction().equals(DUMMY_INSTRUCTION)) {
            Logger.getLogger(BasicBlockBuilder.class.getName()).
                    info("Not a dummy instruction: "+traceInstruction.getInstruction());
         }
          */

         // BasicBlock is complete. Calls listeners and return
         completedBasicBlock();
         return;
      }

      // Check if it is the start of a new basic block
      if(currentBasicBlock == null) {
         currentBasicBlock = new BasicBlock(traceInstruction.getInstructionAddress());
      } else {
         // If not, increase the instruction count
         currentBasicBlock.incrementInstructionCount();
      }

      // Check if current instruction is a branch
      if(traceInstruction.isBranch()) {
         // If it has a delay slot, delay completion of basic block until next
         // instruction
         if(traceInstruction.hasDelaySlot()) {
            previousInstructionHadDelaySlot = true;
         } else {
            // Else, complete basic block
            completedBasicBlock();
         }
      }

   }

   /**
    * Completes execution, flushes any data that is still waiting processing.
    */
   public void flush() {
      if(currentBasicBlock != null) {
         completedBasicBlock();
      }

      // Call flush to all listeners
      for(BasicBlockConsumer consumer : basicBlockConsumers) {
         consumer.flush();
      }

   }

   /**
    * If true, all delay slot instructions will be counted. If false, dummy 
    * instruction "or      r0, r0, r0" in delay slots will be ignored.
    *
    * @param countDelaySlotInstructions
    */
   public void setCountDummyInstructions(boolean countDummyInstructions) {
      this.countDummyInstructions = countDummyInstructions;
   }


   private void completedBasicBlock() {
      // Send current basic block to all listeners
      for(BasicBlockConsumer consumer : basicBlockConsumers) {
         consumer.consumeBasicBlock(currentBasicBlock);
      }
      
      // Erase current basic block
      currentBasicBlock = null;
   }

   /**
    * INSTANCE VARIABLES
    */
   private final List<BasicBlockConsumer> basicBlockConsumers;
   private BasicBlock currentBasicBlock;

   // State
   private boolean previousInstructionHadDelaySlot;

   // Settings
   private boolean countDummyInstructions;

   // Constants
   private final String DUMMY_INSTRUCTION = "or      r0, r0, r0";

}
