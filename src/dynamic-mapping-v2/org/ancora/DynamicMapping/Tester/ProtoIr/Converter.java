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

package org.ancora.DynamicMapping.Tester.ProtoIr;

import org.ancora.DynamicMapping.Tester.ProtoIr.Parser.Parser;
import java.util.List;
import org.ancora.MicroBlaze.Instructions.Instruction;

/**
 * Converts MicroBlaze Instructions into IR Operations.
 *
 * @author Joao Bispo
 */
public class Converter {

   public static void convert(List<Instruction> instructions) {
      ConverterState state = new ConverterState();

      for(Instruction ins : instructions) {
         Parser.parseInstruction(ins, state);
      }

      // Show operations
   }
}
