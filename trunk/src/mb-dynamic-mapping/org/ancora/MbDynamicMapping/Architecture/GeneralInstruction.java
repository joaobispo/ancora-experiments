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

import java.util.ArrayList;
import java.util.List;
import org.ancora.MicroBlaze.Instructions.Instruction;

/**
 * Represents a general instruction which can be mapped on the RPU.
 * 
 * @author Joao Bispo
 */
public class GeneralInstruction {

   public GeneralInstruction(int address, String operation) {
      this.address = address;
      this.operation = operation;
      this.inputs = new ArrayList<Data>();
      this.outputs = new ArrayList<Data>();
   }

   public List<Data> getInputList() {
      return inputs;
   }

   public List<Data> getOutputList() {
      return outputs;
   }

   /**
    * TODO: This parsing is more than meets the eye... there should be a
    * representation independent of the MicroBlaze (add, sub...), and the parsing
    * transforms the microblaze instructions into the general instructions.
    * 
    * @param mbInstruction
    * @return
    */
   public static GeneralInstruction parseMicroblazeInstruction(Instruction mbInstruction) {
      // Create GeneralInstruction
      GeneralInstruction gInst = new GeneralInstruction(mbInstruction.getAddress(), mbInstruction.getOperation());

      // Parse read registers
      Integer[] readRegs = mbInstruction.getReadRegisters();
      for(Integer reg : readRegs) {
         Data newData = null;
         // Check if is r0. If yes, this is considered as literal
         if(reg == 0) {
            newData = new Data(Data.DataType.literal, Integer.toString(0));
         }
         //Add as a register
         else {
            newData = new Data(Data.DataType.register, Data.registerAsString(reg));
         }

         gInst.getInputList().add(newData);
      }

      // Parse immediate
      Integer immReg = mbInstruction.getImmediate();
      if(immReg != null) {
         gInst.getInputList().add(new Data(Data.DataType.literal, Data.registerAsString(immReg)));
      }

      // Parse output
      Integer writeReg = mbInstruction.getWriteRegister();
      if(writeReg != null) {
         gInst.getOutputList().add(new Data(Data.DataType.register, Data.registerAsString(writeReg)));
      }

      return gInst;
   }

   /**
    * INSTANCE VARIABLES
    */
   private final int address;
   private final String operation;
   private final List<Data> inputs;
   private final List<Data> outputs;
}
