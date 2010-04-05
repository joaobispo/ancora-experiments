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

package org.ancora.Partitioners.SuperBlockLoop;

import org.ancora.Partitioners.Shared.PatternFinderInfo;
import org.ancora.Partitioners.Shared.PatternFinder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.ancora.DmeFramework.DataHolders.InstructionBlock;
import org.ancora.DmeFramework.Interfaces.Base.InstructionBlockListener;
import org.ancora.DmeFramework.Interfaces.Base.InstructionBlockSource;
import org.ancora.MicroBlaze.Instructions.Instruction;
import org.ancora.SharedLibrary.BitUtils;

/**
 * Builds SuperBlocksLoops, iteratively.
 *
 * @author Joao Bispo
 */
public class SuperBlockLoopBuilder extends InstructionBlockSource implements InstructionBlockListener {


   public SuperBlockLoopBuilder(int maxPatternSize) {
      initCurrentLoop();
      initPatternLoop();

      // Init object state
      lastPatternSize = 0;
      state = SblBuilderState.LOOKING_FOR_PATTERN;
      patternFinder = new PatternFinder(maxPatternSize);

      //currentLoop = new ArrayList<Instruction>();
      //currentLoopHash = HASH_INITIAL_VALUE;

      //patternLoop = null;
      //patternLoopHash = HASH_INITIAL_VALUE;

      //patternSuperBlockLoop = null;
      //currentSuperBlockLoop = new SuperBlockLoop();


      //patternInfo = new PatternFinderInfo(0, PatternFinderInfo.PatternState.PATTERN_NOT_FOUND);
      

      //consumers = new ArrayList<SuperBlockLoopConsumer>();
   }

   @Override
   public void accept(InstructionBlock instructionBlock) {
      // Give instructions to pattern finder
      patternFinder.accept(instructionBlock);
      PatternFinderInfo patternInfo = patternFinder.getInfo();

      //System.out.println("Pattern Size:"+patternInfo.getPaternSize());
      //System.out.println("Pattern State:"+patternInfo.getPatternState());

      parseInstructionBlock(instructionBlock, patternInfo);

   }

   private void parseInstructionBlock(InstructionBlock instructionBlock, PatternFinderInfo patternInfo) {

      switch(state) {
         case LOOKING_FOR_PATTERN:
            // Check if there is a pattern
            if(patternInfo.getPaternSize() > 0) {
               // Flush current superblocks
               flushCurrentSuperBlockLoop();

               // Prepare Data
               state = SblBuilderState.BUILDING_PATTERN;
               lastPatternSize = patternInfo.getPaternSize();

               // Start processing pattern
               parseInstructionBlock(instructionBlock, patternInfo);
               return;
            }

            addBlockToCurrentLoop(instructionBlock);
            //currentSuperBlockLoop.addSuperBlock(superBlock);
            break;

         case BUILDING_PATTERN:
            // Check pattern size. If it has changed, we can interrupt the building
            // of the pattern.
            if(lastPatternSize != patternInfo.getPaternSize()) {
               flushCurrentSuperBlockLoop();

               state = SblBuilderState.LOOKING_FOR_PATTERN;
               lastPatternSize = 0;

               parseInstructionBlock(instructionBlock, patternInfo);
               //consumeSuperBlock(superBlock);

               return;
            }

            // Just add SuperBlocks to current until it has size equal to the pattern.
            addBlockToCurrentLoop(instructionBlock);
            //currentSuperBlockLoop.addSuperBlock(superBlock);

            if(currentLoopSize == lastPatternSize) {

               buildPatternLoop();
               initCurrentLoop();
               state = SblBuilderState.CHECKING_PATTERN;
            }

            /*
            if(currentSuperBlockLoop.getSuperBlockCount() ==  lastPatternSize) {
                  patternSuperBlockLoop = currentSuperBlockLoop;
                  currentSuperBlockLoop = new SuperBlockLoop();

                  state = SblBuilderState.CHECKING_PATTERN;
            }
             */
            break;

         case CHECKING_PATTERN:
            // First, check if incoming superblock is part of the found pattern
            //int index = currentSuperBlockLoop.getSuperBlockCount();
            int index = currentLoopSize;
            //int patternHash = patternSuperBlockLoop.getSuperBlocks().get(index).getHash();
            int patternHash = patternLoopHashes.get(index);

            // If there is a mismatch, flush pattern and go looking for new patterns.
            if(patternHash != instructionBlock.getHash()) {
               flushPatternSuperBlockLoop();

               state = SblBuilderState.LOOKING_FOR_PATTERN;
               lastPatternSize = 0;

               parseInstructionBlock(instructionBlock, patternInfo);
               //consumeSuperBlock(superBlock);
               return;
            }

            // There is no mismatch, add superblock to current.
            addBlockToCurrentLoop(instructionBlock);
            //currentSuperBlockLoop.addSuperBlock(superBlock);

            // If currentLoop is the same size as pattern, increment pattern.
            //if(currentSuperBlockLoop.getSuperBlockCount() == lastPatternSize) {
            if(currentLoopSize == lastPatternSize) {
               loopIterations++;
               //patternSuperBlockLoop.incrementIterations(1);
               initCurrentLoop();
               //currentSuperBlockLoop = new SuperBlockLoop();
            }
      }
   }

   /*
   public void addSuperBlockLoopConsumer(SuperBlockLoopConsumer comsumer) {
      consumers.add(comsumer);
   }
    */

