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

package org.ancora.SharedLibrary.MbDynamicMapping;

import java.util.EnumSet;
import java.util.logging.Logger;
import org.ancora.MicroBlaze.Instructions.InstructionName;

/**
 *
 * @author Joao Bispo
 */
public class MicroBlazeUtils {
   /**
    * Removes special registers (ex.: R0, which is equivalent to literal 0).
    *
    * @param regs
    * @return
    */
   static public Integer[] parseReadRegisters(Integer[] regs) {
      /*
      if(regs.length > 2) {
         Logger.getLogger(MicroBlazeUtils.class.getName()).
                 warning("This method only supports up to 2 registers.");
      }
       */
      if(regs.length == 0) {
         return regs;
      }

      boolean[] isRegZero = new boolean[regs.length];
      int zeroCount = 0;
      for(int i=0; i<regs.length; i++) {
         if(regs[i] == 0) {
            isRegZero[i] = true;
            zeroCount++;
         }
      }
      
      if(zeroCount == regs.length) {
         return new Integer[0];
      }
      
      if(zeroCount == 0) {
         return regs;
      }
      
      Integer[] newRegs = new Integer[regs.length-zeroCount];
      int counter = 0;
      for(int i=0; i<regs.length; i++) {
         if(!isRegZero[i]) {
            newRegs[counter] = regs[i];
            counter++;
         }
      }

      return newRegs;
   }

   static public Integer parseWriteRegister(Integer reg) {
      if(reg == null) {
         return null;
      }

      if(reg == 0) {
         return null;
      } else {
         return reg;
      }
   }

   /**
    * @param operation the operation name (add, lw...)
    * @return true if the given MicroBlaze operation performs linking. False otherwise.
    */
   public static boolean performsLinking(InstructionName operation) {
      return instructionWithLinking.contains(operation);
   }

   /**
    * @param operation the operation name (add, lw...)
    * @return true if the given MicroBlaze operation is an uncoditional branch
    * which uses an absolute value. False otherwise.
    */
   public static boolean unconditionalBranchWithAbsoluteValue(InstructionName operation) {
      return instructionWithAbsoluteUnconditionalBranch.contains(operation);
   }

   /**
    * Instructions which perform linking
    */
   private static final EnumSet<InstructionName> instructionWithLinking = EnumSet.of(
           InstructionName.brld,
           InstructionName.brald,
           InstructionName.brlid,
           InstructionName.bralid
   );

   /**
    * Instructions which are uncoditional branches with absolute values
    */
   private static final EnumSet<InstructionName> instructionWithAbsoluteUnconditionalBranch = EnumSet.of(
           InstructionName.bra,
           InstructionName.brad,
           InstructionName.brald,
           InstructionName.brai,
           InstructionName.braid,
           InstructionName.bralid
   );

}
