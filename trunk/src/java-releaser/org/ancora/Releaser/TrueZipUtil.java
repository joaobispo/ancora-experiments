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

import de.schlichtherle.io.File;
import java.awt.EventQueue;
import java.util.Arrays;
import javax.swing.JTextField;

/**
 *
 * @author Joao Bispo
 */
public class TrueZipUtil {

   public static void zipFolder(String folder, String outputFile) {
      new File(folder).copyAllTo(new File(outputFile));

      /*
      String foldername = "E:/temp/car";
      File folderFile = new File(foldername);

      String filename = "Car.jar";
      File file = new File(folderFile, filename);

      file.copyTo(new File("E:/result.zip"));
      System.out.println(file.toString());
       */
   }

   public static void zipDist(String releaseName, String inputfolder, String outputfolder) {
      // First, zip javadoc
      File javadocFolder = new File(new File(inputfolder), "javadoc");

      // Zip contents
      String javadocFilename = releaseName+"-javadoc.zip";
      javadocFolder.copyAllTo(new File(new File(outputfolder), javadocFilename));

      // Delete Javadoc
      javadocFolder.deleteAll();

      File readme = new File(new File(inputfolder), "README.TXT");
      readme.delete();

      // Main release
      File dist = new File(inputfolder);
      String distFilename = releaseName+".zip";
      dist.copyAllTo(new File(new File(outputfolder), distFilename));

   }

   public static boolean zipNetbeansDist(String releaseName, String inputFoldername,
           String runFoldername, String outputFoldername, JTextField status) {

      // Create names
      String releaseFilename = releaseName + ".zip";
      String javadocFilename = releaseName + "-javadoc.zip";

      // Create Folders
      File outputFolder = new File(outputFoldername);
      File distFolder = new File(inputFoldername);
      File javadocFolder = new File(distFolder, "javadoc");
      java.io.File tempFolder = new File(outputFolder, TEMP_FOLDER);

      // Create ZipFiles
      File releaseZip = new File(outputFolder, releaseFilename);
      File javadocZip = new File(outputFolder, javadocFilename);

      // Delete zipfiles
      writeTextField(status, "Deleting previous zip files.");
      releaseZip.delete();
      javadocZip.delete();

      // Create Javadoc Zip
      writeTextField(status, "Creating javadoc zip.");
      javadocFolder.copyAllTo(javadocZip);
      //System.out.println("Written "+javadocZip.getName()+".");

      // Copy dist folder to temporary folder
      writeTextField(status, "Copying files to temporary folder.");
      distFolder.copyAllTo(tempFolder);

      // Delete Javadoc
      File javadocTempFolder = new File(tempFolder, "javadoc");
      javadocTempFolder.deleteAll();

      // Delete readme.txt
      File readme = new File(tempFolder, "README.TXT");
      readme.delete();

      // If RunFolder different than Null, copy it too
      if(runFoldername != null) {
         File runFolder = new File(runFoldername);
         runFolder.copyAllTo(tempFolder);
      }

      // Zip contents of tempFolder
      writeTextField(status, "Creating release zip.");
      File tempZipFolder = new File(tempFolder);
      tempZipFolder.copyAllTo(releaseZip);

      // Delete temporary folder
      writeTextField(status, "Deleting temporary files.");
      tempZipFolder.deleteAll();
      /*
      java.io.File[] files = distFolder.listFiles();
      
      for(java.io.File file : files) {
         System.out.println("processing file:"+file.getName());
         if(file.getName().equals("README.TXT")) {
            // Skip file
            continue;
         } else if(file.getName().equals("javadoc")) {
            // Skip file
            continue;
         } else {
            new File(file).copyAllTo(releaseZip);
            System.out.println("added file.");
         }
      }

       */
      // Copy files in distribution folder to release zip
      //releaseZip.copyAllFrom(distFolder);
      // Exclude Javadoc and Readme.txt

      //javadocZip = null;
      //releaseZip = null;
      writeTextField(status, "Finished!");
      return true;
   }

   private static void writeTextField(final JTextField textField, final String message) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            textField.setText(message);
         }

      });
   }

      // Deletes all files and subdirectories under dir.
    // Returns true if all deletions were successful.
    // If a deletion fails, the method stops attempting to delete and returns false.
    public static boolean deleteDir(java.io.File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

   private static String TEMP_FOLDER = "release_temp";
}
