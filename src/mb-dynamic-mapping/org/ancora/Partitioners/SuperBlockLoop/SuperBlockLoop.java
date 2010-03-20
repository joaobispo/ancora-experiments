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

package org.ancora.Partitioners.SuperBlockLoop;

import org.ancora.DmeFramework.DataHolders.InstructionBlock;
import org.ancora.DmeFramework.Interfaces.Base.InstructionBlockListener;
import org.ancora.DmeFramework.Interfaces.Partitioner;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.Partitioners.BasicBlock.BasicBlock;
import org.ancora.Partitioners.SuperBlock.SuperBlockBuilder;

/**
 *
 * @author Joao Bispo
 */
public class SuperBlockLoop extends Partitioner implements InstructionBlockListener {

   public SuperBlockLoop(int maxPatternSize) {
      // Create blocks
      basicBlockBuilder = new BasicBlock();
      superBlockBuilder = new SuperBlockBuilder();
      superBlockLoopBuilder =  new SuperBlockLoopBuilder(maxPatternSize);

      // Link blocks
      basicBlockBuilder.addListener(superBlockBuilder);
      superBlockBuilder.addListener(superBlockLoopBuilder);
      superBlockLoopBuilder.addListener(this);

      flushed = false;
   }



   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public void accept(Instruction instruction) {
      // Give instruction to BasicBlock
      basicBlockBuilder.accept(instruction);
   }

   @Override
   public void flush() {
      if (!flushed) {
         flushed = true;
         basicBlockBuilder.flush();
      } else {
         flushListeners();
      }
   }

   @Override
   public void accept(InstructionBlock instructionBlock) {
      // Send the SuperBlockLoop to the listeners
      noticeListeners(instructionBlock);
   }

   public void setMaxPatternSize(int maxPatternSize) {
      superBlockLoopBuilder.setMaxPatternSize(maxPatternSize);
   }

   /**
    * INSTANCE VARIABLES
    */
    private BasicBlock basicBlockBuilder;
    private SuperBlockBuilder superBlockBuilder;
    private SuperBlockLoopBuilder superBlockLoopBuilder;

    private boolean flushed;

   public static final String NAME = "SuperBlockLoop";
   public static final int DEFAULT_MAX_PATTERN_SIZE = 10;
}
