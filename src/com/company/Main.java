package com.company;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(()-> {
          {
                JFrame frame = new JFrame("Bilard");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setContentPane(new Game(800, 600));
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}