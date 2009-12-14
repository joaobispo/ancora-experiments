/*
 *  Copyright 2009 Ancora Research Group.
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

package org.ancora.SharedLibrary;

/**
 *
 * @author Joao Bispo
 */
public class ParseUtils2 {

   /**
    * Removes, from String text, the portion of text after the rightmost 
    * occurrence of the specified separator.
    * 
    * <p>Ex.: removeSuffix("readme.txt", ".")
    * <br> Returns "readme".
    * @param text
    * @param separator
    * @return
    */
   public static String removeSuffix(String text, String separator) {
      int index = text.lastIndexOf(separator);

      if(index == -1) {
         return text;
      }

      return text.substring(0, index);
   }


}
