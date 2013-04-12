package it.dariofabbri.tve.export.service.configuration;

import it.dariofabbri.tve.export.model.DatiFiscali;
import it.dariofabbri.tve.export.model.Fornitore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class Configuration implements Serializable {

	private static final String filename = "config.bin";
	
	private static final long serialVersionUID = 1L;

	private static Configuration instance;
	
	private Configuration() {
		
		fornitore = new Fornitore();
		
		DatiFiscali datiFiscali = new DatiFiscali();
		fornitore.setDatiFiscali(datiFiscali);
	}
	
	public static Configuration getInstance() {
		
		if(instance == null) {
			instance = new Configuration();
		}
		
		return instance;
	}
	
	
	public void reset() {
		
		instance = new Configuration();
	}
	
	
	public boolean isValid() {
		
		// Check if fornitore is not null in loaded configuration.
		//
		if(instance.fornitore == null) {
			return false;
		}
		
		// Check required fornitore fields.
		//
		if(
				StringUtils.isBlank(instance.fornitore.getDescrizione()) ||
				StringUtils.isBlank(instance.fornitore.getIndirizzo()) ||
				StringUtils.isBlank(instance.fornitore.getCitta()) ||
				StringUtils.isBlank(instance.fornitore.getProvincia()) ||
				StringUtils.isBlank(instance.fornitore.getCap()) ||
				StringUtils.isBlank(instance.fornitore.getCodicePaese())) {
			
			return false;
		}
		
		
		// Check if dati fiscale is not null in loaded configuration.
		//
		if(instance.fornitore.getDatiFiscali() == null) {
			return false;
		}
		
		// Check required dati fiscali fields.
		//
		if(StringUtils.isBlank(instance.fornitore.getDatiFiscali().getPartitaIva())) {
			
			return false;
		}
		
		return true;
	}
	
	
	public void load() {
		
		try {
			File file = new File(filename);
			if(!file.exists()) {
				return;
			}
			
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			instance.fornitore = (Fornitore)ois.readObject();
			ois.close();
			
		} catch(Exception e) {
			
			throw new RuntimeException("Exception caught while reading configuration.", e);
		}
		
	}
	
	public void save() {
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
			oos.writeObject(instance.fornitore);
			oos.close();
			
		} catch(Exception e) {
			
			throw new RuntimeException("Exception caught while writing configuration.", e);
		}
	}
	
	
	private Fornitore fornitore;

	public Fornitore getFornitore() {
		return fornitore;
	}

	public void setFornitore(Fornitore fornitore) {
		this.fornitore = fornitore;
	}
}
