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
package org.ancora.InfiniteMapper;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.ancora.DMExplorer.Global;
import org.ancora.DmeFramework.Interfaces.Mapper;
import org.ancora.DmeFramework.Interfaces.MapperMonitor;
import org.ancora.IrForDynamicMapping.Coordinate;
import org.ancora.IrForDynamicMapping.InputIndex;
import org.ancora.IrForDynamicMapping.Operand;
import org.ancora.IrForDynamicMapping.Operation;
import org.ancora.IrForDynamicMapping.OperationName;
import org.ancora.IrForDynamicMapping.OutputIndex;
import org.ancora.MicroBlazeToIr.Optimizations;

/**
 * Maps IR operations to a Infinite Forward Matrix architecture.
 *
 * @author Joao Bispo
 */
public class IfmMapper implements Mapper {

   public IfmMapper() {
      monitor = new IfmMonitor();

      matrix = new InfiniteForwardMatrix(Global.peLineSize);
      
      conditionalExit = new Hashtable<Operand, Operand>();
      currentProducers = new Hashtable<String, Operand>();
      literalsTable = new Hashtable<String, Operand>();
      liveIns = new HashSet<String>();
      liveOuts = new HashSet<String>();

      feedDistance = Global.feedDistance;
   }

    @Override
   public String getName() {
      return NAME;
   }

   @Override
   public MapperMonitor getMonitor() {
      return mapperMonitor;
   }

   @Override
   public void mapOperations(List<Operation> operations) {
      // Clear previous mapping
      clearArchitecture();

//      System.out.println("Input:");
//      for(Operation op : operations) {
//         System.out.println(op);
//      }
      for (Operation operation : operations) {
         // Check for exit operations
         boolean isExit = processExitOperation(operation);
         if(isExit) {
            // Show conditional exists
            mapperMonitor = updateMonitor();
            // Show Mapping
            // System.out.println(matrix.toString());
            return;
         }

         // Check for move operations
         boolean isMove = processMoveOperation(operation);
         if(isMove) {
            continue;
         }

         // Map operation
         mapOperation(operation);

         if(mappingFailed) {
            return;
         }
      }
      // Operations are mapped; Update MapperMonitor
      mapperMonitor = updateMonitor();
      // Show Mapping
//      System.out.println(matrix.toString());
   }


   /**
    * Check if it is an exit operation. If it is, return true.
    *
    * <br> For conditional exits, checks if the inputs are literals. If yes,
    * tries to resolve the exit. If exit can be resolved and is an exit, returns
    * true. Otherwise, returns false and operation is ignored.
    * <br> For conditional exits which cannot be resolved, false is returned and
    * the conditional exit table is updated with the input data (check if from
    * live-in or fu coordinate) and the address.
    * <br> For all other operations, returns false.
    *
    * @param operation
    * @return
    */
   private boolean processExitOperation(Operation operation) {
      OperationName opName = operation.getOperation();
      // Check if exit
      if(opName == OperationName.exit) {
         //System.out.println("exit:"+operation);
         return true;
      }

      // Check if NOT conditional exit
      if(!IfmUtils.isConditionalExit(opName)) {
         //System.out.println("Not conditional:"+operation);
         return false;
      }

      // Is conditional exit
      //System.out.println("conditional:"+operation);
      // Check if first input is literal
      Operand firstOperand = operation.getInput(InputIndex.firstOperand);
      boolean isLiteral = firstOperand.getOpType() == Operand.OpType.literal;
      if(isLiteral) {
         int result = Optimizations.computeLiteral(opName, firstOperand.getValueAsIntegerLiteral(), 0);
         // If 1, exit
         if(result == 1) {
            return true;
         } else {
            return false;
         }
      }

      // Is not literal
      // Check current producers table to check if we can find the register
      Operand operand = currentProducers.get(firstOperand.getValue());

      Operand jumpAddress = operation.getInput(InputIndex.secondOperand);
      // Update table
      if(operand != null) {
         conditionalExit.put(operand, jumpAddress);
      } else {
         conditionalExit.put(firstOperand, jumpAddress);
      }

      // The register was read. Some processing here?
      // TODO

      return false;
   }

