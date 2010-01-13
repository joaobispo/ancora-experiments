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
import org.ancora.MbTraceAnalyser.DataObjects.SuperBlockLoop;
import org.ancora.MbTraceAnalyser.Interfaces.SuperBlockLoopConsumer;

/**
 * Collects stats about the super block loops feed to it.
 *
 * @author Joao Bispo
 */
public class SuperBlockLoopMonitor implements SuperBlockLoopConsumer {

   public SuperBlockLoopMonitor() {
      totalInstructions = 0;
      instructionsInLoops = 0;
      sblList = new ArrayList<SuperBlockLoop>();
   }



   public void consumeSuperBlockLoop(SuperBlockLoop superBlockLoop) {
      // Show SuperBlockLoop
      // Iterations == 1, just show the blocks
      if(superBlockLoop.getIterations() == 1) {
         //System.out.println(superBlockLoop);
      } else {
         //System.out.println("---- Loop with "+superBlockLoop.getIterations()+" iterations ----");
         //System.out.println(superBlockLoop);
         //System.out.println("---- End Loop ----");
      }

      if(superBlockLoop.getIterations() > 1) {
         instructionsInLoops += superBlockLoop.getTotalInstructions();
      }

      totalInstructions += superBlockLoop.getTotalInstructions();
      sblList.add(superBlockLoop);
   }

   public void flush() {
      // Do nothing
   }

   public int getTotalInstructions() {
      return totalInstructions;
   }

   public float getLoopInstructions() {
      return instructionsInLoops;
   }

   public List<SuperBlockLoop> getSuperBlockLoops() {
      return sblList;
   }

   public void printSuperBlockLoops() {
      for (int i = 0; i < sblList.size(); i++) {
         SuperBlockLoop superBlockLoop = sblList.get(i);
         // Iterations == 1, just show the blocks
         if (superBlockLoop.getIterations() == 1) {
            System.out.println(superBlockLoop);
         } else {
            System.out.println("---- Loop with " + superBlockLoop.getIterations() + " iterations ----");
            System.out.println(superBlockLoop);
            System.out.println("---- End Loop ----");
         }
      }
   }

   public void showStats() {
      float loopInstructionsRatio = (float) instructionsInLoops / (float) totalInstructions;

      float[] sblRatios = new float[sblList.size()];
      boolean[] isLoop = new boolean[sblList.size()];

      for(int i=0; i<sblRatios.length; i++) {
         SuperBlockLoop sbl = sblList.get(i);
         sblRatios[i] = (float) sbl.getTotalInstructions() / (float) totalInstructions;
         isLoop[i] = sbl.getIterations() > 1;
      }
      printBar(sblRatios, isLoop);

      System.out.println("Total Instructions:"+totalInstructions);
      System.out.println("Loop Instructions Ratio:"+loopInstructionsRatio);
   }


   private void printBar(float[] sblRatios, boolean[] loop) {
      boolean printing = loop[0];
      float acc = sblRatios[0];

      float checker = 0;
      for(int i=1; i<sblRatios.length; i++) {

         // When there is a difference, print last one
         if(printing != loop[i]) {
            printBar(acc, printing);
            checker += acc;
            
            printing = loop[i];
            acc = sblRatios[i];
         } else {
            // Accumulate
            acc += sblRatios[i];
         }
      }

      // Check if last was printed
      if(printing == loop[loop.length-1]) {
         printBar(acc, printing);
         checker += acc;
      }

      System.out.println("Sum:"+checker);
   }

   private void printBar(float acc, boolean isLoop) {
      String type;
      if(isLoop) {
         type = "Loop";
      } else {
         type = "Normal";
      }

    double x = 27.5, y = 33.75;

    //System.out.printf("x = %f y = %g", x, y);
    //System.out.printf(" %s = %.3f%%", type, (acc*100));


      System.out.println(type+"(%):"+(acc*100));
   }


   /**
    * INSTANCE VARIABLES
    */
   private int totalInstructions;
   private int instructionsInLoops;
   private List<SuperBlockLoop> sblList;






}
