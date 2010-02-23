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

package org.ancora.SharedLibrary.MbDynamicMapping;

/**
 *
 * @author Joao Bispo
 */
public class BitUtils {
   /**
    * Fuses the lower 16 bits of two ints.
    *
    * TODO: Verify correcteness.
    * <p>Ex.:
    * upper16 = 1001
    * lower16 = 101
    * result = 00000000000010010000000000000101
    *
    * @param upper16
    * @param lower16
    * @return
    */
   public static int fuseInt(int upper16, int lower16){
      // Mask the 16 bits of each one
      upper16 = upper16 & Integer.parseInt("0000FFFF", 16);
      lower16 = lower16 & Integer.parseInt("0000FFFF", 16);
      // Shift Upper16
      upper16 = upper16 << 16;
      // Merge
      return upper16 | lower16;
   }
}
