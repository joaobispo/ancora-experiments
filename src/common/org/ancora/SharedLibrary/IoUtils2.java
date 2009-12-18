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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Logger;

/**
 *
 * @author Joao Bispo
 */
public class IoUtils2 {
   
   /**
    * From the name of the folder and the name of the file, returns a file 
    * inside the folder, returns a File object.
    * 
    * @param folder
    * @param filename
    * @return
    */
   public static File getFile(String folder, String filename) {
      //File file = new File()

      //return file;
      return null;
   }

   /**
    * Method shared among write and append.
    *
    * @param file
    * @param contents
    * @param append
    * @return
    */
   private static boolean writeAppendHelper(File file, String contents, boolean append) {

      FileOutputStream stream = null;
      OutputStreamWriter streamWriter = null;
      try {
         stream = new FileOutputStream(file, append);
         streamWriter = new OutputStreamWriter(stream, DEFAULT_CHAR_SET);
         final BufferedWriter writer = new BufferedWriter(streamWriter);
         writer.write(contents, 0, contents.length());
         writer.close();
         // Inform about the operation
         if (append) {
            // Check if this is the same file as the last time
            String filePath = file.getAbsolutePath();
            if(!filePath.equals(lastAppeddedFileAbsolutePath)) {
               lastAppeddedFileAbsolutePath = filePath;
               Logger.getLogger(IoUtils.class.getName()).
                       info("File appended (" + file.getAbsolutePath() + ").");
            }
         } else {
            Logger.getLogger(IoUtils.class.getName()).
                    info("File written (" + file.getAbsolutePath() + ").");
         }

      } catch (IOException ex) {
         Logger.getLogger(IoUtils.class.getName()).
                 warning("IOException: " + ex.getMessage());
         return false;
      }

      return true;
   }

      /**
    * Given a File object and a String, writes the contents of the String at the
    * end of the file. If successful, returns true.
    *
    * <p>If an error occurs (ex.: the File argument does not represent a file)
    * returns false, logs the cause and nothing is written.
    *
    * @param file a File object representing a file.
    * @param contents a String with the content to write
    * @return true if write is successful. False otherwise.
    */
   public static boolean append(File file, String contents) {
      // Check null argument. If null, it would raise and exception and stop
      // the program when used to create the File object.
      if (file == null) {
         Logger.getLogger(IoUtils.class.getName()).
                 warning("Input 'file' is null.");
         return false;
      }

      if (contents == null) {
         Logger.getLogger(IoUtils.class.getName()).
                 warning("Input 'contents' is null.");
         return false;
      }

      return writeAppendHelper(file, contents, true);
   }


   //
   //DEFINITIONS
   //
   /**
    * Default CharSet used in file operations.
    */
   final public static String DEFAULT_CHAR_SET = "UTF-8";
   // Records the name of the last file appended
   private static String lastAppeddedFileAbsolutePath = "";
   
}