   /**
    * 
    * @param operation
    * @return true if it is a move operation. False otherwise.
    */
   public boolean processMoveOperation(Operation operation) {
      // Check if it is a move operation
      boolean isMove = operation.getOperation() == OperationName.move;
      if(!isMove) {
         return false;
      }

      // Update literals table
      String registerName = operation.getOutput(OutputIndex.firstResult).getValue();
      Operand literal = operation.getInput(InputIndex.firstOperand);
      literalsTable.put(registerName, literal);

      // Remove register from current producers, if it is there
      currentProducers.remove(registerName);

      return true;
   }

   /**
    * Accepts operation to map on the matrix
    * @param operation
    * @return
    */
   
   private void mapOperation(Operation operation) {
      Operand[] opInputs = operation.getInputs();
      Operand[] opOutputs = operation.getOutputs();
      // Fu data
      Operand[] fuInputs = new Operand[opInputs.length];
      //Operand[] fuOutputs = new Operand[opOutputs.length];

      // Check inputs
      for(int i=0; i<opInputs.length; i++) {
         if(opInputs[i] == null) {
            continue;
         }

         if(opInputs[i].getOpType() == Operand.OpType.literal) {
            fuInputs[i] = opInputs[i];
            continue;
         }

         if(opInputs[i].getOpType() == Operand.OpType.register) {
            // This can be combine in one class
            String register = opInputs[i].getValue();
            Operand op = null;
            // Check if literals table has this register
            op = literalsTable.get(register);
            // Check if current producers have this register
            if(op == null) {
               op = currentProducers.get(register);
            }
            
            if(op == null) {
               liveIns.add(register);
            } else {
               fuInputs[i] = op;
            }

            continue;
         }

         System.out.println("mapOperation Error: shouldn't be here");
      }

      int destinationLine = calculateDestinationLine(fuInputs);

      int destinationCol = matrix.getFirstAvaliableColumn(destinationLine);
      Fu mappedOp = matrix.mapNewOperation(destinationLine, destinationCol, operation, fuInputs);

      processOutput(opOutputs, mappedOp.getOutput());

      // Add, in the end
      if(feedDistance > 0) {
         for(int i=0; i<fuInputs.length; i++) {
            Fu newMove = addMoveOperations(destinationLine, fuInputs[i]);
            if(mappingFailed) {
               return;
            }
         }
      }
      
   }


   private int calculateDestinationLine(Operand[] fuInputs) {
      // Always try to put operation in the first line
      int destinationLine = 0;
      // For each input, check if it is a coordinate
      for(Operand operand : fuInputs) {
         if(operand == null) {
            continue;
         }

         if(operand.getOpType() != Operand.OpType.fu) {
            continue;
         }

         destinationLine = Math.max(destinationLine, (operand.getValueAsCoordinate().getLine()+1));
      }

      // Get the first avaliable line from the given destination line
      destinationLine = matrix.getFirstAvaliableLine(destinationLine);

      return destinationLine;
   }

   private void processOutput(Operand[] opOutputs, Operand output) {
      for(int i=0; i<opOutputs.length; i++) {
         // Check if null
         if(opOutputs[i] == null) {
            continue;
         }

         // Check if register
         if(opOutputs[i].getOpType() != Operand.OpType.register) {
            System.out.println("Strange?");
            System.out.println(opOutputs[i]);
            continue;
         }

         // Update current producers table
         String registerName = opOutputs[i].getValue();
         currentProducers.put(registerName, output);
         literalsTable.remove(registerName);

         // Check if it is a temporary register
         boolean isTempReg = isTempReg(registerName);
         if(!isTempReg) {
            liveOuts.add(registerName);
         }
      }
   }


