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

package org.ancora.DynamicMapping.Tester;

import java.io.File;
import java.util.List;
import org.ancora.DynamicMapping.Tester.ProtoIr.Ir.Operation;
import org.ancora.DynamicMapping.Tester.ProtoIr.Parser.Interface.InstructionBusReader;
import org.ancora.DynamicMapping.Tester.ProtoIr.Parser.Interface.IrParser;
import org.ancora.DynamicMapping.Tester.ProtoIr.Parser.Interface.RawInstruction;
import org.ancora.DynamicMapping.Tester.ProtoIr.Parser.MicroBlaze.MicroBlazeToIrParser;
import org.ancora.DynamicMapping.Tester.ProtoIr.Parser.MicroBlaze.MicroBlazeTraceReader;
import org.ancora.SharedLibrary.LoggingUtils;


/**
 * Reads blocks of MicroBlaze instructions and transforms them into
 * an Intermediate Representation.
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        setupProgram();
        executeMain();
    }

   private static void setupProgram() {
      // Configure Logger to capture all output to console
      LoggingUtils.setupConsoleOnly();
   }

   /**
    * Reads a block of instructions, transforms them into IR
    */
   private static void executeMain() {
      // Files
      String filename;
      filename = "adpcm-coder-O0-block1.txt";
      //filename = "adpcm-coder-O0-block2.txt";

      File file = new File(filename);

      // Get Instructions
      // IMPORTANT: These instructions already have the delay slot instructions
      // moved before the jump, and the IMM instruction can be safely ignored,
      // the merged has already been done.
      InstructionBusReader busReader = MicroBlazeTraceReader.createTraceReader(file);
      List<RawInstruction> instructions = busReader.getAllInstructions();


      IrParser parser = new MicroBlazeToIrParser();
      Operation start = parser.parseToIr(instructions);

   }

   /*
   private static List<RawInstruction> getInstructions(File file) {


      List<Instruction> instructions = new ArrayList<Instruction>();
      TraceReader reader = TraceReader.createTraceReader(file);

      Instruction instruction = reader.nextInstruction();
      while(instruction != null) {
         instructions.add(instruction);
         instruction = reader.nextInstruction();
      }

      return instructions;
   }
    */

}
