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

package org.ancora.MbTraceAnalyser.DataObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity which represents a set of one or more SuperBlocks which are executed 
 * sequentially and which sequence may be repeated one or more times. 
 *
 * Each SuperBlockLoop is represented by a sequence of SuperBlocks, and the number
 * of times that sequence is repeated.
 *
 * @author Joao Bispo
 */
public class SuperBlockLoop {

   public SuperBlockLoop() {
      superBlocks = new ArrayList<SuperBlock>();
      iterations = 1;
   }

   public void addSuperBlock(SuperBlock superBlock) {
      superBlocks.add(superBlock);
   }

   /**
    *
    * @param superBlock
    * @param position
    * @return true if the given SuperBlock is equal to the SuperBlock in the
    * given position.
    */
   /*
   public boolean compareSuperBlock(SuperBlock superBlock, int position) {
      return superBlocks.get(position).compareDeep(superBlock);
   }
    */

   /**
    * Returns true if the elements of the given superBlockLoop are the same as
    * the first elements of the current SuperBlockLoop. Verifies that the given
    * SuperBlockLoop is a prefix of this SuperBlockLoop.
    * 
    * @param superBlock
    * @return
    */
   public boolean contains(SuperBlockLoop superBlockLoop) {
      for(int i=0; i<superBlockLoop.getSuperBlockCount(); i++) {
         if(superBlockLoop.superBlocks.get(i).getHash() !=
                 this.superBlocks.get(i).getHash()) {
            return false;
         }
      }

      return true;
   }

   public void incrementIterations() {
      iterations++;
   }

   /**
    * @return the number of SuperBlocks in this SuperBlockLoop, discarding
    * iterations.
    */
   public int getSuperBlockCount() {
      return superBlocks.size();
   }

   /**
    * @return the number of iterations of this SuperBlockLoop.
    */
   public int getIterations() {
      return iterations;
   }

   public List<SuperBlock> getSuperBlocks() {
      return superBlocks;
   }

   public int getTotalInstructions() {
      int totalInstructions = 0;

      for(SuperBlock superBlock : superBlocks) {
         totalInstructions += superBlock.getTotalInstructions();
      }

      return totalInstructions * iterations;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      // Append first
      if(superBlocks.size() > 0) {
         builder.append(superBlocks.get(0).toString());
      }

      // Append remaining
      for(int i=1; i<superBlocks.size(); i++) {
         builder.append("\n");
         builder.append(superBlocks.get(i).toString());
      }

      return builder.toString();
   }



   private final List<SuperBlock> superBlocks;
   private int iterations;
}
