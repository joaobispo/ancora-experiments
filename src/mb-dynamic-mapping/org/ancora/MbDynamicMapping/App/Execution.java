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

package org.ancora.MbDynamicMapping.App;

import java.io.File;
import org.ancora.MbDynamicMapping.Interface.Mapper;
import org.ancora.MbDynamicMapping.Interface.Partitioner;

/**
 * Contains the data for an execution of dynamic mapping.
 *
 * @author Joao Bispo
 */
public class Execution {

   public Execution(File trace, Partitioner partitioner, Mapper mapper) {
      this.trace = trace;
      this.partitioner = partitioner;
      this.mapper = mapper;
   }

   public Mapper getMapper() {
      return mapper;
   }

   public Partitioner getPartitioner() {
      return partitioner;
   }

   public File getTrace() {
      return trace;
   }

   

   /**
    * INSTANCE VARIABLES
    */
   private final File trace;
   private final Partitioner partitioner;
   private final Mapper mapper;
}
