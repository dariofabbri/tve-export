package it.dariofabbri.tve.export.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class TheApplication extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel mainPanel;

	public static void main(String[] args) {
		
		// Set the operating system's look and feel.
		//
		if(!setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel")) {
			setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		
		// Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
		//
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                makeGUI();
            }
        });
	}
	
	
	protected static boolean setLookAndFeel(String lfName) {
		
		try {
			UIManager.setLookAndFeel(lfName);
			return true;
			
		} catch (
				ClassNotFoundException | 
				InstantiationException | 
				IllegalAccessException | 
				UnsupportedLookAndFeelException e) {
			
			e.printStackTrace();
			return false;
		}
	}
	
	
	protected static void makeGUI() {
		
		TheApplication frame = new TheApplication();
		frame.setTitle("Conversione fatture");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationByPlatform(true);
		
		frame.setMainPanel(new HomePanel());
		frame.setVisible(true);		
	}
	
	
	public void setMainPanel(JPanel panel) {
		
		if(mainPanel != null) {
			this.remove(mainPanel);
		}
		
		this.add(panel);
		this.pack();
		
		this.mainPanel = panel;
	}
}
