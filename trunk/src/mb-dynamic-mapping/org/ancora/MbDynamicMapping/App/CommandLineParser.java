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

package org.ancora.MbDynamicMapping.App;

import org.ancora.MbDynamicMapping.Options.ExecutionOption;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.MbDynamicMapping.IoUtils;

/**
 * Parses the arguments of the command line.
 *
 * @author Joao Bispo
 */
public class CommandLineParser {

   /**
    * Parses the command line. If parsing is not successful, returns null.
    *
    * <p>Currently, supports:
    * <br>an argument, which will be the name and path of a file with executions
    * inside.
    *
    * @param args
    * @return a list of Exection objects
    */
   public static List<Execution> parseCommandLine(String[] args) {
      //List<Execution> executionList = new ArrayList<Execution>();

      // Check if there are arguments
      if(args.length == 0) {
         showNoArgsMessage();
         return null;
         //return executionList;
      }

      // Check if first argument is help option
      if(args[0].equals(helpOption)) {
         showOptionList();
         return null;
         //return executionList;
      }

      // Open Execution file
      File executionFile = new File(args[0]);

      // Check if file exists
      if(!executionFile.exists()) {
         Logger.getLogger(CommandLineParser.class.getName()).
                 info("File '"+args[0]+"' does not exist.");
         return null;
         //return executionList;
      }

      // Parse Execution file with an ExecutionParser
      ExecutionParser executionParser = new ExecutionParser();
      List<Execution> executionList = new ArrayList<Execution>();
      List<String> executionLines = IoUtils.readLines(executionFile);

      for(int i=0; i<executionLines.size(); i++) {
         String executionLine = executionLines.get(i);
         Execution[] execution = executionParser.parseExecutionLine(executionLine, (i+1));
         if(execution != null) {
            executionList.addAll(Arrays.asList(execution));
         }
      }

      return executionList;
   }

   /**
    * Show message when there are no arguments
    */
   private static void showNoArgsMessage() {
      Logger logger = Logger.getLogger(CommandLineParser.class.getName());
      logger.info("No CommandLine arguments. Please indicate a file with executions. Example:");
      logger.info("test.txt");
      //logger.info("-"+CommandLineOption.executionfile+" test.txt");
      logger.info("");
      logger.info("The file can contain one execution per line. Example of an execution:");
      logger.info("-setTraceFolder ../traces");
      logger.info("-trace quicksort.txt -part NoPartition -map NoCustomHardware");
      logger.info("");
      logger.info("Use '"+helpOption+"' for more info.");
   }


   private static void showOptionList() {
      Logger logger = Logger.getLogger(CommandLineParser.class.getName());
      logger.info("Execution Options:");
      for(ExecutionOption option : ExecutionOption.values()) {
         logger.info("-"+option.getName()+" - "+option.getHelpMessage()+".");
      }

      logger.info("");
      logger.info("Partitioner Blocks:");
      for(PartitionerName option : PartitionerName.values()) {
         logger.info(option.getName()+" - "+option.getHelpMessage()+".");
      }

      logger.info("");
      logger.info("Mapper Blocks:");
      for(MapperName option : MapperName.values()) {
         logger.info(option.getName()+" - "+option.getHelpMessage()+".");
      }


   }

   /**
    * INSTANCE VARIABLES
    */
   private static String helpOption = "-help";


}
