package hr.java.vjezbe.entitet;

import java.io.Serializable;

public class PoslovniKorisnik extends Korisnik implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7906240740793240521L;

	public PoslovniKorisnik( String naziv, String web,String email, String telefon, Long id) {
		super(id, email, telefon);
		this.naziv = naziv;
		this.web = web;
	}

	private String naziv;
	private String web;
	
	@Override
	public String dohvatiKontakt() {
		// TODO Auto-generated method stub
		return new String("Naziv tvrtke: " + this.getNaziv() + ", " + "mail: " + this.getEmail() + ", " + "tel: " + this.getTelefon() + ", " + "web: " + this.getWeb());
	}
	
	@Override
	public String toString() {
		return String.format(this.getNaziv() + "\nEmail: " + this.getEmail() + "~nWeb: " + this.getWeb() + "\nTel: " + this.getTelefon());
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

}
