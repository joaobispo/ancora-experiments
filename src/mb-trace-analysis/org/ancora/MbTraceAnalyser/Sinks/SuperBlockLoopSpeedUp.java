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
 *
 * @author Joao Bispo
 */
public class SuperBlockLoopSpeedUp implements SuperBlockLoopConsumer {

   public SuperBlockLoopSpeedUp() {
         totalInstructions = 0;
      instructionsInLoops = 0;
      maxIterations = 0;
   }


   public void consumeSuperBlockLoop(SuperBlockLoop superBlockLoop) {
      

      totalInstructions += superBlockLoop.getTotalInstructions();

      int maxInstructionsThreshold = 4;
      boolean loopThreshold = superBlockLoop.getTotalInstructions()/superBlockLoop.getIterations() > maxInstructionsThreshold;
      if(loopThreshold) {
         maxIterations = Math.max(maxIterations, superBlockLoop.getIterations());
      }

      if(superBlockLoop.getIterations() > 1 & loopThreshold) {
         instructionsInLoops += superBlockLoop.getTotalInstructions();
      }
   }

   public void flush() {
      // Do Nothing
   }

   public int getTotalInstructions() {
      return totalInstructions;
   }

   public int getMaxIterations() {
      return maxIterations;
   }

   
   

   /**
    * Calculates the ideal speed up, using the formula:
    *
    * <p>Speedup = (#cycles without optimization)/(#cycles with optimization)
    *
    * <p>where:
    * <br>#cycles without optimization = #total instructions x cpi
    * <br>#cycles with optimization = #cycles MicroProc + #cycles Co-Proc +
    * #cycles Communic
    * <br>Because this is an ideal case, #cycles Co-Proc = 0,
    * #cycles MicroProc = (#total instructions - #loop instructions) x cpi
    *
    * @return
    */
   public float idealSpeedUp(float cpi, int communication) {
      float cyclesWithoutOptimization = ((float) totalInstructions) * cpi;
      float cyclesWithOptimization = ((float)(totalInstructions - instructionsInLoops + communication)) * cpi;
      return cyclesWithoutOptimization / cyclesWithOptimization;
   }

   /**
    * INSTANCE VARIABLES
    */
   private int totalInstructions;
   private int instructionsInLoops;
   private int maxIterations;

}
