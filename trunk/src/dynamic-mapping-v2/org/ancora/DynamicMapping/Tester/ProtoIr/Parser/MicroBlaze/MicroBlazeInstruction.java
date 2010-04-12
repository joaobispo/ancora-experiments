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

import org.ancora.DynamicMapping.Tester.ProtoIr.Parser.Interface.RawInstruction;
import org.ancora.SharedLibrary.BitUtils;

/**
 *
 * @author Joao Bispo
 */
public class MicroBlazeInstruction implements RawInstruction{

   public MicroBlazeInstruction(int address, String instruction) {
      this.address = address;
      this.instruction = instruction;
   }


   public int getAddress() {
      return address;
   }

   public String getInstruction() {
      return instruction;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      builder.append(BitUtils.padHexString(Integer.toHexString(address), 8));
      builder.append(" ");

      builder.append(instruction);

      return builder.toString();
   }



   /**
    * INSTANCE VARIABLES
    */
   private int address;
   private String instruction;


}
