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

package org.ancora.DmeFramework.System;

import java.util.Arrays;
import java.util.List;
import org.ancora.DmeFramework.DataHolders.InstructionBlock;
import org.ancora.DmeFramework.Interfaces.Base.InstructionBlockListener;
import org.ancora.DmeFramework.Interfaces.Mapper;
import org.ancora.IrForDynamicMapping.Operation;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.MicroBlazeToIr.MbToIrParser;

/**
 * Represents a computer system which has a MicroBlaze has the main CPU and an 
 * RPU which can be dynamically mapped.
 *
 * @author Joao Bispo
 */
public class SystemMbRpu implements InstructionBlockListener {

   public SystemMbRpu(Mapper mapper) {
      this.mapper = mapper;
   }

    @Override
   public void accept(InstructionBlock instructionBlock) {
      boolean rpuMappable = instructionBlock.mapToHardware();

       // Check if the block is for the RPU or for the MicroBlaze processor
       if (rpuMappable) {
          mapToRpu(instructionBlock);
       }

      // Save stats to the monitor?

   }


   private void mapToRpu(InstructionBlock instructionBlock) {
      // Transform MicroBlaze instructions in IR operations
      // Feed them to the mapper
      //throw new UnsupportedOperationException("Not yet implemented");

      // Transform array in list
      List<Instruction> instructions = Arrays.asList(instructionBlock.getInstructions());
      // Give them to the parser
      List<Operation> operations = MbToIrParser.parseInstructions(instructions);
      // Feed operations to mapper
      mapper.mapOperations(operations);
   }

   /*
   private void mapToMicroBlaze(InstructionBlock instructionBlock) {
      // Add instructions 
      throw new UnsupportedOperationException("Not yet implemented");
   }
    */

   @Override
   public void flush() {
      // Do Nothing
   }

   /**
    * INSTANCE VARIABLES
    */
   private Mapper mapper;



}
