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
import org.ancora.MbTraceAnalyser.DataObjects.BasicBlock;
import org.ancora.MbTraceAnalyser.DataObjects.SuperBlock;
import org.ancora.MbTraceAnalyser.Interfaces.BasicBlockConsumer;
import org.ancora.MbTraceAnalyser.Interfaces.SuperBlockConsumer;

/**
 * Builds SuperBlocks, iteratively.
 * 
 * @author Joao Bispo
 */
public class SuperBlockBuilder implements BasicBlockConsumer {

   public SuperBlockBuilder() {
      superBlockConsumers = new ArrayList<SuperBlockConsumer>();
      currentSuperBlock = null;
      lastBasicBlockAddress = 0;
   }


   public void addSuperBlockConsumer(SuperBlockConsumer superBlockConsumer) {
      superBlockConsumers.add(superBlockConsumer);
   }


   public void consumeBasicBlock(BasicBlock basicBlock) {
      // Check if it is the start of a new SuperBlock
      if(currentSuperBlock == null) {
         currentSuperBlock = new SuperBlock();
         updateCurrentSuperBlock(basicBlock);
         //currentSuperBlock.addBasicBlock(basicBlock);
         //lastBasicBlockAddress = basicBlock.getStartAddress();
         return;
      }

      // Check if the current basicBlock is a forward jump or a backward jump
      //int lastBasicBlockPosition = currentSuperBlock.getBasicBlockCount() - 1;
      //int addressOfLastBasicBlock = currentSuperBlock.getBasicBlockAddress(lastBasicBlockPosition);
      int addressOfCurrentBasicBlock = basicBlock.getStartAddress();
      boolean forwardJump = addressOfCurrentBasicBlock > lastBasicBlockAddress;

      if(!forwardJump) {
         completeSuperBlock();
         currentSuperBlock  = new SuperBlock();
      }

      updateCurrentSuperBlock(basicBlock);
      //currentSuperBlock.addBasicBlock(basicBlock);
      //lastBasicBlockAddress = basicBlock.getStartAddress();

   }


   private void completeSuperBlock() {
      // Send current super block to all listeners
      for(SuperBlockConsumer consumer : superBlockConsumers) {
         consumer.consumeSuperBlock(currentSuperBlock);
      }

      // Erase current basic block
      currentSuperBlock = null;
      lastBasicBlockAddress = 0;
   }

   private void updateCurrentSuperBlock(BasicBlock basicBlock) {
         currentSuperBlock.addBasicBlock(basicBlock);
         lastBasicBlockAddress = basicBlock.getStartAddress();
   }

   public void flush() {
      completeSuperBlock();
   }

   /**
    * INSTANCE VARIABLES
    */
   private final List<SuperBlockConsumer> superBlockConsumers;
   private SuperBlock currentSuperBlock;

   // State
   private int lastBasicBlockAddress;




}
