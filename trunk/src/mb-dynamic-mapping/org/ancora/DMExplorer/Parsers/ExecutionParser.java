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

package org.ancora.DMExplorer.Parsers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.ancora.DMExplorer.DataHolders.Execution;
import org.ancora.DMExplorer.Options.ExecutionOption;
import org.ancora.DMExplorer.Plugins.MapperName;
import org.ancora.DMExplorer.Plugins.PartitionerName;

/**
 * Parses lines of executions.
 * 
 * @author Joao Bispo
 */
public class ExecutionParser {

   public ExecutionParser() {
      currentTraceFolder = new File(".");
      if(!currentTraceFolder.isDirectory()) {
         Logger.getLogger(ExecutionParser.class.getName()).
                    warning("Start-up folder '"+currentTraceFolder+"' is not valid.");
      }
   }

   /**
    * Parses the line and builds an Execution object. If the line does not
    * generate an Execution block, returns null.
    *
    * @param executionLine
    * @return
    */
   public Execution[] parseExecutionLine(String executionLine, int line) {
      // Check if line is a comment
      if(executionLine.startsWith("//")) {
         return null;
      }

      // Split execution line
      String[] options = executionLine.split(" ");

     // System.out.println("Line:"+Arrays.toString(options));

      Map<ExecutionOption, String> optionsTable = new HashMap<ExecutionOption, String>();

      // Extract options from command line
      for(int i=0; i<options.length; i++) {
         // Check for empty string
         if(options[i].length() == 0) {
            continue;
         }

         if(options[i].charAt(0) != '-') {
               Logger.getLogger(ExecutionParser.class.getName()).
                    info("Error in line '"+line+"': '"+options[i]+"' not indicated has an option (at least put a '-' before it)");
               return null;
         }

         ExecutionOption option = null;
         String optionString = options[i].substring(1);
         try {
            option = ExecutionOption.valueOf(optionString.toLowerCase());
         } catch(IllegalArgumentException ex) {
            Logger.getLogger(ExecutionParser.class.getName()).
                    info("Error in line '"+line+"': Invalid option '-"+optionString+"'");
            return null;
         }

         // Check if option as argument
         if(options.length == (i+1)) {
              Logger.getLogger(ExecutionParser.class.getName()).
                    info("Error in line '"+line+"': Option '"+options[i]+"' needs an argument.");
            return null;
         }

         if(options[i+1].charAt(0) == '-') {
              Logger.getLogger(ExecutionParser.class.getName()).
                    info("Error in line '"+line+"': '"+options[i+1]+"' is not a valid argument.");
            return null;
         }

         // Store pair in table
         optionsTable.put(option, options[i+1]);
         // Increment index
         i++;

      }

      Execution[] execution = buildExecution(optionsTable, line);

      return execution;
   }

   private Execution[] buildExecution(Map<ExecutionOption, String> options, int line) {
      
      // Check if there is a new TraceFolder
      String traceFolder = options.get(ExecutionOption.settracefolder);
      if (traceFolder != null) {
         boolean success = updateTraceFolder(traceFolder);
         if (!success) {
            Logger.getLogger(ExecutionParser.class.getName()).
                    info("Error in line '"+line+"': Folder '" + traceFolder + "' is not a valid folder.");
            return null;
         }
      }

      // Check for a set of execution variables
      List<File> traces = new ArrayList<File>();
//      Mapper mapper = null;
//      Partitioner partitioner = null;
      String mapper = null;
      String partitioner = null;

      // Check trace file
      String traceFilename = options.get(ExecutionOption.trace);
      if(traceFilename != null) {
         // Check if is special keyword 'all'
         if(traceFilename.equals(TRACE_KEYWORD_ALL)) {
            File[] files = currentTraceFolder.listFiles();
            for(File file : files) {
               if(file.isFile()) {
                  traces.add(file);
               }
            }
         } else {
            File file = new File(currentTraceFolder, traceFilename);
            if (!file.isFile()) {
               Logger.getLogger(ExecutionParser.class.getName()).
                       info("Error in line '" + line + "': '" + traceFilename + "' is not a valid file.");
               return null;
            }        
            traces.add(file);
         }
      }

      // Check Partitioner
      String partitionerNameString = options.get(ExecutionOption.part);
      if (partitionerNameString != null) {
         try {
            PartitionerName partitionerName = PartitionerName.valueOf(partitionerNameString.toLowerCase());
            partitioner = partitionerNameString;
         } catch (IllegalArgumentException ex) {
            Logger.getLogger(ExecutionParser.class.getName()).
                    info("Error in line '" + line + "': Invalid Partitioner Block '" + partitionerNameString + "'");
            return null;
         }
      }

      // Check Mapper
      String mapperNameString = options.get(ExecutionOption.map);
      if (mapperNameString != null) {
         try {
            MapperName mapperName = MapperName.valueOf(mapperNameString.toLowerCase());
            mapper = mapperNameString;
         } catch (IllegalArgumentException ex) {
            Logger.getLogger(ExecutionParser.class.getName()).
                    info("Error in line '" + line + "': Invalid Mapper Block '" + mapperNameString + "'");
            return null;
         }
      }


      //System.out.println("trace size "+traces.size());
      //System.out.println("part "+partitioner);
      //System.out.println("mapper "+mapper);

      if(traces.size() > 0 && partitioner != null && mapper != null) {
         Execution[] executionArray = new Execution[traces.size()];
         for(int i=0; i<traces.size(); i++) {
            File file = traces.get(i);
            executionArray[i] = new Execution(file, partitioner, mapper);
         }
         
         return executionArray;
      }
      

      return null;
   }


   private boolean updateTraceFolder(String traceFolder) {
      File newTraceFolder = new File(traceFolder);

      // Check if folder exists and is a folder
      if(newTraceFolder.isDirectory()) {
         this.currentTraceFolder = newTraceFolder;
         return true;
      } else {
         return false;
      }
   }

   /**
    * INSTANCE VARIABLES
    */
   private File currentTraceFolder;

   private final static String TRACE_KEYWORD_ALL = "all";

}
