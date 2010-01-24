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

package org.ancora.MbTraceAnalyser;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.IoUtils;

/**
 *
 * @author Joao Bispo
 */
public class TraceProperties {

   public TraceProperties(File tracePropertiesFile) {
      properties = IoUtils.loadProperties(tracePropertiesFile);
   }

   public String getValue(Key key) {
      return properties.getProperty(key.name());
   }

   public float getCpi() {
      String key = Key.cpi.name();
      String cpiString = properties.getProperty(key);
      if(cpiString == null) {
         Logger.getLogger(TraceProperties.class.getName()).
             warning("TraceProperties does not have a '"+key+"' value. Returning 0.");
         return 0;
      }

      return Float.parseFloat(cpiString);
   }

   public int getInstructions() {
      String key = Key.instructions.name();
      String instString = properties.getProperty(key);
      if(instString == null) {
         Logger.getLogger(TraceProperties.class.getName()).
             warning("TraceProperties does not have a '"+key+"' value. Returning 0.");
         return 0;
      }

      return Integer.parseInt(instString);
   }

   public enum Key {
      instructions,
      cycles,
      cpi
   }

   /**
    * INSTANCE VARIABLES
    */
   final private Properties properties;
}
