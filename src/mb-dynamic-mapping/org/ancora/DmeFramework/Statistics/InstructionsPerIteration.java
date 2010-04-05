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

package org.ancora.DmeFramework.Statistics;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;
import org.ancora.DmeFramework.DataHolders.InstructionBlock;
import org.ancora.DmeFramework.System.MicroBlazeRpuMonitor;
import org.ancora.SharedLibrary.IoUtils;

/**
 *
 * @author Joao Bispo
 */
public class InstructionsPerIteration {

   public InstructionsPerIteration() {
      maxIterations = 0;
      dataSets = new ArrayList<CoverageData>();
   }

   public void addData(String trace, String partition, MicroBlazeRpuMonitor monitor) {
      // Create new object to store the data
      CoverageData data = new CoverageData(trace, partition);

      // Prepare list with RpuExecutions
      //List<InstructionBlock> instructionBlocks = new ArrayList(monitor.getInstructionBlockPartitions());

      // Build list with iterations
      //SortedSet<Integer> iterationsSet = new TreeSet<Integer>();
      for(InstructionBlock block : monitor.getInstructionBlockPartitions()) {
         data.addBlock(block);
         maxIterations = Math.max(maxIterations, block.getIterations());
         //iterationsSet.add(block.getIterations());

         //if(block.getIterations() > 1) {
         //   System.out.println(data.getName());
         //   System.out.println(block.getIterations());
         //}
      }

      dataSets.add(data);
      //System.out.println("Max Iterations:"+maxIterations);
      //System.out.println(Arrays.toString(data.getTraceCoverageArray(maxIterations)));
      //System.out.println(data.instructionsPerIteration);
      //System.out.println(data.iterationSizes);
      // Save maximum iteration
      //
      
   }

   public void saveTraceCoverageData(File instPerItFile) {
      StringBuilder builder;
      // Write Header
      builder = new StringBuilder();
      // Empty column + iteration numbers
      for(int i=0; i<maxIterations; i++) {
         builder.append(",");
         builder.append(i+1);
      }
      builder.append("\n");

      // Iterate over each data set
      for(CoverageData data : dataSets) {
         builder.append(data.getName());
         for(double value : data.getTraceCoverageArray(maxIterations)) {
            builder.append(",");
            builder.append(value);
         }
         builder.append("\n");
      }

      IoUtils.write(instPerItFile, builder.toString());
   }

   public void saveIterationSizeData(File instPerItFile) {
      StringBuilder builder;
      // Write Header
      builder = new StringBuilder();

      // Build set with iterations
      SortedSet<Integer> iterations = new TreeSet<Integer>();
      for(CoverageData data : dataSets) {
         iterations.addAll(data.iterationSizes.keySet());
      }

      // Empty column + iteration numbers
      for(Integer iteration : iterations) {
         builder.append(",");
         builder.append(iteration);
      }
      builder.append("\n");

      addPartitionerAverage("BasicBlockIterations", builder, iterations);
      addPartitionerAverage("SuperBlockIterations", builder, iterations);
      addPartitionerAverage("SuperBlockLoop", builder, iterations);
      // Iterate over each data set
      /*
      for(CoverageData data : dataSets) {
         builder.append(data.getName());
         for(Integer iteration : iterations) {
            builder.append(",");

            // Try to get value
            List<Integer> sizes = data.iterationSizes.get(iteration);
            if(sizes == null) {
               continue;
            }

            // Calculate average
            double sizeAverage = calcAverage(sizes);
            builder.append(sizeAverage);
         }
         builder.append("\n");
      }
       */

      IoUtils.write(instPerItFile, builder.toString());
   }

   private double calcAverage(List<Integer> sizes) {
      int acc = 0;
      for(Integer value : sizes) {
         acc += value;
      }

      return (double)acc / (double)sizes.size();
   }

   private double calcAverageDouble(List<Double> sizes) {
      double acc = 0;
      for(Double value : sizes) {
         acc += value;
      }

      return acc / (double)sizes.size();
   }

