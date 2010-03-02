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

package org.ancora.MicroBlazeToIr;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import org.ancora.IrForDynamicMapping.Operation;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.MicroBlaze.Instructions.InstructionName;

/**
 * Receives a list of MicroBlaze Instructions and returns a list of IR 
 * Operations.
 *
 * @author Joao Bispo
 */
public class MbToIrParser {

   public static List<Operation> parseInstructions(List<Instruction> instructions) {
      // Start index
      int index = 0;
      // List of Operations
      List<Operation> operations = new LinkedList<Operation>();

      while(index < instructions.size()) {
         index = parseIntruction(instructions, operations, index);
      }

      return operations;
   }

   private static int parseIntruction(List<Instruction> instructions, List<Operation> operations, int index) {
      InstructionName instructionName = instructions.get(index).getOperation();
      switch(instructionName) {
         case add:
            return ArithmeticAndLogic.parseIntegerAdd(instructions, operations, index);
         case addc:
            return ArithmeticAndLogic.parseIntegerAdd(instructions, operations, index);
         case addk:
            return ArithmeticAndLogic.parseIntegerAdd(instructions, operations, index);
         case addkc:
            return ArithmeticAndLogic.parseIntegerAdd(instructions, operations, index);
         case addi:
            return ArithmeticAndLogic.parseIntegerAdd(instructions, operations, index);
         case addic:
            return ArithmeticAndLogic.parseIntegerAdd(instructions, operations, index);
         case addik:
            return ArithmeticAndLogic.parseIntegerAdd(instructions, operations, index);
         case addikc:
            return ArithmeticAndLogic.parseIntegerAdd(instructions, operations, index);
            
         default:
            Logger.getLogger(MbToIrParser.class.getName()).
                    warning("Parsing for instruction '"+instructionName+"' not implemented.");
            index++;
            return index;
      }
   }

}
