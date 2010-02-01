/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ancora.SharedLibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Methods for the Java ZIP API.
 * @author Joao Bispo
 */
public class ZipUtils {

   /**
    * Zips the given list of files into an output zip file.
    *
    * <p>TODO: Copy files to current directory, and zip them there, to preserve
    * the folder structure. Method needs to receive parent folder and list
    * of Strings which represent files.
    *
    * <p> Code based on:
    * <br>http://www.java2s.com/Code/Java/File-Input-Output/CompressfilesusingtheJavaZIPAPI.htm
    * @param files
    * @param outputFile
    * @return
    */
   public static boolean zipFiles(List<File> files, File outputFile) {
       // Create a buffer for copying
      byte[] buffer = new byte[4096];
      int bytesRead;
      boolean success = true;

      // Create output file
      ZipOutputStream outputZip = null;
      try {
         outputZip = new ZipOutputStream(new FileOutputStream(outputFile));
      } catch (FileNotFoundException ex) {
         Logger.getLogger(ZipUtils.class.getName()).
                 warning("Could not write to file '"+outputFile.getPath()+"'");
         return false;
      }

      for(File inputFile : files) {
         // Check if file represents a folder
         if (inputFile.isDirectory()) {
            Logger.getLogger(ZipUtils.class.getName()).
                 warning("Ignoring '"+inputFile.getPath()+"' because it " +
                 "is a directory instead of a file.");
            continue;//Ignore directory
         }

         FileInputStream inputStream = null;
         try {
            inputStream = new FileInputStream(inputFile); // Stream to read file
         } catch (FileNotFoundException ex) {
            Logger.getLogger(ZipUtils.class.getName()).
                    warning("Trying to ZIP file '"+inputFile.getPath()+"' but could not find it.");
            continue;
         }
         ZipEntry entry = new ZipEntry(inputFile.getPath()); // Make a ZipEntry

         // Store entry
         try {
            outputZip.putNextEntry(entry);
         } catch (IOException ex) {
            Logger.getLogger(ZipUtils.class.getName()).
                    warning("IOException on ZIP file '"+outputFile.getPath()+
                    "' while trying to make a ZIP entry from file " +
                    "'"+inputFile.getPath()+"'.");
            success = false;
         }
         try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
               outputZip.write(buffer, 0, bytesRead);
            }
            inputStream.close();
         } catch (IOException ex) {
            Logger.getLogger(ZipUtils.class.getName()).
                    warning("IOException while trying to write ZIP file.");
            success = false;
         }


      }
      try {
         // Close outputstream
         outputZip.close();
      } catch (IOException ex) {
         Logger.getLogger(ZipUtils.class.getName()).
                 warning("IOException while trying to close ZIP file stream.");
         success = false;
      }
      /*
      for (int i = 0; i < entries.length; i++) {
         File f = new File(d, entries[i]);
         if (f.isDirectory()) {
            continue;//Ignore directory
         }
         FileInputStream in = new FileInputStream(f); // Stream to read file
         ZipEntry entry = new ZipEntry(f.getPath()); // Make a ZipEntry
         out.putNextEntry(entry); // Store entry
         while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
         }
         in.close();
      }
       */

      return success;
   }

   /**
    * Zips the given list of files into an output zip file.
    *
    * <p>TODO: Copy files to current directory, and zip them there, to preserve
    * the folder structure. Method needs to receive parent folder and list
    * of Strings which represent files.
    *
    * <p> Code based on:
    * <br>http://www.java2s.com/Code/Java/File-Input-Output/CompressfilesusingtheJavaZIPAPI.htm
    * @param files
    * @param outputFile
    * @return
    */
   public static boolean zipFiles(File parentFolder, List<String> files, File outputFile) {

      // Current folder
      File currentFolder = new File("./");
      
      boolean parentFolderIsCurrent = false;
      try {
         // Check if parent folder is current folder
         parentFolderIsCurrent = currentFolder.getCanonicalPath().equals(parentFolder.getCanonicalPath());
      } catch (IOException ex) {
         Logger.getLogger(ZipUtils.class.getName()).
                 warning("IOException while checking folders:"+ex.getMessage());
      }

      // If parent folder is not current, copy files to current folder
      if(!parentFolderIsCurrent) {
         copyFiles(parentFolder, files, currentFolder);
      }

      return false;
/*
      // Create a buffer for copying
      byte[] buffer = new byte[4096];
      int bytesRead;
      boolean success = true;

      // Create output file
      ZipOutputStream outputZip = null;
      try {
         outputZip = new ZipOutputStream(new FileOutputStream(outputFile));
      } catch (FileNotFoundException ex) {
         Logger.getLogger(ZipUtils.class.getName()).
                 warning("Could not write to file '" + outputFile.getPath() + "'");
         return false;
      }

      for (File inputFile : files) {
         // Check if file represents a folder
         if (inputFile.isDirectory()) {
            Logger.getLogger(ZipUtils.class.getName()).
                    warning("Ignoring '" + inputFile.getPath() + "' because it " +
                    "is a directory instead of a file.");
            continue;//Ignore directory
         }

         FileInputStream inputStream = null;
         try {
            inputStream = new FileInputStream(inputFile); // Stream to read file
         } catch (FileNotFoundException ex) {
            Logger.getLogger(ZipUtils.class.getName()).
                    warning("Trying to ZIP file '" + inputFile.getPath() + "' but could not find it.");
            continue;
         }
         ZipEntry entry = new ZipEntry(inputFile.getPath()); // Make a ZipEntry

         // Store entry
         try {
            outputZip.putNextEntry(entry);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
               outputZip.write(buffer, 0, bytesRead);
            }
            inputStream.close();

            // Close outputstream
            outputZip.close();
         } catch (IOException ex) {
            Logger.getLogger(ZipUtils.class.getName()).
                    warning("IOException while building the ZIP file:" + ex.getMessage());
            success = false;
         }

      }
      return success;
 */
   }

   
   /**
    * Copies the files in the given parent folder to the outputfolder.
    * 
    * @param parentFolder
    * @param files
    * @param outputFolder
    */
   public static boolean copyFiles(File parentFolder, List<String> files, File outputFolder) {
      boolean success = true;

      for(String filename : files) {
         // Build files
         File outputFile = new File(outputFolder, filename);
         File inputFile = new File(parentFolder, filename);

         // Copy
         boolean noErrors = copyFile(inputFile, outputFile);
         if(!noErrors) {
            success = false;
         }
      }

      return success;
   }

   public static boolean copyFile(File input, File output) {
      boolean success = true;
      // Create file
      
      try {
         output.createNewFile();
         
         FileInputStream fis = new FileInputStream(input);
         FileOutputStream fos = new FileOutputStream(output);
         try {
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
               fos.write(buf, 0, i);
            }
         } catch (Exception e) {
            Logger.getLogger(ZipUtils.class.getName()).
                    warning("IOException while copying files:" + e.getMessage());
            success = false;
         } finally {
            if (fis != null) {
               fis.close();
            }
            if (fos != null) {
               fos.close();
            }
         }
      } catch (IOException ex) {
         Logger.getLogger(ZipUtils.class.getName()).
                 warning("IOException while closing files:" + ex.getMessage());
         success = false;
      }

    return success;
  }
}
