package it.dariofabbri.tve.export.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Messaggio")
@XmlAccessorType(XmlAccessType.FIELD)
public class Messaggio implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name="DatiTrasmissione")
	private DatiTrasmissione datiTrasmissione;
	
	@XmlElement(name="Documento")
	private List<Documento> documenti;

	public DatiTrasmissione getDatiTrasmissione() {
		return datiTrasmissione;
	}

	public void setDatiTrasmissione(DatiTrasmissione datiTrasmissione) {
		this.datiTrasmissione = datiTrasmissione;
	}

	public List<Documento> getDocumenti() {
		return documenti;
	}

	public void setDocumenti(List<Documento> documenti) {
		this.documenti = documenti;
	}
}
