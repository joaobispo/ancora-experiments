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

package org.ancora.MbDynamicMapping.Architecture.InfiniteForwardMatrixLib;

import java.util.ArrayList;
import java.util.List;
import org.ancora.MbDynamicMapping.Architecture.MicroblazeFu;

/**
 * Represents an infinite line of FUs.
 *
 * @author Joao Bispo
 */
public class FuLine {

   public FuLine() {
      fuLine  = new ArrayList<MicroblazeFu>();
   }


   public void addFu(MicroblazeFu fu) {
      fuLine.add(fu);
   }

   /**
    * @param index
    * @return the Fu at the given index. If there is no Fu at that index, null
    * is returned.
    */
   public MicroblazeFu getFu(int index) {
      if(index >= fuLine.size()) {
         return null;
      }

      return fuLine.get(index);
   }

   /**
    * INSTANCE VARIABLES
    */
   private List<MicroblazeFu> fuLine;
}
