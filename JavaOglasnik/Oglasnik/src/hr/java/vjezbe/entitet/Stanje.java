package hr.java.vjezbe.entitet;

public enum Stanje {
	novo(1, "novo"),
	izvrsno(2, "izvrsno"),
	rabljeno(3, "rabljeno"),
	neispravno(4, "neispravno");
	
	private int kod;
	private String opis;
	
	private Stanje(int kod, String opis) {
		this.kod = kod;
		this.opis = opis;
	}

	public int getKod() {
		return kod;
	}


	public String getOpis() {
		return opis;
	}

}
