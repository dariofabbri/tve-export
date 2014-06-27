package it.dariofabbri.tve.export.service.marshaller;

import it.dariofabbri.tve.export.model.DatiTrasmissione;
import it.dariofabbri.tve.export.model.Documento;
import it.dariofabbri.tve.export.model.Fornitore;
import it.dariofabbri.tve.export.model.Messaggio;
import it.dariofabbri.tve.export.model.RiferimentoDocumento;
import it.dariofabbri.tve.export.service.configurator.Configuration;
import it.dariofabbri.tve.export.service.configurator.Configurator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBContext;


public class Marshaller {

	private SimpleDateFormat sdf;
	private Configurator configurator;
	
	private Configuration configuration = null;
	
	public Marshaller() {
		
		sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		configurator = new Configurator();
		
		
		// Retrieve configuration.
		//
		configuration = configurator.load();
		if(configuration == null) {
			throw new RuntimeException("Unable to load configuration.");
		}
		if(!configuration.isValid()) {
			throw new RuntimeException("Loaded configuration is not valid.");
		}
	}
	
	
	public void generateXml(List<Documento> documenti, Date creationDate, File targetFolder, boolean produzione) {

		// Open output stream.
		//
		String xmlFilename = 
				String.format("INV_%1s_%2$tY%2$tm%2$td.xml", 
						produzione ? configuration.getUsernameProduzione() : configuration.getUsernameTest(), 
						creationDate);
		File xmlFile = new File(targetFolder, xmlFilename);
		
		// Generate XML representation.
		//
		try {
		    FileOutputStream fos = new FileOutputStream(xmlFile);
		    generateXml(documenti, creationDate, fos);
		    fos.flush();
		    fos.close();
		    
		} catch(Exception e) {
			
			throw new RuntimeException("Exception caught while using JAXB to marshal messaggio object.", e);
		}
	}
	
	
	public void generateZip(List<Documento> documenti, Date creationDate, File targetFolder, boolean produzione) {

		// Open output stream.
		//
		String xmlFilename = 
				String.format("INV_%1s_%2$tY%2$tm%2$td.xml", 
						produzione ? configuration.getUsernameProduzione() : configuration.getUsernameTest(), 
						creationDate);
		String zipFilename = 
				String.format("INV_%1s_%2$tY%2$tm%2$td.zip", 
						produzione ? configuration.getUsernameProduzione() : configuration.getUsernameTest(), 
						creationDate);
		File zipFile = new File(targetFolder, zipFilename);
		
		// Generate XML representation.
		//
		try {
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
			zos.putNextEntry(new ZipEntry(xmlFilename));
		    generateXml(documenti, creationDate, zos);
		    zos.flush();
		    zos.close();
		    
		} catch(Exception e) {
			
			throw new RuntimeException("Exception caught while using JAXB to marshal messaggio object.", e);
		}
	}
	
	
	public void generateXml(List<Documento> documenti, Date creationDate, OutputStream os) {
		
		// Prepare temporary in-memory output-stream.
		//
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		// Create messaggio object to be marshalled.
		//		
		Messaggio messaggio = createMessaggio(documenti, creationDate);
		
		// Generate XML representation.
		//
		try {
		    JAXBContext jaxbContext = JAXBContext.newInstance(messaggio.getClass());
		    javax.xml.bind.Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		    jaxbMarshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);

		    jaxbMarshaller.marshal(messaggio, baos);
		    
		    baos.flush();
		    
		} catch(Exception e) {
			
			throw new RuntimeException("Exception caught while using JAXB to marshal messaggio object.", e);
		}
		
		// Post-process XML header.
		//
		postProcessXml(new ByteArrayInputStream(baos.toByteArray()), os);
	}
	
	
	private void postProcessXml(InputStream is, OutputStream os) {
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

			br.readLine();
			br.readLine();
			
			bw.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\"?>");
			bw.newLine();
			bw.write("<Messaggio xmlns=\"http://www.iconamanagement.com\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.iconamanagement.com fattura.xsd\">");
			bw.newLine();
			
			int c;
			while((c = br.read()) >= 0) {
				bw.write(c);
			}
			
			bw.flush();
			bw.close();
			
		} catch(Exception e) {
			
			throw new RuntimeException("Exception caught while postprocessing XML document.", e);			
		}
	}
	
	
	private Messaggio createMessaggio(List<Documento> documenti, Date creationDate) {
		
		// Create messaggio object to be marshalled.
		//		
		Messaggio messaggio = new Messaggio();
		
		// Set the list of documents to be included.
		//
		messaggio.setDocumenti(injectConfiguration(documenti));
				
		// Create dati trasmissione header object.
		//
		DatiTrasmissione datiTrasmissione = createDatiTrasmissione(creationDate);
		messaggio.setDatiTrasmissione(datiTrasmissione);
		
		// Return created object.
		//
		return messaggio;
	}
	
	
	private List<Documento> injectConfiguration(List<Documento> documenti) {
		
		Fornitore fornitore = configuration.getFornitore();

		// Build fixed riferimento documento object.
		//
		RiferimentoDocumento riferimentoDocumento = new RiferimentoDocumento();
		riferimentoDocumento.setTipoDocumento("SERVIZI");
		riferimentoDocumento.setDataDocumento("");
		riferimentoDocumento.setNumeroDocumento(null);
		
		// Iterate on passed list and perform injection.
		//
		for(Documento documento : documenti) {
			
			documento.getFornitore().setDescrizione(fornitore.getDescrizione());
			documento.getFornitore().setDescrizione2(fornitore.getDescrizione2());
			documento.getFornitore().setIndirizzo(fornitore.getIndirizzo());
			documento.getFornitore().setCitta(fornitore.getCitta());
			documento.getFornitore().setProvincia(fornitore.getProvincia());
			documento.getFornitore().setCap(fornitore.getCap());
			documento.getFornitore().setCodicePaese(fornitore.getCodicePaese());
			documento.getFornitore().setDatiFiscali(fornitore.getDatiFiscali());
			
			documento.setRiferimentoDocumento(riferimentoDocumento);
		}
		
		return documenti;
	}
	
	
	private DatiTrasmissione createDatiTrasmissione(Date creationDate) {

		// Create dati trasmissione object.
		//
		DatiTrasmissione datiTrasmissione = new DatiTrasmissione();
		
		// Set the unique id of this object (UUIDs are used).
		//
		datiTrasmissione.setIdentificativo(UUID.randomUUID().toString());
		
		// Set date fields.
		//
		datiTrasmissione.setDataCreazione(sdf.format(creationDate));
		datiTrasmissione.setDataTrasmissione(sdf.format(creationDate));
		
		// Return created object.
		//
		return datiTrasmissione;
	}
}
