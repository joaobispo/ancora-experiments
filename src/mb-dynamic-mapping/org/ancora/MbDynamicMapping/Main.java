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

package org.ancora.MbDynamicMapping;

import java.io.File;
import java.util.List;
import org.ancora.MbDynamicMapping.App.CommandLineParser;
import org.ancora.MbDynamicMapping.App.Execution;
import org.ancora.MbDynamicMapping.Options.TraceProperties;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.MicroBlaze.Trace.TraceReader;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.ParseUtils;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       LoggingUtils.setupConsoleOnly();

       // TODO code application logic here
       //System.out.println("hello");
       protoInit(args);
    }

   private static void protoInit(String[] args) {
      List<Execution> executions = CommandLineParser.parseCommandLine(args);

      // Check if parsing went well
      if(executions == null) {
         return;
      }

      // Execute each "Execution"
      for(Execution execution : executions) {
         System.out.println("Running Execution "+execution);
         run(execution);
         System.out.println(" ");
      }
   }

   private static void run(Execution execution) {
      // Setup Trace Reader
      TraceReader traceReader = TraceReader.createTraceReader(execution.getTrace());

      // Link Mapper to Partitioner
      execution.getPartitioner().addListener(execution.getMapper());

      // Read instructions and feed them to the partitioner
      Instruction instruction = traceReader.nextInstruction();
      while(instruction != null) {
         execution.getPartitioner().accept(instruction);
         instruction = traceReader.nextInstruction();
      }

      // Finish run
      execution.getPartitioner().flush();

      // Treat data
      treatData(execution);
   }

   private static void treatData(Execution execution) {
      TraceProperties traceProps = getTraceProperties(execution.getTrace());

      // Compare total mapped instructions
      int mapperInst = execution.getMapper().getTotalMappedInstructions();
      int traceInst = traceProps.getInstructions();
      if(traceInst != mapperInst) {
         System.out.println("Check Failed: Mapper total instructions '"+mapperInst+"';" +
                 " Should be '"+traceInst+"'");
      } else {
         System.out.println("Check Passed: Mapper Total Instructions.");
      }

   }

   //
   // UTILITY METHODS
   //

   private static TraceProperties getTraceProperties(File trace) {
       // Get Trace Properties
      File propertiesFolder = new File("../common/properties");
      // Get a Trace Properties Filename
      String traceFilename = trace.getName();
      // Build properties filename
      String propertiesFilename = ParseUtils.removeSuffix(traceFilename, ".") + ".properties";
      // Get TraceProperties
      return TraceProperties.buildTraceProperties(new File(propertiesFolder,propertiesFilename));
   }

}
