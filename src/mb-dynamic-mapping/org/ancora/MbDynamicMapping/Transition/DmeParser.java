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

package org.ancora.MbDynamicMapping.Transition;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.ancora.IrForDynamicMapping.Operation;
import org.ancora.IrForDynamicMapping.OperationName;
import org.ancora.MbDynamicMapping.Interface.InstructionBlock;
import org.ancora.MbDynamicMapping.Interface.InstructionBlockListener;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.MicroBlazeToIr.MbToIrParser;
import org.ancora.SharedLibrary.BitUtils;

/**
 *
 * @author Joao Bispo
 */
public class DmeParser implements InstructionBlockListener {

   public DmeParser() {
      shownBlocks = new HashSet<Integer>();
      //operationHistogram = new EnumMap<OperationName, Integer>(OperationName.class);
      operationHistogram = new OperationHistogram();
      optimizedOperationHistogram = new OperationHistogram();

      optimizations = new IrConstantPropagation();
   }



   @Override
   public void accept(InstructionBlock instructionBlock) {
      // Check if instruction block wasn't shown already
      int hash = calculateHash(instructionBlock.getInstructions());

      boolean appearedBefore;
      // Check if hash was already seen
      if(shownBlocks.contains(hash)) {
         appearedBefore = true;
      } else {
         shownBlocks.add(hash);
         appearedBefore = false;
      }

      // Check if it is a block to be mapped on the RPU
      if(!instructionBlock.mapToHardware()) {
         mbInstructions += instructionBlock.getTotalInstructions();
         return;
      }

      /*
      if(lastHash == hash) {
         if(!repeatedHash) {
            System.out.println("!!Loop!!");
            repeatedHash = true;
         }
      } else {
         repeatedHash = false;
      }

      lastHash = hash;
       */

      // Transform array in list
      List<Instruction> instructions = Arrays.asList(instructionBlock.getInstructions());
      // Give them to the parser
      List<Operation> operations = MbToIrParser.parseInstructions(instructions);

      // Show
      if(!appearedBefore) {
         //show(instructions, operations);
      }

      // Perform optimizations on all operations
      for(Operation operation : operations) {
         optimizations.acceptOperation(operation);
      }
      // Show optimized operations
      List<Operation> optimizedOps = optimizations.getOptimizedOperations();

      // Show Comparative
      if (!appearedBefore) {
         //showOptimizedNonOptimezed(operations, optimizedOps);
      }

      //optimizations.clearOptimizedOperations();
      optimizations.reset();

      // Stats
      operationHistogram.addOperations(operations);
      optimizedOperationHistogram.addOperations(optimizedOps);
      //buildOperationHistogram(operations);
      //countAddAndMove(operations);

      // Give operations to the mapper
      

   }

   @Override
   public void flush() {
      System.out.println(operationHistogram.toString());
      System.out.println("-----------------Constant Propagation--------------");
      System.out.println(optimizedOperationHistogram.toString());
      //showHistogram();


      // Do Nothing
      //System.out.println("Number of add_intenger:"+addIntegerCounter);
      //System.out.println("Number of move:"+moveCounter);
   }

   private void show(List<Instruction> instructions, List<Operation> operations) {
      System.out.println("Original/Translated Instructions:");
      System.out.println("-----------------------");
      for(Instruction instruction : instructions) {
         System.out.println(instruction);
      }
      System.out.println("-----------------------");
      for(Operation operation : operations) {
         System.out.println(operation);
      }
      System.out.println("-----------------------");
      System.out.println(" ");
   }

   private void showOptimizedNonOptimezed(List<Operation> operations, List<Operation> optimizedOps) {
      System.out.println("Original/Constant-Propagated Instructions:");
      System.out.println("-----------------------");
      for(Operation operation : operations) {
         System.out.println(operation);
      }

      System.out.println("-----------------------");
      for(Operation operation : optimizedOps) {
         System.out.println(operation);
      }
      System.out.println("-----------------------");
      System.out.println(" ");   }


   /*
   private void buildOperationHistogram(List<Operation> operations) {
       for(Operation operation : operations) {
         OperationName opName = operation.getOperation();

         Integer value = operationHistogram.get(opName);
         if(value == null) {
            value = 0;
         }
         value++;
         operationHistogram.put(opName, value);
         
      }
   }
    */
/*
   private void countAddAndMove(List<Operation> operations) {
      for(Operation operation : operations) {
         OperationName opName = operation.getOperation();

         if(opName == OperationName.integer_add) {
            addIntegerCounter++;
         }

         if(opName == OperationName.move) {
            moveCounter++;
         }
      }
   }
*/
   /**
    * Calculate hash value for given list of instructions.
    *
    * @param instructions
    * @return
    */
   private int calculateHash(Instruction[] instructions) {
      int hash = 4;
      for(Instruction instruction : instructions) {
         hash = BitUtils.superFastHash(instruction.getAddress(), hash);
      }

      return hash;
   }



/*
   private int getNumberOperations(OperationName opName) {
     Integer value = operationHistogram.get(opName);
     if(value == null) {
        return 0;
     } else {
        return value;
     }
   }
 */

   /**
    * INSTANCE VARIABLES
    */
   //private int addIntegerCounter;
   //private int moveCounter;

   //private Map<OperationName, Integer> operationHistogram;
   private OperationHistogram operationHistogram;
   private OperationHistogram optimizedOperationHistogram;

   private IrConstantPropagation optimizations;

   private Set<Integer> shownBlocks;

   private int mbInstructions;





   //private int lastHash;
   //private boolean repeatedHash;




}
