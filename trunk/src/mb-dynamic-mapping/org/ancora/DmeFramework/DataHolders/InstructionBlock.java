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

package org.ancora.DmeFramework.DataHolders;

import java.util.List;
import org.ancora.MicroBlaze.Instructions.Instruction;

/**
 * Block of MicroBlaze Instructions, which can be assigned for mapping.
 *
 * @author Joao Bispo
 */
public class InstructionBlock {

   /**
    * Builds an instruction block with a single instruction, not mappable do HW.
    * @param instruction
    */
   public InstructionBlock(Instruction instruction) {
      this.mapToHardware = false;
      this.instructions = new Instruction[1];
      this.instructions[0] = instruction;
      this.penaltyInstructions = new Instruction[0];
      this.iterations = 1;
      this.hash = -1;
   }

   /**
    * Builds an instruction block from a list of instructions, which iterates only
    * one time.
    * 
    * @param mapToHardware
    * @param instructions
    */
   public InstructionBlock(boolean mapToHardware, List<Instruction> instructions) {
      this(mapToHardware, instructions.toArray(new Instruction[instructions.size()]), new Instruction[0], 1);
   }

   public InstructionBlock(boolean mapToHardware, Instruction[] instructions, Instruction[] penaltyInstructions, int iterations) {
      this.mapToHardware = mapToHardware;
      this.instructions = instructions;
      this.penaltyInstructions = penaltyInstructions;
      this.iterations = iterations;
      this.hash = -1;
   }

   

   /**
    * @return true if the InstructionBlock was identified as a candidate to be 
    * mapped in the custom hardware
    */
   public boolean mapToHardware() {
      return mapToHardware;
   }

   /**
    * @return the number of times this block of instructions will be executed
    */
   public int getIterations() {
      return iterations;
   }

   /**
    * @return an array with MicroBlaze Instructions
    */
   public Instruction[] getInstructions() {
      return instructions;
   }

   /**
    * @return an array with MicroBlaze Instructions which were executed before
    * discovering that the execution of the block of instructions was
    * interrupted.
    */
   public Instruction[] getPenaltyInstructions() {
      return penaltyInstructions;
   }

   /**
    * @return the number of instructions in the InstructionBlock, multiplied by
    * the number of iterations
    */
   public int getTotalInstructions() {
      return instructions.length * iterations;
   }

   /**
    * @return a number with uniquely identifies this block of instructions.
    */
   public int getHash() {
      return hash;
   }

   /**
    * Sets the ID of this InstructionBlock.
    * 
    * @param id
    */
   public void setHash(int id) {
      this.hash = id;
   }

   public void setIterations(int iterations) {
      this.iterations = iterations;
   }

   public void setMapToHardware(boolean mapToHardware) {
      this.mapToHardware = mapToHardware;
   }

   

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      builder.append("-----Instruction Block: "+iterations+" iterations, ");
      if(mapToHardware) {
         builder.append("RPU");
      } else {
         builder.append("MicroBlaze");
      }
      builder.append(" ------\n");

      for(Instruction inst : instructions) {
         builder.append(inst);
         builder.append("\n");
      }

      return builder.toString();
   }





   /**
    * INSTANCE VARIABLES
    */
   private boolean mapToHardware;
   private Instruction[] instructions;
   private Instruction[] penaltyInstructions;
   private int iterations;
   private int hash;
}
