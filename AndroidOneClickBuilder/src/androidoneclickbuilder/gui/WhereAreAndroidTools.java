/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package androidoneclickbuilder.gui;

import java.awt.Dialog;
import java.io.File;
import java.sql.Connection;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author mfc5
 */
public class WhereAreAndroidTools extends javax.swing.JPanel {

    private JFrame frame;
    private File file;
    private Connection conn;
    
    /**
     * Creates new form ProjectLoader
     */
    public WhereAreAndroidTools(JFrame frame, File file, Connection conn) {
        this.frame = frame;
        this.file = file;
        this.conn = conn;
        initComponents();
    }

    /**
     
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jFileChooser1 = new javax.swing.JFileChooser();

        jLabel1.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel1.setText("Where are the Android Development Tools?");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Use the dialog below to tell us where you have saved the android");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("development tools.");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jFileChooser1.setFileHidingEnabled(false);
        jFileChooser1.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        jFileChooser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFileChooser1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jFileChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jFileChooser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFileChooser1ActionPerformed

       
        if (evt.getActionCommand().equals("ApproveSelection")) {
       
            File f = this.jFileChooser1.getSelectedFile();
            boolean foundPartA = false;
            
            if (f.isDirectory()) {
                
                File[] files = f.listFiles();
                for (int i = 0; i < files.length; i++) {
                    System.out.printf("File: %s\n", files[i].getName());
                    if (files[i].getName().equalsIgnoreCase("tools")) {
                        foundPartA = true;
                        f = files[i];
                        break;
                    }
                }
            
            
                if (foundPartA) {
                
                    boolean foundPartB = false;
                    for (int i = 0; i < files.length; i++) {
                        System.out.printf("File: %s\n", files[i].getName());
                        if (files[i].getName().equalsIgnoreCase("build-tools")) {
                            foundPartB = true;
                            break;
                        }
                    }
                  
                    if (foundPartB) {
                   
                        frame.remove(this);
                        frame.add(new NearlyDone(frame, file, conn,
                                this.jFileChooser1.getSelectedFile()));
                        frame.pack();
                        
                        
                    } else {
                  
                        int result = JOptionPane.showConfirmDialog(frame,
                            "SDK found, but missing required build tools."
                         + "We can help you fix this.", "Do you need our help?",
                                                    JOptionPane.YES_NO_OPTION);

                        System.out.printf("%d\n", result);

                        if (result == 0) {
                            try {
                                
                                File aTool = new File(f, "android");
                                
                                Process p = Runtime.getRuntime().exec(
                                        new String[]{ 
                                            aTool.getAbsolutePath(), 
                                                            "update", "sdk"});
                                p.waitFor();
                                
                           } catch (Exception e) {
                               
                                e.printStackTrace();
                                
                           }
                       }
                    }
                    
                } else {
             
                JOptionPane.showMessageDialog(frame,
                   "Selected folder does not contain the Android SDK tools. You"
                           + "can download the tools from:\n" +
                           "http://developer.android.com/sdk/",
                            "Please try again.", JOptionPane.ERROR_MESSAGE);
                
                }
            }
        }
    }//GEN-LAST:event_jFileChooser1ActionPerformed

    private javax.swing.JFileChooser fileChooser;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
