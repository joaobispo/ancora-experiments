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

package org.ancora.MbDynamicMapping.Architecture;

import java.util.List;
import org.ancora.SharedLibrary.MbDynamicMapping.DataContainer.Coordinate;

/**
 * Represents an FU which can map a MicroBlaze instruction.
 *
 * @author Joao Bispo
 */
public class MicroblazeFu {

   public MicroblazeFu(String operationName, List<MicroblazeFu> inputs, Coordinate coordinate) {
      this.operationName = operationName;
      this.inputs = inputs;
      this.coordinate = coordinate;
   }


   public Coordinate getCoordinate() {
      return coordinate;
   }

   public List<MicroblazeFu> getInputs() {
      return inputs;
   }

   public String getOperationName() {
      return operationName;
   }



   /**
    * INSTANCE VARIABLES
    */
   private String operationName;
   private List<MicroblazeFu> inputs;
   private Coordinate coordinate;
}
