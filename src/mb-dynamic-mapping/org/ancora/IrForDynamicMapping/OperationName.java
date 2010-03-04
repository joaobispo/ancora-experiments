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
   integer_div(2,1),
   integer_div_unsigned(2,1),
   integer_mul(2,1),
   integer_and(2,1),
   integer_and_not(2,1),
   integer_or(2,1),
   integer_xor(2,1),
   integer_srl(2,1),
   integer_sll(2,1),
   integer_sra(2,1),
   integer_compare_msb(2,1),
   integer_compare_msb_unsigned(2,1),
   integer_sext_16(1,1),
   integer_sext_8(1,1),
   load_8(1,1),
   load_16(1,1),
   load_32(1,1),
   store_8(2,0),
   store_16(2,0),
   store_32(2,0),
   move(1,1),
   move_bit(3,1),
   exit(1,0),
   exit_equal(2,0),
   exit_not_equal(2,0),
   exit_less(2,0),
   exit_less_or_equal(2,0),
   exit_greater(2,0),
   exit_greater_or_equal(2,0),
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
