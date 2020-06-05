package hr.java.vjezbe.entitet;

import java.io.Serializable;
import java.math.BigDecimal;


public class Stan extends Artikl implements Nekretnina, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8454591410552984037L;
	private int kvadratura;
	private String izvodjac;

	public Stan( String naslov, String opis, BigDecimal cijena,int kvadratura, Stanje stanje, Long id) {
		super( naslov, opis, cijena, stanje, id);
		this.kvadratura = kvadratura;
		
	}

	@Override
	public String tekstOglasa(){
		String oglas = null;
		oglas = (this.getNaslov() + ", " + this.getOpis()
		+ ", Kvadratura: " + this.kvadratura + ", Stanje: " + this.getStanje().getOpis() + ", Cijena: " + this.getCijena());
		return oglas;
	}

	@Override
	public String toString() {
		return String.format(getNaslov()+" "+getOpis()+"\nCijena: "+getCijena().toString()+"kn"+"\nKvadratura: "+Integer.valueOf(getKvadratura()).toString()+"\nStanje: "+getStanje().getOpis());
	}
	
	public int getKvadratura() {
		return kvadratura;
	}

	public void setKvadratura(int kvadratura) {
		this.kvadratura = kvadratura;
	}

	public String getIzvodjac() {
		return izvodjac;
	}

	public void setIzvodjac(String izvodjac) {
		this.izvodjac = izvodjac;
	}

}
