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

package org.ancora.DynamicMapping.Tester.ProtoIr.Ir;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Joao Bispo
 */
public class Dotty {

   public static void printOperation(Operation op) {
      System.out.println("["+op+"]");

      for(Operand operand : op.getInputs()) {
         System.out.println(operand.toString() + " -> "+ op);
      }

      for(Operand operand : op.getOutputs()) {
         System.out.println(op + " -> "+operand.toString());
      }

      for(Operand operand : op.getOutputs()) {
         printOperand(operand);
      }
   }


   private static void printOperand(Operand operand) {
      System.out.println("["+operand.toString()+"]");

      if(operand.getProducer() != null) {
         System.out.println(operand.getProducer() + " -> "+operand.toString());
      }

      for(Operation operation : operand.getConsumers()) {
         System.out.println(operand.toString() + " -> "+ operation);
      }

      for(Operation operation : operand.getConsumers()) {
         printOperation(operation);
      }
   }

   public static String generateDot(Operation start) {
      // Collect Operands and Operations
      Set<Operand> operands = new HashSet<Operand>();
      Set<Operation> operations = new HashSet<Operation>();

      collectOperation(start, operations, operands);

      //System.out.println("Operations:");
      //System.out.println(operations);

      //System.out.println("Operands:");
      //System.out.println(operands);

      StringBuilder builder = new StringBuilder();

      builder.append("digraph graphname {\n");
      declareOperations(operations, builder);
      declareOperands(operands, builder);
      builder.append("\n");

      for(Operation operation: operations) {
         for(Operand operand : operation.getOutputs()) {
            builder.append(operation.hashCode());
            builder.append(" -> ");
            builder.append(operand.hashCode());
            builder.append(";\n");
         }
      }

      for(Operand operand : operands) {
         for(Operation operation : operand.getConsumers()) {
            builder.append(operand.hashCode());
            builder.append(" -> ");
            builder.append(operation.hashCode());
            builder.append(";\n");
         }
      }

      //appendOperationOutputs(start, builder, operands, operations);
      builder.append("}\n");

      return builder.toString();
   }

   private static void appendOperationOutputs(Operation operation, StringBuilder builder,
           Set<Operand> operands, Set<Operation> operations) {

      // If operation already append, return
      if(operations.contains(operation)) {
         return;
      }

      // Add a line for each output operand
      for(Operand outOp : operation.getOutputs()) {
         builder.append(operation);
         builder.append(" -> ");
         builder.append(outOp.toString());
         builder.append(";\n");
      }

      // For each output operand, if it was not visited yet, print it


   }

   private static void collectOperation(Operation operation, Set<Operation> operations, Set<Operand> operands) {
      // Add operation to Set. If it was already present, return.
      boolean newElement = operations.add(operation);
      if(!newElement) {
         return;
      }

      // Add each of the operands. If it was not already in the set, add it's
      // consumers too.
      for(Operand outOp : operation.getOutputs()) {
         collectOperand(outOp, operations, operands);
         /*
         boolean newOperand = operands.add(outOp);
         if(newOperand) {
            collectOperand(outOp, operations, operands);
         }
          */
      }

      for(Operand inOp : operation.getInputs()) {
         collectOperand(inOp, operations, operands);
         /*
         boolean newOperand = operands.add(inOp);
         if(newOperand) {
            collectOperand(inOp, operations, operands);
         }
          */
      }

   }

   private static void collectOperand(Operand operand, Set<Operation> operations, Set<Operand> operands) {
      // Add operand to Set. If it was already present, return.
      //System.out.println("Operand:"+operand);
      //System.out.println("Set:"+operands);
      boolean newElement = operands.add(operand);
      if(!newElement) {
         //System.out.println("Not new.");
         return;
      }

      // Add each of the consumers. If it was not already in the set, add it's
      // output operands too.
      for(Operation outOp : operand.getConsumers()) {
         collectOperation(outOp, operations, operands);
         /*
         boolean newOperation = operations.add(outOp);

         if(newOperation) {
            collectOperation(outOp, operations, operands);
         }
          */
      }

      // Add each of the consumers. If it was not already in the set, add it's
      // output operands too.
      if(operand.getProducer() != null) {
         collectOperation(operand.getProducer(), operations, operands);
         /*
         boolean newOperation = operations.add(operand.getProducer());

         if(newOperation) {
            collectOperation(operand.getProducer(), operations, operands);
         }
          */
      }


   }

   private static void declareOperations(Set<Operation> operations, StringBuilder builder) {
      for(Operation op : operations) {
         builder.append(op.hashCode());
         builder.append("[label=\"");
         builder.append(op);
         builder.append("\"];\n");
      }
   }

   private static void declareOperands(Set<Operand> operands, StringBuilder builder) {
      for(Operand op : operands) {
         builder.append(op.hashCode());
         builder.append("[label=\"");
         builder.append(op);
         builder.append("\", shape=box];\n");
      }
   }


   /*
   private static void collectOperationOutputs(Operation operation, List<Operation> operations,
           List<Operand> operands, Set<Operation> operationCheck, Set<Operand> operandCheck) {
      
      // If operation already append, return
      if(operations.contains(operation)) {
         return;
      }
      operationCheck.
      // Append operation to list and check
      operands.c
   }
    */

}
