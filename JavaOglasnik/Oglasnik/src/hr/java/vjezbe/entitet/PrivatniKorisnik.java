package hr.java.vjezbe.entitet;

import java.io.Serializable;

public class PrivatniKorisnik extends Korisnik implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6969331475773875695L;
	private String ime;
	private String prezime;

	public PrivatniKorisnik(String ime, String prezime, String email, String telefon, Long id) {
		super(id, email, telefon);
		this.ime = ime;
		this.prezime = prezime;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	@Override
	public String dohvatiKontakt() {
			//String Kontakt = "";
		return new String("Osobni podaci prodavatelja: " + this.getIme() + " " + this.getPrezime() + ", " + "mail: " + this.getEmail() + ", " + "tel: " + this.getTelefon());
	}
	
	@Override
	public String toString() {
		return String.format(this.getIme() + " " + this.getPrezime() + "\nEmail: " + this.getEmail() + "\nTel: " + this.getTelefon());
	}

}
