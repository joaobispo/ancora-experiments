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
import org.ancora.MbTraceAnalyser.DataObjects.PatternFinderInfo;
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
      //updatedPatternSize = 0;
      //previousPatternSize = 0;
      //superBlocks = new ArrayList<SuperBlock>();
      //singleBlocksLoop = new SuperBlockLoop();
      patternSuperBlockLoop = null;
      currentSuperBlockLoop = new SuperBlockLoop();
      currentPatternSize = 0;
      state = SblBuilderState.LOOKING_FOR_PATTERN;

      patternInfo = new PatternFinderInfo(0, PatternFinderInfo.PatternState.PATTERN_NOT_FOUND);

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

   
   /**
    * Check if patternBlock needs update, and if yes updates it.
    */
   private void updatePatternBlock() {
      // Check if patternBlock needs update
      if (currentSuperBlockLoop.getSuperBlockCount() != currentPatternSize) {
         return;
      }
      
      // SuperBlockLoop has pattern size. Check if patternBlock is built.
      if(patternSuperBlockLoop == null) {
         patternSuperBlockLoop = currentSuperBlockLoop;
      } else {
         patternSuperBlockLoop.incrementIterations(1);
      }

      // "Erase" currentSuperBlockLoop
      currentSuperBlockLoop = new SuperBlockLoop();
   }


   /**
    * Sends the current SuperBlockLoop to registred consumers and resets it.
    */
   private void flushCurrentSuperBlockLoop() {
      if (currentSuperBlockLoop.getSuperBlockCount() == 0) {
         //System.out.println("Empty SuperBlockLoop:");
         //System.out.println(currentSuperBlockLoop);
         return;
      }

      updateConsumers(currentSuperBlockLoop);
      currentSuperBlockLoop = new SuperBlockLoop();
   }

   /**
    * Sends the pattern SuperBlockLoop to registred consumers and resets it.
    */
   private void flushPatternSuperBlockLoop() {
      if(patternSuperBlockLoop != null) {
         updateConsumers(patternSuperBlockLoop);
         patternSuperBlockLoop = null;
      }
   }

   public void consumeSuperBlock(SuperBlock superBlock) {
      switch(state) {
         case LOOKING_FOR_PATTERN:
            // Check if there is a pattern
            if(patternInfo.getPaternSize() > 0) {
               // Flush current superblocks
               flushCurrentSuperBlockLoop();

               // Prepare Data
               state = SblBuilderState.BUILDING_PATTERN;
               currentPatternSize = patternInfo.getPaternSize();

               // Start processing pattern
               consumeSuperBlock(superBlock);
               return;
            }

            currentSuperBlockLoop.addSuperBlock(superBlock);           
            break;
            
         case BUILDING_PATTERN:
            // Check pattern size. If it has changed, we can interrupt the building
            // of the pattern.
            if(currentPatternSize != patternInfo.getPaternSize()) {
               flushCurrentSuperBlockLoop();

               state = SblBuilderState.LOOKING_FOR_PATTERN;
               currentPatternSize = 0;

               consumeSuperBlock(superBlock);

               return;
            }

            // Just add SuperBlocks to current until it has size equal to the pattern.
            currentSuperBlockLoop.addSuperBlock(superBlock);

            if(currentSuperBlockLoop.getSuperBlockCount() ==  currentPatternSize) {
                  patternSuperBlockLoop = currentSuperBlockLoop;
                  currentSuperBlockLoop = new SuperBlockLoop();

                  state = SblBuilderState.CHECKING_PATTERN;
            }
            break;

         case CHECKING_PATTERN:
            // First, check if incoming superblock is part of the found pattern
            int index = currentSuperBlockLoop.getSuperBlockCount();
            int patternHash = patternSuperBlockLoop.getSuperBlocks().get(index).getHash();

            // If there is a mismatch, flush pattern and go looking for new patterns.
            if(patternHash != superBlock.getHash()) {
               flushPatternSuperBlockLoop();

               state = SblBuilderState.LOOKING_FOR_PATTERN;
               currentPatternSize = 0;

               consumeSuperBlock(superBlock);
               return;
            }

            // There is no mismatch, add superblock to current.
            currentSuperBlockLoop.addSuperBlock(superBlock);

            // If currentLoop is the same size as pattern, increment pattern.
            if(currentSuperBlockLoop.getSuperBlockCount() == currentPatternSize) {
               patternSuperBlockLoop.incrementIterations(1);
               currentSuperBlockLoop = new SuperBlockLoop();
            }
      }

/*
      switch(patternInfo.getPatternState()) {
         case PATTERN_NOT_FOUND:
            currentSuperBlockLoop.addSuperBlock(superBlock);
            break;

         case PATTERN_UNCHANGED:
            currentSuperBlockLoop.addSuperBlock(superBlock);
            updatePatternBlock();
            break;

         case PATTERN_STARTED:
            flushCurrentSuperBlockLoop();
            currentPatternSize = patternInfo.getPaternSize();
            currentSuperBlockLoop.addSuperBlock(superBlock);
            updatePatternBlock();
            break;

         case PATTERN_STOPED:
            flushPatternSuperBlockLoop();
            flushCurrentSuperBlockLoop();
            currentSuperBlockLoop.addSuperBlock(superBlock);
            break;

         case PATTERN_CHANGED_SIZES:
            flushPatternSuperBlockLoop();
            flushCurrentSuperBlockLoop();
            currentPatternSize = patternInfo.getPaternSize();
            currentSuperBlockLoop.addSuperBlock(superBlock);
            updatePatternBlock();
            break;
            
      }
 */
      /*
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
       */
   }


   public void consumePatternSize(PatternFinderInfo patternInfo) {
      this.patternInfo = patternInfo;
   }

   /*
   public void consumePatternSize(int patternSize) {
      this.updatedPatternSize = patternSize;
   }
    */

   /**
    * Flush remaining SuperBlockLoops.
    */
   public void flush() {
      flushPatternSuperBlockLoop();

      //if(superBlockDelayOne != null) {
      //   currentSuperBlockLoop.addSuperBlock(superBlockDelayOne);
      //}
      //if(superBlockDelayZero != null) {
      //   currentSuperBlockLoop.addSuperBlock(superBlockDelayZero);
      //}
      flushCurrentSuperBlockLoop();
      /*
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
*/
      // Call flush to all listeners
      for(SuperBlockLoopConsumer consumer : consumers) {
         consumer.flush();
      }
   }

   /*
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
*/
   /*
   private void buildSuperBlockLoop(SuperBlock superBlock) {
      singleBlocksLoop.addSuperBlock(superBlock);
      //SuperBlockLoop sbloop = new SuperBlockLoop();
      //sbloop.addSuperBlock(superBlock);
      //updateConsumers(sbloop);
   }
*/

   /**
    * INSTANCE VARIABLE
    */
   //private int updatedPatternSize;
   //private int previousPatternSize;

   //private List<SuperBlock> superBlocks;
   //private SuperBlockLoop singleBlocksLoop;

   private SuperBlockLoop patternSuperBlockLoop;
   private SuperBlockLoop currentSuperBlockLoop;

   private PatternFinderInfo patternInfo;

   private int currentPatternSize;
   private SblBuilderState state;

   private List<SuperBlockLoopConsumer> consumers;

   enum SblBuilderState {
      LOOKING_FOR_PATTERN,
      BUILDING_PATTERN,
      CHECKING_PATTERN;
   }





}
