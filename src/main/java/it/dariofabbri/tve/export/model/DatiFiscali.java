package it.dariofabbri.tve.export.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="DatiFiscali")
public class DatiFiscali implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name="PartitaIVA")
	private String partitaIva;

	@XmlElement(name="CapitaleSociale")
	private String capitaleSociale;

	@XmlElement(name="NumeroRegistroImprese")
	private String numeroRegistroImprese;

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public String getCapitaleSociale() {
		return capitaleSociale;
	}

	public void setCapitaleSociale(String capitaleSociale) {
		this.capitaleSociale = capitaleSociale;
	}

	public String getNumeroRegistroImprese() {
		return numeroRegistroImprese;
	}

	public void setNumeroRegistroImprese(String numeroRegistroImprese) {
		this.numeroRegistroImprese = numeroRegistroImprese;
	}
}
