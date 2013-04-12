package it.dariofabbri.tve.export.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="TestoLibero")
public class TestoLibero implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name="Progressivo")
	private Integer progressivo;

	@XmlElement(name="Testo1")
	private String testo1;

	@XmlElement(name="Testo2")
	private String testo2;

	@XmlElement(name="Testo3")
	private String testo3;

	@XmlElement(name="Testo4")
	private String testo4;

	@XmlElement(name="Testo5")
	private String testo5;

	public Integer getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(Integer progressivo) {
		this.progressivo = progressivo;
	}

	public String getTesto1() {
		return testo1;
	}

	public void setTesto1(String testo1) {
		this.testo1 = testo1;
	}

	public String getTesto2() {
		return testo2;
	}

	public void setTesto2(String testo2) {
		this.testo2 = testo2;
	}

	public String getTesto3() {
		return testo3;
	}

	public void setTesto3(String testo3) {
		this.testo3 = testo3;
	}

	public String getTesto4() {
		return testo4;
	}

	public void setTesto4(String testo4) {
		this.testo4 = testo4;
	}

	public String getTesto5() {
		return testo5;
	}

	public void setTesto5(String testo5) {
		this.testo5 = testo5;
	}
}
