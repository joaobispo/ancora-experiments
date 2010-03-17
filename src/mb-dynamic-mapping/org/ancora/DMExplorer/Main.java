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

package org.ancora.DMExplorer;

import java.util.List;
import org.ancora.DMExplorer.DataHolders.Execution;
import org.ancora.DMExplorer.Options.TraceProperties;
import org.ancora.DMExplorer.Parsers.CommandLineParser;
import org.ancora.DmeFramework.System.SystemMbRpu;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.MicroBlaze.Trace.TraceReader;
import org.ancora.SharedLibrary.LoggingUtils;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        setupProgram();
        execute(args);
    }

   private static void setupProgram() {
      // Configure Logger to capture all output to console
      LoggingUtils.setupConsoleOnly();
   }

   private static void execute(String[] args) {
      List<Execution> executions = CommandLineParser.parseCommandLine(args);
      
      // Check if parsing gave a null value
      if(executions == null) {
         return;
      }

      for(Execution execution : executions) {
         System.out.println("Running Execution "+execution);
         // Link Partitioner and Mapper and run them
         runMicroBlazeRpuSystem(execution);
         // Print Empty Line
         System.out.println(" ");
      }

      /*


      // Execute each "Execution"
      for(Execution execution : executions) {
         System.out.println("Running Execution "+execution);
         //run(execution);
         testIR(execution);
         System.out.println(" ");
      }
       */
   }

   private static void runMicroBlazeRpuSystem(Execution execution) {
      // Setup Trace Reader
      TraceReader traceReader = TraceReader.createTraceReader(execution.getTrace());

      // Instantiate MicroBlaze-RPU system with given mapper
      SystemMbRpu system = new SystemMbRpu(execution.getMapper());

      // Link System to Partitioner
      execution.getPartitioner().addListener(system);

      // Read instructions and feed them to the partitioner
      Instruction instruction = traceReader.nextInstruction();
      while(instruction != null) {
         execution.getPartitioner().accept(instruction);
         instruction = traceReader.nextInstruction();
      }

      // Finish run
      execution.getPartitioner().flush();

      // Output statistics
      processData(execution, system);
   }

   private static void processData(Execution execution, SystemMbRpu system) {
      TraceProperties traceProps = Utils.getTraceProperties(execution.getTrace());

      // See what kind of data we want to output here.
      // Probably should be work for another class.
   }

}
