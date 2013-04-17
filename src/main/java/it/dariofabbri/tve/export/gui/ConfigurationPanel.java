package it.dariofabbri.tve.export.gui;

import it.dariofabbri.tve.export.model.DatiFiscali;
import it.dariofabbri.tve.export.model.Fornitore;
import it.dariofabbri.tve.export.service.configurator.Configuration;
import it.dariofabbri.tve.export.service.configurator.Configurator;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.StringUtils;

public class ConfigurationPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JTextField username;
	private JTextField descrizione;
	private JTextField descrizione2;
	private JTextField indirizzo;
	private JTextField indirizzo2;
	private JTextField citta;
	private JTextField provincia;
	private JTextField cap;
	private JTextField codicePaese;
	private JTextField partitaIva;
	private JTextField capitaleSociale;
	private JTextField numeroRegistroImprese;
	

	public ConfigurationPanel() {
		
		this.setLayout(new BorderLayout(10, 10));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JLabel labelHeader = ControlFactory.makeHeaderLabel("Pannello di configurazione");
		this.add(labelHeader, BorderLayout.NORTH);		
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		this.add(centerPanel, BorderLayout.CENTER);

		centerPanel.add(
				ControlFactory.makeFormLabel("Descrizione:"),
				ControlFactory.makeFormLabelConstraint(0));
		
		descrizione = ControlFactory.makeTextField();
		centerPanel.add(descrizione, ControlFactory.makeFormInputConstraint(0));

		centerPanel.add(
				ControlFactory.makeFormLabel("Descrizione 2:"),
				ControlFactory.makeFormLabelConstraint(1));
		
		descrizione2 = ControlFactory.makeTextField();
		centerPanel.add(descrizione2, ControlFactory.makeFormInputConstraint(1));

		centerPanel.add(
				ControlFactory.makeFormLabel("Indirizzo:"),
				ControlFactory.makeFormLabelConstraint(2));
		
		indirizzo = ControlFactory.makeTextField();
		centerPanel.add(indirizzo, ControlFactory.makeFormInputConstraint(2));

		centerPanel.add(
				ControlFactory.makeFormLabel("Indirizzo 2:"),
				ControlFactory.makeFormLabelConstraint(3));
		
		indirizzo2 = ControlFactory.makeTextField();
		centerPanel.add(indirizzo2, ControlFactory.makeFormInputConstraint(3));

		centerPanel.add(
				ControlFactory.makeFormLabel("Città:"),
				ControlFactory.makeFormLabelConstraint(4));
		
		citta = ControlFactory.makeTextField();
		centerPanel.add(citta, ControlFactory.makeFormInputConstraint(4));

		centerPanel.add(
				ControlFactory.makeFormLabel("Provincia:"),
				ControlFactory.makeFormLabelConstraint(5));
		
		provincia = ControlFactory.makeTextField();
		centerPanel.add(provincia, ControlFactory.makeFormInputConstraint(5));

		centerPanel.add(
				ControlFactory.makeFormLabel("CAP:"),
				ControlFactory.makeFormLabelConstraint(6));
		
		cap = ControlFactory.makeTextField();
		centerPanel.add(cap, ControlFactory.makeFormInputConstraint(6));

		centerPanel.add(
				ControlFactory.makeFormLabel("Codice paese:"),
				ControlFactory.makeFormLabelConstraint(7));
		
		codicePaese = ControlFactory.makeTextField();
		centerPanel.add(codicePaese, ControlFactory.makeFormInputConstraint(7));

		centerPanel.add(
				ControlFactory.makeFormLabel("Partita IVA:"),
				ControlFactory.makeFormLabelConstraint(8));
		
		partitaIva = ControlFactory.makeTextField();
		centerPanel.add(partitaIva, ControlFactory.makeFormInputConstraint(8));

		centerPanel.add(
				ControlFactory.makeFormLabel("Capitale sociale:"),
				ControlFactory.makeFormLabelConstraint(9));
		
		capitaleSociale = ControlFactory.makeTextField();
		centerPanel.add(capitaleSociale, ControlFactory.makeFormInputConstraint(9));

		centerPanel.add(
				ControlFactory.makeFormLabel("Numero registro imprese:"),
				ControlFactory.makeFormLabelConstraint(10));
		
		numeroRegistroImprese = ControlFactory.makeTextField();
		centerPanel.add(numeroRegistroImprese, ControlFactory.makeFormInputConstraint(10));

		centerPanel.add(
				ControlFactory.makeFormLabel("Username portale:"),
				ControlFactory.makeFormLabelConstraint(11));
		
		username = ControlFactory.makeTextField();
		centerPanel.add(username, ControlFactory.makeFormInputConstraint(11));
		
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.add(buttonPanel, BorderLayout.SOUTH);		
		
		JButton cancelButton = ControlFactory.makeFormButton("Annulla");
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TheApplication frame = (TheApplication)SwingUtilities.getWindowAncestor(ConfigurationPanel.this);
				frame.setMainPanel(new HomePanel());				
			}
		});
		buttonPanel.add(cancelButton);
		
		JButton saveButton = ControlFactory.makeFormButton("Salva");
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!saveConfiguration()) {
					return;
				}
				
				TheApplication frame = (TheApplication)SwingUtilities.getWindowAncestor(ConfigurationPanel.this);
				frame.setMainPanel(new HomePanel());				
			}
		});
		buttonPanel.add(saveButton);
		
		loadConfiguration();
	}


	private void loadConfiguration() {
		
		Configurator configurator = new Configurator();
		Configuration configuration = configurator.load();
		
		if(configuration == null) {
			return;
		}
		
		Fornitore fornitore = configuration.getFornitore();
		if(fornitore == null) {
			return;
		}
		
		descrizione.setText(fornitore.getDescrizione());
		descrizione2.setText(fornitore.getDescrizione2());
		indirizzo.setText(fornitore.getIndirizzo());
		indirizzo2.setText(fornitore.getIndirizzo2());
		citta.setText(fornitore.getCitta());
		provincia.setText(fornitore.getProvincia());
		cap.setText(fornitore.getCap());
		codicePaese.setText(fornitore.getCodicePaese());
		
		DatiFiscali datiFiscali = fornitore.getDatiFiscali();
		if(datiFiscali == null) {
			return;
		}
		
		partitaIva.setText(datiFiscali.getPartitaIva());
		capitaleSociale.setText(datiFiscali.getCapitaleSociale());
		numeroRegistroImprese.setText(datiFiscali.getNumeroRegistroImprese());
		
		username.setText(configuration.getUsername());
	}
	
	
	private boolean saveConfiguration() {
		
		Configuration configuration = new Configuration();
		
		Fornitore fornitore = configuration.getFornitore();
		fornitore.setDescrizione(StringUtils.trimToNull(descrizione.getText().trim()));
		fornitore.setDescrizione2(StringUtils.trimToNull(descrizione2.getText().trim()));
		fornitore.setIndirizzo(StringUtils.trimToNull(indirizzo.getText().trim()));
		fornitore.setIndirizzo2(StringUtils.trimToNull(indirizzo2.getText().trim()));
		fornitore.setCitta(StringUtils.trimToNull(citta.getText().trim()));
		fornitore.setProvincia(StringUtils.trimToNull(provincia.getText().trim()));
		fornitore.setCap(StringUtils.trimToNull(cap.getText().trim()));
		fornitore.setCodicePaese(StringUtils.trimToNull(codicePaese.getText().trim()));
		
		DatiFiscali datiFiscali = fornitore.getDatiFiscali();
		datiFiscali.setPartitaIva(StringUtils.trimToNull(partitaIva.getText().trim()));
		datiFiscali.setCapitaleSociale(StringUtils.trimToNull(capitaleSociale.getText().trim()));
		datiFiscali.setNumeroRegistroImprese(StringUtils.trimToNull(numeroRegistroImprese.getText().trim()));
		
		configuration.setUsername(StringUtils.trimToNull(username.getText().trim()));
		
		if(!configuration.isValid()) {
			
			JOptionPane.showMessageDialog(
					null,
				    "La configurazione corrente non è valida. " +
				    "Controllare di aver immesso tutti i campi obbligatori.",
				    "Errore",
				    JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		try {
			Configurator configurator = new Configurator();
			configurator.save(configuration);
			
		} catch(Exception e) {
			return false;
		}
		
		return true;
	}
}
