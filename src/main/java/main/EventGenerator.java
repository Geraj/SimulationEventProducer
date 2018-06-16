
package main;

import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import event.EventType;
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
	JTextField nrToGenereateTextField = new JTextField(3);
	JTextField indexTextField = new JTextField(3);
	JComboBox<EventType> combobox;
	JCheckBox checkbox;

	SimulationEventProducer eventProducerAndSender = new SimulationEventProducer();

	public void addComponentsToPane(Container pane) {
		if (RIGHT_TO_LEFT) {
			pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}

		JButton button;
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10);
		if (shouldFill) {
			// natural height, maximum width
			c.fill = GridBagConstraints.HORIZONTAL;
		}

		JLabel lable = new JLabel("Nr of events");
		// if (shouldWeightX) {
		// c.weightx = 0.5;
		// }
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(lable, c);

		lable.setLabelFor(nrToGenereateTextField);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.gridx = 1;
		c.gridy = 0;
		pane.add(nrToGenereateTextField, c);

		
		checkbox = new JCheckBox("generate special");
		checkbox.setSelected(true);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.gridx = 2;
		c.gridy = 0;
		pane.add(checkbox, c);

		button = new JButton("Generate");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.3;
		c.gridx = 4;
		c.gridy = 0;
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {					
					if (eventProducerAndSender.getFuture() != null && !eventProducerAndSender.getFuture().isDone()) {						
						JOptionPane.showMessageDialog(null, "Event sending ounder way, please wait");
						try {
							String result = eventProducerAndSender.getFuture().get();
							JOptionPane.showMessageDialog(null, result + " will generate and send new batch of");
						} catch (InterruptedException | ExecutionException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					eventProducerAndSender.setNrOfGeneration(Integer.parseInt(nrToGenereateTextField.getText()));
					eventProducerAndSender.generateAndSend(eventProducerAndSender.getNrOfGeneration(), checkbox.isSelected());
				} catch (NumberFormatException exc) {
					eventProducerAndSender.generateAndSend(1, false);
				}
			}
		});

		pane.add(button, c);
		initSpecialEvents(pane, c);

	}

	/**
	 * Combobox with special events to add.
	 * 
	 * @param pane
	 * @param c
	 */
	private void initSpecialEvents(Container pane, GridBagConstraints c) {
		JLabel lable;
		lable = new JLabel("Type of event to add");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		pane.add(lable, c);

		JComboBox<EventType> combobox = new JComboBox<>();
		for (EventType eventType : EventType.values()) {
			if (!EventType.TIME_CHANGE.equals(eventType)) {
				combobox.addItem(eventType);
			}			
		}
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		pane.add(combobox, c);
		lable = new JLabel("@ event index of");
		c.gridx = 2;
		c.gridy = 1;
		pane.add(lable, c);
		lable.setLabelFor(indexTextField);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.gridx = 3;
		c.gridy = 1;
		pane.add(indexTextField, c);

		JButton button = new JButton("add");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.gridx = 4;
		c.gridy = 1;

		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					eventProducerAndSender.addSpecial((EventType) combobox.getSelectedItem(),
							Long.parseLong(indexTextField.getText()));
				} catch (NumberFormatException exc) {
					// empty
				}
			}
		});
		pane.add(button, c);
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked
	 * from the event-dispatching thread.
	 */
	void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Event producer");
		frame.setSize(600, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set up the content pane.
		addComponentsToPane(frame.getContentPane());

		// Display the window.
		// frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		EventGenerator generator = new EventGenerator();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				generator.createAndShowGUI();
			}
		});
	}
}