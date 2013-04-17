package it.dariofabbri.tve.export.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class PdfChoicePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public PdfChoicePanel() {
		
		this.setLayout(new GridLayout(4, 1, 10, 10));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JLabel labelHeader = ControlFactory.makeHeaderLabel("Scelta del PDF da importare");
		this.add(labelHeader);
		
		JButton buttonCancel = new JButton("Annulla");
		buttonCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TheApplication frame = (TheApplication)SwingUtilities.getWindowAncestor(PdfChoicePanel.this);
				frame.setMainPanel(new HomePanel());				
			}
		});
		this.add(buttonCancel);
	}
	
}
