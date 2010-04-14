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

package org.ancora.DynamicMapping.Tester.ProtoIr.Ir;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joao Bispo
 */
public class Operation {

   public Operation(String type) {
   //public Operation(OperationType type) {
      inputs = new ArrayList<Operand>();
      outputs = new ArrayList<Operand>();
      parameters = new ArrayList<Operand>();
      this.type = type;
   }

   public List<Operand> getInputs() {
      return inputs;
   }

   public List<Operand> getOutputs() {
      return outputs;
   }

   public List<Operand> getParameters() {
      return parameters;
   }

   public String getType() {
      return type;
   }

   @Override
   public String toString() {
      return type;
   }



   /**
    * INSTANCE VARIABLES
    */
   private List<Operand> inputs;
   private List<Operand> outputs;
   private List<Operand> parameters;
   //private OperationType type;
   private String type;
}
