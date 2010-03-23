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

package org.ancora.Partitioners.SuperBlock;

import org.ancora.DmeFramework.DataHolders.InstructionBlock;
import org.ancora.DmeFramework.Interfaces.Base.InstructionBlockListener;
import org.ancora.DmeFramework.Interfaces.Partitioner;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.Partitioners.BasicBlock.BasicBlock;

/**
 *
 * @author Joao Bispo
 */
public class SuperBlock extends Partitioner implements InstructionBlockListener {

   public SuperBlock() {
      // Create blocks
      basicBlockBuilder = new BasicBlock();
      superBlockBuilder = new SuperBlockBuilder();

      // Link blocks
      basicBlockBuilder.addListener(superBlockBuilder);
      superBlockBuilder.addListener(this);

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

      //flushListeners();
   }


   @Override
   public void accept(InstructionBlock instructionBlock) {
      System.out.println(instructionBlock);
      // Send the superblock to the listeners
      noticeListeners(instructionBlock);
   }

   /**
    * INSTANCE VARIABLES
    */
    private BasicBlock basicBlockBuilder;
    private SuperBlockBuilder superBlockBuilder;

    private boolean flushed;

   public static final String NAME = "SuperBlock";

}
