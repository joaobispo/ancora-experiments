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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.ancora.IrForDynamicMapping.Operand;
import org.ancora.IrForDynamicMapping.Operation;
import org.ancora.SharedLibrary.MbDynamicMapping.ParseUtils;

/**
 * Repreents an Infinite Forward Matrix which accepts maps of parameterizable
 * elements.
 *
 * @author Joao Bispo
 */
public class InfiniteForwardMatrix {

   public InfiniteForwardMatrix() {
      lines = new ArrayList<IfmMapperLine>();
   }

   public int getFirstAvaliableColumn(int line) {
      // Check if line exists. If not, create the lines
      boolean lineExists = lineExists(line);
      if(!lineExists) {
         createLines(line);
      }
      // Get line size
      return lines.get(line).getSize();
   }

   /**
    * There are no outputs because at map time... the output is the self?
    * @param line
    * @param col
    * @param operation
    * @param inputs
    * @return
    */
   public Fu mapNewOperation(int line, int col, Operation operation,  Operand[] inputs) {
      // Create Operand for Fu Coordinate
      Operand fuSelfCoordinate = IfmUtils.newCoordinateOperand(line, col);
      // Create Fu
      Fu fu = new Fu(operation, inputs, fuSelfCoordinate);

      //Add Fu to the matrix
      boolean success = addFu(line, col, fu);
      if(!success) {
         Logger.getLogger(InfiniteForwardMatrix.class.getName()).
                 warning("Could not add FU "+fu+" to matrix.");
      }

      return fu;
   }

   public Fu mapMoveOperation(int line, int col, Operand input) {
      // Create Operand for Fu Coordinate
      Operand fuSelfCoordinate = IfmUtils.newCoordinateOperand(line, col);
      // Create Fu
      Operand[] inputs = new Operand[1];
      inputs[0] = input;
      Fu fu = new Fu(null, inputs, fuSelfCoordinate);

      //Add Fu to the matrix
      boolean success = addFu(line, col, fu);
      if(!success) {
         Logger.getLogger(InfiniteForwardMatrix.class.getName()).
                 warning("Could not add FU "+fu+" to matrix.");
      }

      return fu;
   }


   private boolean lineExists(int line) {
      return line < lines.size();
   }

   private void createLines(int line) {
      /*
       * Recursive Version
      if(lineExists(line)) {
         return;
      }

      createLines(line-1);
      lines.add(new IfmMapperLine<T>());
       */
      // How many lines need to be created
      int numberOfLines = line-lines.size()+1;

      for(int i=0; i<numberOfLines; i++) {
         lines.add(new IfmMapperLine());
      }
   }


   private boolean addFu(int line, int col, Fu fu) {
      // Get line
      IfmMapperLine mapperLine = null;
      try {
         mapperLine = lines.get(line);
      } catch(IndexOutOfBoundsException ex) {
         Logger.getLogger(InfiniteForwardMatrix.class.getName()).
                 warning("Line index '"+line+"' does not exist. Mapper has '"+
                 lines.size()+"' lines.");
         return false;
      }

      // Add to column
      // Check if position already has Fu
      if(col < mapperLine.getSize()) {
         decrementMove(mapperLine.getFu(col));
         mapperLine.setFu(col, fu);
         Logger.getLogger(IfmMapperLine.class.getName()).
                 warning("Replacing Fu at line '"+line+"', column '"+col+"'");

         incrementMove(fu);
         return true;
      }
      // Check if position is the next Fu
      if(col == mapperLine.getSize()) {
         mapperLine.addFu(fu);
         incrementMove(fu);
         totalOperations++;
         return true;
      }

      Logger.getLogger(IfmMapperLine.class.getName()).
              warning("Could not map Fu at line '" + line + "', column '" + col + "': line has '"+
              mapperLine.getSize()+"' FUs.");
      return false;
   }


   public int getNumberOfMappedLines() {
      return lines.size();
   }

   public int getMoveOperations() {
  /*
      int totalOps = 0;
      for(IfmMapperLine line : lines) {
         for(int i=0; i<line.getSize(); i++) {
            if(line.getFu(i).getOperation() == null) {
               totalOps++;
            }
         }
      }
      //return totalOps;
      
      if(totalOps != moveOperations) {
         System.out.println("MoveDiff - Original("+totalOps+") vs New("+moveOperations+")");
      }
*/
      return moveOperations;
   }

/**
 * Total Operatiosn, counting move operations
 * @return
 */
   public int getNumberOfMappedOps() {
/*
      int totalOps = 0;
      for(IfmMapperLine line : lines) {
         totalOps += line.getSize();
      }

     if(totalOps != totalOperations) {
         System.out.println("TotalDiff - Original("+totalOps+") vs New("+totalOperations+")");
      }
      //return totalOps;
 */
      return totalOperations;
   }

   public float getIlp() {
      if(getNumberOfMappedLines() == 0) {
         return 0;
      }

      int mappedOperationsWithoutMoves = getNumberOfMappedOps()-getMoveOperations();
      return (float)mappedOperationsWithoutMoves / (float)getNumberOfMappedLines();
   }

   public float getIlpWithMoves() {
      if(getNumberOfMappedLines() == 0) {
         return 0;
      }

      return (float)getNumberOfMappedOps() / (float)getNumberOfMappedLines();
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Matrix Mapping:\n");
      for(IfmMapperLine fuLine : lines) {
         for(int i=0; i<fuLine.getSize(); i++) {
            Operation op = fuLine.getFu(i).getOperation();
            String opName;
            if(op == null) {
               opName = "MOVE";
            } else {
               opName = op.getOperation().toString();
            }
            builder.append(ParseUtils.padRight(opName, OPERATION_MAX_STRING_LENGHT));
            builder.append("|");
         }
         // End line
         builder.append("\n");
      }
      return builder.toString();
   }



   /**
    * INSTANCE VARIABLES
    */
   private List<IfmMapperLine> lines;

   private int moveOperations;
   private int totalOperations;

   private final int OPERATION_MAX_STRING_LENGHT = 8;

   private void decrementMove(Fu fu) {
      if(fu.getOperation() == null) {
         moveOperations--;
      }
   }

   private void incrementMove(Fu fu) {
      if(fu.getOperation() == null) {
         moveOperations++;
      }
   }


}
