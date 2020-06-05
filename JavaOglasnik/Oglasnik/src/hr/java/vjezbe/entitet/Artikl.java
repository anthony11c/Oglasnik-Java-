package hr.java.vjezbe.entitet;

import java.io.Serializable;
import java.math.BigDecimal;

import hr.java.vjezbe.iznimke.CijenaJePreniskaException;
import hr.java.vjezbe.iznimke.NemoguceOdreditiGrupuOsiguranjaException;

public abstract class Artikl extends Entitet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Artikl( String naslov, String opis, BigDecimal cijena, Stanje stanje, Long id) {
		super(id);
		this.naslov = naslov;
		this.opis = opis;
		this.cijena = cijena;
		this.stanje = stanje;
	}

	public Artikl() {

	}

	private String naslov;
	private String opis;
	private BigDecimal cijena;
	private Stanje stanje;

	public String getNaslov() {
		return naslov;
	}

	public void setNaslov(String naslov) {
		this.naslov = naslov;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public BigDecimal getCijena() {
		return cijena;
	}

	public void setCijena(BigDecimal cijena) {
		this.cijena = cijena;
	}

	public Stanje getStanje() {
		return stanje;
	}

	public void setStanje(Stanje stanje) {
		this.stanje = stanje;
	}

	public abstract String tekstOglasa() throws NemoguceOdreditiGrupuOsiguranjaException, CijenaJePreniskaException;

}
