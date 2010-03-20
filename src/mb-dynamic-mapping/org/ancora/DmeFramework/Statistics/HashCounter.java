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

package org.ancora.DmeFramework.Statistics;

import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author Joao Bispo
 */
public class HashCounter {

   public HashCounter() {
      hashes = new Hashtable<Integer,Integer>();
      counter = 1;
   }


   /**
    * Given an hash value, returns a normalized value for this hash.
    *
    * @param hash
    * @return
    */
   public int convertHash(int hash) {
      Integer index = hashes.get(hash);
      if (index == null) {
         index = counter;
         hashes.put(hash, index);
         counter++;
      }

      return index;
   }

   /**
    * INSTANCE VARIABLES
    */
      private Map<Integer,Integer> hashes;
   private int counter;
}
