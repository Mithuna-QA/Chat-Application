/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LocalUserInstance
 */
public class ChatApplication {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        MainView view = new MainView();
        view.setResizable(false);
        view.pack();
        view.setLocationRelativeTo(null);
        view.setVisible(true);
        
    }
    
}
