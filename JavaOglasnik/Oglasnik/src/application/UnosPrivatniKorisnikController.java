package application;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.OptionalLong;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Artikl;
import hr.java.vjezbe.entitet.Automobil;
import hr.java.vjezbe.entitet.Entitet;
import hr.java.vjezbe.entitet.Korisnik;
import hr.java.vjezbe.entitet.PoslovniKorisnik;
import hr.java.vjezbe.entitet.PrivatniKorisnik;
import hr.java.vjezbe.entitet.Prodaja;
import hr.java.vjezbe.entitet.Stan;
import hr.java.vjezbe.entitet.Stanje;
import hr.java.vjezbe.entitet.Usluga;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class UnosPrivatniKorisnikController {

	private TableView<Automobil> table = new TableView<>();
	private TableView<Stan> tableS = new TableView<>();
	private TableView<Usluga> tableU = new TableView<>();
	private TableView<PrivatniKorisnik> tablePrivate = new TableView<>();
	private TableView<PoslovniKorisnik> tablePoslovni = new TableView<>();
	private TableView<Prodaja> tableProdaja = new TableView<>();
	private ObservableList<Automobil> data;
	private ObservableList<Stan> dataS;
	private ObservableList<Usluga> dataU;
	private ObservableList<PrivatniKorisnik> dataPrivate;
	private ObservableList<PoslovniKorisnik> dataPoslovni;
	private ObservableList<Prodaja> dataProdaja;

	@FXML
	private TextField ime;

	@FXML
	private TextField prezime;

	@FXML
	private TextField email;

	@FXML
	private TextField telefon;

	@FXML
	public void unosNovogPrivatnogKorisnikaWindow() {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("PrivatniKorisnikUnos.fxml"));
			Scene scene = new Scene(root, 800, 700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Main.getPrimaryStage().setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void unosNovogPrivatnogKorisnika() throws BazaPodatakaException, Exception {
		String errorText = new String();
		if (ime.getText().isBlank() || prezime.getText().isBlank() || email.getText().isBlank()
				|| telefon.getText().isBlank()) {
			if (ime.getText().isBlank()) {
				errorText += "Ime je obavezno!" + System.lineSeparator();
			}
			if (prezime.getText().isBlank()) {
				errorText += "Prezime je obavezno!" + System.lineSeparator();
			}
			if (email.getText().isBlank()) {
				errorText += "Email je obavezan!" + System.lineSeparator();
			}
			if (telefon.getText().isBlank()) {
				errorText += "Telefon je obavezan !" + System.lineSeparator();
			}
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Fale podaci!");
			alert.setContentText(errorText);
			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Unos");
			alert.setHeaderText(null);
			alert.setContentText("Uspješan unos!");
			alert.showAndWait();

			List<PrivatniKorisnik> listaKorisnika = BazaPodataka.dohvatiPrivatneKorisnikePremaKriterijima(null);
			OptionalLong maxId = listaKorisnika.stream().mapToLong(Entitet::getId).max();
			PrivatniKorisnik korisnik = new PrivatniKorisnik(ime.getText(), prezime.getText(), email.getText(),
					telefon.getText(), maxId.getAsLong() + 1);
			BazaPodataka.pohraniNovogPrivatnogKorisnika(korisnik);
		}
		prikaziEkranZaPrivatneKorisnike();
	}

	@SuppressWarnings("unchecked")
	@FXML
	public void prikaziEkranZaAutomobile() throws Exception, BazaPodatakaException {

		try {
			data = FXCollections.observableList(BazaPodataka.dohvatiAutomobilePremaKriterijima(null));

			TableColumn<Automobil, String> naslov = new TableColumn<>("NASLOV");
			naslov.setMinWidth(70);
			naslov.setCellValueFactory(new PropertyValueFactory<>("naslov"));
			TableColumn<Automobil, String> opis = new TableColumn<>("OPIS");
			opis.setMinWidth(70);
			opis.setCellValueFactory(new PropertyValueFactory<>("opis"));
			TableColumn<Automobil, BigDecimal> snaga = new TableColumn<>("SNAGA");
			snaga.setMinWidth(70);
			snaga.setCellValueFactory(new PropertyValueFactory<>("snagaKs"));
			TableColumn<Automobil, BigDecimal> cijena = new TableColumn<>("CIJENA");
			cijena.setMinWidth(70);
			cijena.setCellValueFactory(new PropertyValueFactory<>("cijena"));

			table.setItems(data);
			table.getColumns().addAll(naslov, opis, snaga, cijena);

			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("Automobili.fxml"));
			Scene scene = new Scene(root, 800, 700);
			root.setBottom(table);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Main.getPrimaryStage().setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public void prikaziEkranZaStanove() throws Exception, BazaPodatakaException {

		try {
			dataS = FXCollections.observableList(BazaPodataka.dohvatiStanovePremaKriterijima(null));

			TableColumn<Stan, String> naslov = new TableColumn<>("NASLOV");
			naslov.setMinWidth(70);
			naslov.setCellValueFactory(new PropertyValueFactory<>("naslov"));
			TableColumn<Stan, String> opis = new TableColumn<>("OPIS");
			opis.setMinWidth(70);
			opis.setCellValueFactory(new PropertyValueFactory<>("opis"));
			TableColumn<Stan, Integer> kvadratura = new TableColumn<>("KVADRATURA");
			kvadratura.setMinWidth(70);
			kvadratura.setCellValueFactory(new PropertyValueFactory<>("kvadratura"));
			TableColumn<Stan, BigDecimal> cijena = new TableColumn<>("CIJENA");
			cijena.setMinWidth(70);
			cijena.setCellValueFactory(new PropertyValueFactory<>("cijena"));
			TableColumn<Stan, Stanje> stanje = new TableColumn<>("STANJE");
			stanje.setMinWidth(70);
			stanje.setCellValueFactory(new PropertyValueFactory<>("stanje"));

			tableS.setMaxHeight(200);
			tableS.setItems(dataS);
			tableS.getColumns().addAll(naslov, opis, kvadratura, cijena, stanje);

			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("Stanovi.fxml"));
			Scene scene = new Scene(root, 800, 700);
			root.setBottom(tableS);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Main.getPrimaryStage().setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void prikaziEkranZaUsluge() throws Exception, BazaPodatakaException {

		try {
			dataU = FXCollections.observableList(BazaPodataka.dohvatiUslugePremaKriterijima(null));

			TableColumn<Usluga, String> naslov = new TableColumn<>("NASLOV");
			naslov.setMinWidth(70);
			naslov.setCellValueFactory(new PropertyValueFactory<>("naslov"));
			TableColumn<Usluga, String> opis = new TableColumn<>("OPIS");
			opis.setMinWidth(70);
			opis.setCellValueFactory(new PropertyValueFactory<>("opis"));
			TableColumn<Usluga, BigDecimal> cijena = new TableColumn<>("CIJENA");
			cijena.setMinWidth(70);
			cijena.setCellValueFactory(new PropertyValueFactory<>("cijena"));
			TableColumn<Usluga, Stanje> stanje = new TableColumn<>("STANJE");
			stanje.setMinWidth(70);
			stanje.setCellValueFactory(new PropertyValueFactory<>("stanje"));

			tableU.setMaxHeight(200);
			tableU.setItems(dataU);
			tableU.getColumns().addAll(naslov, opis, cijena, stanje);

			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("Usluge.fxml"));
			Scene scene = new Scene(root, 800, 700);
			root.setBottom(tableU);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Main.getPrimaryStage().setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void prikaziEkranZaPrivatneKorisnike() throws Exception, BazaPodatakaException {

		try {
			dataPrivate = FXCollections.observableList(BazaPodataka.dohvatiPrivatneKorisnikePremaKriterijima(null));

			TableColumn<PrivatniKorisnik, String> ime = new TableColumn<>("IME");
			ime.setMinWidth(70);
			ime.setCellValueFactory(new PropertyValueFactory<>("ime"));
			TableColumn<PrivatniKorisnik, String> prezime = new TableColumn<>("PREZIME");
			prezime.setMinWidth(70);
			prezime.setCellValueFactory(new PropertyValueFactory<>("prezime"));
			TableColumn<PrivatniKorisnik, String> email = new TableColumn<>("EMAIL");
			email.setMinWidth(70);
			email.setCellValueFactory(new PropertyValueFactory<>("email"));
			TableColumn<PrivatniKorisnik, String> telefon = new TableColumn<>("TELEFON");
			telefon.setMinWidth(70);
			telefon.setCellValueFactory(new PropertyValueFactory<>("telefon"));

			tablePrivate.setItems(dataPrivate);
			tablePrivate.getColumns().addAll(ime, prezime, email, telefon);

			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("PrivatniKorisnik.fxml"));
			Scene scene = new Scene(root, 800, 700);
			root.setBottom(tablePrivate);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Main.getPrimaryStage().setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public void prikaziEkranZaPoslovneKorisnike() throws Exception, BazaPodatakaException {

		try {
			dataPoslovni = FXCollections.observableList(BazaPodataka.dohvatiPoslovneKorisnikePremaKriterijima(null));

			TableColumn<PoslovniKorisnik, String> naziv = new TableColumn<>("NAZIV");
			naziv.setMinWidth(70);
			naziv.setCellValueFactory(new PropertyValueFactory<>("naziv"));
			TableColumn<PoslovniKorisnik, String> web = new TableColumn<>("WEB");
			web.setMinWidth(70);
			web.setCellValueFactory(new PropertyValueFactory<>("web"));
			TableColumn<PoslovniKorisnik, String> email = new TableColumn<>("EMAIL");
			email.setMinWidth(70);
			email.setCellValueFactory(new PropertyValueFactory<>("email"));
			TableColumn<PoslovniKorisnik, String> telefon = new TableColumn<>("TELEFON");
			telefon.setMinWidth(70);
			telefon.setCellValueFactory(new PropertyValueFactory<>("telefon"));

			tablePoslovni.setItems(dataPoslovni);
			tablePoslovni.getColumns().addAll(naziv, web, email, telefon);

			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("PoslovniKorisnik.fxml"));
			Scene scene = new Scene(root, 800, 700);
			root.setBottom(tablePoslovni);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Main.getPrimaryStage().setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void unosNovogAutomobilaWindow() {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("AutomobiliUnos.fxml"));
			Scene scene = new Scene(root, 800, 700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Main.getPrimaryStage().setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void unosNoveUslugeWindow() {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("UslugaUnos.fxml"));
			Scene scene = new Scene(root, 800, 700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Main.getPrimaryStage().setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void unosNovogStanaWindow() {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("StanoviUnos.fxml"));
			Scene scene = new Scene(root, 800, 700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Main.getPrimaryStage().setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void unosNovogPoslovnogKorisnikaWindow() {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("PoslovniKorisnikUnos.fxml"));
			Scene scene = new Scene(root, 800, 700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Main.getPrimaryStage().setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@FXML
	public void EkranProdaje() throws SQLException, BazaPodatakaException {
		try {
			dataProdaja = FXCollections.observableList(BazaPodataka.dohvatiProdajuPremaKriterijima(null));

			TableColumn<Prodaja, Artikl> artikl = new TableColumn<>("ARTIKL");
			artikl.setMinWidth(70);
			artikl.setCellValueFactory(new PropertyValueFactory<>("artikl"));
			TableColumn<Prodaja, Korisnik> korisnik = new TableColumn<>("KORISNIK");
			korisnik.setMinWidth(70);
			korisnik.setCellValueFactory(new PropertyValueFactory<>("korisnik"));
			TableColumn<Prodaja, LocalDate> datum = new TableColumn<>("DATUM");
			datum.setMinWidth(70);
			datum.setCellValueFactory(new PropertyValueFactory<>("datumObjave"));
			tableProdaja.setItems(dataProdaja);
			tableProdaja.getColumns().addAll(artikl, korisnik, datum);

			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("Prodaja.fxml"));
			Scene scene = new Scene(root, 800, 700);
			root.setBottom(tableProdaja);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Main.getPrimaryStage().setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void unosNoveProdajeWindow() {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("ProdajaUnos.fxml"));
			Scene scene = new Scene(root, 800, 700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Main.getPrimaryStage().setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
