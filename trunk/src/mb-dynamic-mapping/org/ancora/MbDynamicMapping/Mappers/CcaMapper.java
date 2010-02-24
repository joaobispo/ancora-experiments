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

package org.ancora.MbDynamicMapping.Mappers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ancora.MbDynamicMapping.Architecture.GeneralInstruction;
import org.ancora.MbDynamicMapping.Architecture.InfiniteForwardMatrix;
import org.ancora.MbDynamicMapping.IR.MicroBlazeParsing;
import org.ancora.MbDynamicMapping.IR.Operation;
import org.ancora.MbDynamicMapping.Interface.InstructionBlock;
import org.ancora.MbDynamicMapping.Interface.InstructionWindow;
import org.ancora.MbDynamicMapping.Interface.Mapper;
import org.ancora.MbDynamicMapping.Mappers.InfiniteCcaLib.Cca;
import org.ancora.MbDynamicMapping.Mappers.InfiniteCcaLib.Coordinate;
import org.ancora.MbDynamicMapping.Mappers.InfiniteCcaLib.Monitor;
import org.ancora.MbDynamicMapping.Options.MapperName;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.SharedLibrary.MbDynamicMapping.BitUtils;
import org.ancora.SharedLibrary.MbDynamicMapping.MicroBlaze.InstructionName;

/**
 *
 * @author Joao Bispo
 */
public class CcaMapper implements Mapper {

   public CcaMapper() {
      this.monitor = new Monitor();
   }



   @Override
   public String getName() {
      return MapperName.infinitecca.getName();
   }

   @Override
   public int getTotalMappedInstructions() {
      return monitor.totalInstructions;
   }

   @Override
   public void accept(InstructionBlock instructionBlock) {
      updateMonitorInstructionCount(instructionBlock);
      
      //clearState();
      // Create new Matrix for Mapping
      InfiniteForwardMatrix matrix = new InfiniteForwardMatrix();

      // Parse each instruction
      InstructionWindow instWindow = new InstructionWindow(instructionBlock);
      Instruction instruction = instWindow.nextInstruction();
      int immCounter = 0;
      while(instruction != null) {
         Operation operation = MicroBlazeParsing.parseInstruction(instruction, instWindow);
         /*
         Operation operation = new Operation();
         operation.setAddress(instruction.getAddress());
         operation.setOperation(instruction.getOperation());
         MicroBlazeParsing.parseOperandsType1(instruction, operation);
         */
          System.out.println(operation);

         // Parse instruction into a general instruction
         //GeneralInstruction gInst = GeneralInstruction.parseMicroblazeInstruction(instruction);
/*
         if(!instruction.getOperation().equals(InstructionName.imm.getName())) {
            immCounter++;
         }

         // Check in instruction is "IMM". If yes, join instruction with the
         // next one.
         instruction = fuseImm(instruction, instWindow);
*/
         
         instruction = instWindow.nextInstruction();

      }

      
 /*
      for(Instruction inst : instructionBlock.getInstructions()) {
         clearInstructionState();

         //if(inst.getOperation().equals("imm")) {
         //   hasImm = true;
         //}

         // Parse Read Registers
         for(Integer read : inst.getReadRegisters()) {
            
            boolean validReg = parseReadRegs(read);
         }

         // Destination line is known; Increment it;
         
         //Add MOVE operations
      
         for(int i=0; i<inputProducers.size(); i++) {
            Coordinate moveCoor = cca.addMoveOperations(destinationLine, inputProducers.get(i));
            // If MOVE operation where introduced, update currentProducers
            
            if(moveCoor != null) {
               currentProducers.put(inputRegisters.get(i), moveCoor);
            }
             
         }
        

         // Place operation
         Coordinate coor = cca.mapOperation(destinationLine, inst.getOperation(), inputProducers);

         // Parse Write Register
         boolean validReg = parseWriteRegister(inst.getWriteRegister(), coor);

      }
*/
      //System.out.println("Number of Moves:"+cca.getMoveOperations());
      //if(hasImm) {
      //   System.out.println(cca);
      //}
      //System.out.println(cca);
      //System.out.println("Live-Ins:"+liveIns);
      //System.out.println("Live-Outs:"+liveOuts);
      //updateStats();
   }

