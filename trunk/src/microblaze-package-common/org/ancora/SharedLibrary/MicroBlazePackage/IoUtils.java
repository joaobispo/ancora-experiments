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

package org.ancora.SharedLibrary.MicroBlazePackage;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author Joao Bispo
 */
public class IoUtils {

   /**
    * Given a File object, loads the contents of the file into a Java Properties
    * object.
    *
    * <p>If an error occurs (ex.: the File argument does not represent a file,
    * could not load the Properties object) returns null and logs the cause.
    *
    * @param file a File object representing a file.
    * @return If successfull, a Properties objects with the contents of the
    * file. Null otherwise.
    */
   public static Properties loadProperties(File file) {
      // Check null argument. If null, it would raise and exception and stop
      // the program when used to create the File object.
      if (file == null) {
         Logger.getLogger(IoUtils.class.getName()).
                 warning("Input 'file' is null.");
         return null;
      }


      try {
         Properties props = new Properties();
         props.load(new java.io.FileReader(file));
         return props;
      } catch (IOException ex) {
         Logger.getLogger(IoUtils.class.getName()).
                 warning("IOException: " + ex.getMessage());
      }

      return null;
   }
}
