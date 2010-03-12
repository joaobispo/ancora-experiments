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

import java.util.EnumSet;
import org.ancora.IrForDynamicMapping.Operand;
import org.ancora.IrForDynamicMapping.OperationName;

/**
 *
 * @author Joao Bispo
 */
public class IfmUtils {

   public static Operand newCoordinateOperand(int line, int col) {
      return new Operand(Operand.OpType.fu, Operand.coordinateAsString(line, col), 0);
   }

   public static boolean isConditionalExit(OperationName operation) {
      return isConditionalExit.contains(operation);
   }

  public static final EnumSet<OperationName> isConditionalExit = EnumSet.of(
           OperationName.exit_equal,
           OperationName.exit_greater,
           OperationName.exit_greater_or_equal,
           OperationName.exit_less,
           OperationName.exit_less_or_equal,
           OperationName.exit_not_equal
      );
}
