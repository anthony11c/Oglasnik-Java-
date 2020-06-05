package hr.java.vjezbe.entitet;

import java.io.Serializable;
import java.time.LocalDate;

public class Prodaja extends Entitet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8608474630648989475L;
	private Artikl artikl;
	private Korisnik korisnik;
	private LocalDate datumObjave;

	public Prodaja(Artikl artikl, Korisnik korisnik, LocalDate datumObjave) {
		this.artikl = artikl;
		this.korisnik = korisnik;
		this.datumObjave = datumObjave;
	}
	

	public Artikl getArtikl() {
		return artikl;
	}

	public void setArtikl(Artikl artikl) {
		this.artikl = artikl;
	}

	public Korisnik getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}

	public LocalDate getDatumObjave() {
		return datumObjave;
	}

	public void setDatumObjave(LocalDate datumObjave) {
		this.datumObjave = datumObjave;
	}
	
	
}
