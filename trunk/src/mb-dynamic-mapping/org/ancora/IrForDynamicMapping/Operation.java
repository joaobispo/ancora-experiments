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

import org.ancora.SharedLibrary.BitUtils;

/**
 * Represents a general instruction which can be mapped on the RPU.
 * 
 * @author Joao Bispo
 */
public class Operation {


   private Operation(int numInputs, int numOutputs) {
      this.address = 0;
      this.operation = OperationName.invalid_op;
      this.inputs = new Operand[numInputs];
      this.outputs = new Operand[numOutputs];
   }

   public static Operation newOperation(OperationName operationName) {
      Operation operation = new Operation(operationName.getNumInputs(), operationName.getNumOutputs());
      operation.setOperation(operationName);
      return operation;
   }

   /**
    * Creates a copy of the operation
    * @param operation
    * @return
    */
   public Operation copy() {
      Operation newOperation = new Operation(this.inputs.length, this.outputs.length);

      newOperation.address = this.address;
      newOperation.operation = this.operation;

      for(int i=0; i<newOperation.inputs.length;i++) {
         newOperation.inputs[i] = this.inputs[i];
      }

      for(int i=0; i<newOperation.outputs.length;i++) {
         newOperation.outputs[i] = this.outputs[i];
      }

      return newOperation;
   }

   /*
   public Operation(int address, String operation) {
      this.address = address;
      this.operation = operation;
      this.inputs = new ArrayList<Operand>();
      this.outputs = new ArrayList<Operand>();
   }
    */

   public OperationName getOperation() {
      return operation;
   }

   public int getAddress() {
      return address;
   }

   
   public Operand[] getInputs() {
      return inputs;
   }
    

   public Operand[] getOutputs() {
      return outputs;
   }

   public void setAddress(int address) {
      this.address = address;
   }
/*
   public void setInputs(Operand[] inputs) {
      this.inputs = inputs;
   }
*/
   private void setOperation(OperationName operation) {
      this.operation = operation;
   }
/*
   public void setOutputs(Operand[] outputs) {
      this.outputs = outputs;
   }
*/
   public Operand getInput(InputIndex index) {
      return inputs[index.getIndex()];
   }

   public Operand getInput(int index) {
      return inputs[index];
   }

   public Operand getOutput(OutputIndex index) {
      return outputs[index.getIndex()];
   }

   public Operand getOutput(int index) {
      return outputs[index];
   }

   public void setInput(InputIndex index, Operand operand) {
      inputs[index.getIndex()] = operand;
   }

   public void setOutput(OutputIndex index, Operand operand) {
      outputs[index.getIndex()] = operand;
   }

   public void setInput(int index, Operand operand) {
      inputs[index] = operand;
   }

   public void setOutput(int index, Operand operand) {
      outputs[index] = operand;
   }

   @Override
   public String toString() {

      StringBuilder builder = new StringBuilder();

      builder.append(BitUtils.padHexString(Integer.toHexString(address), 8));
      builder.append(" ");
      builder.append(operation);

      builder.append(";");
      for(Operand operand : inputs) {
         if(operand != null) {
            builder.append(" ");
            builder.append(operand);
         }
      }
      
      builder.append(";");
      for(Operand operand : outputs) {
         if(operand != null) {
            builder.append(" ");
            builder.append(operand);
         }
      }

      return builder.toString();
   }

   

   /**
    * INSTANCE VARIABLES
    */
   private int address;
   private OperationName operation;
   private final Operand[] inputs;
   private final Operand[] outputs;
}
