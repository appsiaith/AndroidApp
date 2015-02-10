/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package androidoneclickbuilder.gui;


import androidoneclickbuilder.gui.Done;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

/**
 *
 * @author mfc5
 */
public class NearlyDone extends javax.swing.JPanel {

    private JFrame frame;
    private File projectFiles, androidTools;
    private Connection databaseConnection;
    private JPanel jp = null;
    
    /**
     * Creates new form ProjectLoader
     */
    public NearlyDone(JFrame frame, File projectFiles, 
                            Connection databaseConnection, File androidTools) {
        this.frame = frame;
        this.projectFiles = projectFiles;
        this.databaseConnection = databaseConnection;
        this.androidTools = androidTools;
        
        jp = this;
        
        initComponents();
        
        Thread t = new Thread() {
            
            public void run() {
                
                try {
        
                    /* First we need some space to work...*/
                    jLabel3.setText("Creating directories... ");
            
            
                    File tempDir = new File(projectFiles.getParentFile(), 
                                                                    "temp");
                    
                    jLabel4.setText(tempDir.getAbsolutePath());
                    tempDir.mkdirs();
            
                    File buildDir = new File(tempDir, "build");
                    jLabel4.setText(buildDir.getAbsolutePath());
                    buildDir.mkdirs();
            
                    Statement st = databaseConnection.createStatement();
                    ResultSet rs = st.executeQuery("SELECT key, defaultValue " +
                                      "FROM config WHERE key=\"PROJECT_NAME\"");
                    String projectName = "Default Project";
                    while(rs.next()) {
                        projectName = rs.getString("defaultValue");
                    }
            
                    /* Now we need to create the scafolding using the android 
                     * tools in the build directory. 
                     */
                    jLabel3.setText("Running Android apk tool to build " + 
                                                            "app scafold...");
                    jLabel4.setText("");
            
                    File toolPath = new File(androidTools, "tools");
                    File[] files = toolPath.listFiles();
                    File androidTool = null;
                    for (int i =0; i < files.length; i++)
                        if (files[i].getName().contains("android"))
                            androidTool = files[i];
            
                    String packageName = "uk.ac.aber.astute." + 
                                                projectName.replaceAll(" ",".");
                    jLabel4.setText("Package: " +packageName);
       
                    String[] execString = new String[] { 
                        androidTool.getAbsolutePath(),
                        "create",
                        "project",
                        "--target",
                        "32",
                       "--name",
                        projectName,
                        "--path",
                        buildDir.getAbsolutePath(),
                        "--activity",
                        "DrawActivity",
                        "--package",
                        packageName
                    };
            
                    Process p = Runtime.getRuntime().exec(execString);
                    InputStream is = p.getInputStream();
                    p.waitFor();
                    String str = "";
                    while(is.available() > 0) {
                        char c = (char)is.read();
                        if (c == '\n') {
                            jLabel4.setText(str);
                            str = "";
                        } else {
                            str += c;
                        }
                    }
            
                    int counter = 0;
                    int current = 0;
                    
                    /* Now we need to extract the project files! They have
                     * been added to the JAR file, so we just need to put
                     * them in the right places.
                     */
                    try{
                    
                        ZipInputStream zis = new ZipInputStream(
                                new FileInputStream("app_template.zip"));
                        File f = new File("app_template.zip");
                        jLabel3.setText("Extracting app_template.zip");
                        
                        while ((zis.getNextEntry()) != null) counter++;
                        zis.close();
                        
                        zis = new ZipInputStream(
                                new FileInputStream("app_template.zip"));
                        ZipEntry ze = zis.getNextEntry();
                        
                        while (ze != null) {
                            
                            current++;
                            jProgressBar1.setValue(counter/100*(current/2));
                            
                            File tempFile = new File(tempDir, ze.getName());
                            if (ze.isDirectory()){
                                tempFile.mkdirs();
                                ze = zis.getNextEntry();
                                continue;
                            }
                            new File(tempFile.getParent()).mkdirs();
                            
                            jLabel4.setText(ze.getName());
                            System.out.printf("%s\n", ze.getName());
                        
                            FileOutputStream fos = new FileOutputStream(tempFile);
                            String buffer = "";
                            
                            for (int i = 0; i < ze.getSize(); i++)
                               if (zis.available() > 0)
                                   fos.write(zis.read());
                            
                        
                            fos.close();
                            ze = zis.getNextEntry();
                            
                        }
                        
                        zis.closeEntry();
                        zis.close();
                        
                    } catch (Exception e) {
                        System.err.printf("ERROR!");
                        e.printStackTrace();;
                    }
                    
                    /* Now we have the template, we need to copy stuff
                     * to the right places in the language app folder.
                     */
                    jLabel3.setText("Copying source tree...");
                    File destDir = new File(buildDir, "src");
                    File srcDir  = new File(new File(tempDir, "app_template"), "src");
                    current += copyOperation(srcDir, destDir, "app_template/src/");
                    jProgressBar1.setValue(counter/100*(current/2));
                    
                    jLabel3.setText("Copying asset files...");
                    File resourceDir = new File(new File(tempDir, "app_template"), "assets");
                    File assetDir = new File(buildDir, "assets");
                    current += copyOperation(resourceDir, assetDir, "app_template/assets/");
                    jProgressBar1.setValue(counter/100*(current/2));
                    
                    jLabel3.setText("Copying database...");
                    File langDB = new File(tempDir.getParentFile(), "langDB.sqlite3");
                    if (copySingleFile(langDB, new File(assetDir, "langDB.sqlite3")))
                        current++;
                    jProgressBar1.setValue(counter/100*(current/2));
                    
                    jLabel3.setText("Copying html...");
                    File htmlDir = new File(tempDir.getParentFile(), "html");
                    destDir = new File(assetDir,"html");
                    current += copyOperation(htmlDir, destDir, "html/");
                    jProgressBar1.setValue(counter/100*(current/2));
                    
                    jLabel3.setText("Copying images...");
                    File imageDir = new File(tempDir.getParentFile(), "image");
                    destDir = new File(assetDir,"image");
                    current += copyOperation(imageDir, destDir, "image/");
                    jProgressBar1.setValue(counter/100*(current/2));
                    
                    jLabel3.setText("Copying audio...");
                  
                    File audioDir = new File(tempDir.getParentFile(), "audio");
                    destDir = new File(assetDir,"audio");
                    current += copyOperation(audioDir, destDir, "audio/");
                    jProgressBar1.setValue(counter/100*(current/2));
                    
                    jLabel3.setText("Copying resource files...");
                    resourceDir = new File(new File(tempDir, "app_template"), "res");
                    File resDir = new File(buildDir, "res");
                    current += copyOperation(resourceDir, resDir, "app_template/res/");
                    jProgressBar1.setValue(counter/100*(current/2));
                    
                    jLabel3.setText("Copying libs...");
                    resourceDir = new File(new File(tempDir, "app_template"), "libs");
                    File libsDir = new File(buildDir, "libs");
                    current += copyOperation(resourceDir, libsDir, "app_template/libs/");
                    jProgressBar1.setValue(counter/100*(current/2));
                    
                    jLabel3.setText("Copying gen...");
                    resourceDir = new File(new File(tempDir, "app_template"), "gen");
                    File genDir = new File(buildDir, "gen");
                    current += copyOperation(resourceDir, genDir, "app_template/gen/");
                    jProgressBar1.setValue(counter/100*(current/2));
            
                    jLabel3.setText("Copying manifest file...");
                    File manifest = new File(new File(tempDir, "app_template"), "AndroidManifest.xml");
                    if (copySingleFile(manifest, new File(assetDir.getParentFile(), "AndroidManifest.xml")))
                        current++;
                    jProgressBar1.setValue(counter/100*(current/2));
                    
                    jLabel3.setText("Preparing project for build...");
                    jLabel4.setText("");
                    
                    srcDir = new File(new File(tempDir, "build"), "src");
                    
                    updateFilesForPackage(srcDir, packageName, projectName);
                    updateFile(new File(new File(tempDir, "build"), "AndroidManifest.xml"), projectName);
                    updateFile(new File(new File(new File(
                                    new File(tempDir, "build"), "res"), "values"), 
                                                        "strings.xml"), projectName);
                    
            
                    jLabel3.setText("Starting build...");
                    jLabel4.setText("");
                    File buildFile = new File(new File(tempDir, "build"), "build.xml");
                    Project proj = new Project();
                    System.setProperty("java.home", System.getProperty("java.home").replace(File.separator + "jre", ""));
                    System.out.printf("JAVA_HOME: %s\n", System.getProperty("java.home"));
                    proj.setUserProperty("ant.file", buildFile.getAbsolutePath());
                    //proj.setProperty("java.home", System.getenv("JAVA_HOME"));
                    proj.init();
                    ProjectHelper helper = ProjectHelper.getProjectHelper();
                    proj.addReference("ant.projectHelper", helper);
                    helper.parse(proj, buildFile);
                    proj.executeTarget("release");
                    
                    jLabel3.setText("Build Complete, Moving .apk files...");
                    jLabel4.setText("This may take some time.");
                    
              //      copySingleFile(new File(new File(new File(tempDir, "build"), "bin"), projectName+"-debug.apk"), 
                //                        new File(tempDir.getParentFile(), projectName+"-debug.apk"));
                    
                    copySingleFile(new File(new File(new File(tempDir, "build"), "bin"), projectName+"-release-unsigned.apk"),
                                        new File(tempDir.getParentFile(), projectName+"-release-unsigned.apk"));
                    
                    jLabel3.setText("Cleaning up...");
                    jLabel4.setText("");
                    
                    ArrayList<File> fullTree = recursiveCopyList(tempDir);
                    for (int i = 0; i < fullTree.size(); i++) {
                        if (fullTree.get(i).isFile()) {
                            jLabel4.setText(fullTree.get(i).getAbsolutePath());
                            fullTree.get(i).delete();
                        }
                    }
                    
                    fullTree = recursiveCopyList(tempDir);
                    while (fullTree.size() >0 ) {
                        for (int i = 0; i < fullTree.size(); i++) {
                            jLabel4.setText(fullTree.get(i).getAbsolutePath());
                            fullTree.get(i).delete();
                        }
                        fullTree = recursiveCopyList(tempDir);
                    }
                    
                    jLabel4.setText("Done!");
                    
                    frame.remove(jp);
                    frame.add(new Done(frame));
                    frame.pack();
                    
            
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }   
            }
        };
    
        t.start();
       
    }
    
