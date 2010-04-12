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

package org.ancora.DynamicMapping.Tester.ProtoIr.Parser.MicroBlaze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.ancora.DynamicMapping.Tester.ProtoIr.Parser.Interface.InstructionBusReader;
import org.ancora.DynamicMapping.Tester.ProtoIr.Parser.Interface.RawInstruction;
import org.ancora.SharedLibrary.ParseUtils;

/**
 *
 * @author Joao Bispo
 */
public class MicroBlazeTraceReader implements InstructionBusReader {
   /**
    * Private constructor for static creator method.
    *
    * @param reader
    */
    private MicroBlazeTraceReader(BufferedReader reader) {
       this.reader = reader;
    }

   /**
    * Builds a TraceReader from the given file. If the object could
    * not be created, returns null.
    *
    * <p>Creating a TraceReader involves File operations which can lead
    * to failure in creation of the object. That is why we use a public
    * static method instead of a constructor.
    *
    * @param traceFile a file representing a MicroBlaze Trace, as in the format
    * of the tool of the Ancora Group (not avaliable yet).
    * @return a TraceReader If the object could not be created, returns null.
    */
   public static MicroBlazeTraceReader createTraceReader(File traceFile) {

      FileInputStream stream = null;
      InputStreamReader streamReader = null;

        try {
            // Try to read the contents of the file into the StringBuilder
            stream = new FileInputStream(traceFile);
            streamReader = new InputStreamReader(stream, DEFAULT_CHAR_SET);
            BufferedReader newReader = new BufferedReader(streamReader);
            return new MicroBlazeTraceReader(newReader);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MicroBlazeTraceReader.class.getName()).
                    warning("FileNotFoundException: "+ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MicroBlazeTraceReader.class.getName()).
                    warning("UnsupportedEncodingException: "+ex.getMessage());
        }

      return null;
   }

   
   public List<RawInstruction> getBasicBlock() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public List<RawInstruction> getAllInstructions() {
      List<RawInstruction> instructions = new ArrayList<RawInstruction>();

      RawInstruction instruction = nextInstruction();
      while(instruction != null) {
         instructions.add(instruction);
         instruction = nextInstruction();
      }

      return instructions;
   }

    /**
     * Returns the next line in the file which qualifies as a trace instruction.
     * A line is considered as a trace instruction if it starts with "0x".
     *
     * <p>The trace file is read line by line, instead of being parsed
     * completely. This was done to avoid loading the entire
     * trace in memory, to enable the execution of very large traces
     * (ex.: > 50Mb).
     * <br>The trace is read as a sequential stream of instructions.
     *
     * @return the next String which qualifies as an instruction, or null if
     * the end of the stream has been reached
     */
    private MicroBlazeInstruction nextInstruction() {

        // While there are lines and a trace instruction was not found, loop.
        String line = null;
        while(true) {

           try {
               // Read next line
              line = reader.readLine();

              // Check if end of stream has arrived.
              if (line == null) {
                 return null;
              }

              // Check if current line is a trace instruction
              if (line.startsWith(TRACE_PREFIX)) {
                 // Create MicroBlazeInstruction
                 MicroBlazeInstruction instruction = createMicroBlazeInstruction(line);
                 return instruction;
              }

           } catch (IOException ex) {
              Logger.getLogger(MicroBlazeTraceReader.class.getName()).
                      warning("IOException: "+ex.getMessage());
              return null;
           }
        }

    }

    private MicroBlazeInstruction createMicroBlazeInstruction(String line) {
      /// Split the trace instruction in parts
      int whiteSpaceIndex = ParseUtils.indexOfFirstWhiteSpace(line);
      
      // Get Address
      String addressString = line.substring(0, whiteSpaceIndex);
      // Remove prefix
      addressString = addressString.substring("0x".length());
      // Parse to integer
      int instructionAddress = Integer.valueOf(addressString, 16);

      // Get instruction
      String instruction = line.substring(whiteSpaceIndex).trim();

      return new MicroBlazeInstruction(instructionAddress, instruction);
   }

   /**
    * INSTANCE VARIABLES
    */
    private final BufferedReader reader;

    // Definitions
    private final String TRACE_PREFIX = "0x";

   /**
    * Default CharSet used in file operations.
    */
   public static final String DEFAULT_CHAR_SET = "UTF-8";




}
