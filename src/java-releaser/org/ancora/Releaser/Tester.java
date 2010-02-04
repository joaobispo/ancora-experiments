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

package org.ancora.Releaser;

import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.ancora.Releaser.Gui.ReleaserFrame;
import org.ancora.SharedLibrary.ZipUtils;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class Tester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
       //testZip();
       //testZip2();
       //testRealZip();
       //testApp();
       //testApp2();
       main();
    }

   private static void testZip() {
      List<File> files = new ArrayList<File>();
     // files.add(new File("cd/ccc.txt"));
     // files.add(new File("aaa.txt"));
     // files.add(new File("bbb.txt"));

      //files.add(new File("E:/temp/car/Car.jar"));
      //files.add(new File("E:/temp/car/rxtxSerial.dll"));
      //files.add(new File("E:/temp/car/lib/RXTXcomm.jar"));
      //files.add(new File("E:/temp/car/"));
      

      File outputFile;
      outputFile = new File("result.zip");
      //outputFile = new File("E:/temp");

      System.out.println(ZipUtils.zipFiles(files, outputFile));
   }

   private static void testZip2() {
      List<String> files = new ArrayList<String>();

      files.add("Car.jar");
      files.add("rxtxSerial.dll");
      files.add("lib/RXTXcomm.jar");

      File parentFolder;
      parentFolder = new File("E:/temp/car/");

      File outputFile;
      outputFile = new File("E:/result.zip");

      System.out.println(ZipUtils.zipFiles(parentFolder, files, outputFile));
   }

   private static void testRealZip() {

      String foldername;
      foldername = "E:/temp/car/";

      String outputFile;
      outputFile = "E:/result.zip";

      TrueZipUtil.zipFolder(foldername, outputFile);
   }

   private static void testApp() {
      String releaseName = "microblaze-package-1.0";
      String inputfolder = "D:\\Programming\\Ancora\\AncoraExperiments\\projects\\microblaze-package-netbeans6.7\\dist\\";
      String outputfolder = "E:\\";
      TrueZipUtil.zipDist(releaseName, inputfolder, outputfolder);
   }

   private static void testApp2() {
      String releaseName = "microblaze-package-1.0";
      String inputfolder = "D:\\Programming\\Ancora\\AncoraExperiments\\projects\\microblaze-package-netbeans6.7\\dist\\";
      String outputfolder = "E:\\";
      //TrueZipUtil.zipNetbeansDist(releaseName, inputfolder, null, outputfolder);
   }

   private static void main() {
      Releaser releaser = new Releaser();
      releaser.execute();
      /*
      final ReleaserFrame frame = new ReleaserFrame();
      // Setup Frame
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            frame.setup();
         }
      });


      // Show frame
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            frame.setVisible(true);
         }
      });
       */
   }

   private static void setupFrame(ReleaserFrame frame) {
      throw new UnsupportedOperationException("Not yet implemented");
   }

}