    private boolean updateFile(File file, String projectName) {
        
        projectName.replaceAll(" ", ".");
        
        try {
        
                FileInputStream fi = new FileInputStream(file);
                jLabel4.setText(file.getName());

                String buffer = "";
                while (fi.available() > 0)
                    buffer += (char)fi.read();

                fi.close();
                buffer = buffer.replace("APPLICATION_NAME_HERE", projectName);

                FileOutputStream fo = new FileOutputStream(file);
                for (int j = 0; j < buffer.length(); j++)
                    fo.write(buffer.charAt(j));
                fo.close();
                
        } catch (Exception e) { 
            e.printStackTrace(); 
            return false;
        }
        
        return true;
        
    }
    
    private void updateFilesForPackage(File root, 
                        String packageName, String projectName) {
     
        /* Get a list of all files. */
        ArrayList<File> fullTree = recursiveCopyList(root);
        
        /* Read every file, and replace "APPLICATION_NAME_HERE" with 
         * projectName.
        */
        for (int i = 0; i < fullTree.size(); i++)
            if (fullTree.get(i).isFile())
                    updateFile(fullTree.get(i), projectName);
            
        
    }
    
    private ArrayList<File> recursiveCopyList(File src) {
        
        
        ArrayList<File> fullTree = new ArrayList<File>();
        File f = src;
        
        int cnt = 0;
        while (f != null) {
                        
            File[] files = f.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    fullTree.add(files[i]);
                }
            }
                        
