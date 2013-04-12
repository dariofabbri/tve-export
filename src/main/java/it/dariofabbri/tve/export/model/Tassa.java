package it.dariofabbri.tve.export.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Tassa")
public class Tassa implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name="PercentualeIVA")
	private String percentualeIva;

	@XmlElement(name="Imponibile")
	private String imponibile;

	@XmlElement(name="ImportoTassa")
	private String importoTassa;

	public String getPercentualeIva() {
		return percentualeIva;
	}

	public void setPercentualeIva(String percentualeIva) {
		this.percentualeIva = percentualeIva;
	}

	public String getImponibile() {
		return imponibile;
	}

	public void setImponibile(String imponibile) {
		this.imponibile = imponibile;
	}

	public String getImportoTassa() {
		return importoTassa;
	}

	public void setImportoTassa(String importoTassa) {
		this.importoTassa = importoTassa;
	}
}
