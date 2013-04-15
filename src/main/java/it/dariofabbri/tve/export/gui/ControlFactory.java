package it.dariofabbri.tve.export.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ControlFactory {

	public static JLabel makeHeaderLabel(String text) {
		
		JLabel label = new JLabel(text);
		label.setFont(label.getFont().deriveFont(Font.BOLD, 24.0f));
		label.setForeground(new Color(106, 122, 232));

		return label;
	}

	
	public static JLabel makeFormLabel(String text) {
		
		JLabel label = new JLabel(text);
		label.setForeground(new Color(71, 130, 226));

		return label;
	}


	public static JTextField makeTextField() {
		
		JTextField field = new JTextField();
		return field;
	}
	
	
	public static GridBagConstraints makeFormLabelConstraint(int row) {
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.insets = new Insets(0, 0, 0, 5);
		c.gridx = 0;
		c.gridy = row;

		return c;
	}
	
	
	public static GridBagConstraints makeFormInputConstraint(int row) {
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.insets = new Insets(0, 5, 0, 0);
		c.gridx = 1;
		c.gridy = row;

		return c;
	}


	public static JButton makeFormButton(String string) {
		
		JButton button = new JButton(string);
		return button;
	}
}
