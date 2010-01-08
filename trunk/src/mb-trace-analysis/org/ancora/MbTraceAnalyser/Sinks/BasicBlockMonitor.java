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
import org.ancora.MbTraceAnalyser.DataObjects.BasicBlock;
import org.ancora.MbTraceAnalyser.Interfaces.BasicBlockConsumer;

/**
 * Collects stats about the basic blocks feed to it.
 *
 * @author Joao Bispo
 */
public class BasicBlockMonitor implements BasicBlockConsumer {

   public BasicBlockMonitor() {
      logger = Logger.getLogger(BasicBlockMonitor.class.getName());
      totalInstructions = 0;
   }



   public void consumeBasicBlock(BasicBlock basicBlock) {
      //logger.info(basicBlock.toString());
      totalInstructions += basicBlock.getInstructionCount();
   }

   public void showStats() {
      logger.info("Total Instructions (BB): "+totalInstructions);
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
