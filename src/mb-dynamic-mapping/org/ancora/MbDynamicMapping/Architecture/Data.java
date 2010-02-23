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

package org.ancora.MbDynamicMapping.Architecture;

import org.ancora.MbDynamicMapping.Mappers.InfiniteCcaLib.Coordinate;

/**
 * Represents data inside the architecture.
 *
 * @author Joao Bispo
 */
public class Data {

   public Data(DataType dataType, String value) {
      this.dataType = dataType;
      this.value = value;
   }

   public enum DataType {
      register,
      literal,
      fu;
   }

   public DataType getDataType() {
      return dataType;
   }

   public String getValue() {
      return value;
   }

   public Coordinate getValueAsCoordinate() {
      String[] coordinate = value.split(FU_COORDINATE_SEPARATOR);
      return new Coordinate(Integer.valueOf(coordinate[0]), Integer.valueOf(coordinate[1]));
   }

   public static String registerAsString(int reg) {
      return REGISTER_PREFIX+reg;
   }

   /**
    * INSTANCE VARIABLES
    */
   final private DataType dataType;
   final private String value;

   final public static String FU_COORDINATE_SEPARATOR = ",";
   final public static String REGISTER_PREFIX = "r";
}
