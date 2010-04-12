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

package org.ancora.DynamicMapping.Tester.ProtoIr.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import org.ancora.DynamicMapping.Tester.ProtoIr.ConverterState;
import org.ancora.DynamicMapping.Tester.ProtoIr.Ir.Operand;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.MicroBlaze.Instructions.InstructionName;

/**
 *
 * @author Joao Bispo
 */
public class Parser {

   public static void parseInstruction(Instruction instruction, ConverterState state) {
      // Do a light parsing on the instruction, to extract the name and the values
      // Already done in this case

      InstructionName instName = instruction.getOperation();

      // If IMM, ignore


      // Get "read" Operands
      List<Operand> readOperands = parseReadOperands(instName, instruction.getRegisters());



      // Find right parser
      /*
      if(isArithmeticWithCarry.contains(instName)) {
         Arithmetic.parseArithmeticWithCarry(instName, instruction.getRegisters(), state);
         return;
      }
       */
      
   }

   private static List<Operand> parseReadOperands(InstructionName instructionName, Integer[] data) {
      List<Operand> operands = new ArrayList<Operand>();

      System.out.println("Name:"+instructionName);
      System.out.println("Data:"+Arrays.toString(data));

      return operands;
   }

   /**
    * DEFINITIONS
    */
   public static final EnumSet<InstructionName> isArithmeticWithCarry = EnumSet.of(
           InstructionName.add,
           InstructionName.addc,
           InstructionName.addi,
           InstructionName.addic,
           InstructionName.addik,
           InstructionName.addikc,
           InstructionName.addk,
           InstructionName.addkc
      );


}
