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

package org.ancora.DynamicMapping.Tester.ProtoIr.Parser.MicroBlaze.Data;

import static org.ancora.DynamicMapping.Tester.ProtoIr.Parser.MicroBlaze.Data.Tables.RegisterProperty.*;
import static org.ancora.DynamicMapping.Tester.ProtoIr.Parser.MicroBlaze.Data.InstructionType.*;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;
import org.ancora.DynamicMapping.Tester.ProtoIr.Parser.MicroBlaze.Data.Tables.RegisterProperty;



/**
 * Indicates, for each trace instruction, which registers are for read or
 * write, by the order they appear in the trace instruction.
 *
 * @author Joao Bispo
 */
public class Tables {
    private static RegisterProperty[] writeReadRead = {write, read, read};
    private static RegisterProperty[] readRead = {read, read};
    private static RegisterProperty[] singleRead = {read};
    private static RegisterProperty[] writeRead = {write, read};
    private static RegisterProperty[] readReadRead = {read, read, read};

    /**
     * It has an incomplete set of instructions
     */
   public static final Map<InstructionType, RegisterProperty[]> registerProperties;
    static {
        Map<InstructionType, RegisterProperty[]> aMap = new Hashtable<InstructionType, RegisterProperty[]>();
        aMap.put(add, writeReadRead);
        aMap.put(addc, writeReadRead);
        aMap.put(addk, writeReadRead);
        aMap.put(addkc, writeReadRead);
        aMap.put(addi, writeReadRead);
        aMap.put(addic, writeReadRead);
        aMap.put(addik, writeReadRead);
        aMap.put(addikc, writeReadRead);
        aMap.put(and, writeReadRead);
        aMap.put(andi, writeReadRead);
        aMap.put(andn, writeReadRead);
        aMap.put(andni, writeReadRead);

        //--- Only used in traces
        aMap.put(beqi, readRead);
        aMap.put(beqid, readRead);
        aMap.put(bgei, readRead);
        aMap.put(bgeid, readRead);
        aMap.put(bgti, readRead);
        aMap.put(bgtid, readRead);
        aMap.put(blei, readRead);
        aMap.put(bleid, readRead);
        aMap.put(blti, readRead);
        aMap.put(bltid, readRead);
        aMap.put(bnei, readRead);
        aMap.put(bneid, readRead);
        
        aMap.put(br, singleRead);
        aMap.put(bra, singleRead);
        aMap.put(brd, singleRead);
        aMap.put(brad, singleRead);
        aMap.put(brld, writeRead);
        aMap.put(brald, writeRead);
        aMap.put(bri, singleRead);
        aMap.put(brai, singleRead);
        aMap.put(brid, singleRead);
        aMap.put(braid, singleRead);
        aMap.put(brlid, writeRead);
        aMap.put(bralid, writeRead);
        aMap.put(bsrli, writeReadRead);
        aMap.put(bsrai, writeReadRead);
        aMap.put(bslli, writeReadRead);
        aMap.put(cmp, writeReadRead);
        aMap.put(cmpu, writeReadRead);
        aMap.put(idiv, writeReadRead);
        aMap.put(idivu, writeReadRead);
        aMap.put(imm, singleRead);
        aMap.put(lbu, writeReadRead);
        aMap.put(lbui, writeReadRead);
        aMap.put(lhu, writeReadRead);
        aMap.put(lhui, writeReadRead);
        aMap.put(lw, writeReadRead);
        aMap.put(lwi, writeReadRead);
        aMap.put(or, writeReadRead);
        aMap.put(ori, writeReadRead);
        aMap.put(rsub, writeReadRead);
        aMap.put(rsubc, writeReadRead);
        aMap.put(rsubk, writeReadRead);
        aMap.put(rsubkc, writeReadRead);
        aMap.put(rtsd, readRead);

/*
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
   */

        //aMap.put(1, "one");
        //aMap.put(2, "two");
        registerProperties = Collections.unmodifiableMap(aMap);
    }

   

   ///
   // The enums are initialized with the order by which the registers of the
   // instructions appear in the reference manual of MicroBlaze.
   //
   /// Examples:
   // Reference -> rD,rA,rB
   // Code -> RegisterProperty.write, RegisterProperty.read, RegisterProperty.read
   //
   // Reference -> rD,rA
   // Code -> RegisterProperty.write, RegisterProperty.read
   //
   // Reference -> rA
   // Code -> RegisterProperty.read
   //
   // Reference -> rD
   // Code -> RegisterProperty.write
/*
  

   imm(),

   
   
   
   
   
   
   
   xori(Property.write, Property.read),
   rsubi(Property.write, Property.read),
   rsubic(Property.write, Property.read),
   rsubik(Property.write, Property.read),
   rsubikc(Property.write, Property.read),
   muli(Property.write, Property.read);


   private Tables(RegisterProperty... properties) {
      this.properties = properties;
   }

   public static RegisterProperty[] getProperties(String operation) {
      RegisterProperty[] props = new RegisterProperty[0];
      try {
         Tables regProps = Tables.valueOf(operation);
         props = regProps.properties;
      } catch(IllegalArgumentException ex) {
         Logger.getLogger(Tables.class.getName()).
                 warning("Operation not supported: '"+operation+"'.");
      }

      return props;
   }
*/
public enum RegisterProperty {
      write,
      read};

   /**
    * INSTANCE VARIABLES
    */
   //final private RegisterProperty[] properties;

}


