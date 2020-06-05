package hr.java.vjezbe.entitet;

import java.math.BigDecimal;

import hr.java.vjezbe.iznimke.NemoguceOdreditiGrupuOsiguranjaException;

public interface Vozilo {

	public default BigDecimal izracunajKw(BigDecimal konji) {
		BigDecimal kilowati = new BigDecimal(1.341).multiply(konji);
		return kilowati;
	}

	public BigDecimal izracunajGrupuOsiguranja() throws NemoguceOdreditiGrupuOsiguranjaException;

	public default BigDecimal izracunajCijenuOsiguranja() throws NemoguceOdreditiGrupuOsiguranjaException{
		BigDecimal grupe = izracunajGrupuOsiguranja();
		BigDecimal cijenaOsiguranja = null;
		
		switch(grupe.intValueExact()) {
		case(1):
			cijenaOsiguranja = new BigDecimal(1000);
			break;
		case(2):
			cijenaOsiguranja = new BigDecimal(1500);
				break;
		case(3):
			cijenaOsiguranja = new BigDecimal(1800);
			break;
		case(4):
			cijenaOsiguranja = new BigDecimal(2000);
			break;
		case(5):
			cijenaOsiguranja = new BigDecimal(2500);
			break;
		}
		
		return cijenaOsiguranja;
	}
}
