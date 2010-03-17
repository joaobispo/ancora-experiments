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
import org.ancora.DMExplorer.Options.TraceProperties;
import org.ancora.SharedLibrary.ParseUtils;

/**
 *
 * @author Joao Bispo
 */
public class Utils {

   public static TraceProperties getTraceProperties(File trace) {
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
