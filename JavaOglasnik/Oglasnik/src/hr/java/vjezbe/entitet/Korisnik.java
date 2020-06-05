package hr.java.vjezbe.entitet;

public abstract class Korisnik extends Entitet {
	private String email;
	private String telefon;

	public Korisnik(Long id, String email, String telefon) {
		super(id);

		this.email = email;
		this.telefon = telefon;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	
	//apstraktna metoda
	public abstract String dohvatiKontakt();

}
