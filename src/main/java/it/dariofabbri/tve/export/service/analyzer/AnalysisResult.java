package it.dariofabbri.tve.export.service.analyzer;

import it.dariofabbri.tve.export.model.Documento;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class AnalysisResult {

	List<Pair<Integer, String>> messaggi;
	List<Documento> documenti;

	public AnalysisResult() {
		
		messaggi = new ArrayList<Pair<Integer,String>>();
		documenti = new ArrayList<Documento>();
	}
	
	public List<Pair<Integer, String>> getMessaggi() {
		return messaggi;
	}

	public void setMessaggi(List<Pair<Integer, String>> messaggi) {
		this.messaggi = messaggi;
	}

	public void addMessaggio(Integer pageNumber, String messaggio) {
		
		Pair<Integer, String> pair = new ImmutablePair<Integer, String>(pageNumber, messaggio);
		this.messaggi.add(pair);
	}

	public List<Documento> getDocumenti() {
		return documenti;
	}

	public void setDocumenti(List<Documento> documenti) {
		this.documenti = documenti;
	}
	
	public void addDocumento(Documento documento) {
		this.documenti.add(documento);
	}
}
