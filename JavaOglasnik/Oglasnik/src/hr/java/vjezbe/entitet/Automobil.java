package hr.java.vjezbe.entitet;

import java.io.Serializable;
import java.math.BigDecimal;


import hr.java.vjezbe.iznimke.NemoguceOdreditiGrupuOsiguranjaException;

public class Automobil extends Artikl implements Vozilo, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7582805146552226534L;

	public BigDecimal snagaKs;

	public Automobil(String naslov, String opis, BigDecimal cijena, BigDecimal snagaKs, Stanje stanje, Long id) {
		super(naslov, opis, cijena, stanje, id);
		this.snagaKs = snagaKs;
	}

	public Automobil() {

	}

	@Override
	public BigDecimal izracunajGrupuOsiguranja() throws NemoguceOdreditiGrupuOsiguranjaException {
		BigDecimal grupaOsiguranja = null;
		if ((snagaKs.compareTo(new BigDecimal(100)) < 0) && (snagaKs.compareTo(new BigDecimal(0)) > 0)) {
			grupaOsiguranja = new BigDecimal(1);
			return grupaOsiguranja;
		}
		if ((snagaKs.compareTo(new BigDecimal(150)) < 0) && (snagaKs.compareTo(new BigDecimal(100)) >= 0)) {
			grupaOsiguranja = new BigDecimal(2);
			return grupaOsiguranja;
		}
		if ((snagaKs.compareTo(new BigDecimal(175)) < 0) && (snagaKs.compareTo(new BigDecimal(150)) >= 0)) {
			grupaOsiguranja = new BigDecimal(3);
			return grupaOsiguranja;
		}
		if ((snagaKs.compareTo(new BigDecimal(200)) < 0) && (snagaKs.compareTo(new BigDecimal(175)) >= 0)) {
			grupaOsiguranja = new BigDecimal(4);
			return grupaOsiguranja;
		}
		if ((snagaKs.compareTo(new BigDecimal(225)) < 0) && (snagaKs.compareTo(new BigDecimal(200)) >= 0)) {
			grupaOsiguranja = new BigDecimal(5);
			return grupaOsiguranja;
		} else {
			throw new NemoguceOdreditiGrupuOsiguranjaException("Previse kw, ne mogu odrediti grupu osiguranja. ");
		}
	}

	@Override
	public String tekstOglasa(){
		String oglas = null;
		oglas = (this.getNaslov() + ", " + this.getOpis()
		+ ", Snaga: " + this.getSnagaKs() + ", Cijena: " + this.getCijena() + ", Stanje:  " + this.getStanje().getOpis());
		return oglas;
	}
	
	@Override
	public String toString() {
		return String.format(getOpis()+"\nCijena: "+getCijena().toString()+"kn"+"\nSnaga: "+snagaKs.toString()+"\nStanje: "+getStanje().getOpis());
	}

	public BigDecimal getSnagaKs() {
		return snagaKs;
	}

	public void setSnagaKs(BigDecimal snagaKs) {
		this.snagaKs = snagaKs;
	}

}
