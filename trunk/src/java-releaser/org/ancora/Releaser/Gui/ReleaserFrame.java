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

/*
 * ReleaserFrame.java
 *
 * Created on 4/Fev/2010, 18:36:15
 */

package org.ancora.Releaser.Gui;

import de.schlichtherle.io.swing.JFileChooser;
import java.io.File;
import org.ancora.Releaser.ReleaserPreferences;
import org.ancora.Releaser.TrueZipUtil;
import org.ancora.SharedLibrary.Preferences.EnumPreferences;

/**
 *
 * @author Ancora Group <ancora.codigo@gmail.com>
 */
public class ReleaserFrame extends javax.swing.JFrame {

    /** Creates new form ReleaserFrame */
    public ReleaserFrame() {
        initComponents();
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        prefs = ReleaserPreferences.getPreferences();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jLabel1 = new javax.swing.JLabel();
      jLabel2 = new javax.swing.JLabel();
      jLabel3 = new javax.swing.JLabel();
      jLabel4 = new javax.swing.JLabel();
      jTextField1 = new javax.swing.JTextField();
      jTextField2 = new javax.swing.JTextField();
      jTextField3 = new javax.swing.JTextField();
      jTextField4 = new javax.swing.JTextField();
      jCheckBox1 = new javax.swing.JCheckBox();
      jButton2 = new javax.swing.JButton();
      jButton3 = new javax.swing.JButton();
      jButton4 = new javax.swing.JButton();
      jButton5 = new javax.swing.JButton();
      jTextField5 = new javax.swing.JTextField();

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

      jLabel1.setText("Release Name");

      jLabel2.setText("Dist Folder");

      jLabel3.setText("Run Folder");

      jLabel4.setText("Output Folder");

      jTextField1.setText("jTextField1");

      jTextField2.setText("jTextField1");
      jTextField2.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jTextField2ActionPerformed(evt);
         }
      });

      jTextField3.setText("jTextField1");

      jTextField4.setText("jTextField1");

      jCheckBox1.setText("Use Folder");
      jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jCheckBox1ActionPerformed(evt);
         }
      });

      jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/ancora/Releaser/browse-icon.PNG"))); // NOI18N
      jButton2.setMargin(new java.awt.Insets(0, 0, 0, 0));
      jButton2.setMaximumSize(new java.awt.Dimension(30, 28));
      jButton2.setMinimumSize(new java.awt.Dimension(30, 28));
      jButton2.setPreferredSize(new java.awt.Dimension(28, 28));
      jButton2.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton2ActionPerformed(evt);
         }
      });

      jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/ancora/Releaser/browse-icon.PNG"))); // NOI18N
      jButton3.setMargin(new java.awt.Insets(0, 0, 0, 0));
      jButton3.setMaximumSize(new java.awt.Dimension(30, 28));
      jButton3.setMinimumSize(new java.awt.Dimension(30, 28));
      jButton3.setPreferredSize(new java.awt.Dimension(28, 28));
      jButton3.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton3ActionPerformed(evt);
         }
      });

      jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/ancora/Releaser/browse-icon.PNG"))); // NOI18N
      jButton4.setMargin(new java.awt.Insets(0, 0, 0, 0));
      jButton4.setMaximumSize(new java.awt.Dimension(30, 28));
      jButton4.setMinimumSize(new java.awt.Dimension(30, 28));
      jButton4.setPreferredSize(new java.awt.Dimension(28, 28));
      jButton4.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton4ActionPerformed(evt);
         }
      });

      jButton5.setText("Build ZIPs!");
      jButton5.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton5ActionPerformed(evt);
         }
      });

      jTextField5.setEditable(false);

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(layout.createSequentialGroup()
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addComponent(jLabel1)
                     .addComponent(jLabel2)
                     .addComponent(jLabel3))
                  .addGap(26, 26, 26)
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jCheckBox1))
                     .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                     .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
               .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
               .addGroup(layout.createSequentialGroup()
                  .addComponent(jLabel4)
                  .addGap(26, 26, 26)
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addComponent(jButton5)
                     .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
            .addContainerGap())
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
               .addComponent(jCheckBox1)
               .addGroup(layout.createSequentialGroup()
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                     .addComponent(jLabel1)
                     .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                     .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                           .addComponent(jLabel2)
                           .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                           .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                           .addComponent(jLabel3)))
                     .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                  .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jLabel4))
               .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButton5)
            .addGap(18, 18, 18)
            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
      );

      pack();
   }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
       // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
       // Save state to Preferences
       String stringState = Boolean.toString(jCheckBox1.isSelected());
       prefs.putPreference(ReleaserPreferences.RunFolderEnabled, stringState);
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
      // Open a FileChooser Panel
       String currentDir = jTextField2.getText();
       prefs.putPreference(ReleaserPreferences.DistFolder, currentDir);
       fileChooser.setCurrentDirectory(new File(currentDir));
       int returnValue = fileChooser.showOpenDialog(this);

       // Save file to text field and preferences
       if(returnValue == JFileChooser.APPROVE_OPTION) {
          String absolutePath = fileChooser.getSelectedFile().getAbsolutePath();
          jTextField2.setText(absolutePath);
          prefs.putPreference(ReleaserPreferences.DistFolder, absolutePath);
       }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
      // Open a FileChooser Panel
       String currentDir = jTextField3.getText();
       prefs.putPreference(ReleaserPreferences.RunFolder, currentDir);
       fileChooser.setCurrentDirectory(new File(currentDir));
       int returnValue = fileChooser.showOpenDialog(this);

       // Save file to text field and preferences
       if(returnValue == JFileChooser.APPROVE_OPTION) {
          String absolutePath = fileChooser.getSelectedFile().getAbsolutePath();
          jTextField3.setText(absolutePath);
          prefs.putPreference(ReleaserPreferences.RunFolder, absolutePath);
       }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
      // Open a FileChooser Panel
       String currentDir = jTextField4.getText();
       prefs.putPreference(ReleaserPreferences.OutputFolder, currentDir);
       fileChooser.setCurrentDirectory(new File(currentDir));
       int returnValue = fileChooser.showOpenDialog(this);

       // Save file to text field and preferences
       if(returnValue == JFileChooser.APPROVE_OPTION) {
          String absolutePath = fileChooser.getSelectedFile().getAbsolutePath();
          jTextField4.setText(absolutePath);
          prefs.putPreference(ReleaserPreferences.OutputFolder, absolutePath);
       }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
       // Deactivate button
       jButton5.setEnabled(false);

       // Collect info
       String releaseName = jTextField1.getText();
       String distFoldername = jTextField2.getText();
       String runFoldername = null;
       if(jCheckBox1.isSelected()) {
          runFoldername = jTextField3.getText();
       }
       String outputFoldername = jTextField4.getText();

       // Run zipping method
       TrueZipUtil.zipNetbeansDist(releaseName, distFoldername, runFoldername,
               outputFoldername, jTextField5);

       // Activate button
       jButton5.setEnabled(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    public void writeMessage(String message) {
       jTextField5.setText(message);
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReleaserFrame().setVisible(true);
            }
        });
    }

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton jButton2;
   private javax.swing.JButton jButton3;
   private javax.swing.JButton jButton4;
   private javax.swing.JButton jButton5;
   private javax.swing.JCheckBox jCheckBox1;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JLabel jLabel4;
   private javax.swing.JTextField jTextField1;
   private javax.swing.JTextField jTextField2;
   private javax.swing.JTextField jTextField3;
   private javax.swing.JTextField jTextField4;
   private javax.swing.JTextField jTextField5;
   // End of variables declaration//GEN-END:variables

   /*
    * Read the values from Preferences and update the fields with those values.
    */
   public void loadPreferenceValues() {
      jTextField1.setText(prefs.getPreference(ReleaserPreferences.ReleaseName));
      jTextField2.setText(prefs.getPreference(ReleaserPreferences.DistFolder));
      jTextField3.setText(prefs.getPreference(ReleaserPreferences.RunFolder));
      jTextField4.setText(prefs.getPreference(ReleaserPreferences.OutputFolder));

      boolean checkbox = Boolean.parseBoolean(prefs.getPreference(ReleaserPreferences.RunFolderEnabled));
      jCheckBox1.setSelected(checkbox);
   }

   /**
    * PERSONALIZED INSTANCE VARIABLES
    */
   //Create a file chooser
   private final JFileChooser fileChooser;
   private final EnumPreferences prefs;

}
