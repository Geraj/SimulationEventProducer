/*
 * Copyright (C) TBA BV
 * All rights reserved.
 * www.tba.nl
 */
package main;

import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import producer.SimulationEventProducer;

/**
 * TODO DESCRIPTION
 * 
 * @author Geraj
 */
public class EventGenerator {
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
    static JTextField textField =  new JTextField(3);
 
  public static void addComponentsToPane(Container pane) {
    if (RIGHT_TO_LEFT) {
      pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    }

    JButton button;
    pane.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    if (shouldFill) {
      // natural height, maximum width
      c.fill = GridBagConstraints.HORIZONTAL;
    }

    JLabel lable = new JLabel("Nr of events");
    if (shouldWeightX) {
      c.weightx = 0.5;
    }
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;
    pane.add(lable, c);

    lable.setLabelFor(textField);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 0.5;
    c.gridx = 1;
    c.gridy = 0;
    pane.add(textField, c);

    button = new JButton("Generate");
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 0.3;
    c.gridx = 2;
    c.gridy = 0;
    button.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        try {
          SimulationEventProducer producer = new SimulationEventProducer(Integer.parseInt(
              textField.getText()));
          producer.startAndSend();
        } catch (NumberFormatException exc) {
          SimulationEventProducer producer = new SimulationEventProducer(1);
          producer.startAndSend();
        }
      }
    });
    pane.add(button, c);

  }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window
        JFrame frame = new JFrame("GridBagLayoutDemo");
        frame.setSize(300, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());
 
        //Display the window.
        //frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}