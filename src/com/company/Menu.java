package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu implements ActionListener {

    JMenu menu, submenu, submenu2;
    JMenuItem i1, i2, i3, i4, i5, i6, i7, i8;
    Game game = new Game(900, 600);
    JFrame frame = new JFrame("Bilard");

    public Menu() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar mb = new JMenuBar();
        menu = new JMenu("Menu");
        submenu = new JMenu("Speed");
        submenu2 = new JMenu("Balls Size");

        i1 = new JMenuItem("Restart");
        i2 = new JMenuItem("About application");
        i3 = new JMenuItem("Slow");
        i4 = new JMenuItem("Medium");
        i5 = new JMenuItem("Fast");
        i6 = new JMenuItem("Small");
        i7 = new JMenuItem("Medium");
        i8 = new JMenuItem("Big");

        i1.addActionListener(this);
        i2.addActionListener(this);
        i3.addActionListener(this);
        i4.addActionListener(this);
        i5.addActionListener(this);
        i6.addActionListener(this);
        i7.addActionListener(this);
        i8.addActionListener(this);

        menu.add(i1);
        menu.add(i2);
        submenu.add(i3);
        submenu.add(i4);
        submenu.add(i5);
        submenu2.add(i6);
        submenu2.add(i7);
        submenu2.add(i8);

        menu.add(submenu);
        menu.add(submenu2);
        mb.add(menu);
        frame.setJMenuBar(mb);
        frame.setLayout(null);
        frame.setContentPane(game);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if(source == i1) {
            frame.setVisible(false);
            new Menu();
        }

        else if(source == i2)
            JOptionPane.showMessageDialog(i2, "Tutaj napiszemy co robi apka co wykorzystuje i jacy to jestesmy zajebisci, a strzelczyk moze nam ssać drągala");

        else if (source == i3)
            game.setBallsSlow(0.95);

        else if (source == i4)
            game.setBallsSlow(0.97);

        else if (source == i5)
            game.setBallsSlow(0.99);

        else if (source == i6)
            game.setBallsRadius(10);

        else if (source == i7)
            game.setBallsRadius(30);

        else if (source == i8)
            game.setBallsRadius(50);

    }
}
