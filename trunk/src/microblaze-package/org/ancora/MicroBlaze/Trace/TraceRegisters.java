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

package org.ancora.MicroBlaze.Trace;

import java.util.logging.Logger;

/**
 * Indicates, for each trace instruction, which registers are for read or
 * write, by the order they appear in the trace instruction.
 *
 * @author Joao Bispo
 */
public enum TraceRegisters {
   ///
   // The enums are initialized with the order by which the registers of the
   // instructions appear in the reference manual of MicroBlaze.
   //
   /// Examples:
   // Reference -> rD,rA,rB
   // Code -> Property.write, Property.read, Property.read
   //
   // Reference -> rD,rA
   // Code -> Property.write, Property.read
   //
   // Reference -> rA
   // Code -> Property.read
   //
   // Reference -> rD
   // Code -> Property.write

   add(Property.write, Property.read, Property.read),
   addc(Property.write, Property.read, Property.read),
   addk(Property.write, Property.read, Property.read),
   addck(Property.write, Property.read, Property.read),
   addi(Property.write, Property.read),
   addic(Property.write, Property.read),
   addik(Property.write, Property.read),
   addikc(Property.write, Property.read),
   and(Property.write, Property.read, Property.read),

   imm(),
   andi(Property.write, Property.read),
   beqi(Property.read),
   beqid(Property.read),
   bgei(Property.read),
   bgeid(Property.read),
   bgti(Property.read),
   bgtid(Property.read),
   blei(Property.read),
   bleid(Property.read),
   blti(Property.read),
   bltid(Property.read),
   bnei(Property.read),
   bneid(Property.read),
   br(Property.read),
   bra(Property.read),
   brd(Property.read),
   brad(Property.read),
   brld(Property.write, Property.read),
   brald(Property.write, Property.read),
   bri(),
   brai(),
   brid(),
   braid(),
   brlid(Property.write),
   bralid(Property.write),
   bsrli(Property.write, Property.read),
   bsrai(Property.write, Property.read),
   bslli(Property.write, Property.read),
   cmp(Property.write, Property.read, Property.read),
   cmpu(Property.write, Property.read, Property.read),
   idiv(Property.write, Property.read, Property.read),
   idivu(Property.write, Property.read, Property.read),
   lbu(Property.write, Property.read, Property.read),
   lbui(Property.write, Property.read),
   lhu(Property.write, Property.read, Property.read),
   lhui(Property.write, Property.read),
   lw(Property.write, Property.read, Property.read),
   lwi(Property.write, Property.read),
   mul(Property.write, Property.read, Property.read),
   or(Property.write, Property.read, Property.read),
   ori(Property.write, Property.read),
   rsub(Property.write, Property.read, Property.read),
   rsubc(Property.write, Property.read, Property.read),
   rsubk(Property.write, Property.read, Property.read),
   rsubkc(Property.write, Property.read, Property.read),
   rtsd(Property.read),
   sb(Property.write, Property.read, Property.read),
   sbi(Property.write, Property.read),
   sext16(Property.write, Property.read),
   sext8(Property.write, Property.read),
   sh(Property.write, Property.read, Property.read),
   shi(Property.write, Property.read),
   sra(Property.write, Property.read),
   srl(Property.write, Property.read),
   sw(Property.write, Property.read, Property.read),
   swi(Property.write, Property.read),
   xori(Property.write, Property.read),
   rsubi(Property.write, Property.read),
   rsubic(Property.write, Property.read),
   rsubik(Property.write, Property.read),
   rsubikc(Property.write, Property.read),
   muli(Property.write, Property.read);


   private TraceRegisters(Property... properties) {
      this.properties = properties;
   }

   public static Property[] getProperties(String operation) {
      Property[] props = new Property[0];
      try {
         TraceRegisters regProps = TraceRegisters.valueOf(operation);
         props = regProps.properties;
      } catch(IllegalArgumentException ex) {
         Logger.getLogger(TraceRegisters.class.getName()).
                 warning("Operation not supported: '"+operation+"'.");
      }

      return props;
   }

   public enum Property {
      write,
      read
   };

   /**
    * INSTANCE VARIABLES
    */
   final private Property[] properties;
}
