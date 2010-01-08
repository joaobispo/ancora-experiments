/*
 *  Copyright 2009 Ancora Research Group.
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

package org.ancora.MbTraceAnalyser.DataObjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Stores a list of hashes, which represent SuperBlocks, and a table which maps
 * the hashes to InstructionFlow objects.
 * @author Joao Bispo
 */
public class TraceFlow {

   public TraceFlow(Map<Integer, InstructionFlow> sequenceTable, List<Integer> superblockFlow) {
      this.sequenceTable = sequenceTable;
      this.superblockFlow = superblockFlow;
      
      // Maps the hash values to numbers, for easier identification
      this.hashToIndex = new HashMap<Integer, Integer>();
      int index = 1;
      for(Integer hashValue : sequenceTable.keySet()) {
         hashToIndex.put(hashValue, index);
         index++;
      }
   }

   /**
    * Returns a unique, human-readable value for each hash value
    * 
    * @param hashValue
    * @return
    */
   public Integer getHashIndex(Integer hashValue) {
      Integer index = hashToIndex.get(hashValue);
      if(index == null) {
         Logger.getLogger(TraceFlow.class.getName()).
                 warning("Hash value '"+hashValue+"' has no index associated." +
                 " Returning 0.");
         return 0;
      }

      return index;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      for(Integer superblockHash : superblockFlow) {
         builder.append(sequenceTable.get(superblockHash).toString());
         builder.append("\n");
      }

      return builder.toString();
   }

   public Map<Integer, InstructionFlow> getSequenceTable() {
      return sequenceTable;
   }

   

   public List<Integer> getFlow() {
      return superblockFlow;
   }

   ///
   // INSTANCE VARIABLES
   ///
   private final Map<Integer, InstructionFlow> sequenceTable;
   private final List<Integer> superblockFlow;
   private final Map<Integer, Integer> hashToIndex;
}
