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

package org.ancora.MbDynamicMapping.Options;

import java.util.logging.Logger;
import org.ancora.MbDynamicMapping.Interface.Mapper;
import org.ancora.MbDynamicMapping.Mappers.CcaMapper;
import org.ancora.MbDynamicMapping.Mappers.CcaMapperOld;
import org.ancora.MbDynamicMapping.Mappers.NoCustomHardware;

/**
 * Name of the supported mappers.
 *
 * @author Joao Bispo
 */
public enum MapperName {
   nocustomhardware,
   infinitecca;

   public String getHelpMessage() {
      switch (this) {
         case nocustomhardware:
            return "Maps all instruction to the MicroBlaze Processor.";
         case infinitecca:
            return "Maps hardware instruction to an infinite CCA.";
         default:
            return "Message Not Defined";
      }
   }

   public String getName() {
      switch (this) {
         case nocustomhardware:
            return "NoCustomHardware";
         case infinitecca:
            return "InfiniteCCA";
         default:
            return "Name Not Defined";
      }
   }

   public Mapper getMapper() {
      switch (this) {
         case nocustomhardware:
            return new NoCustomHardware();
         case infinitecca:
            //return new CcaMapperOld();
            return new CcaMapper();
         default:
             Logger.getLogger(MapperName.class.getName()).
                 info("Mapper Constructor for '"+this.getName()+"' not defined.'");
            return null;
      }
   }
}
