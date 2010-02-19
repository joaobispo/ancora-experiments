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

package org.ancora.MbDynamicMapping.Mappers.InfiniteCcaLib;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.MbDynamicMapping.ParseUtils;

/**
 *
 * @author Joao Bispo
 */
public class Cca {

   public Cca() {
      // Initialize main list
      fuMatrix = new ArrayList<List<Fu>>();
      // Initialize first line
      fuMatrix.add(new ArrayList<Fu>());

      moveOperations = 0;
   }

   public List<Fu> getLine(int line) {
      if (line >= fuMatrix.size()) {
         return null;
      } 
         
      return fuMatrix.get(line);
      
   }

   public Fu getFu(int line, int column) {
      List<Fu> fuLine = getLine(line);
      if(fuLine == null) {
         return null;
      }

      if(column >= fuLine.size() ) {
         return null;
      }

      return fuLine.get(column);
      
   }

   /**
    * @param line
    * @param opName
    * @param inputs
    * @return the coordinate where the operation was mapped
    */
   public Coordinate mapOperation(int line, String opName, List<Coordinate> inputs) {
      // Add move operations for given inputs, if needed
      //addMoveOperations(line, inputs);

      // Get the column
      List<Fu> fuLine = getLineSafe(line);
      int column = fuLine.size();

      // Create Fu
      Coordinate coor = new Coordinate(line, column);
      Fu fu = new Fu(opName, inputs, coor);
      fuLine.add(fu);

      return coor;
   }


   /**
    * Given a line and an input, returns null if no MOVE operations were added,
    * or returns the coordinate of the last MOVE operation.
    *
    * @param line
    * @param inputs
    * @return
    */
   public Coordinate addMoveOperations(int line, Coordinate input) {
      // Check distance between them input and line
         int distance = line - input.line;
         if(distance <= 0) {
            Logger.getLogger(Cca.class.getName()).
                    warning("Input line:"+input.line+"; Consumer line:"+line);
            return null;
         }

         // No MOVE operations is needed
         if(distance == 1) {
            return null;
         }

         if(distance > 1) {
            //System.out.println("DestLine:"+line);
            //System.out.println("InputLine:"+input.line);
            Coordinate coor = null;
            int numberOfMoves = distance-1;
            int startingLine = input.line+1;
            for(int i=0; i<numberOfMoves; i++) {
               coor = mapOperation(startingLine+i, MOVE_OPERATION, new ArrayList<Coordinate>());
               moveOperations++;
            }

            return coor;
         }

         
      Logger.getLogger(Cca.class.getName()).
              warning("Should not reach this place");
      return null;
   }
    /*
   public Coordinate addMoveOperations(int line, List<Coordinate> inputs) {
      // For each input, check distance between them
      for(Coordinate input : inputs) {
         int distance = line - input.line;
         if(distance <= 0) {
            Logger.getLogger(Cca.class.getName()).
                    warning("Input line:"+input.line+"; Consumer line:"+line);
         }

         if(distance > 1) {
            int numberOfMoves = distance-1;
            int startingLine = input.line+1;
            for(int i=0; i<numberOfMoves; i++) {
               mapOperation(startingLine+i, MOVE_OPERATION, new ArrayList<Coordinate>());
            }
         }
      }
   }
     */

   /**
    * If line does not exist, is created.
    * 
    * @param line
    * @return
    */
   private List<Fu> getLineSafe(int line) {
      List<Fu> fuLine = getLine(line);
      if(fuLine != null) {
         return fuLine;
      }

      //System.out.println("line:"+line);
      //System.out.println("fuMatrixSize:"+fuMatrix.size());
      // Check how many lines need to be created
      int missingLines = line-fuMatrix.size()+1;
      //System.out.println("Missing Lines:"+missingLines);
      for(int i=0; i<missingLines; i++) {
         fuMatrix.add(new ArrayList<Fu>());
      }

      fuLine = getLine(line);
      if(fuLine == null) {
         System.out.println("getLineSafe: PROBLEM!");
      }
      
      return fuLine;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("CCA Mapping:\n");
      for(List<Fu> fuLine : fuMatrix) {
         // Beginning a line
         for(Fu fu : fuLine) {
            builder.append(ParseUtils.padRight(fu.getOpName(), OPERATION_MAX_STRING_LENGHT));
         }
         // End line
         builder.append("\n");
      }
      return builder.toString();
   }

   public int getMoveOperations() {
      return moveOperations;
   }

   public int getNumberMappedLines() {
      return fuMatrix.size();
   }

   public int getNumberOfMappedOps() {
      int totalOps = 0;
      for(List<Fu> fuLine : fuMatrix) {
         totalOps += fuLine.size();
      }
      return totalOps-moveOperations;
   }
   
   public float getIlp() {
      return (float)getNumberOfMappedOps() / (float)getNumberMappedLines();
   }

   /**
    * INSTANCE VARIABLES
    */
   private List<List<Fu>> fuMatrix;

   private final String MOVE_OPERATION = "MOVE";
   private final int OPERATION_MAX_STRING_LENGHT = 7;

   private int moveOperations;
}