   //private Fu addMoveOperations(int destinationLine, Operand[] fuInputs, Operand[] opInputs) {
   private Fu addMoveOperations(int destinationLine, Operand fuInput) {
      // For each input
      //for(int i=0; i<fuInputs.length; i++) {
         if(fuInput == null) {
            return null;
         }

         if(fuInput.getOpType() != Operand.OpType.fu) {
            return null;
         }

         //System.out.println("fuInput:"+fuInput);
         Coordinate coor = fuInput.getValueAsCoordinate();
         
         // Calculate distance between the input and the destinationLine
         int distance = destinationLine - coor.getLine();

         // Check if destination is before input or in the same line as the input
         if(distance <= 0) {
            Logger.getLogger(IfmMapper.class.getName()).
                    warning("Input line:"+coor.getLine()+"; Consumer line:"+destinationLine);
            return null;
         }

         // Check if destination line is within the feed distance
         if(distance <= feedDistance) {
            // No move operation is needed
            return null;
         }

         // Calculate number of moves
         int numberOfMoves = distance - feedDistance;
         int startingLine = coor.getLine()+1;
         Operand input = fuInput;
         Fu newMove = null;
         for(int j=0; j<numberOfMoves; j++) {
            // Line
            int line = startingLine+j;
            // Check if line is avaliable. If not, mapping is not possible
            boolean isAvaliable = matrix.isLineAvaliable(line);
            if(!isAvaliable) {
               Logger.getLogger(IfmMapper.class.getName()).
                       warning("Mapping is not possible for a matrix with " +
                       "maximum line size of "+Global.peLineSize);
               mappingFailed = true;
               return null;
            }

            line = matrix.getFirstAvaliableLine(line);
            // Get column
            int col = matrix.getFirstAvaliableColumn(line);
            newMove = matrix.mapMoveOperation(line, col, input);
            input = newMove.getOutput();
         }
      //}
         return newMove;

   }
   
   private boolean isTempReg(String registerName) {
      return registerName.startsWith("t");
   }


   public void saveStats() {
      monitor.totalMappedInstructions += matrix.getNumberOfMappedOps() - matrix.getMoveOperations();
      monitor.totalMappedBlocks += 1;

      monitor.totalMoveInstructions += matrix.getMoveOperations();
      monitor.totalLiveIn += liveIns.size();
      monitor.totalLiveOut += liveOuts.size();
      monitor.totalCcaLines += matrix.getNumberOfMappedLines();

      monitor.maxIlp = Math.max(monitor.maxIlp, matrix.getIlp());
      monitor.maxMappedLines = Math.max(monitor.maxMappedLines, matrix.getNumberOfMappedLines());
      monitor.maxLiveIn = Math.max(monitor.maxLiveIn, liveIns.size());
      monitor.maxLiveOut = Math.max(monitor.maxLiveOut, liveOuts.size());

      /*
      mappings++;
      // Number of executed cycles
      executedCycles += matrix.getNumberOfMappedLines();
      // Ilp
      ilp += matrix.getMaxIlp();
      ilpWithMoves += matrix.getIlpWithMoves();
       */

   }

   private IfmMapperMonitor updateMonitor() {
      // Create new Monitor
      IfmMapperMonitor newMonitor = new IfmMapperMonitor();
      newMonitor.liveIn = liveIns.size();
      newMonitor.liveOut = liveOuts.size();
      newMonitor.mappedOperations = matrix.getNumberOfMappedOps() - matrix.getMoveOperations();
      newMonitor.mappedElements = matrix.getNumberOfMappedOps();
      newMonitor.cycles = matrix.getNumberOfMappedLines();
      newMonitor.maxIlp = matrix.getSizeOfBiggestLine();


      return newMonitor;
   }

   public void clearArchitecture() {
      matrix = new InfiniteForwardMatrix(Global.peLineSize);

      conditionalExit = new Hashtable<Operand, Operand>();
      currentProducers = new Hashtable<String, Operand>();
      liveIns = new HashSet<String>();
      liveOuts = new HashSet<String>();
      literalsTable = new Hashtable<String, Operand>();
   }

   public void showStats() {
      System.out.println(monitor.getStats());
      //System.out.println("Literals Table");
      //System.out.println(literalsTable);
   }

   
   @Override
   public boolean hasMappingFailed() {
      return mappingFailed;
   }

   /**
    * INSTANCE VARIABLES
    */
   private InfiniteForwardMatrix matrix;
   private IfmMonitor monitor;
   private IfmMapperMonitor mapperMonitor;

   private int feedDistance = 1;

   private Map<Operand, Operand> conditionalExit;
   private Map<String, Operand> currentProducers;
   private Map<String, Operand> literalsTable;
   private Set<String> liveIns;
   private Set<String> liveOuts;

   public static final String NAME = "InfiniteForwardMatrix";

   private boolean mappingFailed = false;














   /*
   private int executedCycles;
   private int mappings;
   private int ilp;
   private int ilpWithMoves;
    */
}
