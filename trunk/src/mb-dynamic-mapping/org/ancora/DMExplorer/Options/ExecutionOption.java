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

package org.ancora.DMExplorer.Options;

/**
 *
 * @author Joao Bispo
 */
public enum ExecutionOption {
   feeddistance,
   pelinesize,
   settracefolder,
   trace,
   part,
   map;

   public String getHelpMessage() {
      switch(this) {
         case feeddistance:
            return "Sets the maximum distance for communication between PEs.";
         case pelinesize:
            return "Sets the maximum number of PEs per line. Use 0 for an infinite line.";
         case settracefolder:
            return "Sets the folder where the trace is";
         case trace:
            return "The name of the file with the MicroBlaze trace";
         case part:
            return "The name of the Partitioner block";
         case map:
            return "The name of the Mapper block";
         default:
            return "Help Message Not Defined";
      }
   }

   public String getName() {
      switch(this) {
         case feeddistance:
            return "feedDistance";
         case pelinesize:
            return "peLineSize";
         case settracefolder:
            return "setTraceFolder";
         case trace:
            return "trace";
         case part:
            return "part";
         case map:
            return "map";
         default:
            return "Name Not Defined";
      }
   }

}