   /*
   private void updateConsumers(SuperBlockLoop superBlockLoop) {
      // Send current pattern size to all listeners
      for(SuperBlockLoopConsumer consumer : consumers) {
         consumer.consumeSuperBlockLoop(superBlockLoop);
      }
   }
    */

   
   /**
    * Check if patternBlock needs update, and if yes updates it.
    */
   /*
   private void updatePatternBlock() {
      // Check if patternBlock needs update
      if (currentSuperBlockLoop.getSuperBlockCount() != lastPatternSize) {
         return;
      }
      
      // SuperBlockLoop has pattern size. Check if patternBlock is built.
      if(patternSuperBlockLoop == null) {
         patternSuperBlockLoop = currentSuperBlockLoop;
      } else {
         patternSuperBlockLoop.incrementIterations(1);
      }

      // "Erase" currentSuperBlockLoop
      currentSuperBlockLoop = new SuperBlockLoop();
   }
   */


   /**
    * Sends the current SuperBlockLoop to registred consumers and resets it.
    */
   private void flushCurrentSuperBlockLoop() {
      //if (currentSuperBlockLoop.getSuperBlockCount() == 0) {
      if (currentLoopSize == 0) {
         return;
      }

      //updateConsumers(currentSuperBlockLoop);

      // Build InstructionBlock
      //InstructionBlock newBlock =  new InstructionBlock(true, currentLoop);
      InstructionBlock newBlock =  new InstructionBlock(false, currentLoop);
      int loopHash = calcLoopHash(currentLoopHashes);
      newBlock.setHash(loopHash);
      // Send it to listeners
      noticeListeners(newBlock);
      //currentSuperBlockLoop = new SuperBlockLoop();
      initCurrentLoop();
   }

   /**
    * Sends the pattern SuperBlockLoop to registred consumers and resets it.
    */
   private void flushPatternSuperBlockLoop() {
      if(patternLoop != null) {
         Instruction[] blockInst = patternLoop.toArray(new Instruction[patternLoop.size()]);
         Instruction[] penalty = currentLoop.toArray(new Instruction[currentLoop.size()]);
         int loopHash = calcLoopHash(patternLoopHashes);

         // All blocks mappable
         //boolean mapToHardware = true;


         boolean mapToHardware = false;
         if(loopIterations > 1) {
            mapToHardware = true;
         }
          

         // Build InstructionBlock
         InstructionBlock newBlock = new InstructionBlock(mapToHardware, blockInst, penalty, loopIterations);
         newBlock.setHash(loopHash);
         // Send it to listeners
         noticeListeners(newBlock);
         //updateConsumers(patternSuperBlockLoop);
         //patternSuperBlockLoop = null;
         initPatternLoop();
      }
   }

   


   /*
   public void consumePatternSize(PatternFinderInfo patternInfo) {
      this.patternInfo = patternInfo;
   }
*/


   /**
    * Flush remaining SuperBlockLoops.
    */
   @Override
   public void flush() {
      flushPatternSuperBlockLoop();
      flushCurrentSuperBlockLoop();

      flushListeners();

      // Call flush to all listeners
      /*
      for(SuperBlockLoopConsumer consumer : consumers) {
         consumer.flush();
      }
       */
   }


   private void addBlockToCurrentLoop(InstructionBlock instructionBlock) {
      currentLoop.addAll(Arrays.asList(instructionBlock.getInstructions()));
      currentLoopSize++;
      currentLoopHashes.add(instructionBlock.getHash());

      //currentSuperBlockLoop.addSuperBlock(superBlock);
   }

   private void initCurrentLoop() {
      currentLoop = new ArrayList<Instruction>();
      currentLoopSize = 0;
      currentLoopHashes = new ArrayList<Integer>();

      //currentSuperBlockLoop = new SuperBlockLoop();
      // hashes
   }

   private void initPatternLoop() {
      patternLoop = null;
      loopIterations = 0;
      patternLoopHashes = null;
   }

   private void buildPatternLoop() {
      patternLoop = currentLoop;
      loopIterations = 1;
      patternLoopHashes = currentLoopHashes;
   }

   private int calcLoopHash(List<Integer> currentLoopHashes) {
      int hash = HASH_INITIAL_VALUE;

      for(Integer superBlockHash : currentLoopHashes) {
         hash = BitUtils.superFastHash(superBlockHash, hash);
      }

      return hash;
   }

   public void setMaxPatternSize(int maxPatternSize) {
      patternFinder = new PatternFinder(maxPatternSize);
   }

   /**
    * INSTANCE VARIABLE
    */
   //private SuperBlockLoop patternSuperBlockLoop;
   //private SuperBlockLoop currentSuperBlockLoop;
   //private int currentLoopHash;
   //private int patternLoopHash;

   // State for the Loop being built
   private List<Instruction> currentLoop;
   private int currentLoopSize;
   private List<Integer> currentLoopHashes;
   
   // State for a found loop pattern
   private List<Instruction> patternLoop;
   private int loopIterations = 0;
   private List<Integer> patternLoopHashes;
   
   // Stores the hashes of each SuperBlock in sequence
   
   

   


   // Object state for finding patterns
   private SblBuilderState state;
   private int lastPatternSize;
   private PatternFinder patternFinder;

   //private List<SuperBlockLoopConsumer> consumers;
   private static final int HASH_INITIAL_VALUE = 4;






   enum SblBuilderState {
      LOOKING_FOR_PATTERN,
      BUILDING_PATTERN,
      CHECKING_PATTERN;
   }





}
