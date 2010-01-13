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
import org.ancora.MbTraceAnalyser.DataObjects.SuperBlock;
import org.ancora.MbTraceAnalyser.DataObjects.SuperBlockLoop;
import org.ancora.MbTraceAnalyser.Interfaces.PatternFinderConsumer;
import org.ancora.MbTraceAnalyser.Interfaces.SuperBlockConsumer;
import org.ancora.MbTraceAnalyser.Interfaces.SuperBlockLoopConsumer;

/**
 * Builds SuperBlocksLoops, iteratively.
 *
 * @author Joao Bispo
 */
public class SuperBlockLoopBuilder implements SuperBlockConsumer, PatternFinderConsumer {

   public SuperBlockLoopBuilder() {
      updatedPatternSize = 0;
      previousPatternSize = 0;
      superBlocks = new ArrayList<SuperBlock>();
      singleBlocksLoop = new SuperBlockLoop();

      consumers = new ArrayList<SuperBlockLoopConsumer>();
   }

   public void addSuperBlockLoopConsumer(SuperBlockLoopConsumer comsumer) {
      consumers.add(comsumer);
   }

   private void updateConsumers(SuperBlockLoop superBlockLoop) {
      // Send current pattern size to all listeners
      for(SuperBlockLoopConsumer consumer : consumers) {
         consumer.consumeSuperBlockLoop(superBlockLoop);
      }
   }


   public void consumeSuperBlock(SuperBlock superBlock) {
// Check if there was change
      if(previousPatternSize != updatedPatternSize) {
         // There was a pattern before, and now there is none.
         if(updatedPatternSize == 0) {
            // You know that the last superblock added is not part of the pattern
            SuperBlock outsiderSuperBlock = superBlocks.remove(superBlocks.size()-1);
            // Print found pattern
            buildSuperBlockLoop(superBlocks);
            superBlocks = new ArrayList<SuperBlock>();
            buildSuperBlockLoop(outsiderSuperBlock);
            buildSuperBlockLoop(superBlock);
         }
         // Start new pattern
         else if(previousPatternSize == 0) {
            superBlocks.add(superBlock);
         }
         // Pattern changed size
         else {
            System.out.println("CHANGED SIZE");
            buildSuperBlockLoop(superBlocks);
            superBlocks = new ArrayList<SuperBlock>();

            //singleBlocksLoop.addSuperBlock(superBlock);
            superBlocks.add(superBlock);
         }

         previousPatternSize = updatedPatternSize;
      }
      else {
         if(previousPatternSize > 0) {
            superBlocks.add(superBlock);
         } else {
            buildSuperBlockLoop(superBlock);
         }
      }
   }

   public void consumePatternSize(int patternSize) {
      this.updatedPatternSize = patternSize;
   }

   /**
    * Flush remaining SuperBlockLoops.
    */
   public void flush() {
      // Flush superBlocks
      if(superBlocks.size() > 0) {
         buildSuperBlockLoop(superBlocks);
         superBlocks = new ArrayList<SuperBlock>();
      }

      // Flush singleBlocksLoop
      if(singleBlocksLoop.getSuperBlockCount() > 0) {
         updateConsumers(singleBlocksLoop);
         singleBlocksLoop = new SuperBlockLoop();
      }

      // Call flush to all listeners
      for(SuperBlockLoopConsumer consumer : consumers) {
         consumer.flush();
      }
   }

   private void buildSuperBlockLoop(List<SuperBlock> superBlocks) {
      int remainderBlocks = superBlocks.size() % previousPatternSize;
      int iterations = superBlocks.size() / previousPatternSize;

      if(iterations != 0) {
         // Empty previous singleBlocksLoop
         updateConsumers(singleBlocksLoop);
         singleBlocksLoop = new SuperBlockLoop();

         SuperBlockLoop sbloop = new SuperBlockLoop();
         // Add blocks
         for(int i=0; i<previousPatternSize; i++) {
            sbloop.addSuperBlock(superBlocks.get(i));
         }

         // Increment iterations
         for(int i=1; i<iterations; i++) {
            sbloop.incrementIterations();
         }

         updateConsumers(sbloop);
      }

      if(remainderBlocks != 0) {
         int startIndex = superBlocks.size()-remainderBlocks;
         for(int i = startIndex; i<superBlocks.size(); i++) {
            //System.out.println(superBlocks.get(i));
            singleBlocksLoop.addSuperBlock(superBlocks.get(i));
         }
      }
   }

   private void buildSuperBlockLoop(SuperBlock superBlock) {
      singleBlocksLoop.addSuperBlock(superBlock);
      //SuperBlockLoop sbloop = new SuperBlockLoop();
      //sbloop.addSuperBlock(superBlock);
      //updateConsumers(sbloop);
   }


   /**
    * INSTANCE VARIABLE
    */
   private int updatedPatternSize;
   private int previousPatternSize;

   private List<SuperBlock> superBlocks;
   private SuperBlockLoop singleBlocksLoop;

   private List<SuperBlockLoopConsumer> consumers;



}
