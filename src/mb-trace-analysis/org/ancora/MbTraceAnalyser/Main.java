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

package org.ancora.MbTraceAnalyser;

import org.ancora.MbTraceAnalyser.TraceAlgorithm.TraceflowAlgorithm;
import java.io.File;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ancora.MbTraceAnalyser.TraceAlgorithm.SingleSuperBlock;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.ParseUtils2;
import org.ancora.SharedLibrary.Preferences.EnumPreferences;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       // Setup logging to console
       LoggingUtils.setupConsoleOnly();
       //Logger.getLogger("").setLevel(Level.WARNING);
       

       //parseArguments(args);
       // Load preferences
       EnumPreferences preferences = TraceflowPreferences.getPreferences();
       String input = preferences.getPreference(TraceflowPreferences.Input);
       String outputFoldername = preferences.getPreference(TraceflowPreferences.OutputFolder);

       // Check if input is defined
       if(input == null) {
          Logger.getLogger(Main.class.getName()).
                  info("Input file/folder not specified. Please define one in the properties file.");
          return;
       }

       File outputFolder = IoUtils.safeFolder(outputFoldername);
       if(outputFolder == null) {
           Logger.getLogger(Main.class.getName()).
                  info("Could not access output folder. Please define another one in the properties file.");
          return;
       }

      // Find the files to analyse
      File[] traceFiles = extractFiles(input);

      // Process each one of the trace files
      for (File traceFile : traceFiles) {
         // Get output file
         String outputFilename = ParseUtils2.removeSuffix(traceFile.getName(), ".") + ".trace-flow";
         File outputFile = new File(outputFolder, outputFilename);

         // Verification
         if (outputFile == null) {
            Logger.getLogger(Main.class.getName()).
                    info("Could not access output file.");

         } else {
            Logger.getLogger(Main.class.getName()).
                    info("Processing file '"+traceFile.getName()+"'");
            //TraceflowAlgorithm.doTraceFlowV1(traceFile, outputFile);
            //TraceflowAlgorithm.doTraceFlowV1_2(traceFile, outputFile);
            TraceFlow traceFlow = SingleSuperBlock.doTraceFlow(traceFile, outputFile);
            //PatternFinder.findPatterns(traceFlow.getFlow());
            PatternFinder.patternFinder(traceFlow, outputFile, 7);
         }
      }

   }

   private static void parseArguments(String[] args) {
      // Build set with options
      EnumSet<TraceflowPreferences> preferencesNames = EnumSet.allOf(TraceflowPreferences.class);
      System.out.println(preferencesNames);

      // Checks arguments
      for(int i=0; i<args.length; i++) {
         
      }

         // If after parsing arguments, an input file was not found, show info

   }

   public static String usageMessage() {
      //return "Usage: - i inputFolder | inputFile [-o outputFolder]";
      return "Usage: - i inputFolder | inputFile";
   }

   private static File[] extractFiles(String input) {
      // Discover if input is a file or a folder
      File file = new File(input);

      // Is a file?
      if(file.isFile()) {
         File[] files = new File[1];
         files[0] = file;
         return files;
      }

      // Is not a folder?
      if(!file.isDirectory()) {
         if(file.exists()) {
            Logger.getLogger(Main.class.getName()).
                  info("Input is neither a file, nor a folder. Please define another one in the properties file.");
         } else {
            Logger.getLogger(Main.class.getName()).
                  info("Input does not exist. Please define another one in the properties file.");
         }
         return new File[0];
      }

      // Extract files from folder
      File[] files = file.listFiles();

      // Count files
      Set<Integer> fileIndexes = new HashSet<Integer>();
      for(int i=0; i<files.length; i++) {
         if(files[i].isFile()) {
            fileIndexes.add(i);
         }
      }

      // Build Array
      File[] returnFiles = new File[fileIndexes.size()];
      int counter = 0;
      for(Integer index : fileIndexes) {
         returnFiles[counter] = files[index];
         counter++;
      }

      return returnFiles;

   }

}
