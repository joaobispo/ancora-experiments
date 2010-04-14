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

package org.ancora.DynamicMapping.Tester.ProtoIr.Parser.MicroBlaze.MbParsing;

import java.util.logging.Logger;
import org.ancora.DynamicMapping.Tester.ProtoIr.Ir.Operand;
import org.ancora.DynamicMapping.Tester.ProtoIr.Ir.OperandType;
import org.ancora.DynamicMapping.Tester.ProtoIr.Ir.Operation;
import org.ancora.DynamicMapping.Tester.ProtoIr.Parser.MicroBlaze.Data.ArgumentsProperties.ArgumentProperty;
//import org.ancora.DynamicMapping.Tester.ProtoIr.Ir.OperationType;

/**
 *
 * @author Joao Bispo
 */
public class ParserState {

   public ParserState(int numRegs) {
      startOperation = new Operation(START_OPERATION);
      endOperation = new Operation(END_OPERATION);
//      startOperation = new Operation(OperationType.Start);
//      endOperation = new Operation(OperationType.End);
     registerVersion = new int[numRegs];
     registerOperand = new Operand[numRegs];

   }

   public Operation getStartOperation() {
      return startOperation;
   }

   public Operation getEndOperation() {
      return endOperation;
   }


   Operand buildRegisterOperand(int argument, ArgumentProperty argumentProperty) {
      switch(argumentProperty) {
         case read:
            return buildReadRegisterOperand(argument);
         case write:
            return buildWriteRegisterOperand(argument);
         default:
            Logger.getLogger(ParserState.class.getName()).
                    warning("Case not defined: '"+argumentProperty+"'.");
            return null;
      }
      //throw new UnsupportedOperationException("Not yet implemented");
   }

   private Operand buildReadRegisterOperand(int argument) {
      // Check if operand for the given register already exists
      if(registerOperand[argument] != null) {
         return registerOperand[argument];
      }

      // Create operand
      String value = getRegisterOperandName(argument);
      Operand op = new Operand(OperandType.data, value);

      // Add operand to start, and start to operand
      startOperation.getOutputs().add(op);
      op.setProducer(startOperation);

      // Update table
      registerOperand[argument] = op;

      return op;
   }

   private Operand buildWriteRegisterOperand(int argument) {
      // It is a write; increment version and create operand.
      //System.out.println("Argument:"+argument);
      //System.out.println("reg vers before:"+registerVersion[argument]);
      registerVersion[argument]++;
      //System.out.println("reg vers after:"+registerVersion[argument]);
      String value = getRegisterOperandName(argument);
      //System.out.println("Value:"+value);
      Operand op = new Operand(OperandType.data, value);

      // Replace reference to previous Operand of this register
      registerOperand[argument] = op;
      return op;
   }

   private String getRegisterOperandName(int argument) {
      return "r"+argument+"."+registerVersion[argument];
   }

   /**
    * INSTANCE VARIABLES
    */
   private Operation startOperation;
   private Operation endOperation;

   private int[] registerVersion;
   private Operand[] registerOperand;

   private final String START_OPERATION = "start";
   private final String END_OPERATION = "end";







}
