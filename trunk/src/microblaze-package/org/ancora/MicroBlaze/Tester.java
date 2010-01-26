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

package org.ancora.MicroBlaze;

import java.io.File;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.MicroBlaze.Trace.TraceReader;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class Tester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //testTraceReader();
        testTraceReaderMultipleFiles();
    }

   private static void testTraceReader() {
      String foldername = "D:\\Programming\\Ancora\\AncoraExperiments\\run\\common\\traces-without-optimization";
      String filename = "autocorrelation_trace_without_optimization.txt";

      File folder = new File(foldername);
      File file = new File(folder, filename);

      TraceReader reader = TraceReader.createTraceReader(file);
      Instruction instruction = reader.nextInstruction();
      while(instruction != null) {
         //boolean isBranch = MicroBlazeUtils.isBranch(instruction.getOperation());
         //System.out.println(instruction+"; Is branch:"+isBranch);

         boolean hasDelaySlot = MicroBlazeUtils.hasDelaySlot(instruction.getOperation());
         //System.out.println(instruction+"; Has Delay Slot:"+hasDelaySlot);
         instruction = reader.nextInstruction();
      }
      
      //System.out.println(reader.nextInstruction().toString());

   }

   private static void testTraceReaderMultipleFiles() {
      String foldername = "D:\\Programming\\Ancora\\AncoraExperiments\\run\\common\\traces-without-optimization";
      File folder = new File(foldername);

      File[] files = folder.listFiles();

      for (File file : files) {
         System.out.println("Processing file '" + file.getName() + "'");

         TraceReader reader = TraceReader.createTraceReader(file);
         Instruction instruction = reader.nextInstruction();
         while (instruction != null) {
            instruction = reader.nextInstruction();
         }
      }
   }


}
