/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.Releaser;

import de.schlichtherle.io.File;

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
}
