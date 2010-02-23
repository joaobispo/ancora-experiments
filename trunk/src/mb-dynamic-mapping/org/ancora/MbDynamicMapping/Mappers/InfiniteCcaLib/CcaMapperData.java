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

package org.ancora.MbDynamicMapping.Mappers.InfiniteCcaLib;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Contains the data needed to perform the mapping.
 *
 * @author Joao Bispo
 */
public class CcaMapperData {

   public CcaMapperData() {
      this.liveIns = new HashSet<Integer>();
      this.liveOuts = new HashSet<Integer>();
      this.currentProducers = new Hashtable<Integer, Coordinate>();
      this.destinationLine = 0;
      this.inputProducers = new ArrayList<Coordinate>();
      this.inputRegisters = new ArrayList<Integer>();
   }

   public void clearInstructionState() {
      this.destinationLine = 0;
      this.inputProducers = new ArrayList<Coordinate>();
      this.inputRegisters = new ArrayList<Integer>();
   }

   /**
    * 
    * @param read
    * @return
    */
   public boolean parseReadRegs(Integer read) {
      // When reading, R0 is equivalent to literal 0
      if(read == 0) {
         return false;
      }

      // Check if there is a producer for this register
      if(currentProducers.containsKey(read)) {
         Coordinate coor = currentProducers.get(read);
         // Calculate new candidate destination line
         destinationLine = Math.max(destinationLine, (coor.getLine()+1));
         inputProducers.add(coor);
         inputRegisters.add(read);

         //System.out.println("Register "+read+" @ line "+coor.getLine());
         //System.out.println("Destination Line:"+destinationLine);

      } else {
         liveIns.add(read);
      }

      return true;
   }

   /**
    * INSTANCE VARIABLES
    */
    private Map<Integer, Coordinate> currentProducers;
    private Set<Integer> liveIns;
    private Set<Integer> liveOuts;
    private int destinationLine;
    private List<Coordinate> inputProducers;
    private List<Integer> inputRegisters;
}
