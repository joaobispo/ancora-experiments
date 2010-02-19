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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ancora.MbDynamicMapping.Interface.InstructionBlock;
import org.ancora.MbDynamicMapping.Interface.Mapper;
import org.ancora.MbDynamicMapping.Mappers.InfiniteCcaLib.Cca;
import org.ancora.MbDynamicMapping.Mappers.InfiniteCcaLib.Coordinate;
import org.ancora.MbDynamicMapping.Options.MapperName;
import org.ancora.MicroBlaze.Instructions.Instruction;

/**
 *
 * @author Joao Bispo
 */
public class InfiniteCca implements Mapper {

   public InfiniteCca() {

   }



   @Override
   public String getName() {
      return MapperName.infinitecca.getName();
   }

   @Override
   public int getTotalMappedInstructions() {
      return 0;
   }

   @Override
   public void accept(InstructionBlock instructionBlock) {
      clearState();


      for(Instruction inst : instructionBlock.getInstructions()) {

         // Parse Read Registers
         for(Integer read : inst.getReadRegisters()) {
            
            boolean validReg = parseReadRegs(read);
         }

         // Place operation
         Coordinate coor = cca.mapOperation(destinationLine, inst.getOperation(), inputProducers);

         // Parse Write Register
         boolean validReg = parseWriteRegister(inst.getWriteRegister(), coor);


         // Find a placement
         //findPlacement();
/*
         if(inst.getWriteRegister() != null) {
            liveOuts.add(inst.getWriteRegister());
         }
 */
         /*
         Integer[] readRegs = MicroBlazeUtils.parseReadRegisters(inst.getReadRegisters());
         if(readRegs.length == 0 && inst.getWriteRegister() == null) {
            if(!inst.getOperation().equals("imm")) {
            System.out.println("Instruction Discarded:");
            System.out.println(inst.getOperation()+":"+inst);
            }
         }
          */
      }
      System.out.println(cca);
      //System.out.println("Live-Ins:"+liveIns);
      //System.out.println("Live-Outs:"+liveOuts);
   }

   @Override
   public void flush() {
      // Do Nothing
   }


   private void clearState() {
      this.liveIns = new HashSet<Integer>();
      this.liveOuts = new HashSet<Integer>();
      this.currentProducers = new HashMap<Integer, Coordinate>();
      this.destinationLine = 0;
      this.inputProducers = new ArrayList<Coordinate>();
      this.cca = new Cca();
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

   /**
    * INSTANCE VARIABLES
    */
    private Map<Integer, Coordinate> currentProducers;
    private Set<Integer> liveIns;
    private Set<Integer> liveOuts;
    private int destinationLine;
    private List<Coordinate> inputProducers;

    private Cca cca;




}
