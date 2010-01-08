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

package org.ancora.MbTraceAnalyser.TraceAlgorithm;

import org.ancora.MbTraceAnalyser.DataObjects.InstructionFlow;
import org.ancora.MbTraceAnalyser.*;
import java.io.File;
import java.util.logging.Logger;
import org.ancora.SharedLibrary.IoUtils;
import org.ancora.SharedLibrary.IoUtils2;
import org.ancora.SharedLibrary.ParseUtils2;

/**
 * Builds a trace-flow from a trace.
 *
 * @author Joao Bispo
 */
public class TraceflowAlgorithm implements Comparable {

   private static void reset() {
      totalInstructions = 0;
      instructionsInLoops = 0;
      loopsIterations = 0;
      numberOfLoops = 0;
   }

   private static boolean exit(boolean result) {
      reset();
      return result;
   }


   private static boolean saveOutput(InstructionFlow instructionFlow, File outputFile) {
      if (instructionFlow.getRecurrence() > 1) {
         // Show Statistics
         //System.out.println("Flow with recurrence " + instructionFlow.getRecurrence()+". Size: "+instructionFlow.getTotalInstructions());
         instructionsInLoops += (instructionFlow.getTotalInstructions());
         loopsIterations += instructionFlow.getRecurrence();
         numberOfLoops++;
      }

      // Test addresses of instruction flow, to check if they all are forward jumps.
      for(int i=1; i<instructionFlow.getSize(); i++) {
         long firstInst = instructionFlow.getAddress(i-1);
         long secondInst = instructionFlow.getAddress(i);

         if(firstInst > secondInst) {
            String firstInstString = ParseUtils2.toHexString(firstInst, 8);
            String secondInstString = ParseUtils2.toHexString(secondInst, 8);
         
            Logger.getLogger(TraceflowAlgorithm.class.getName()).
                    warning("Violated Forward Branch. "+firstInstString+"->"+secondInstString);
         }
      }
      
      totalInstructions += instructionFlow.getTotalInstructions();
      return IoUtils2.append(outputFile, instructionFlow.toString());
   }

   /**
    * Builds the trace-flow from the trace in traceFile, writes the results to
    * outputFile.
    *
    * @param traceFile the trace file
    * @param outputFile the output file
    * @return true if operation is successful, false otherwise.
    */
public static boolean doTraceFlowV1(File traceFile, File outputFile) {
   // Open trace file
   MicroblazeTraceReader reader = MicroblazeTraceReader.createTraceReader(traceFile);

   // Delete contents of outputfile
   IoUtils.write(outputFile, "");

   InstructionFlow recurringTrace = null;
      InstructionFlow currentTrace = new InstructionFlow();
      MicroblazeTraceInstruction currentInst = reader.nextInstruction();
      MicroblazeTraceInstruction nextInst = reader.nextInstruction();

      currentTrace.addAddress(currentInst.getInstructionAddress());

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
                     if(!saveOutput(recurringTrace, outputFile)) {
                        return exit(false);
                     }
                     // Reset recurring trace
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
                     if(!saveOutput(recurringTrace, outputFile)) {
                        return exit(false);
                     }
                     recurringTrace = null;
                  }
                  // Save current trace to file
                     if(!saveOutput(currentTrace, outputFile)) {
                        return exit(false);
                     }
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
         // Save recurring trace to file
         if (!saveOutput(recurringTrace, outputFile)) {
            return exit(false);
         }
         recurringTrace = null;
      }

      if (currentTrace != null) {
         // Save current trace to file
         if (!saveOutput(currentTrace, outputFile)) {
            return exit(false);
         }
         currentTrace = null;
      }


      // Show Statistics
      System.out.println("Total Instructions:"+totalInstructions);

      return true;
   }

   /**
    * Builds the trace-flow from the trace in traceFile, writes the results to
    * outputFile.
    *
    * @param traceFile the trace file
    * @param outputFile the output file
    * @return true if operation is successful, false otherwise.
    */
   public static boolean doTraceFlowV1_1(File traceFile, File outputFile) {
      // Open trace file
      MicroblazeTraceReader reader = MicroblazeTraceReader.createTraceReader(traceFile);

      // Delete contents of outputfile
      IoUtils.write(outputFile, "");

      InstructionFlow recurringTrace = null;
      InstructionFlow currentTrace = new InstructionFlow();
      MicroblazeTraceInstruction currentInst = reader.nextInstruction();
      MicroblazeTraceInstruction nextInst = reader.nextInstruction();

      currentTrace.addAddress(currentInst.getInstructionAddress());

      boolean considerMergingFlow = false;

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
               if (recurringTrace != null) {
                  // If merging conditions have not met, check if they met now
                  // They met when the current trace is not equal to the
                  //recurrence trace, and when recurring trace has recurrence == 1
                  if (!considerMergingFlow) {

                     // check if current trace does not follow recurrent trace
                     int index = currentTrace.getSize() - 1;
                     boolean addressesAreEqual = recurringTrace.isSameAddress(nextInstAddress, index);
                     if (!addressesAreEqual) {
                        if (recurringTrace.getRecurrence() == 1) {
                           // Instead of saving recurring trace, keep track of
                           // following branches until a backwards branch happens.
                           considerMergingFlow = true;
                        } else {
                           // Save recurring trace to file
                           if (!saveOutput(recurringTrace, outputFile)) {
                              return exit(false);
                           }
                           // Reset recurring trace
                           recurringTrace = null;
                        }
                     }
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
                     if(considerMergingFlow) {
                        considerMergingFlow = false;

                        // Here is the place to do some analysis to recurring
                        // trace to decide if we continue with it or instead,
                        // we save it and promote currentTrace to recurrentTrace.

                        boolean condition = false;
                        
                        // Example condition: 100 basic blocks 
                        if((recurringTrace.getSize() + currentTrace.getSize()) > 100) {
                           condition = true;
                           Logger.getLogger(TraceflowAlgorithm.class.getName()).
                                   info("Recurring Trace failed condition (is bigger than 100 Basic Blocks).");
                        }
                        
                        if (condition) {
                           // Save recurring trace to file
                           if (!saveOutput(recurringTrace, outputFile)) {
                              return exit(false);
                           }
                           // Promote current trace to recurrent trace
                           recurringTrace = currentTrace;
                        } else {
                           recurringTrace.addInstructionFlow(currentTrace);
                        }

                     } else {
                        recurringTrace.incrementRecurrence();
                     }

                  }
               }
               else {
                  if(recurringTrace != null) {
                     // Save recurring trace to file
                     if(!saveOutput(recurringTrace, outputFile)) {
                        return exit(false);
                     }
                     recurringTrace = null;
                  }
                  // Save current trace to file
                     if(!saveOutput(currentTrace, outputFile)) {
                        return exit(false);
                     }
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
         // Save recurring trace to file
         if (!saveOutput(recurringTrace, outputFile)) {
            return exit(false);
         }
         recurringTrace = null;
      }

      if (currentTrace != null) {
         // Save current trace to file
         if (!saveOutput(currentTrace, outputFile)) {
            return exit(false);
         }
         currentTrace = null;
      }


      // Show Statistics
      System.out.println("Total Instructions:"+totalInstructions);

      return true;
   }

    /**
    * Builds the trace-flow from the trace in traceFile, writes the results to
    * outputFile.
     *
     * <p> Previous algorithms had a bug: they did not account for instructions
     * with delay slots.
    *
    * @param traceFile the trace file
    * @param outputFile the output file
    * @return true if operation is successful, false otherwise.
    */
