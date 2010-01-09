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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.ancora.MbTraceAnalyser.DataObjects.SuperBlock;
import org.ancora.MbTraceAnalyser.Interfaces.SuperBlockConsumer;

/**
 * Checks SuperBlock hashes, and warns if there is a collision within SuperBlocks.
 *
 * @author Joao Bispo
 */
public class HashMonitor implements SuperBlockConsumer {

   public HashMonitor() {
      table = new HashMap<Integer, SuperBlock>();
   }


   public void consumeSuperBlock(SuperBlock superBlock) {
      // Check if superblock is not in table
      if(!table.containsKey(superBlock.getHash())) {
         table.put(superBlock.getHash(), superBlock);
         return;
      }

      // Hash already exists. Check if stored SuperBlock corresponds to current
      // SuperBlock
      SuperBlock oldSuperBlock = table.get(superBlock.getHash());
      // If they are different, there was a collision
      if(!oldSuperBlock.compareDeep(superBlock)) {
         Logger logger = Logger.getLogger(HashMonitor.class.getName());
         logger.warning("Collision detected between SuperBlock hashes:");
         logger.warning("Previous SuperBlock:"+oldSuperBlock.toString());
         logger.warning("Current SuperBlock:"+superBlock.toString());
      }
   }

   public void flush() {
      // Do Nothing
   }

   /**
    * INSTANCE VARIABLES
    */
   private final Map<Integer,SuperBlock> table;

}
