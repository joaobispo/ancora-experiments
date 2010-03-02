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

package org.ancora.IrForDynamicMapping;

/**
 *
 * @author Joao Bispo
 */
public enum OperationName {

   integer_add(3,2),
   integer_rsub(3,2),
   integer_and(2,1),
   move(1,1),
   exit_equal(1,1),
   exit_not_equal(1,1),
   exit_less(1,1),
   exit_less_or_equal(1,1),
   exit_greater(1,1),
   exit_greater_or_equal(1,1),
   invalid_op(0,0);

   private OperationName(int numInputs, int numOutputs) {
      this.numInputs = numInputs;
      this.numOutputs = numOutputs;
   }

   public int getNumInputs() {
      return numInputs;
   }

   public int getNumOutputs() {
      return numOutputs;
   }



   /**
    * INSTANCE VARIABLES
    */
   private final int numInputs;
   private final int numOutputs;
}
