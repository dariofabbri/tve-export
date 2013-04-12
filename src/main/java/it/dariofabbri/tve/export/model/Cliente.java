package it.dariofabbri.tve.export.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Cliente")
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name="Descrizione")
	private String descrizione;

	@XmlElement(name="Descrizione2")
	private String descrizione2;

	@XmlElement(name="Indirizzo")
	private String indirizzo;

	@XmlElement(name="Indirizzo2")
	private String indirizzo2;

	@XmlElement(name="Citta")
	private String citta;

	@XmlElement(name="Provincia")
	private String provincia;

	@XmlElement(name="CAP")
	private String cap;

	@XmlElement(name="CodicePaese")
	private String codicePaese;

	@XmlElement(name="DatiFiscali")
	private DatiFiscali datiFiscali;

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getDescrizione2() {
		return descrizione2;
	}

	public void setDescrizione2(String descrizione2) {
		this.descrizione2 = descrizione2;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getIndirizzo2() {
		return indirizzo2;
	}

	public void setIndirizzo2(String indirizzo2) {
		this.indirizzo2 = indirizzo2;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCodicePaese() {
		return codicePaese;
	}

	public void setCodicePaese(String codicePaese) {
		this.codicePaese = codicePaese;
	}

	public DatiFiscali getDatiFiscali() {
		return datiFiscali;
	}

	public void setDatiFiscali(DatiFiscali datiFiscali) {
		this.datiFiscali = datiFiscali;
	}
}
