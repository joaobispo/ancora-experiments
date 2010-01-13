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

package org.ancora.MbTraceAnalyser.Sinks;

import java.util.ArrayList;
import java.util.List;
import org.ancora.MbTraceAnalyser.DataObjects.SuperBlock;
import org.ancora.MbTraceAnalyser.Interfaces.PatternFinderConsumer;
import org.ancora.MbTraceAnalyser.Interfaces.SuperBlockConsumer;

/**
 *
 * @author Joao Bispo
 */
public class PatternFinderMonitor implements PatternFinderConsumer, SuperBlockConsumer{

   public PatternFinderMonitor() {
      updatedPatternSize = 0;
      previousPatternSize = 0;
      superBlocks = new ArrayList<SuperBlock>();
   }


   public void consumeSuperBlock(SuperBlock superBlock) {
      // Check if there was change
      if(previousPatternSize != updatedPatternSize) {
         // There was a pattern before, and now there is none.
         if(updatedPatternSize == 0) {
            // You know that the last superblock added is not part of the pattern
            SuperBlock outsiderSuperBlock = superBlocks.remove(superBlocks.size()-1);
            // Print found pattern
            showSuperBlocks();
            superBlocks = new ArrayList<SuperBlock>();
            showRegularSuperBlock(outsiderSuperBlock);
            showRegularSuperBlock(superBlock);
         }
         // Start new pattern
         else if(previousPatternSize == 0) {
            superBlocks.add(superBlock);
         }
         // Pattern changed size
         else {
            System.out.println("PATTERN CHANGED SIZE, "+previousPatternSize
                    +" -> "+updatedPatternSize);
            showSuperBlocks();
            superBlocks = new ArrayList<SuperBlock>();
            superBlocks.add(superBlock);
            //showRegularSuperBlock(superBlock);
         }

         previousPatternSize = updatedPatternSize;
      }
      else {
         if(previousPatternSize > 0) {
            superBlocks.add(superBlock);
         } else {
            showRegularSuperBlock(superBlock);
         }
      }

      /*
      if(patternSize != 0) {
         System.out.println("Pattern of size "+patternSize+" with superblock:");
         System.out.println(superBlock);
         patternSize = 0;
      }
       */
   }


   private void showSuperBlocks() {
      int remainderBlocks = superBlocks.size() % previousPatternSize;
      int iterations = superBlocks.size() / previousPatternSize;

      if(iterations != 0) {
         System.out.println("---Loop with "+ iterations + " iterations---");
         for(int i=0; i<previousPatternSize; i++) {
            System.out.println(superBlocks.get(i));
         }
         System.out.println("---------End Loop---------");
      }

      if(remainderBlocks != 0) {
         int startIndex = superBlocks.size()-remainderBlocks;
         for(int i = startIndex; i<superBlocks.size(); i++) {
            System.out.println(superBlocks.get(i));
         }
      }

      /*
      System.out.println("Pattern of size " + previousPatternSize + " , " +
              superBlocks.size() + " superblocks.");
      for (int i = 0; i < superBlocks.size(); i++) {
         System.out.println(superBlocks.get(i));
      }
       */
   }

   private void showRegularSuperBlock(SuperBlock superBlock) {
            //System.out.println("Regular SuperBlock:");
            System.out.println(superBlock);
   }


   public void consumePatternSize(int patternSize) {
      this.updatedPatternSize = patternSize;
   }

   public void flush() {
      // Do nothing.
   }

   /**
    * INSTANCE VARIABLES
    */
   private int updatedPatternSize;
   private int previousPatternSize;

   private List<SuperBlock> superBlocks;



}
