package it.dariofabbri.tve.export.gui;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HomePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public HomePanel() {
		
		this.setLayout(new GridLayout(4, 1, 10, 10));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		this.add(new JLabel("Menù principale"));
		this.add(new JButton("Importazione"));
		this.add(new JButton("Configurazione"));
		this.add(new JButton("Uscita"));
	}
	
}