   private void addPartitionerAverage(String partitionerName, StringBuilder builder, SortedSet<Integer> iterations) {
      builder.append(partitionerName);

      // Collect datasets of given partitioners
      List<CoverageData> partitionerData = new ArrayList<CoverageData>();
      for(CoverageData data : dataSets) {
         if(data.partitioner.equals(partitionerName)) {
            partitionerData.add(data);
         }
      }

      // Collect values for each iteration
      for(Integer iteration : iterations) {
         builder.append(",");

         // Collect sizes
         List<Double> sizesAverage = new ArrayList<Double>();
         for(CoverageData data : partitionerData) {
            List<Integer> sizes = data.iterationSizes.get(iteration);
            if(sizes != null) {
               sizesAverage.add(calcAverage(sizes));
            }
         }

         // Add value if sizesAverage > 0
         if(sizesAverage.size() > 0) {
            builder.append(calcAverageDouble(sizesAverage));
         }
      }

      builder.append("\n");
   }

   /**
    * INSTANCE VARIABLES
    */
   private int maxIterations;
   private List<CoverageData> dataSets;





   class CoverageData {

      public CoverageData(String trace, String partitioner) {
         this.trace = trace;
         this.partitioner = partitioner;
         totalInstructions = 0;
         instructionsPerIteration = new Hashtable<Integer, Integer>();
         iterationSizes = new Hashtable<Integer, List<Integer>>();
      }

      private void addBlock(InstructionBlock block) {
         int blockInstructions = block.getTotalInstructions();
         totalInstructions += blockInstructions;

         int iteration = block.getIterations();
         
         // Fill iteration coverage data
         Integer iterationInstructions = instructionsPerIteration.get(iteration);
         if(iterationInstructions == null) {
            instructionsPerIteration.put(iteration, blockInstructions);
         }
         else {
            instructionsPerIteration.put(iteration, iterationInstructions+blockInstructions);
         }

         // Fill iteration size data
         List<Integer> iterationSize = iterationSizes.get(iteration);
         if(iterationSize == null) {
            iterationSize = new ArrayList<Integer>();
            iterationSizes.put(iteration, iterationSize);
         }
         iterationSize.add(block.getInstructions().length);
      }

      String getName() {
         return trace+"-"+partitioner;
      }

      double[] getTraceCoverageArray(int size) {
         double[] results = new double[size];

         int currentTotal = totalInstructions;
         Collection<Integer> iterations = instructionsPerIteration.keySet();
         SortedSet<Integer> iterationsSet = new TreeSet<Integer>(iterations);
         int arrayIndex = 0;

         // Check if there is blocks with only one iteration
         if(iterationsSet.first() != 1) {
            Logger.getLogger(InstructionsPerIteration.class.getName()).
                    warning("Not Supported - This trace does not have a block with " +
                    "only one iteration.");
            return results;
         }

         // Main Loop
         while(iterationsSet.size() > 0) {
            int currentIndex = iterationsSet.first();
            results[arrayIndex] = (double)currentTotal/(double)totalInstructions;
            arrayIndex++;

            //System.out.println("Added "+currentTotal+" to index "+(arrayIndex-1));

            currentTotal -= instructionsPerIteration.get(currentIndex);
            int nextIndex = getNextIndex(currentIndex, iterationsSet);

            //System.out.println("Old Set:"+iterationsSet);
            iterationsSet = iterationsSet.tailSet(nextIndex);
            //System.out.println("New Subset:"+iterationsSet);

            // Get number of fill-ins
            int numberOfFillIns = nextIndex - currentIndex;
            int previousValue = arrayIndex;
            for (int i = 1; i < numberOfFillIns; i++) {
               results[arrayIndex] = (double)currentTotal/(double)totalInstructions;
               arrayIndex++;
            }
            //System.out.println("Number of FillsIns:"+numberOfFillIns+"; Added "+(arrayIndex-previousValue)+" values.");


         }

         return results;
      }

      /**
       * If returns -1, there is no more indexes.
       * 
       * @param iterationsSet
       * @return
       */
      private int getNextIndex(int currentIndex, SortedSet<Integer> iterationsSet) {
         if(iterationsSet.size() > 1) {
            return iterationsSet.tailSet(currentIndex+1).first();
         }

         return maxIterations+1;
         /*
         if(currentIndex < maxIterations) {
            return maxIterations;
         } else {
            return maxIterations+1;
         }
          */

      }

      // INSTANCE VARIABLES
      String trace;
      String partitioner;
      int totalInstructions;
      Map<Integer,Integer> instructionsPerIteration;
      Map<Integer,List<Integer>> iterationSizes;



   }

}
