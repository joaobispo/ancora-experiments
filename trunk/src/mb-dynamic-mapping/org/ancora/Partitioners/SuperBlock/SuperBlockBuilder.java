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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.ancora.DmeFramework.DataHolders.InstructionBlock;
import org.ancora.DmeFramework.Interfaces.Base.InstructionBlockListener;
import org.ancora.DmeFramework.Interfaces.Base.InstructionBlockSource;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.SharedLibrary.BitUtils;


/**
 * Builds SuperBlocks, iteratively.
 * 
 * @author Joao Bispo
 */
public class SuperBlockBuilder extends InstructionBlockSource implements InstructionBlockListener {

   public SuperBlockBuilder() {
      //instructions = null;
      //lastBasicBlockAddress = -1;
      prepareStateForNewSuperBlock();
   }



   @Override
   public void accept(InstructionBlock instructionBlock) {
      /*
      if(instructionBlock.getInstructions()[0].getAddress() == 472) {
         System.out.println("FOUND");
      }
       */

      // This block may be redundant...
      // Check if it is the start of a new SuperBlock
      /*
      if(instructions == null) {
         prepareStateForNewSuperBlock();
         updateCurrentSuperBlock(instructionBlock);
         return;
      }
       */


      // Check if the current basicBlock is a forward jump or a backward jump
      //int lastBasicBlockPosition = currentSuperBlock.getBasicBlockCount() - 1;
      //int addressOfLastBasicBlock = currentSuperBlock.getBasicBlockAddress(lastBasicBlockPosition);
 

      //int addressOfCurrentBasicBlock = basicBlock.getStartAddress();
      // We assume the instruction block comes from a basic block builder
      // Get the address of the first instruction of the block

      int addressOfCurrentBasicBlock = instructionBlock.getInstructions()[0].getAddress();
      boolean forwardJump = addressOfCurrentBasicBlock > lastBasicBlockAddress;

      if(!forwardJump) {
         completeSuperBlock();
         prepareStateForNewSuperBlock();
      }

      updateCurrentSuperBlock(instructionBlock);
   }


   private void completeSuperBlock() {
      // Build new InstructionBlock
      InstructionBlock newBlock = new InstructionBlock(true, instructions);
      newBlock.setHash(hash);
      // Notice Listeners
      noticeListeners(newBlock);

      // Send current super block to all listeners
      //for (SuperBlockConsumer consumer : superBlockConsumers) {
      //   consumer.consumeSuperBlock(currentSuperBlock);
      //}

      prepareStateForNewSuperBlock();
      // Erase current basic block
      //instructions = null;
      //lastBasicBlockAddress = -1;
   }

   private void updateCurrentSuperBlock(InstructionBlock basicBlock) {
      Instruction[] instructionArray = basicBlock.getInstructions();
      // Add all instructions to SuperBlock
      instructions.addAll(Arrays.asList(instructionArray));
      //currentSuperBlock.addBasicBlock(basicBlock);
      // Update hash value
      hash = BitUtils.superFastHash(instructionArray[0].getAddress(), hash);

         //lastBasicBlockAddress = basicBlock.getStartAddress();
         //lastBasicBlockAddress = basicBlock.getLastAddress();
         // Determine last address
         lastBasicBlockAddress = instructionArray[instructionArray.length-1].getAddress();
   }

   @Override
   public void flush() {
      if (instructions != null) {
         completeSuperBlock();
      }

      flushListeners();
   }

   private void prepareStateForNewSuperBlock() {
      instructions = new ArrayList<Instruction>();
      hash = HASH_INITIAL_VALUE;
      lastBasicBlockAddress = -1;
   }



   /**
    * INSTANCE VARIABLES
    */
   //private final List<SuperBlockConsumer> superBlockConsumers;
   //private SuperBlock currentSuperBlock;
   
   // State
   private int lastBasicBlockAddress;
      private List<Instruction> instructions;
   private int hash;
   
   // Constants
      private static final int HASH_INITIAL_VALUE = 4;





}
