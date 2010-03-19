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

import org.ancora.IrForDynamicMapping.Operand;
import org.ancora.IrForDynamicMapping.Operation;

/**
 *
 * @author Joao Bispo
 */
public class Fu {

   public Fu(Operation operation, Operand[] inputs, Operand output) {
      this.operation = operation;
      this.inputs = inputs;
      this.output = output;
      //this.selfCoordinates = selfCoordinates;
   }

   public Operation getOperation() {
      return operation;
   }

   public Operand getOutput() {
      return output;
   }

   

   /**
    * INSTANCE VARIABLES
    */
   private Operation operation;
   private Operand[] inputs;
   private Operand output;
   //private Operand selfCoordinates;
}
