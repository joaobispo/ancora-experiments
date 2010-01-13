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
import java.util.logging.Logger;
import org.ancora.MbTraceAnalyser.DataObjects.SuperBlock;
import org.ancora.MbTraceAnalyser.Interfaces.PatternFinderConsumer;
import org.ancora.MbTraceAnalyser.Interfaces.SuperBlockConsumer;
import org.ancora.SharedLibrary.BitUtils;
import org.ancora.SharedLibrary.DataStructures.RotatingQueue;

/**
 * Looks for patterns in SuperBlocks
 *
 * @author Joao Bispo
 */
public class PatternFinder implements SuperBlockConsumer {

   /**
    * Creates a new PatternFinder which will try to find patterns of maximum
    * size 'maxPatternSize', in the given SuperBlocks.
    * 
    * <p>The maximum size for the pattern is 32.
    * 
    * @param maxPatternSize
    */
   public PatternFinder(int maxPatternSize) {
      consumers = new ArrayList<PatternFinderConsumer>();

      // Check Pattern Size
      if(maxPatternSize > 32) {
         Logger.getLogger(PatternFinder.class.getName()).
                 warning("Maximum pattern size is 32. Setting size to 32.");
         maxPatternSize = 32;
      }

      this.maxPatternSize = maxPatternSize;
      matchQueues = new int[maxPatternSize];
      queue = new RotatingQueue<Integer>(maxPatternSize + 1);

      // Initiallize Queue
      for(int i=0; i<queue.size(); i++) {
         queue.insertElement(0);
      }
   }

   public void addPatternFinderConsumer(PatternFinderConsumer patternFinderConsumer) {
      consumers.add(patternFinderConsumer);
   }


   public void consumeSuperBlock(SuperBlock superBlock) {
      int hashValue = superBlock.getHash();
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

      // Check if there is a pattern
      // Look if bit position is set, only first encountered matters
      int bitIndex = maxPatternSize - 1;
      for (int i = 0; i < matchQueues.length; i++) {
         //if (BitUtils.getBit(bitIndex, matchQueues[i]) > 0) {
         // Instead of using the same bitIndex for every queue, look at the bit
         // the same size as the pattern
         if (BitUtils.getBit(i, matchQueues[i]) > 0) {
            // There was a match! Inform and return.
            updateConsumers(i + 1);
            return;
         }
      }

      // No pattern was found
      updateConsumers(0);

   }


   private void updateConsumers(int patternSize) {
      // Send current pattern size to all listeners
      for(PatternFinderConsumer consumer : consumers) {
         consumer.consumePatternSize(patternSize);
      }
   }

   public void flush() {
      // Call flush to all listeners
      for(PatternFinderConsumer consumer : consumers) {
         consumer.flush();
      }
   }

   /**
    * INSTANCE VARIABLES
    */
   private int maxPatternSize;
   private int[] matchQueues;
   private RotatingQueue<Integer> queue;

   private List<PatternFinderConsumer> consumers;


}
