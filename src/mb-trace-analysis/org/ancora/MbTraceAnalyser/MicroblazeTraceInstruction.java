/*
 *  Copyright 2009 Ancora Research Group.
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

package org.ancora.MbTraceAnalyser;

import org.ancora.SharedLibrary.BitUtils;

/**
 * Stores information about a Microblaze instruction read from a Microblaze 
 * trace.
 *
 * @author Joao Bispo
 */
public class MicroblazeTraceInstruction {

   public MicroblazeTraceInstruction(boolean isBranch, long instructionAddress) {
      this.isBranch = isBranch;
      this.instructionAddress = instructionAddress;
   }

   /**
    * @return true if instruction is a branch.
    */
   public boolean isBranch() {
      return isBranch;
   }

   /**
    *
    * @return the instruction address of this instruction.
    */
   public long getInstructionAddress() {
      return instructionAddress;
   }

   @Override
   public String toString() {
      String hexString = Long.toHexString(instructionAddress);
      hexString = BitUtils.padHexString(hexString, HEX_STRING_SIZE);

      return hexString+";Is branch: "+isBranch;
   }






   ///
   // INSTANCE VARIABLES
   ///
   private final boolean isBranch;
   private final long instructionAddress;

   // Constants
   private final int HEX_STRING_SIZE = 8;

}
