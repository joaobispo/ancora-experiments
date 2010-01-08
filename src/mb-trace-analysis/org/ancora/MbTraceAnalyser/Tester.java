/*
 *  Copyright 2009 Ancora Research Group.
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

package org.ancora.MbTraceAnalyser;

import org.ancora.MbTraceAnalyser.DataObjects.InstructionFlow;
import org.ancora.MbTraceAnalyser.PatternFinder.PatternFinderWithCicle;
import org.ancora.MbTraceAnalyser.TraceAlgorithm.TraceflowAlgorithm;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.ancora.SharedLibrary.BitUtils;
import org.ancora.SharedLibrary.DataStructures.RotatingQueue;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.LoggingUtils;
import org.ancora.SharedLibrary.ParseUtils2;


/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class Tester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        init();

        //testTraceReader();
        //testAlgorithm();

        //testReferencingPassingAndNull();
        //testReferencingPassingAndNull2();

        //testFlowAlgortihm();
        //testHashFunction();

        //testHashes();

        //testRotatingQueue();
        testFindPatterns();
    }

   private static void init() {
      LoggingUtils.setupConsoleOnly();
   }

   private static void testTraceReader() {
      String filename;
      filename = "./traces/fdct_trace_without_optimization.txt";

      File traceFile = IoUtils.existingFile(filename);

      MicroblazeTraceReader reader = MicroblazeTraceReader.createTraceReader(traceFile);

      MicroblazeTraceInstruction instruction = reader.nextInstruction();

      System.out.println("1:"+instruction);

      while(instruction != null) {
         System.out.println(instruction);
         instruction = reader.nextInstruction();
      }
   }

   private static void testAlgorithm() {
      String filename;
      filename = "./traces/fdct_trace_without_optimization.txt";

      File traceFile = IoUtils.existingFile(filename);

      MicroblazeTraceReader reader = MicroblazeTraceReader.createTraceReader(traceFile);


      File outputFile = new File("test.txt");

      // Delete contents of outputfile
      IoUtils.write(outputFile, "");


      InstructionFlow recurringTrace = null;
      InstructionFlow currentTrace = new InstructionFlow();
      MicroblazeTraceInstruction currentInst = reader.nextInstruction();
      MicroblazeTraceInstruction nextInst = reader.nextInstruction();

      currentTrace.addAddress(currentInst.getInstructionAddress());

      int totalInstructions = 0;

      while(nextInst != null) {
         currentTrace.incrementInstruction();

         // Check if instruction is a branch
         if(currentInst.isBranch()) {
            long nextInstAddress = nextInst.getInstructionAddress();
            // Check if instruction is a forward branch
            boolean forwardBranch = currentInst.getInstructionAddress() < nextInstAddress;
            if(forwardBranch) {
               // Add address to current trace
               currentTrace.addAddress(nextInstAddress);

               // Check recurrence trace
               if(recurringTrace != null) {
                  // check if current trace does not follow recurrent trace
                  int index = currentTrace.getSize() - 1;
                  if(!recurringTrace.isSameAddress(nextInstAddress, index)) {
                     // Save recurring trace to file
                     totalInstructions += recurringTrace.getTotalInstructions();
                     IoUtils.append(outputFile, recurringTrace.toString());
                     recurringTrace = null;
                  }
               }
            }
            else { // Is a backward branch
               // Check if next inst address is the same as the address of
               // the current trace
               boolean sameStartAddress = nextInstAddress == currentTrace.getAddress(0);
               if(sameStartAddress) {
                  if(recurringTrace == null) {
                     recurringTrace = currentTrace;
                  } else {
                     recurringTrace.incrementRecurrence();
                  }
               }
               else {
                  if(recurringTrace != null) {
                     // Save recurring trace to file
                     totalInstructions += recurringTrace.getTotalInstructions();
                     IoUtils.append(outputFile, recurringTrace.toString());
                     recurringTrace = null;
                  }
                  // Save current trace to file
                  totalInstructions += currentTrace.getTotalInstructions();
                  IoUtils.append(outputFile, currentTrace.toString());
                  currentTrace = null;
               }

               currentTrace = new InstructionFlow();
               currentTrace.addAddress(nextInstAddress);
            }

         }



         currentInst = nextInst;
         nextInst = reader.nextInstruction();
      }

      if(recurringTrace != null) {
         totalInstructions += recurringTrace.getTotalInstructions();
         IoUtils.append(outputFile, recurringTrace.toString());
      }

      if(currentTrace != null) {
         totalInstructions += currentTrace.getTotalInstructions();
         IoUtils.append(outputFile, currentTrace.toString());
      }

      System.out.println("Total Instructions:"+totalInstructions);
   }

   private static void testReferencingPassingAndNull() {
      // Create a reference
      Integer a = new Integer(3);
      // Pass reference to method which will nullify it
      nullify(a);
      // Check reference
      System.out.println(a);
   }

   private static void nullify(Integer a) {
      a = null;
   }
   
   private static void testReferencingPassingAndNull2() {
      nullify(b);
      System.out.println(b);
   }


   private static void testFlowAlgortihm() {
      String traceFilename;
      traceFilename = "../common/traces-without-optimization/fdct_trace_without_optimization.txt";

      String outputFoldername;
      outputFoldername = "./trace-flow";
      

      File traceFile = IoUtils.existingFile(traceFilename);
      File outputFolder = IoUtils.safeFolder(outputFoldername);

      String outputFilename = ParseUtils2.removeSuffix(traceFile.getName(), ".") + ".trace-flow";
      File outputFile = new File(outputFolder, outputFilename);

      // Verification
      
      if(traceFile == null || outputFile == null) {
         return;
      }

      TraceflowAlgorithm.doTraceFlowV1(traceFile, outputFile);
      
   }

   private static void testHashFunction() {
      
      long data = 13;
      int dataLength = 64;
      int hashedValue = dataLength;


      hashedValue = superFastHash(data, dataLength, hashedValue);

      System.out.println("Hashed Value:"+hashedValue);
   }

   private static void testGet16BitsAligned() {
      long data;

      long MASK_16_BITS = 0xFFFFL;

      data = MASK_16_BITS << 16*3;

      int i = BitUtils.get16BitsAligned(data, 3);
      System.out.println(Integer.toHexString(i));
   }

   /**
    *
    * @param data data to hash
    * @param dataLength length of the data, in bytes
    * @param hashedValue previous value of the hash. If it is the start of the
    * method, used the length of the data (ex.: 8 bytes).
    * @return
    */
   
   private static int superFastHash(long data, int len, int hash) {
      int tmp;
      //int rem;

      if (len <= 0) return 0;

      //rem = len & 3;
      len >>= 2;

      //Main Loop
      for(int i=0; i<4; i+=2) {
         // Get lower 16 bits
         hash += BitUtils.get16BitsAligned(data, i);
         // Calculate some random value with second-lower 16 bits
         tmp = (BitUtils.get16BitsAligned(data, i+1) << 11 ) ^ hash;
         hash = (hash << 16) ^ tmp;
         // At this point, it would advance the data, but since it is restricted
         // to longs (64-bit values), it is unnecessary).
         hash += hash >> 11;
      }

      // Handle end cases //
      // There are no end cases, main loop is done in chuncks of 32 bits.

    // Force "avalanching" of final 127 bits //
    hash ^= hash << 3;
    hash += hash >> 5;
    hash ^= hash << 4;
    hash += hash >> 17;
    hash ^= hash << 25;
    hash += hash >> 6;

      return hash;
   }


   private static Integer b = new Integer(5);

   private static void testHashes() {
      //int[] addresses1 = {284};
      //int[] addresses2 = {348};
      int[] addresses1 = {432, 488, 556, 592, 664, 736, 792, 820, 864, 888, 944, 972, 1004, 1044, 1084};
      int[] addresses2 = {472, 488, 556, 592, 664, 736, 792, 820, 864, 888, 944, 972, 1004, 1044, 1084};


      int hash1 = 4;
      int hash2 = 4;




      // Calculate hash 1
      for(int i=0; i<addresses1.length; i++) {
         hash1 = BitUtils.superFastHash(addresses1[i], hash1);
      }

      // Calculate hash 2
      for(int i=0; i<addresses2.length; i++) {
         hash2 = BitUtils.superFastHash(addresses2[i], hash2);
      }

      System.out.println("Hash1:"+hash1);
      System.out.println("Hash2:"+hash2);
      System.out.println("Are equal?:"+(hash1 ==hash2));
   }

   private static void testRotatingQueue() {
      RotatingQueue<Integer> queue = new RotatingQueue<Integer>(4);

      // Insert elements
      queue.insertElement(1);
      queue.insertElement(2);
      queue.insertElement(3);
      queue.insertElement(4);

      // Check order by which they are returned. It should be 4,3,2,1
      for(int i=0; i<queue.size(); i++) {
         System.out.println(queue.getElement(i));
      }
   }

   private static void testFindPatterns() {
      List<Integer> numbers = new ArrayList<Integer>();
      numbers.add(1);
      numbers.add(1);
      numbers.add(1);
      numbers.add(1);
      numbers.add(1);
      numbers.add(1);
      numbers.add(1);
      numbers.add(1);
      numbers.add(1);
      numbers.add(1);
      numbers.add(1);


      PatternFinderWithCicle.findPatterns(numbers);
   }





}
