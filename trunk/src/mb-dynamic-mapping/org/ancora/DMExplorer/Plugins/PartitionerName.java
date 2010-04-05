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
import org.ancora.DMExplorer.Global;
import org.ancora.DMExplorer.Plugins.Dummies.DummyPartitioner;
import org.ancora.DmeFramework.Interfaces.Partitioner;
import org.ancora.Partitioners.BasicBlock.BasicBlock;
import org.ancora.Partitioners.BasicBlockLoop.BasicBlockLoop;
import org.ancora.Partitioners.SuperBlock.SuperBlock;
import org.ancora.Partitioners.SuperBlockLoop.SuperBlockLoop;
import org.ancora.Partitioners.MegaBlockLoop.MegaBlockLoop;

/**
 * Name of the supported partitioners.
 *
 * @author Joao Bispo
 */
public enum PartitionerName {
   dummypartitioner,
   basicblock,
   superblock,
   megablockloop,
   superblockloop,
   basicblockloop;

  public String getHelpMessage() {
      switch(this) {
         case dummypartitioner:
            return "Outputs blocks with one instruction, not mappable on custom hardware";
         case basicblock:
            return "Builds BasicBlocks, mappable on custom hardware. Inspects" +
                    " the content of the instruction to determine the limits of the BasicBlock";
         case superblock:
            return "Builds SuperBlocks, mappable on custom hardware. Inspects " +
                    "the content of the instruction to determine the limits of BasicBlocks," +
                    " which are used to build SuperBlocks.";
         case megablockloop:
            return "Builds SuperBlockLoops, mappable on custom hardware. Inspects " +
                    "the content of the instruction to determine the limits of BasicBlocks " +
                    " and SuperBlocks, which are used to build SuperBlockLoops.";
         case superblockloop:
            return "Builds sequences of the same SuperBlock. Inspects " +
                    "the content of the instruction to determine the limits of BasicBlocks " +
                    " and SuperBlocks.";
         case basicblockloop:
            return "Builds sequences of the same BasicBlock. Inspects " +
                    "the content of the instruction to determine the limits of BasicBlocks " +
                    " and SuperBlocks";
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
         case superblock:
            return SuperBlock.NAME;
         case megablockloop:
            return MegaBlockLoop.NAME;
         case superblockloop:
            return SuperBlockLoop.NAME;
         case basicblockloop:
            return BasicBlockLoop.NAME;
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
         case superblock:
            return new SuperBlock();
         case megablockloop:
            return new MegaBlockLoop(Global.maxPatternSize);
         case superblockloop:
            return new SuperBlockLoop();
         case basicblockloop:
            return new BasicBlockLoop();
         default:
             Logger.getLogger(PartitionerName.class.getName()).
                 info("Partitioner Constructor for '"+this.getName()+"' not defined.'");
            return null;
      }
   }
}
