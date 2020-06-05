package hr.java.vjezbe.entitet;

import java.math.BigDecimal;

import hr.java.vjezbe.iznimke.CijenaJePreniskaException;

public interface Nekretnina {
	public default BigDecimal izracunajPorez(BigDecimal cijenaNekretnine) throws CijenaJePreniskaException {

		BigDecimal porez = cijenaNekretnine.multiply(new BigDecimal(0.03));
		if(porez.compareTo(new BigDecimal(10000)) < 0) {
			throw new CijenaJePreniskaException("Cijena ne smije biti manja od 10000kn!");
		}
		return porez;
	}

}
