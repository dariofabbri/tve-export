package it.dariofabbri.tve.export.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="RiferimentoDocumento")
public class RiferimentoDocumento implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name="TipoDocumento")
	private String tipoDocumento;
	
	@XmlElement(name="DataDocumento")
	private String dataDocumento;
	
	@XmlElement(name="NumeroDocumento")
	private String numeroDocumento;

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getDataDocumento() {
		return dataDocumento;
	}

	public void setDataDocumento(String dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
}
