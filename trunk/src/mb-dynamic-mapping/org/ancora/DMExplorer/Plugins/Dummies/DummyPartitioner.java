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

package org.ancora.DMExplorer.Plugins.Dummies;

import org.ancora.DMExplorer.Plugins.PartitionerName;
import org.ancora.DmeFramework.DataHolders.InstructionBlock;
import org.ancora.DmeFramework.Interfaces.Partitioner;
import org.ancora.MicroBlaze.Instructions.Instruction;

/**
 * Dummy Partitioner which only output lines which can only be mapped in the
 * MicroBlaze Processor.
 *
 * @author Joao Bispo
 */
public class DummyPartitioner extends Partitioner {

   @Override
   public String getName() {
      return PartitionerName.dummypartitioner.getName();
   }

   @Override
   public void accept(Instruction instruction) {
      // Build InstructionBlock using only one instruction
      InstructionBlock iBlock = new InstructionBlock(instruction);
      // Use the instruction address as the hash
      iBlock.setHash(instruction.getAddress());
      noticeListeners(new InstructionBlock(instruction));
   }

   @Override
   public void flush() {
      flushListeners();
   }

}