            if (cnt < fullTree.size()) f = fullTree.get(cnt++);
            else                         f = null;
                        
        }
                    
        return fullTree;
    }
    
    private boolean copySingleFile(File src, File dest) {
        
        try {
            
            FileInputStream fi = new FileInputStream(src);
            FileOutputStream fo = new FileOutputStream(dest);
            while (fi.available() > 0)
                fo.write(fi.read());
            fi.close();
            fo.close();
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
    }
    
    private int copyOperation(File src, File dest, String replace) {

        if (!replace.isEmpty())
            replace = ".*" + replace.replaceFirst("/", File.separator);
        
        ArrayList<File> filesToCopy = recursiveCopyList(src);
        
        int count = 0;
        for (int i = 0; i < filesToCopy.size(); i++) {
            
            System.out.printf("--> %s\n", filesToCopy.get(i).getName());
                        
            jLabel4.setText(filesToCopy.get(i).getPath());
            if (filesToCopy.get(i).isDirectory()) {
                //new File(src, filesToCopy.get(i).getName()).mkdirs();
                            continue;
            }
                        
            File f = new File(dest, filesToCopy.get(i).
                                getPath().replaceFirst(replace, ""));
            new File(f.getParent()).mkdirs();
            if (f.isDirectory()) continue;
             
            if (copySingleFile(filesToCopy.get(i), f)) count++;
            
        }
        
        return count;
        
    }

    /**
     
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel1.setText("Nearly Done...");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Please wait, we are now building your app.");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel3.setText("jLabel3");

        jLabel4.setText("jLabel4");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private javax.swing.JFileChooser fileChooser;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables
}
