package hr.java.vjezbe.entitet;

import java.math.BigDecimal;

public class Usluga extends Artikl {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8418379135128057504L;

	public Usluga(String naslov, String opis, BigDecimal cijena, Stanje stanje, Long id) {
		super( naslov, opis, cijena, stanje, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String tekstOglasa() {
		// TODO Auto-generated method stub
		return new String(this.getNaslov() + ",  " + this.getOpis()
				+ ", Cijena: " + this.getCijena() + "kn");
	}

	@Override
	public String toString() {
		return String.format(getOpis()+"\nCijena: "+getCijena().toString()+"kn"+"\nStanje: "+getStanje().getOpis());
	}
}
