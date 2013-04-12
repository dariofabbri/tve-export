package it.dariofabbri.tve.export.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="CondizioniPagamento")
public class CondizioniPagamento implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name="Strumento")
	private String strumento;
	
	@XmlElement(name="Termini")
	private String termini;
	
	@XmlElement(name="Giorni")
	private Integer giorni;

	public String getStrumento() {
		return strumento;
	}

	public void setStrumento(String strumento) {
		this.strumento = strumento;
	}

	public String getTermini() {
		return termini;
	}

	public void setTermini(String termini) {
		this.termini = termini;
	}

	public Integer getGiorni() {
		return giorni;
	}

	public void setGiorni(Integer giorni) {
		this.giorni = giorni;
	}
}
