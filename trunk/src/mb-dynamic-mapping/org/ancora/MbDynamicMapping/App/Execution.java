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

import org.ancora.MbDynamicMapping.Options.PartitionerName;
import org.ancora.MbDynamicMapping.Options.MapperName;
import java.io.File;
import java.util.logging.Logger;
import org.ancora.MbDynamicMapping.Interface.Mapper;
import org.ancora.MbDynamicMapping.Interface.Partitioner;

/**
 * Contains the data for an execution of dynamic mapping.
 *
 * @author Joao Bispo
 */
public class Execution {

   public Execution(File trace, String partitioner, String mapper) {
      this.trace = trace;
      this.mapperName = mapper;
      this.partitionerName = partitioner;
      this.partitioner = null;
      this.mapper = null;
   }

   public Mapper getMapper() {
      if(mapper == null) {
         try {
            MapperName map = MapperName.valueOf(mapperName.toLowerCase());
            mapper = map.getMapper();
         } catch (IllegalArgumentException ex) {
            Logger.getLogger(Execution.class.getName()).
                    info("Invalid Mapper Block '" + mapperName + "'");
            return null;
         }
      }
      
      return mapper;
   }

   
   public Partitioner getPartitioner() {
      if (partitioner == null) {
         try {
            PartitionerName part = PartitionerName.valueOf(partitionerName.toLowerCase());
            partitioner = part.getPartitioner();
         } catch (IllegalArgumentException ex) {
            Logger.getLogger(ExecutionParser.class.getName()).
                    info("Invalid Partitioner Block '" + partitionerName + "'");
            return null;
         }
      }
      return partitioner;
   }

   public File getTrace() {
      return trace;
   }

   @Override
   public String toString() {
      return "File '"+trace.getName()+"', Partitioner '"+getPartitioner().getName()+"'" +
              ", Mapper '"+getMapper().getName()+"'";
   }

   

   /**
    * INSTANCE VARIABLES
    */
   private final File trace;
   private Partitioner partitioner;
   private Mapper mapper;

   private final String mapperName;
   private final String partitionerName;
}
