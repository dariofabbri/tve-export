package it.dariofabbri.tve.export.service.configurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Configurator {

	private static final String filename = System.getProperty("java.io.tmpdir") + "/config.bin";

	public Configuration load() {
		
		try {
			File file = new File(filename);
			if(!file.exists()) {
				return null;
			}
			
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			Configuration configuration = (Configuration)ois.readObject();
			ois.close();
			
			return configuration;
			
		} catch(Exception e) {
			
			throw new RuntimeException("Exception caught while reading configuration.", e);
		}		
	}
	
	public void save(Configuration configuration) {
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
			oos.writeObject(configuration);
			oos.close();
			
		} catch(Exception e) {
			
			throw new RuntimeException("Exception caught while writing configuration.", e);
		}
	}
}
