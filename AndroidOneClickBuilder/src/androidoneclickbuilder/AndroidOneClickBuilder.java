/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package androidoneclickbuilder;

import androidoneclickbuilder.gui.ProjectLoader;
import javax.swing.JFrame;

/**
 *
 * @author mfc5
 */
public class AndroidOneClickBuilder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        JFrame frame = new JFrame();
        frame.add(new ProjectLoader(frame));
        frame.setVisible(true);
        frame.pack();
        
    }
    
}
