package it.dariofabbri.tve.export.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Documento")
public class Documento implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name="TipoDocumento")
	private String tipoDocumento;

	@XmlElement(name="Produzione")
	private String produzione;

	@XmlElement(name="NumeroDocumento")
	private String numeroDocumento;

	@XmlElement(name="DataDocumento")
	private String dataDocumento;

	@XmlElement(name="Valuta")
	private String valuta;

	@XmlElement(name="TestoLibero")
	private TestoLibero testoLibero;
	
	@XmlElement(name="RiferimentoDocumento")
	private RiferimentoDocumento riferimentoDocumento;

	@XmlElement(name="Fornitore")
	private Fornitore fornitore;

	@XmlElement(name="Cliente")
	private Cliente cliente;

	@XmlElement(name="CondizioniPagamento")
	private CondizioniPagamento condizioniPagamento;

	@XmlElementWrapper(name="Dettaglio")
	@XmlElement(name="Prodotto")
	private List<Prodotto> prodotti;

	@XmlElement(name="Importo")
	private Importo importo;
	
	@XmlElement(name="Tassa")
	private Tassa tassa;
	
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getProduzione() {
		return produzione;
	}

	public void setProduzione(String produzione) {
		this.produzione = produzione;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getDataDocumento() {
		return dataDocumento;
	}

	public void setDataDocumento(String dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	public String getValuta() {
		return valuta;
	}

	public void setValuta(String valuta) {
		this.valuta = valuta;
	}

	public TestoLibero getTestoLibero() {
		return testoLibero;
	}

	public void setTestoLibero(TestoLibero testoLibero) {
		this.testoLibero = testoLibero;
	}

	public RiferimentoDocumento getRiferimentoDocumento() {
		return riferimentoDocumento;
	}

	public void setRiferimentoDocumento(
			RiferimentoDocumento riferimentoDocumento) {
		this.riferimentoDocumento = riferimentoDocumento;
	}

	public Fornitore getFornitore() {
		return fornitore;
	}

	public void setFornitore(Fornitore fornitore) {
		this.fornitore = fornitore;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public CondizioniPagamento getCondizioniPagamento() {
		return condizioniPagamento;
	}

	public void setCondizioniPagamento(CondizioniPagamento condizioniPagamento) {
		this.condizioniPagamento = condizioniPagamento;
	}

	public List<Prodotto> getProdotti() {
		return prodotti;
	}

	public void setProdotti(List<Prodotto> prodotti) {
		this.prodotti = prodotti;
	}

	public Importo getImporto() {
		return importo;
	}

	public void setImporto(Importo importo) {
		this.importo = importo;
	}

	public Tassa getTassa() {
		return tassa;
	}

	public void setTassa(Tassa tassa) {
		this.tassa = tassa;
	}
}
