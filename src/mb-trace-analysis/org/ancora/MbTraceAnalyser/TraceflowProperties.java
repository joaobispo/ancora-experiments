/*
 *  Copyright 2009 Abstract Maze.
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

import org.ancora.SharedLibrary.Interfaces.EnumKey;
import org.ancora.SharedLibrary.Preferences.PropertiesFile;


/**
 * Contains information about the Client’s Properties file.
 *
 * @author Joao Bispo
 */
public class TraceflowProperties extends PropertiesFile {

   public TraceflowProperties() {
      super(TraceflowPreferences.values().length);
   }

   public void buildSections() {
      
      // Initial Header
      addSection("#");
      addSection("Properties file for Trace-Flow");
      addSection("#");
      addSection("");

      // Server Part
      addSection("#");
      addSection(" Files & Folders");
      addSection("#");
      addSection("");
      addSection(" Input", TraceflowPreferences.Input);
      addSection("");
      addSection(" Output Folder", TraceflowPreferences.OutputFolder);
      addSection("");
      addSection(" TraceProperties Folder", TraceflowPreferences.TracePropertiesFolder);
   }


   public EnumKey valueOf(String keyName) {
      EnumKey parameter;
      try {
         parameter = TraceflowPreferences.valueOf(keyName);
      } catch (IllegalArgumentException ex) {
         parameter = null;
      }
      return parameter;
   }


   public String getPropertiesFilename() {
      return CLIENT_PROPERTIES_FILENAME;
   }

   /**
    * INSTANCE VARIABLES
    */
   private static final String CLIENT_PROPERTIES_FILENAME = "trace-flow.properties";

}
