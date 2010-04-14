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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.ancora.DynamicMapping.Tester.ProtoIr.Ir.Dotty;
import org.ancora.DynamicMapping.Tester.ProtoIr.Ir.Operand;
import org.ancora.DynamicMapping.Tester.ProtoIr.Ir.OperandType;
import org.ancora.DynamicMapping.Tester.ProtoIr.Ir.Operation;
import org.ancora.DynamicMapping.Tester.ProtoIr.Parser.MicroBlaze.Data.ArgumentsProperties;
import org.ancora.DynamicMapping.Tester.ProtoIr.Parser.MicroBlaze.Data.ArgumentsProperties.ArgumentProperty;
import org.ancora.DynamicMapping.Tester.ProtoIr.Parser.MicroBlaze.Data.InstructionType;
import org.ancora.SharedLibrary.ParseUtils;

/**
 *
 * @author Joao Bispo
 */
public class Utils {

   public static InstructionType getType(String instruction) {

      int whiteSpaceIndex = ParseUtils.indexOfFirstWhiteSpace(instruction);
      String operationString = instruction.substring(0, whiteSpaceIndex);
      return InstructionType.getEnum(operationString);

   }

   public static String[] getRegisters(String instruction) {
      int whiteSpaceIndex = ParseUtils.indexOfFirstWhiteSpace(instruction);
      String registersString = instruction.substring(whiteSpaceIndex).trim();

      String[] regs = registersString.split(",");
      for(int i=0; i<regs.length; i++) {
         regs[i] = regs[i].trim();
      }

      return regs;
   }

   public static Operation buildOperation(InstructionType type, String[] arguments, ParserState state) {
   //public static Operand[] extractOperands(InstructionType type, String[] arguments , ParserState state) {
      String operationName = "mb-"+type.getName();
      Operation operation = new Operation(operationName);

      Operand[] operands = new Operand[arguments.length];

      // Get arguments properties
      ArgumentProperty[] argProp = ArgumentsProperties.getProperties(type);
      
      // Check arguments properties have the same size as the arguments
      if(arguments.length != argProp.length) {
         Logger.getLogger(Utils.class.getName()).
                 warning("Number of arguments ("+arguments.length+") different from " +
         //        "the number of properties ("+argProp.length+"). Returning empty list of operands.");
                 "the number of properties ("+argProp.length+"). Returning null.");
         //return operands;
         return null;
      }

      // For each argument, return the correct operand
      // INFO: Loop is done backwards because in MicroBlaze instructions, writes
      // appear always before reads. This prevents parsing problems when there
      // is a read and write to the same register.
      for (int i = arguments.length - 1; i >= 0; i--) {
         //System.out.println("Arg:" + arguments[i]);
         //System.out.println("Prop:" + argProp[i]);
         operands[i] = parseMbArgument(arguments[i], argProp[i], state);
      }

      // Add operands to operation,
      for(int i=0; i<operands.length; i++) {
         //System.out.println("Operand:"+operands[i].toString());
         switch(argProp[i]) {
            case read:
               operation.getInputs().add(operands[i]);
               operands[i].getConsumers().add(operation);
               //System.out.println("Operation:"+operation);
               break;
            case write:
               operation.getOutputs().add(operands[i]);
               operands[i].setProducer(operation);
               break;
            default:
               Logger.getLogger(Utils.class.getName()).
                       warning("Case '"+argProp[i]+"' not defined.");
         }

         //System.out.println("Operand:" + operands[i]);
      }

      
      return operation;
   }

   private static Operand parseMbArgument(String argument, ArgumentProperty property, ParserState state) {

      // Check if register
      Integer register = parseRegister(argument);
      if(register != null) {
         // Check for R0
         if(register == 0) {
            // Return literal 0
            return new Operand(OperandType.literal, "0");
         }

         return state.buildRegisterOperand(register, property);
      }

      // Check if literal
      try {
         Integer.parseInt(argument);
         return new Operand(OperandType.literal, argument);
      } catch(NumberFormatException ex) {
         Logger.getLogger(Utils.class.getName()).
                 warning("Expecting a literal: '"+argument+"'.");
         return null;
      }

   }

   /**
    * @param argument ex.: "r1"
    * @return if it is a MicroBlaze registers, returns the number of the
    * registers. Otherwise, returns null.
    */
   private static Integer parseRegister(String argument) {
      final String registerPrefix = "r";
      if(!argument.startsWith(registerPrefix)) {
         return null;
      }

      return Integer.valueOf(argument.substring(registerPrefix.length()));
   }


}
