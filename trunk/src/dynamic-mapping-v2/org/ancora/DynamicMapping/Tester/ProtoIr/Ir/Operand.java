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

package org.ancora.DynamicMapping.Tester.ProtoIr.Ir;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joao Bispo
 */
public class Operand {

   public Operand(OperandType type, String value) {
      producer = null;
      consumers = new ArrayList<Operation>();
      this.type = type;
      this.value = value;
   }

   public void setProducer(Operation producer) {
      this.producer = producer;
   }

   public Operation getProducer() {
      return producer;
   }

   public List<Operation> getConsumers() {
      return consumers;
   }

   public String getValue() {
      return value;
   }

  
   @Override
   public String toString() {
      return type+"."+value;
   }

   /*
   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();

      if(producer != null) {
         builder.append(producer.getType());
      } else {
         builder.append("null");
      }
      builder.append(" -> (");
      builder.append(toString());
      builder.append(")");

      for(Operation op : consumers) {
         builder.append(",");
         builder.append(op.getType());
      }
      builder.append("\n");


      return builder.toString();
   }
    */



   /**
    * INSTANCE VARIABLES
    */
   private Operation producer;
   private List<Operation> consumers;
   private OperandType type;
   private String value;
}