   @Override
   public void flush() {
      // Do Nothing
      //System.out.println(monitor.getStats());
      System.out.println("Number of IMMs:"+monitor.immInstructions);
   }


   private void clearState() {
      this.liveIns = new HashSet<Integer>();
      this.liveOuts = new HashSet<Integer>();
      this.currentProducers = new Hashtable<Integer, Coordinate>();
      this.destinationLine = 0;
      this.inputProducers = new ArrayList<Coordinate>();
      this.inputRegisters = new ArrayList<Integer>();
      this.cca = new Cca();
   }


   private void clearInstructionState() {
      this.destinationLine = 0;
      this.inputProducers = new ArrayList<Coordinate>();
      this.inputRegisters = new ArrayList<Integer>();
   }


   private boolean parseReadRegs(Integer read) {
      // When reading, R0 is equivalent to literal 0
      if(read == 0) {
         return false;
      }

      // Check if there is a producer for this register
      if(currentProducers.containsKey(read)) {
         Coordinate coor = currentProducers.get(read);
         // Calculate new candidate destination line
         destinationLine = Math.max(destinationLine, (coor.getLine()+1));
         inputProducers.add(coor);
         inputRegisters.add(read);

         //System.out.println("Register "+read+" @ line "+coor.getLine());
         //System.out.println("Destination Line:"+destinationLine);

      } else {
         liveIns.add(read);
      }

      return true;
   }

   private boolean parseWriteRegister(Integer write, Coordinate coor) {
      if(write == null) {
         return false;
      }

      // Writing to R0 is discarded
      if(write == 0) {
         return false;
      }
      // Add to live-outs
      liveOuts.add(write);

      // Update current producers table
      currentProducers.put(write, coor);

      return true;
   }


   private void updateStats() {
      monitor.totalMoveInstructions += cca.getMoveOperations();
      monitor.totalLiveIn += liveIns.size();
      monitor.totalLiveOut += liveOuts.size();
      //monitor.totalIlp += cca.getIlp();
      monitor.totalCcaLines += cca.getNumberMappedLines();

      monitor.maxIlp = Math.max(monitor.maxIlp, cca.getIlp());
      monitor.maxMappedLines = Math.max(monitor.maxMappedLines, cca.getNumberMappedLines());
      monitor.maxLiveIn = Math.max(monitor.maxLiveIn, liveIns.size());
      monitor.maxLiveOut = Math.max(monitor.maxLiveOut, liveOuts.size());
   }

   private void updateMonitorInstructionCount(InstructionBlock instructionBlock) {
      monitor.totalInstructions += instructionBlock.getTotalInstructions();
      if (instructionBlock.mapToHardware()) {
         // If I put limitations in what can be mapped, this is not right.
         monitor.totalMappedInstructions += instructionBlock.getTotalInstructions();
         monitor.totalMappedBlocks += 1;
      }
   }

   /**
    * Check if instruction is an "IMM". If yes, fuse this instruction with the
    * following one.
    *
    * @param instruction
    * @param instWindow
    */
   private Instruction fuseImm(Instruction instruction, InstructionWindow instWindow) {
      if(!instruction.getOperation().equals(InstructionName.imm.getName())) {
         return instruction;
      }

      monitor.immInstructions++;

      // Change literal of next instruction, by adding the literal of the current
      // instruction to the upper 16 bits of the next literal.
      Instruction typeBInst = instWindow.nextInstruction();

      int newLiteral = BitUtils.fuseInt(instruction.getImmediate(), typeBInst.getImmediate());

      typeBInst.setImmediate(newLiteral);
      return typeBInst;
   }

   /**
    * INSTANCE VARIABLES
    */
    private Map<Integer, Coordinate> currentProducers;
    private Set<Integer> liveIns;
    private Set<Integer> liveOuts;
    private int destinationLine;
    private List<Coordinate> inputProducers;
    private List<Integer> inputRegisters;

    private Cca cca;
    private Monitor monitor;









}
