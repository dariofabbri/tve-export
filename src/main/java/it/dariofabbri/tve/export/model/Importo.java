package it.dariofabbri.tve.export.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Importo")
public class Importo implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name="ImponibileTotale")
	private String imponibileTotale;

	@XmlElement(name="ImportoTotaleTassa")
	private String importoTotaleTassa;

	@XmlElement(name="ImportoTotale")
	private String importoTotale;

	@XmlElement(name="ImportoTotaleEuro")
	private String importoTotaleEuro;

	public String getImponibileTotale() {
		return imponibileTotale;
	}

	public void setImponibileTotale(String imponibileTotale) {
		this.imponibileTotale = imponibileTotale;
	}

	public String getImportoTotaleTassa() {
		return importoTotaleTassa;
	}

	public void setImportoTotaleTassa(String importoTotaleTassa) {
		this.importoTotaleTassa = importoTotaleTassa;
	}

	public String getImportoTotale() {
		return importoTotale;
	}

	public void setImportoTotale(String importoTotale) {
		this.importoTotale = importoTotale;
	}

	public String getImportoTotaleEuro() {
		return importoTotaleEuro;
	}

	public void setImportoTotaleEuro(String importoTotaleEuro) {
		this.importoTotaleEuro = importoTotaleEuro;
	}
}
