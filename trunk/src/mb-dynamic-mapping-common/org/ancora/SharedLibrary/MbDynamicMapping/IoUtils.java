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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Joao Bispo
 */
public class IoUtils {

   /**
    * Given a File object, returns a String with the contents of the file.
    *
    * <p>If an error occurs (ex.: the File argument does not represent a file)
    * returns null and logs the cause.
    *
    * @param file a File object representing a file.
    * @return a String with the contents of the file, or null if an error
    * occurred.
    */
   public static List<String> readLines(File file) {
      // Check null argument. If null, it would raise and exception and stop
      // the program when used to create the File object.
      if (file == null) {
         Logger.getLogger(IoUtils.class.getName()).
                 warning("Input 'file' is null.");
         return null;
      }

      List<String> lines = new ArrayList<String>();
      FileInputStream stream = null;
      InputStreamReader streamReader = null;

      // Try to read the contents of the file into the StringBuilder
      try {
         stream = new FileInputStream(file);
         streamReader = new InputStreamReader(stream, DEFAULT_CHAR_SET);
         final BufferedReader bufferedReader = new BufferedReader(streamReader);

         String line = bufferedReader.readLine();
         while(line != null) {
            lines.add(line);
            line = bufferedReader.readLine();
         }

         // File read. Close StreamReader
         streamReader.close();

      } catch (FileNotFoundException ex) {
         Logger.getLogger(IoUtils.class.getName()).
                 warning("FileNotFoundException: " + ex.getMessage());
         return null;
      } catch (IOException ex) {
         Logger.getLogger(IoUtils.class.getName()).
                 warning("IOException: " + ex.getMessage());
         return null;
      }

      return lines;
   }


   private static final String DEFAULT_CHAR_SET = org.ancora.SharedLibrary.IoUtils.DEFAULT_CHAR_SET;
}
