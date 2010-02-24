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

package org.ancora.MbDynamicMapping.IR;

import org.ancora.MbDynamicMapping.Architecture.*;
import org.ancora.MbDynamicMapping.IR.Operand;
import java.util.ArrayList;
import java.util.List;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.SharedLibrary.BitUtils;
import org.ancora.SharedLibrary.MbDynamicMapping.MicroBlaze.MicroBlazeParam;

/**
 * Represents a general instruction which can be mapped on the RPU.
 * 
 * @author Joao Bispo
 */
public class Operation {

   public Operation() {
      this.address = 0;
      this.operation = OperationName.invalid_op;
      this.inputs = new ArrayList<Operand>();
      this.outputs = new ArrayList<Operand>();
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

   public List<Operand> getInputs() {
      return inputs;
   }

   public List<Operand> getOutputs() {
      return outputs;
   }

   public void setAddress(int address) {
      this.address = address;
   }

   public void setInputs(List<Operand> inputs) {
      this.inputs = inputs;
   }

   public void setOperation(OperationName operation) {
      this.operation = operation;
   }

   public void setOutputs(List<Operand> outputs) {
      this.outputs = outputs;
   }

   @Override
   public String toString() {

      StringBuilder builder = new StringBuilder();

      builder.append(BitUtils.padHexString(Integer.toHexString(address), 8));
      builder.append(" ");
      builder.append(operation);

      builder.append(";");
      for(Operand operand : inputs) {
         builder.append(" ");
         builder.append(operand);
      }
      
      builder.append(";");
      for(Operand operand : outputs) {
         builder.append(" ");
         builder.append(operand);
      }

      return builder.toString();
   }

   

   /**
    * TODO: This parsing is more than meets the eye... there should be a
    * representation independent of the MicroBlaze (add, sub...), and the parsing
    * transforms the microblaze instructions into the general instructions.
    * 
    * @param mbInstruction
    * @return
    */
   /*
   public static Operation parseMicroblazeInstruction(Instruction mbInstruction) {
      // Create GeneralInstruction
      Operation gInst = new Operation(mbInstruction.getAddress(), mbInstruction.getOperation());

      // Parse read registers
      Integer[] readRegs = mbInstruction.getReadRegisters();
      for(Integer reg : readRegs) {
         Operand newData = null;
         // Check if is r0. If yes, this is considered as literal
         if(reg == 0) {
            newData = new Operand(Operand.OpType.literal, Integer.toString(0), MicroBlazeParam.REGISTER_SIZE_BITS);
         }
         //Add as a register
         else {
            newData = new Operand(Operand.OpType.register, Operand.registerAsString(reg), MicroBlazeParam.REGISTER_SIZE_BITS);
         }

         gInst.getInputList().add(newData);
      }

      // Parse immediate
      Integer immReg = mbInstruction.getImmediate();
      if(immReg != null) {
         gInst.getInputList().add(new Operand(Operand.OpType.literal, Operand.registerAsString(immReg), MicroBlazeParam.IMMEDIATE_SIZE_BITS));
      }

      // Parse output
      Integer writeReg = mbInstruction.getWriteRegister();
      if(writeReg != null) {
         gInst.getOutputList().add(new Operand(Operand.OpType.register, Operand.registerAsString(writeReg), MicroBlazeParam.REGISTER_SIZE_BITS));
      }

      return gInst;
   }
    */

   /**
    * INSTANCE VARIABLES
    */
   private int address;
   private OperationName operation;
   private List<Operand> inputs;
   private List<Operand> outputs;
}
