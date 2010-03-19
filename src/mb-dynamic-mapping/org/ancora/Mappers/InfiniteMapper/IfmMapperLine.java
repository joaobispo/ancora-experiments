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

package org.ancora.Mappers.InfiniteMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a line of the Infinite Forward Matrix.
 *
 * @author Joao Bispo
 */
public class IfmMapperLine {

   public IfmMapperLine() {
      elements = new ArrayList<Fu>();
   }

   public int getSize(){
      return elements.size();
   }

   public Fu getFu(int position) {
      return elements.get(position);
   }

   public void setFu(int position, Fu fu) {
      elements.set(position, fu);
   }

   public void addFu(Fu fu) {
      elements.add(fu);
   }

   /**
    * INSTANCE VARIABLE
    */
   private List<Fu> elements;
}
