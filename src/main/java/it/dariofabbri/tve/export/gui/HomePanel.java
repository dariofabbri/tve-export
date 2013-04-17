package it.dariofabbri.tve.export.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class HomePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public HomePanel() {
		
		this.setLayout(new GridLayout(4, 1, 10, 10));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JLabel labelHeader = ControlFactory.makeHeaderLabel("Menù principale");
		this.add(labelHeader);
		
		JButton buttonElaborazione = ControlFactory.makeFormButton("Elaborazione");
		buttonElaborazione.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TheApplication frame = (TheApplication)SwingUtilities.getWindowAncestor(HomePanel.this);
				frame.setMainPanel(new PdfChoicePanel());
			}
		});
		this.add(buttonElaborazione);
		
		JButton buttonConfigurazione = ControlFactory.makeFormButton("Configurazione");
		buttonConfigurazione.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TheApplication frame = (TheApplication)SwingUtilities.getWindowAncestor(HomePanel.this);
				frame.setMainPanel(new ConfigurationPanel());
			}
		});
		this.add(buttonConfigurazione);
		
		JButton buttonUscita = new JButton("Uscita");
		buttonUscita.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TheApplication frame = (TheApplication)SwingUtilities.getWindowAncestor(HomePanel.this);
				frame.dispose();
			}
		});
		this.add(buttonUscita);
	}
	
}
