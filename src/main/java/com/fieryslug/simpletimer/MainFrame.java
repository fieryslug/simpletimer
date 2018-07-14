package com.fieryslug.simpletimer;

import javax.swing.*;

public class MainFrame extends JFrame {

    PanelTimer panelTimer;

    public MainFrame() {

        panelTimer = new PanelTimer();

        setTitle("Simple Timer");
        setSize(1500, 800);
        setLocationRelativeTo(null);
        getContentPane().add(panelTimer);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);




    }

}
