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

package org.ancora.DMExplorer.Plugins;

import java.util.logging.Logger;
import org.ancora.DMExplorer.Plugins.Dummies.DummyMapper;
import org.ancora.DmeFramework.Interfaces.Mapper;
import org.ancora.Mappers.InfiniteMapper.IfmMapper;


/**
 * Name of the supported mappers.
 *
 * @author Joao Bispo
 */
public enum MapperName {
   dummyrpu,
   infiniteforwardmatrix;

   public String getHelpMessage() {
      switch (this) {
         case dummyrpu:
            return "Does nothing with the received operations.";
            //return "All operations are mapped to a single universal FU which executes" +
            //        " each operation in one cycle.";
         case infiniteforwardmatrix:
            return "Maps operations to an infinite forward matrix.";
         default:
            return "Message Not Defined";
      }
   }

   public String getName() {
      switch (this) {
         case dummyrpu:
            return "DummyRPU";
         case infiniteforwardmatrix:
            return IfmMapper.NAME;
         default:
            return "Name Not Defined";
      }
   }

   public Mapper getMapper() {
      switch (this) {
         case dummyrpu:
            return new DummyMapper();
         case infiniteforwardmatrix:
            return new IfmMapper();
         default:
             Logger.getLogger(MapperName.class.getName()).
                 info("Mapper Constructor for '"+this.getName()+"' not defined.'");
            return null;
      }
   }
}
