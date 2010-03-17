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
import org.ancora.DMExplorer.Plugins.Dummies.DummyPartitioner;
import org.ancora.DmeFramework.Interfaces.Partitioner;
import org.ancora.PartitionerBasicBlock.BasicBlock;

/**
 * Name of the supported partitioners.
 *
 * @author Joao Bispo
 */
public enum PartitionerName {
   dummypartitioner,
   basicblock;

  public String getHelpMessage() {
      switch(this) {
         case dummypartitioner:
            return "Outputs blocks with one instruction, not mappable on custom hardware";
         case basicblock:
            return "Builds BasicBlocks, mappable on custom hardware. Inspects" +
                    " the content of the instruction to determine the limits of the BasicBlock";
         default:
            return "Message Not Defined";
      }
  }

   public String getName() {
      switch (this) {
         case dummypartitioner:
            return "DummyPartitioner";
         case basicblock:
            return BasicBlock.NAME;
         default:
            return "Name Not Defined";
      }
   }

   public Partitioner getPartitioner() {
      switch (this) {
         case dummypartitioner:
            return new DummyPartitioner();
         case basicblock:
            return new BasicBlock();
         default:
             Logger.getLogger(PartitionerName.class.getName()).
                 info("Partitioner Constructor for '"+this.getName()+"' not defined.'");
            return null;
      }
   }
}
