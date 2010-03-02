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

package org.ancora.MbDynamicMapping.Transition;

import java.util.Arrays;
import java.util.List;
import org.ancora.IrForDynamicMapping.Operation;
import org.ancora.MbDynamicMapping.Interface.InstructionBlock;
import org.ancora.MbDynamicMapping.Interface.InstructionBlockListener;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.MicroBlazeToIr.MbToIrParser;

/**
 *
 * @author Joao Bispo
 */
public class DmeParser implements InstructionBlockListener {

   @Override
   public void accept(InstructionBlock instructionBlock) {
      // Transform array in list
      List<Instruction> instructions = Arrays.asList(instructionBlock.getInstructions());
      // Give them to the parser
      List<Operation> operations = MbToIrParser.parseInstructions(instructions);
      // Show
      show(instructions, operations);

   }

   @Override
   public void flush() {
      // Do Nothing
   }

   private void show(List<Instruction> instructions, List<Operation> operations) {
      System.out.println("Translated Instructions:");
      System.out.println("-----------------------");
      for(Instruction instruction : instructions) {
         System.out.println(instruction);
      }
      System.out.println("-----------------------");
      for(Operation operation : operations) {
         System.out.println(operation);
      }
      System.out.println(" ");
   }

}
