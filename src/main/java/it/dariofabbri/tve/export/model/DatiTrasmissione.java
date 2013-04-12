package it.dariofabbri.tve.export.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="DatiTrasmissione")
public class DatiTrasmissione implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name="Identificativo")
	private String identificativo;
	
	@XmlElement(name="DataCreazione")
	private String dataCreazione;
	
	@XmlElement(name="DataTrasmissione")
	private String dataTrasmissione;

	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	public String getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(String dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public String getDataTrasmissione() {
		return dataTrasmissione;
	}

	public void setDataTrasmissione(String dataTrasmissione) {
		this.dataTrasmissione = dataTrasmissione;
	}
}
