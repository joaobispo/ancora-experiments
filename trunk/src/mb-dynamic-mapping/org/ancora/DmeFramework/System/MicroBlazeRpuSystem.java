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
import org.ancora.DmeFramework.Statistics.HashCounter;
import org.ancora.IrForDynamicMapping.Operation;
import org.ancora.MbDynamicMapping.Transition.IrConstantPropagation;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.MicroBlazeToIr.MbToIrParser;

/**
 * Represents a computer system which has a MicroBlaze has the main CPU and an 
 * RPU which can be dynamically mapped.
 *
 * @author Joao Bispo
 */
public class MicroBlazeRpuSystem implements InstructionBlockListener {

   public MicroBlazeRpuSystem(Mapper mapper, MicroBlazeRpuMonitor systemMonitor) {
      this.mapper = mapper;
      this.monitor = systemMonitor;
      hashCounter = new HashCounter();

   }

    @Override
   public void accept(InstructionBlock instructionBlock) {
//             System.out.println(instructionBlock);

       if(mappingFailed) {
          return;
       }

      // Map all blocks on the RPU
       instructionBlock.setMapToHardware(true);
       
       boolean rpuMappable = instructionBlock.mapToHardware();

       // Check if the block is for the RPU or for the MicroBlaze processor
       if (rpuMappable) {
          mapToRpu(instructionBlock, mapper);
          monitor.addRpuExecution(instructionBlock, mapper.getMonitor());
       } else {
         monitor.addMicroBlazeExecution(instructionBlock);
       }

      // Update number of MicroBlaze Instructions
      monitor.incrementTotalMicroBlazeInstructions(instructionBlock.getTotalInstructions());

      //showInstructionBlock(instructionBlock);
      //mbInstructionsCounter+=instructionBlock.getTotalInstructions();
   }


   private void mapToRpu(InstructionBlock instructionBlock, Mapper mapper) {
      // Transform MicroBlaze instructions in IR operations
      // Feed them to the mapper
      //throw new UnsupportedOperationException("Not yet implemented");

      // Transform array in list
      List<Instruction> instructions = Arrays.asList(instructionBlock.getInstructions());
      // Give them to the parser
      List<Operation> operations = MbToIrParser.parseInstructions(instructions);

      
      // Perform optimizations on the operations
      IrConstantPropagation optimizations = new IrConstantPropagation();
      // Perform optimizations on all operations
      for(Operation operation : operations) {
         optimizations.acceptOperation(operation);
      }
      // Show optimized operations
      operations = optimizations.getOptimizedOperations();

      
      // Feed operations to mapper
      mapper.mapOperations(operations);

      // Check if mapping failed
      if(mapper.hasMappingFailed()) {
         mappingFailed = true;
         return;
      }

      // Check ilp
      /*
      if(mapper.getMonitor().getMaxIlp() > 1000) {
         System.out.println("ILP > 1000");
         System.out.println(instructionBlock);
         System.out.println("------Operations--------");
         System.out.println(operations);
      }
       */
   }

   /*
   private void mapToMicroBlaze(InstructionBlock instructionBlock) {
      // Add instructions 
      throw new UnsupportedOperationException("Not yet implemented");
   }
    */

   @Override
   public void flush() {
      //System.out.println("TotalMbInstructions:"+mbInstructionsCounter);
      // Do Nothing
   }

   public MicroBlazeRpuMonitor getMonitor() {
      return monitor;
   }

   public boolean hasMappingFailed() {
      return mappingFailed;
   }


   

   /**
    * INSTANCE VARIABLES
    */
   private Mapper mapper;
   private MicroBlazeRpuMonitor monitor;
   private boolean mappingFailed = false;

   //private Map<Integer,Integer> hashes;
   //private int counter;
   private HashCounter hashCounter;
   //private int mbInstructionsCounter;

   private void showInstructionBlock(InstructionBlock block) {
      int index = hashCounter.convertHash(block.getHash());
      int instruc = block.getTotalInstructions();
      System.out.println("Hash:"+index+", Iterations:"+block.getIterations()+", Instructions:"+instruc);

      // Check if instructionblock was already shown
      /*
      if(!shownBlocks.contains(block.getHash())) {
         shownBlocks.add(block.getHash());
         System.out.println(block);
      }
       */
   }


}
