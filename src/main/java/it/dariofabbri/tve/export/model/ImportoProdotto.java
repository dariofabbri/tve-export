package it.dariofabbri.tve.export.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ImportoProdotto")
public class ImportoProdotto implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name="Imponibile")
	private String imponibile;

	public String getImponibile() {
		return imponibile;
	}

	public void setImponibile(String imponibile) {
		this.imponibile = imponibile;
	}
}
