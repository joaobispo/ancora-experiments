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

import java.util.logging.Logger;
import org.ancora.MbTraceAnalyser.DataObjects.SuperBlock;
import org.ancora.MbTraceAnalyser.Interfaces.SuperBlockConsumer;

/**
 * Collects stats about the super blocks feed to it.
 *
 * @author Joao Bispo
 */
public class SuperBlockMonitor implements SuperBlockConsumer {

   public SuperBlockMonitor() {
      logger = Logger.getLogger(SuperBlockMonitor.class.getName());
      totalInstructions = 0;
   }



   public void consumeSuperBlock(SuperBlock superBlock) {
      //logger.info(superBlock.toString());
      totalInstructions += superBlock.getTotalInstructions();
   }

   public void showStats() {
      logger.info("Total Instructions (SB): "+totalInstructions);
   }


   public void flush() {
      // Do nothing.
   }

   /**
    * INSTANCE VARIABLES
    */
   private final Logger logger;
   private int totalInstructions;



}
