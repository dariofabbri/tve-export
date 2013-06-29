package it.dariofabbri.tve.export.service.configurator;

import it.dariofabbri.tve.export.model.DatiFiscali;
import it.dariofabbri.tve.export.model.Fornitore;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class Configuration implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Fornitore fornitore;
	private String usernameProduzione;
	private String usernameTest;

	public Configuration() {
		
		fornitore = new Fornitore();
		
		DatiFiscali datiFiscali = new DatiFiscali();
		fornitore.setDatiFiscali(datiFiscali);
	}
	
	
	public boolean isValid() {
		
		// Check if fornitore is not null in loaded configuration.
		//
		if(fornitore == null) {
			return false;
		}
		
		// Check required fornitore fields.
		//
		if(
				StringUtils.isBlank(fornitore.getDescrizione()) ||
				StringUtils.isBlank(fornitore.getIndirizzo()) ||
				StringUtils.isBlank(fornitore.getCitta()) ||
				StringUtils.isBlank(fornitore.getProvincia()) ||
				StringUtils.isBlank(fornitore.getCap()) ||
				StringUtils.isBlank(fornitore.getCodicePaese())) {
			
			return false;
		}
		
		
		// Check if dati fiscale is not null in loaded configuration.
		//
		if(fornitore.getDatiFiscali() == null) {
			return false;
		}
		
		// Check required dati fiscali fields.
		//
		if(StringUtils.isBlank(fornitore.getDatiFiscali().getPartitaIva())) {
			return false;
		}
		
		
		// Check username fields.
		//
		if(StringUtils.isBlank(usernameProduzione)) {
			return false;
		}
		if(StringUtils.isBlank(usernameTest)) {
			return false;
		}
		
		return true;
	}

	public Fornitore getFornitore() {
		return fornitore;
	}

	public void setFornitore(Fornitore fornitore) {
		this.fornitore = fornitore;
	}

	public String getUsernameProduzione() {
		return usernameProduzione;
	}

	public void setUsernameProduzione(String usernameProduzione) {
		this.usernameProduzione = usernameProduzione;
	}

	public String getUsernameTest() {
		return usernameTest;
	}

	public void setUsernameTest(String usernameTest) {
		this.usernameTest = usernameTest;
	}
}
