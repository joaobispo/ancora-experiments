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

package org.ancora.DynamicMapping.Tester.ProtoIr.Parser.MicroBlaze;

import java.util.List;
import org.ancora.DynamicMapping.Tester.ProtoIr.Ir.Operation;
import org.ancora.DynamicMapping.Tester.ProtoIr.Ir.OperationType;
import org.ancora.DynamicMapping.Tester.ProtoIr.Parser.Interface.IrParser;
import org.ancora.DynamicMapping.Tester.ProtoIr.Parser.Interface.RawInstruction;
import org.ancora.DynamicMapping.Tester.ProtoIr.Parser.MicroBlaze.MbParsing.ParserState;
import org.ancora.DynamicMapping.Tester.ProtoIr.Parser.MicroBlaze.MbParsing.Utils;

/**
 *
 * @author Joao Bispo
 */
public class MicroBlazeToIrParser implements IrParser {

   public Operation parseToIr(List<RawInstruction> instructions) {
      ParserState state = new ParserState();

      for(int i=0; i<instructions.size(); i++) {
         RawInstruction instruction = instructions.get(i);
         parseInstruction(instruction, state);
      }

      return state.getStartOperation();
   }

   private void parseInstruction(RawInstruction instruction, ParserState state) {
      String sInst = instruction.getInstruction();
      Utils.getRegisters(sInst);
   }

}
