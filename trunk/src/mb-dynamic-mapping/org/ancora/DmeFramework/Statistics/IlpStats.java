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

package org.ancora.DmeFramework.Statistics;

import java.io.File;
import java.util.logging.Logger;
import org.ancora.DMExplorer.DataHolders.Execution;
import org.ancora.DmeFramework.System.MicroBlazeRpuMonitor;
import org.ancora.SharedLibrary.IoUtils;

/**
 *
 * @author Joao Bispo
 */
public class IlpStats {

   public static void addData(File file, MicroBlazeRpuMonitor monitor, Execution execution) {
      // Check if file exists
      if(!file.isFile()) {
         // Create new IlpStats file
         boolean success = createIlpFile(file);
         if(!success) {
            Logger.getLogger(IlpStats.class.getName()).
                    warning("Could not create file "+file);
         }
      }


      // Extract Data
      ExecutionData execData = new ExecutionData(execution);
      IlpData ilpData = new IlpData(monitor);

      // Write line of data
      boolean success = writeLine(file, execData, ilpData);
      if (!success) {
         Logger.getLogger(IlpStats.class.getName()).
                 warning("Could not write to file " + file);
      }
   }

   private static boolean createIlpFile(File file) {
      StringBuilder builder = new StringBuilder();

      CsvField[] parameters = CsvField.values();
      // Write first parameter
      builder.append(parameters[0].toString());
      // Write the other parameters
      for(int i=1; i<parameters.length; i++) {
         builder.append(",");
         builder.append(parameters[i]);
      }
      builder.append("\n");

      return IoUtils.append(file, builder.toString());
   }

   private static boolean writeLine(File file, ExecutionData execData, IlpData ilpData) {
      StringBuilder builder = new StringBuilder();

      CsvField[] parameters = CsvField.values();
      // Write first parameter
      builder.append(parameters[0].getValue(ilpData, execData));
      // Write the other parameters
      for(int i=1; i<parameters.length; i++) {
         builder.append(",");
         builder.append(parameters[i].getValue(ilpData, execData));
      }
      builder.append("\n");

      return IoUtils.append(file, builder.toString());
   }

   public enum CsvField {
      trace,
      partitioner,
      mapper,
      blockInstructionsAvg,
      blockInstructionsMin,
      blockInstructionsMax,
      blockFrequencyAvg,
      blockFrequencyMin,
      blockFrequencyMax,
      blockIterationsAvg,
      blockIterationsMin,
      blockIterationsMax;

      String getValue(IlpData monitor, ExecutionData execution) {
         switch(this) {
            case trace:
               return execution.getTraceName();
            case partitioner:
               return execution.getPartitionerName();
            case mapper:
               return execution.getMapperName();
            case blockFrequencyAvg:
               return String.valueOf(monitor.getBlockFrequencyAvg());
            case blockFrequencyMax:
               return String.valueOf(monitor.getBlockFrequencyMax());
            case blockFrequencyMin:
               return String.valueOf(monitor.getBlockFrequencyMin());
            case blockInstructionsAvg:
               return String.valueOf(monitor.getBlockInstructionsAvg());
            case blockInstructionsMax:
               return String.valueOf(monitor.getBlockInstructionsMax());
            case blockInstructionsMin:
               return String.valueOf(monitor.getBlockInstructionsMin());
            case blockIterationsAvg:
               return String.valueOf(monitor.getBlockIterationsAvg());
            case blockIterationsMax:
               return String.valueOf(monitor.getBlockIterationsMax());
            case blockIterationsMin:
               return String.valueOf(monitor.getBlockIterationsMin());
            default:
               Logger.getLogger(IlpStats.class.getName()).
                       warning("Value not defined:"+this);
               return "null";
         }
      }

      @Override
      public String toString() {
         return name();
      }


   }
}
