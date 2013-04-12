package it.dariofabbri.tve.export.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Prezzo")
public class Prezzo implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name="PrezzoCessione")
	private String prezzoCessione;

	public String getPrezzoCessione() {
		return prezzoCessione;
	}

	public void setPrezzoCessione(String prezzoCessione) {
		this.prezzoCessione = prezzoCessione;
	}
}
