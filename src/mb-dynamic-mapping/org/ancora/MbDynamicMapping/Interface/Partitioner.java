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

package org.ancora.MbDynamicMapping.Interface;

import java.util.List;

/**
 * Partitions incoming instructions in blocks which will be executed in the
 * MicroBlaze or in custom hardware.
 *
 * @author Joao Bispo
 */
public abstract class Partitioner implements InstructionListener {

   public void addListener(InstructionBlockListener listener) {
      listeners.add(listener);
   }

   protected void noticeListeners(InstructionBlock instructionBlock) {
      for(int i=0; i<listeners.size(); i++) {
         listeners.get(i).accept(instructionBlock);
      }
   }


   /**
    * INSTANCE VARIABLES
    */
   private List<InstructionBlockListener> listeners;
}
