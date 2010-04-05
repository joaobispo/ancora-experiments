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

import java.io.File;
import java.util.List;
import java.util.logging.Logger;
import org.ancora.DMExplorer.DataHolders.Execution;
import org.ancora.DMExplorer.Options.TraceProperties;
import org.ancora.DMExplorer.Parsers.CommandLineParser;
import org.ancora.DmeFramework.Statistics.IlpStats;
import org.ancora.DmeFramework.Statistics.InstructionsPerIteration;
import org.ancora.DmeFramework.Statistics.MemoryInstAndCommCounter;
import org.ancora.DmeFramework.Statistics.MicroBlazeRpuDataProcess;
import org.ancora.DmeFramework.System.MicroBlazeRpuMonitor;
import org.ancora.DmeFramework.System.MicroBlazeRpuSystem;
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

      errorsHappened = false;

      InstructionsPerIteration instPerIt = new InstructionsPerIteration();
      MemoryInstAndCommCounter memAndCommCount = new MemoryInstAndCommCounter();

      for(Execution execution : executions) {
         System.out.println("Running Execution "+execution);
         // Link Partitioner and Mapper and run them
         MicroBlazeRpuMonitor monitor = runMicroBlazeRpuSystem(execution);
         // Print Empty Line
         System.out.println(" ");

         String traceName = execution.getTrace().getName();
         String partitionerName = execution.getPartitioner().getName();

         instPerIt.addData(traceName, partitionerName, monitor);
         memAndCommCount.addData(traceName, partitionerName, monitor);
      }

      if(errorsHappened) {
         Logger.getLogger(Main.class.getName()).
                 warning("There were errors. Please, check the log.");
      }

      //File memAndCommFile = new File("memAndComm.csv");
      //memAndCommCount.saveData(memAndCommFile);

      File instPerItFile = new File("inst-it-graph.csv");
      instPerIt.saveTraceCoverageData(instPerItFile);

      //File avgIterationSize = new File("avg-it-size.csv");
      //instPerIt.saveIterationSizeData(avgIterationSize);

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

   private static MicroBlazeRpuMonitor runMicroBlazeRpuSystem(Execution execution) {
      // Setup Trace Reader
      TraceReader traceReader = TraceReader.createTraceReader(execution.getTrace());

      // Get CPI (Cycles Per Instruction) of Trace
      float traceCpi = getTraceCpi(execution.getTrace());

      // Instantiate Monitor for the System
      MicroBlazeRpuMonitor systemMonitor = new MicroBlazeRpuMonitor(traceCpi);

      // Instantiate MicroBlaze-RPU system with given mapper and monitor
      MicroBlazeRpuSystem system = new MicroBlazeRpuSystem(execution, systemMonitor);

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
      

      // Mapper Statistics
      //MicroBlazeRpuDataProcess stats = new MicroBlazeRpuDataProcess(systemMonitor);
      //processData(stats);
      
      // Partition Statistics
      //File ilpStatsFile = new File("ilpStats.csv");
      //IlpStats.addData(ilpStatsFile, systemMonitor, execution);

      // Do some checks
      checkData(systemMonitor, execution);

      return systemMonitor;
   }


   private static void processData(MicroBlazeRpuDataProcess stats) {
      //stats.showDiffMbCyclesSysHis();
      //stats.showSpeedup();
      //System.out.println(" ");
      stats.showIlp();
      //System.out.println(" ");
      stats.showMax();
   }

   /*
   private static void processData(Execution execution, MicroBlazeRpuSystem system) {
      System.out.println("Instructions executed on Mb:"+system.getMonitor().getTotalMicroblazeInstructions());


      // See what kind of data we want to output here.
      // Probably should be work for another class.
   }
    */
    

   private static float getTraceCpi(File trace) {
      TraceProperties traceProps = Utils.getTraceProperties(trace);
      return traceProps.getCpi();
   }

   private static int getTraceTotalInstructions(File trace) {
      TraceProperties traceProps = Utils.getTraceProperties(trace);
      return traceProps.getInstructions();
   }

   private static void checkData(MicroBlazeRpuMonitor systemMonitor, Execution execution) {
      // Check if Partitioned Instructions Add Up
      int monitorInst = systemMonitor.getTotalMicroblazeInstructions();
      int traceInst = getTraceTotalInstructions(execution.getTrace());
      if(monitorInst != traceInst) {
         Logger.getLogger(Main.class.getName()).
                 warning("Total instructions does not add up: Trace("+traceInst+") " +
                 "vs. Partitioner("+monitorInst+")");
         errorsHappened = true;
      }
   }


   private static boolean errorsHappened;
}
