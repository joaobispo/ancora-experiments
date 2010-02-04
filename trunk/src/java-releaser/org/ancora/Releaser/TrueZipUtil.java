/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.Releaser;

import de.schlichtherle.io.File;
import java.util.Arrays;

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
           String runFoldername, String outputFoldername) {

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
      releaseZip.delete();
      javadocZip.delete();

      // Create Javadoc Zip
      javadocFolder.copyAllTo(javadocZip);
      System.out.println("Written "+javadocZip.getName()+".");

      // Copy dist folder to temporary folder
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
      File tempZipFolder = new File(tempFolder);
      tempZipFolder.copyAllTo(releaseZip);

      // Delete temporary folder
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

      System.out.println("written file "+Arrays.toString(releaseZip.listFiles()));
      return true;
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
