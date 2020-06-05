package hr.java.vjezbe.niti;


import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Prodaja;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DatumObjaveNit implements Runnable {

	@SuppressWarnings("unused")
	private String name;

	public DatumObjaveNit(String name) {
		this.name = name;
	}

	@Override
	public void run() {

		try {
			Prodaja prodaja = BazaPodataka.dohvatiZadnjuProdaju(null);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Zadnje unešeni artikl");
			alert.setHeaderText(null);
			alert.setContentText("Oglas: " + prodaja.getArtikl().toString() + "\n\nProdavatelj: "
					+ prodaja.getKorisnik().toString() + "\nDatum objave: "
					+ prodaja.getDatumObjave().toString());
			alert.showAndWait();
			
		} catch (Exception | BazaPodatakaException e) {
		}
	}

	public void start() {

	}
}
