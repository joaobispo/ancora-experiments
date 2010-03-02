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

package org.ancora.IrForDynamicMapping;

import java.util.logging.Logger;
import org.ancora.MbDynamicMapping.IR.*;
import org.ancora.MbDynamicMapping.Mappers.InfiniteCcaLib.Coordinate;

/**
 * Represents data inside the architecture.
 *
 * @author Joao Bispo
 */
public class Operand {

   public Operand(OpType dataType, String value, int bitSize) {
      this.dataType = dataType;
      this.value = value;
      this.bitSize = bitSize;
   }

   public enum OpType {
      register,
      literal,
      fu;

      public String getShortForm() {
         return this.name().substring(0,1);
      }
   }

   public OpType getOpType() {
      return dataType;
   }

   public String getValue() {
      return value;
   }

   public int getBitSize() {
      return bitSize;
   }

   public Coordinate getValueAsCoordinate() {
      String[] coordinate = value.split(FU_COORDINATE_SEPARATOR);
      return new Coordinate(Integer.valueOf(coordinate[0]), Integer.valueOf(coordinate[1]));
   }

   public int getValueAsIntegerLiteral() {
      int result = 0;
      try {
         result = Integer.parseInt(value);
      } catch(NumberFormatException e) {
         Logger.getLogger(Operand.class.getName()).
                 warning("Error when parsing '"+value+"' to an Integer.");
      }

      return result;
   }

   public static String registerAsString(int reg) {
      return REGISTER_PREFIX+reg;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      builder.append(dataType.getShortForm());
      builder.append("_");
      builder.append(value);
      builder.append("(");
      builder.append(bitSize);
      builder.append(")");

      return builder.toString();
   }



   /**
    * INSTANCE VARIABLES
    */
   final private OpType dataType;
   final private String value;
   final private int bitSize;

   final public static String FU_COORDINATE_SEPARATOR = ",";
   final public static String REGISTER_PREFIX = "r";
}
