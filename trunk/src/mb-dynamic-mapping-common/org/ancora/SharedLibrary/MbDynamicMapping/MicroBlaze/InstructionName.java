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

package org.ancora.SharedLibrary.MbDynamicMapping.MicroBlaze;

import java.util.logging.Logger;

/**
 * Represents the valid operations in the MicroBlaze Architecture, according
 * to MicroBlaze Processor Reference Guide v10.3.
 *
 * TODO: Add the names of the instructions; Change MicroBlaze.Instruction to
 * use the enum instead of a String.
 * @author Joao Bispo
 */
public enum InstructionName {

   add,
   addc,
   addk,
   addkc,
   addi,
   addic,
   addik,
   addikc,
   and,
   andi,
   andn,
   andni,
   beq,
   beqd,
   beqi,
   beqid,
   //...
   imm,
   //...
   xori;

   public String getName() {
      return this.name();
   }

   public static InstructionName getEnum(String instructionName) {
      try{
         return valueOf(instructionName);
      } catch(IllegalArgumentException ex) {
         Logger.getLogger(InstructionName.class.getName()).
                 warning("Instruction not yet present in the list: '"+instructionName+"'");
         return null;
      }
   }

}
