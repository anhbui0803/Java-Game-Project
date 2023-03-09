package main;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow {

    private JFrame jframe;
    public GameWindow(GamePanel gamePanel) {

        jframe = new JFrame(); // create new JFrame object

//        jframe.setSize(400, 400); // set size for the frame
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close the program when we close the app
        jframe.add(gamePanel); // add "picture" to "frame"
        jframe.setResizable(false);
        jframe.pack();
        jframe.setLocationRelativeTo(null); // set the frame to be in the middle of screen
        jframe.setVisible(true);
        jframe.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowFocusLost();
            }
        });

    }
}
