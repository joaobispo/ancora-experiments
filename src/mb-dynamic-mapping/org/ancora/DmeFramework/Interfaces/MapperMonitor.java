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

package org.ancora.DmeFramework.Interfaces;

/**
 * Statistics about the mapping
 *
 * @author Joao Bispo
 */
public interface MapperMonitor {

   int getMaxIlp();

   int getLiveIn();

   int getLiveOut();

   /**
    * @return number of IR operations mapped
    */
   int getMappedOperations();

   /**
    * @return number of RPU elements used
    */
   int getMappedElements();

   int getCycles();

   /**
    * Builds a copy of this MapperMonitor.
    *
    * @return a MapperMonitor
    */
   MapperMonitor copy();
}
