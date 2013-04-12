package it.dariofabbri.tve.export.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Prodotto")
public class Prodotto implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name="Progressivo")
	private Integer progressivo;

	@XmlElement(name="CodiceProdotto")
	private String codiceProdotto;

	@XmlElement(name="CodificaProdotto")
	private String codificaProdotto;

	@XmlElement(name="Descrizione")
	private String descrizione;

	@XmlElement(name="Quantita")
	private String quantita;

	@XmlElement(name="UnitaMisura")
	private String unitaMisura;

	@XmlElement(name="Importo")
	private ImportoProdotto importo;

	@XmlElement(name="Prezzo")
	private Prezzo prezzo;

	public Integer getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(Integer progressivo) {
		this.progressivo = progressivo;
	}

	public String getCodiceProdotto() {
		return codiceProdotto;
	}

	public void setCodiceProdotto(String codiceProdotto) {
		this.codiceProdotto = codiceProdotto;
	}

	public String getCodificaProdotto() {
		return codificaProdotto;
	}

	public void setCodificaProdotto(String codificaProdotto) {
		this.codificaProdotto = codificaProdotto;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getQuantita() {
		return quantita;
	}

	public void setQuantita(String quantita) {
		this.quantita = quantita;
	}

	public String getUnitaMisura() {
		return unitaMisura;
	}

	public void setUnitaMisura(String unitaMisura) {
		this.unitaMisura = unitaMisura;
	}

	public ImportoProdotto getImporto() {
		return importo;
	}

	public void setImporto(ImportoProdotto importo) {
		this.importo = importo;
	}

	public Prezzo getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(Prezzo prezzo) {
		this.prezzo = prezzo;
	}
}
