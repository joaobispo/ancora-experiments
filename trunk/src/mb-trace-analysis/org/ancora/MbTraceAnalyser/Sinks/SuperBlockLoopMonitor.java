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

      totalInstructions += superBlockLoop.getTotalInstructions();
   }

   public void flush() {
      // Do nothing
   }

   public int getTotalInstructions() {
      return totalInstructions;
   }

   

   /**
    * INSTANCE VARIABLES
    */
   private int totalInstructions;

}
