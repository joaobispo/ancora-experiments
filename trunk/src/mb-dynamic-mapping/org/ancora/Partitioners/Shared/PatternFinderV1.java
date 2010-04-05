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

package org.ancora.Partitioners.Shared;

import java.util.BitSet;
import java.util.logging.Logger;
import org.ancora.DmeFramework.DataHolders.InstructionBlock;
import org.ancora.DmeFramework.Interfaces.Base.InstructionBlockListener;
import org.ancora.DmeFramework.Statistics.HashCounter;
import org.ancora.SharedLibrary.BitUtils;
import org.ancora.SharedLibrary.DataStructures.PushingQueue;

/**
 * Looks for patterns in SuperBlocks
 *
 * @author Joao Bispo
 */
public class PatternFinderV1 implements InstructionBlockListener {

   /**
    * Creates a new PatternFinder which will try to find patterns of maximum
    * size 'maxPatternSize', in the given SuperBlocks.
    * 
    * <p>The maximum size for the pattern is 32.
    * 
    * @param maxPatternSize
    */
   public PatternFinderV1(int maxPatternSize) {
      //consumers = new ArrayList<PatternFinderConsumer>();
      previousPatternSize = 0;

      // Check Pattern Size
      if(maxPatternSize > 32) {
         Logger.getLogger(PatternFinderV1.class.getName()).
                 warning("Maximum pattern size is 32. Setting size to 32.");
         maxPatternSize = 32;
      }

      this.maxPatternSize = maxPatternSize;
      matchQueues = new int[maxPatternSize];
      queue = new PushingQueue<Integer>(maxPatternSize + 1);

      // Initiallize Queue
      for(int i=0; i<queue.size(); i++) {
         queue.insertElement(0);
      }

      // Initiallize Info
      info = new PatternFinderInfo(0, PatternFinderInfo.PatternState.PATTERN_NOT_FOUND);

      hashCounter = new HashCounter();
   }

   /*
   public void addPatternFinderConsumer(PatternFinderConsumer patternFinderConsumer) {
      consumers.add(patternFinderConsumer);
   }
    */


   @Override
   public void accept(InstructionBlock instructionBlock) {
      //System.out.println("PatternFinder - Hash:"+hashCounter.convertHash(instructionBlock.getHash()));
      int hashValue = instructionBlock.getHash();
      // Insert new element
      queue.insertElement(hashValue);

      // Compare first element with all other elements and store result on
      // match queues
      for (int i = 0; i < maxPatternSize; i++) {
         // Shift match queue
         matchQueues[i] <<= 1;
         // Calculate match
         if (hashValue == queue.getElement(i + 1)) {
            // We have a match. Set the bit.
            matchQueues[i] = BitUtils.setBit(0, matchQueues[i]);
         } else {
            // Reset queue
            matchQueues[i] = 0;
         }
      }

      // Put all the results in a single bit array
      BitSet bitArray = new BitSet();
      for (int i = 0; i < matchQueues.length; i++) {
         if (BitUtils.getBit(i, matchQueues[i]) > 0) {
            bitArray.set(i);
         } else {
            bitArray.clear(i);
         }
      }

      int patternSize = calculatePatternSize(bitArray, previousPatternSize);
      updateConsumers(patternSize);
      // Check if there is a pattern
      // Look if bit position is set, only first encountered matters
  /*
      int bitIndex = maxPatternSize - 1;
      int foundMatch = 0;
      for (int i = 0; i < matchQueues.length; i++) {
         //if (BitUtils.getBit(bitIndex, matchQueues[i]) > 0) {
         // Instead of using the same bitIndex for every queue, look at the bit
         // the same size as the pattern
         if (BitUtils.getBit(i, matchQueues[i]) > 0) {
            // There was a match! Store it
            foundMatch = i+1;
            // If match is smaller than the last found match, continue
            // Otherwise, cancel it
            
            //if(foundMatch >= previousPatternSize) {
            //   break;
            //}
             
            // There was a match! Inform and return.
            updateConsumers(i + 1);
            return;
         }
      }

      //updateConsumers(foundMatch);
      // No pattern was found
      updateConsumers(0);
*/
   }

   private int calculatePatternSize(BitSet bitArray, int previousPatternSize) {
      int firstSetSize = bitArray.nextSetBit(0) + 1;
      if(previousPatternSize > firstSetSize) {
         // Check if previous pattern size is still active
         boolean previousPatternStillActive = bitArray.get(previousPatternSize-1);
         if(previousPatternStillActive) {
            return previousPatternSize;
         }
      }

      return firstSetSize;
   }

   private void updateConsumers(int patternSize) {
      PatternFinderInfo.PatternState patternState;
      // Check if pattern state has changed
      if(previousPatternSize != patternSize) {
         // If previous pattern size was 0, a new pattern started
         if(previousPatternSize == 0) {
            patternState = PatternFinderInfo.PatternState.PATTERN_STARTED;
         }
         // If current pattern size is 0, the previous pattern has stopped.
         else if(patternSize == 0) {
            patternState = PatternFinderInfo.PatternState.PATTERN_STOPED;
         }
         // The case that is left is that the previous pattern stopped, but
         // there is a new pattern with a different size.
         else {
            patternState = PatternFinderInfo.PatternState.PATTERN_CHANGED_SIZES;
         }

         // Because the pattern has a different size, the value needs to be
         // updated
         previousPatternSize = patternSize;
      }
      // The size of the pattern hasn't changed
      else {
         if(patternSize > 0) {
            patternState = PatternFinderInfo.PatternState.PATTERN_UNCHANGED;
         } else {
            patternState = PatternFinderInfo.PatternState.PATTERN_NOT_FOUND;
         }
      }

      // Create object
      //PatternFinderInfo info = new PatternFinderInfo(patternSize, patternState);
      info = new PatternFinderInfo(patternSize, patternState);
/*
      // Send current pattern size to all listeners
      for(PatternFinderConsumer consumer : consumers) {
         consumer.consumePatternSize(info);
      }
 */
   }

   @Override
   public void flush() {
      // Call flush to all listeners
      /*
      for(PatternFinderConsumer consumer : consumers) {
         consumer.flush();
      }
       */
      // DO NOTHING

   }

   public PatternFinderInfo getInfo() {
      return info;
   }

   /**
    * INSTANCE VARIABLES
    */
   private int maxPatternSize;
   private int[] matchQueues;
   private PushingQueue<Integer> queue;

   //private List<PatternFinderConsumer> consumers;

   private int previousPatternSize;

   private PatternFinderInfo info;

   private HashCounter hashCounter;



}
