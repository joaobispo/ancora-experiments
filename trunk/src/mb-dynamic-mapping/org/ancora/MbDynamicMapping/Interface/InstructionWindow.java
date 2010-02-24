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

package org.ancora.MbDynamicMapping.Interface;

import org.ancora.MicroBlaze.Instructions.Instruction;

/**
 * Wrapper for InstructionBlock. Reads instructions one-by-one, when requested.
 *
 * @author Joao Bispo
 */
public class InstructionWindow {

   public InstructionWindow(InstructionBlock instructionBlock) {
      pointer = 0;
      instructions = instructionBlock.getInstructions();
   }

   /**
    * @return the next instruction. If there are no more instructions, returns
    * null;
    */
   public Instruction nextInstruction() {
      if(pointer >= instructions.length) {
         return null;
      }

      Instruction inst = instructions[pointer];
      pointer++;
      return inst;
   }

   /**
    * Peeks the window to see if there are instructions left. If false, it means
    * that a call to nextInstruction would return null. 
    * 
    * @return true if it still has instructions. False otherwise.
    */
   public boolean hasInstructions() {
      if(pointer >= instructions.length) {
         return false;
      } else {
        return true;
      }
   }

   /**
    * Returns to the beginning of the stream
    */
   public void reset() {
      pointer = 0;
   }

   /**
    * INSTANCE VARIABLES
    */
   private int pointer;
   private Instruction[] instructions;
}