public static boolean doTraceFlowV1_2(File traceFile, File outputFile) {
   // Open trace file
   MicroblazeTraceReader reader = MicroblazeTraceReader.createTraceReader(traceFile);

   // Delete contents of outputfile
   IoUtils.write(outputFile, "");

   InstructionFlow recurringTrace = null;
      InstructionFlow currentTrace = new InstructionFlow();
      MicroblazeTraceInstruction currentInst = reader.nextInstruction();
      MicroblazeTraceInstruction nextInst = reader.nextInstruction();

      currentTrace.addAddress(currentInst.getInstructionAddress());

      while(nextInst != null) {
         currentTrace.incrementInstruction();

         // Check if instruction is a branch
         if(currentInst.isBranch()) {
            //String branchAddress = ParseUtils2.toHexString(currentInst.getInstructionAddress(), 8);
            //String destinationAddress;
            //String delaySlotAddress = null;

            //System.err.println("Branch:"+ParseUtils2.toHexString(currentInst.getInstructionAddress(), 8));
            long nextInstAddress = nextInst.getInstructionAddress();
          
            // Check if branch has a delay slot
            if(currentInst.hasDelaySlot()) {
               //System.err.println("Delay Slot:"+ParseUtils2.toHexString(nextInstAddress,8));
               currentTrace.incrementInstruction();
               nextInst = reader.nextInstruction();
               nextInstAddress = nextInst.getInstructionAddress();
            }
            // currentTrace.incrementInstruction
            // nextInstAddress = nextInst.getInstructionAddress()


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
                     if(!saveOutput(recurringTrace, outputFile)) {
                        return exit(false);
                     }
                     // Reset recurring trace
                     recurringTrace = null;
                  }
               }
            }
            else { // Is a backward branch
               //System.err.println("Backward Branch:----------------------");
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
                     if(!saveOutput(recurringTrace, outputFile)) {
                        return exit(false);
                     }
                     recurringTrace = null;
                  }
                  // Save current trace to file
                     if(!saveOutput(currentTrace, outputFile)) {
                        return exit(false);
                     }
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
         // Save recurring trace to file
         if (!saveOutput(recurringTrace, outputFile)) {
            return exit(false);
         }
         recurringTrace = null;
      }

      if (currentTrace != null) {
         // Save current trace to file
         if (!saveOutput(currentTrace, outputFile)) {
            return exit(false);
         }
         currentTrace = null;
      }

      // Show Statistics
      showStats();


      return exit(true);
   }

   private static void showStats() {
      // ratio of instructions in loop vs total instructions
      float ratioInstructionsInLoops = (float) instructionsInLoops / (float) totalInstructions;
      // Average instructions per loop
      float averageLoopSize = (float) instructionsInLoops / (float) loopsIterations;
      // Average iterations in loops
      float averageIterationsInLoop = (float) loopsIterations / (float) numberOfLoops;
      //System.out.println(instructionsInLoops);
      //System.out.println(loopsIterations);
      System.out.println("Total Instructions:" + totalInstructions);
      System.out.println("Loop Instructions/Total Instructions:" + ratioInstructionsInLoops);
      System.out.println("Average Instructions in Loops:" + averageLoopSize);
      System.out.println("Average Iterations in Loops:" + averageIterationsInLoop);
      System.out.println("Number of Loops:" + numberOfLoops);

   }


   ///
   // INSTANCE VARIABLE
   ///
   private static int totalInstructions = 0;
   private static int instructionsInLoops = 0;
   private static int loopsIterations = 0;
   private static int numberOfLoops = 0;

   public int compareTo(Object o) {
      throw new UnsupportedOperationException("Not supported yet.");
   }



}
