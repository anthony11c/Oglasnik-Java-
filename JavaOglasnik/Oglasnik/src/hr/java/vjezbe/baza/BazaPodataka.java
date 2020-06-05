package hr.java.vjezbe.baza;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.entitet.Artikl;
import hr.java.vjezbe.entitet.Automobil;
import hr.java.vjezbe.entitet.Korisnik;
import hr.java.vjezbe.entitet.PoslovniKorisnik;
import hr.java.vjezbe.entitet.PrivatniKorisnik;
import hr.java.vjezbe.entitet.Prodaja;
import hr.java.vjezbe.entitet.Stan;
import hr.java.vjezbe.entitet.Stanje;
import hr.java.vjezbe.entitet.Usluga;
import hr.java.vjezbe.iznimke.BazaPodatakaException;

public class BazaPodataka {

	public static final Logger logger = LoggerFactory.getLogger(BazaPodataka.class);

	public static List<Stan> dohvatiStanovePremaKriterijima(Stan stan) throws BazaPodatakaException {
		List<Stan> listaStanova = new ArrayList<>();
		try (Connection connection = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder(
					"SELECT distinct artikl.id, naslov, opis, cijena, kvadratura, stanje.naziv "
							+ "FROM artikl inner join stanje on stanje.id = artikl.idStanje "
							+ "inner join tipArtikla on tipArtikla.id = artikl.idTipArtikla WHERE tipArtikla.naziv = 'Stan'");
			if (Optional.ofNullable(stan).isEmpty() == false) {
				if (Optional.ofNullable(stan).map(Stan::getId).isPresent())
					sqlUpit.append(" AND artikl.id = " + stan.getId());

				if (Optional.ofNullable(stan.getNaslov()).map(String::isBlank).orElse(true) == false)
					sqlUpit.append(" AND artikl.naslov LIKE '%" + stan.getNaslov() + "%'");

				if (Optional.ofNullable(stan.getOpis()).map(String::isBlank).orElse(true) == false)
					sqlUpit.append(" AND artikl.opis LIKE '%" + stan.getOpis() + "%'");

				if (Optional.ofNullable(stan).map(Stan::getCijena).isPresent())
					sqlUpit.append(" AND artikl.cijena = " + stan.getCijena());

				if (Optional.ofNullable(stan).map(Stan::getKvadratura).isPresent())
					sqlUpit.append(" AND artikl.kvadratura = " + stan.getKvadratura());
			}

			Statement query = connection.createStatement();
			ResultSet resultSet = query.executeQuery(sqlUpit.toString());

			while (resultSet.next()) {
				Long id = resultSet.getLong("id");
				String naslov = resultSet.getString("naslov");
				String opis = resultSet.getString("opis");
				BigDecimal cijena = resultSet.getBigDecimal("cijena");
				Integer kvadratura = resultSet.getInt("kvadratura");
				String stanje = resultSet.getString("naziv");

				Stan newStan = new Stan(naslov, opis, cijena, kvadratura, Stanje.valueOf(stanje), id);
				listaStanova.add(newStan);
			}

		} catch (SQLException | IOException e) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, e);
			throw new BazaPodatakaException(poruka, e);
		}
		return listaStanova;
	}

	public static void pohraniNoviStan(Stan stan) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("insert into artikl(Naslov, Opis, Cijena, Kvadratura, idStanje, idTipArtikla) "
							+ "values (?, ?, ?, ?, ?, 3);");
			preparedStatement.setString(1, stan.getNaslov());
			preparedStatement.setString(2, stan.getOpis());
			preparedStatement.setBigDecimal(3, stan.getCijena());
			preparedStatement.setInt(4, stan.getKvadratura());
			preparedStatement.setLong(5, (stan.getStanje().ordinal() + 1));
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}

	public static void pohraniNoviAutomobil(Automobil auto) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("insert into artikl(Naslov, Opis, Cijena, Snaga, idStanje, idTipArtikla) "
							+ "values (?, ?, ?, ?, ?, 1);");
			preparedStatement.setString(1, auto.getNaslov());
			preparedStatement.setString(2, auto.getOpis());
			preparedStatement.setBigDecimal(3, auto.getCijena());
			preparedStatement.setBigDecimal(4, auto.getSnagaKs());
			preparedStatement.setLong(5, (auto.getStanje().ordinal() + 1));
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}

	public static void pohraniNovuUslugu(Usluga usluga) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza.prepareStatement(
					"insert into artikl(Naslov, Opis, Cijena, idStanje, idTipArtikla) " + "values (?, ?, ?, ?, ?, 3);");
			preparedStatement.setString(1, usluga.getNaslov());
			preparedStatement.setString(2, usluga.getOpis());
			preparedStatement.setBigDecimal(3, usluga.getCijena());
			preparedStatement.setLong(5, (usluga.getStanje().ordinal() + 1));
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}

	public static void pohraniNovogPrivatnogKorisnika(PrivatniKorisnik korisnik) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza.prepareStatement(
					"insert into korisnik(Ime, Prezime, Email, Telefon, idTipKorisnika) " + "values (?, ?, ?, ?, 1);");
			preparedStatement.setString(1, korisnik.getIme());
			preparedStatement.setString(2, korisnik.getPrezime());
			preparedStatement.setString(3, korisnik.getEmail());
			preparedStatement.setString(4, korisnik.getTelefon());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static void pohraniNovuProdaju(Prodaja prodaja) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza.prepareStatement(
					"insert into prodaja(IdArtikl, IdKorisnik, DatumObjave) " + "values (?, ?, ?);");
			preparedStatement.setLong(1, prodaja.getArtikl().getId());
			preparedStatement.setLong(2, prodaja.getKorisnik().getId());
			preparedStatement.setDate(3, Date.valueOf(prodaja.getDatumObjave()));
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u pohrani nove prodaje!";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}

	public static void pohraniNovogPoslovnogKorisnika(PoslovniKorisnik korisnik) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza.prepareStatement(
					"insert into korisnik(Naziv, Web, Email, Telefon, idTipKorisnika) " + "values (?, ?, ?, ?, 2);");
			preparedStatement.setString(1, korisnik.getNaziv());
			preparedStatement.setString(2, korisnik.getWeb());
			preparedStatement.setString(3, korisnik.getEmail());
			preparedStatement.setString(4, korisnik.getTelefon());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}

	public static Connection spajanjeNaBazu() throws SQLException, IOException {
		Connection veza = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/Java-NovaBaza", "student", "student");
		return veza;
	}

	public static List<Automobil> dohvatiAutomobilePremaKriterijima(Automobil auto) throws BazaPodatakaException {
		List<Automobil> listaAutomobila = new ArrayList<>();
		try (Connection connection = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder(
					"SELECT distinct artikl.id, naslov, opis, cijena, snaga, stanje.naziv "
							+ "FROM artikl inner join stanje on stanje.id = artikl.idStanje "
							+ "inner join tipArtikla on tipArtikla.id = artikl.idTipArtikla WHERE tipArtikla.naziv = 'Automobil'");
			if (Optional.ofNullable(auto).isEmpty() == false) {
				if (Optional.ofNullable(auto).map(Automobil::getId).isPresent())
					sqlUpit.append(" AND artikl.id = " + auto.getId());

				if (Optional.ofNullable(auto.getNaslov()).map(String::isBlank).orElse(true) == false)
					sqlUpit.append(" AND artikl.naslov LIKE '%" + auto.getNaslov() + "%'");

				if (Optional.ofNullable(auto.getOpis()).map(String::isBlank).orElse(true) == false)
					sqlUpit.append(" AND artikl.opis LIKE '%" + auto.getOpis() + "%'");

				if (Optional.ofNullable(auto).map(Automobil::getCijena).isPresent())
					sqlUpit.append(" AND artikl.cijena = " + auto.getCijena());

				if (Optional.ofNullable(auto).map(Automobil::getSnagaKs).isPresent())
					sqlUpit.append(" AND artikl.snaga = " + auto.getSnagaKs());
			}

			Statement query = connection.createStatement();
			ResultSet resultSet = query.executeQuery(sqlUpit.toString());

			while (resultSet.next()) {
				Long id = resultSet.getLong("id");
				String naslov = resultSet.getString("naslov");
				String opis = resultSet.getString("opis");
				BigDecimal cijena = resultSet.getBigDecimal("cijena");
				BigDecimal snaga = resultSet.getBigDecimal("snaga");
				String stanje = resultSet.getString("naziv");

				Automobil newAuto = new Automobil(naslov, opis, cijena, snaga, Stanje.valueOf(stanje), id);
				listaAutomobila.add(newAuto);
			}

		} catch (SQLException | IOException e) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, e);
			throw new BazaPodatakaException(poruka, e);
		}
		return listaAutomobila;
	}

	public static List<Usluga> dohvatiUslugePremaKriterijima(Usluga usluga) throws BazaPodatakaException {
		List<Usluga> listaUsluga = new ArrayList<>();
		try (Connection connection = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder("SELECT distinct artikl.id, naslov, opis, cijena, stanje.naziv "
					+ "FROM artikl inner join stanje on stanje.id = artikl.idStanje "
					+ "inner join tipArtikla on tipArtikla.id = artikl.idTipArtikla WHERE tipArtikla.naziv = 'Usluga'");
			if (Optional.ofNullable(usluga).isEmpty() == false) {
				if (Optional.ofNullable(usluga).map(Usluga::getId).isPresent())
					sqlUpit.append(" AND artikl.id = " + usluga.getId());

				if (Optional.ofNullable(usluga.getNaslov()).map(String::isBlank).orElse(true) == false)
					sqlUpit.append(" AND artikl.naslov LIKE '%" + usluga.getNaslov() + "%'");

				if (Optional.ofNullable(usluga.getOpis()).map(String::isBlank).orElse(true) == false)
					sqlUpit.append(" AND artikl.opis LIKE '%" + usluga.getOpis() + "%'");

				if (Optional.ofNullable(usluga).map(Usluga::getCijena).isPresent())
					sqlUpit.append(" AND artikl.cijena = " + usluga.getCijena());

			}

			Statement query = connection.createStatement();
			ResultSet resultSet = query.executeQuery(sqlUpit.toString());

			while (resultSet.next()) {
				Long id = resultSet.getLong("id");
				String naslov = resultSet.getString("naslov");
				String opis = resultSet.getString("opis");
				BigDecimal cijena = resultSet.getBigDecimal("cijena");
				String stanje = resultSet.getString("naziv");

				Usluga newUsluga = new Usluga(naslov, opis, cijena, Stanje.valueOf(stanje), id);
				listaUsluga.add(newUsluga);
			}

		} catch (SQLException | IOException e) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, e);
			throw new BazaPodatakaException(poruka, e);
		}
		return listaUsluga;
	}

	public static List<PrivatniKorisnik> dohvatiPrivatneKorisnikePremaKriterijima(PrivatniKorisnik privatniKorisnik)
			throws BazaPodatakaException {
		List<PrivatniKorisnik> listaPrivatnihKorisnika = new ArrayList<>();
		try (Connection connection = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder("SELECT distinct korisnik.id, ime, prezime, telefon, email "
					+ "from korisnik inner join tipKorisnika on tipKorisnika.id = korisnik.idTipKorisnika where tipKorisnika.naziv = 'PrivatniKorisnik' ");
			if (Optional.ofNullable(privatniKorisnik).isEmpty() == false) {
				if (Optional.ofNullable(privatniKorisnik).map(PrivatniKorisnik::getId).isPresent())
					sqlUpit.append(" AND korisnik.id = " + privatniKorisnik.getId());

				if (Optional.ofNullable(privatniKorisnik.getIme()).map(String::isBlank).orElse(true) == false)
					sqlUpit.append(" AND korisnik.ime LIKE '%" + privatniKorisnik.getIme() + "%'");

				if (Optional.ofNullable(privatniKorisnik.getPrezime()).map(String::isBlank).orElse(true) == false)
					sqlUpit.append(" AND korisnik.opis LIKE '%" + privatniKorisnik.getPrezime() + "%'");

				if (Optional.ofNullable(privatniKorisnik.getTelefon()).map(String::isBlank).orElse(true) == false)
					sqlUpit.append(" AND korisnik.cijena = " + privatniKorisnik.getTelefon());

				if (Optional.ofNullable(privatniKorisnik.getEmail()).map(String::isBlank).orElse(true) == false)
					sqlUpit.append(" AND korisnik.cijena = " + privatniKorisnik.getEmail());

			}

			Statement query = connection.createStatement();
			ResultSet resultSet = query.executeQuery(sqlUpit.toString());

			while (resultSet.next()) {
				Long id = resultSet.getLong("id");
				String ime = resultSet.getString("ime");
				String prezime = resultSet.getString("prezime");
				String email = resultSet.getString("email");
				String telefon = resultSet.getString("telefon");

				PrivatniKorisnik newPrivatniKorisnik = new PrivatniKorisnik(ime, prezime, email, telefon, id);
				listaPrivatnihKorisnika.add(newPrivatniKorisnik);
			}

		} catch (SQLException | IOException e) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, e);
			throw new BazaPodatakaException(poruka, e);
		}
		return listaPrivatnihKorisnika;
	}

	public static List<PoslovniKorisnik> dohvatiPoslovneKorisnikePremaKriterijima(PoslovniKorisnik poslovniKorisnik)
			throws BazaPodatakaException {
		List<PoslovniKorisnik> listaPoslovnihKorisnika = new ArrayList<>();
		try (Connection connection = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder(
					"SELECT distinct korisnik.id, korisnik.naziv, web, email, telefon "
							+ "from korisnik inner join tipKorisnika on tipKorisnika.id = korisnik.idTipKorisnika where tipKorisnika.naziv = 'PoslovniKorisnik' ");
			if (Optional.ofNullable(poslovniKorisnik).isEmpty() == false) {
				if (Optional.ofNullable(poslovniKorisnik).map(PoslovniKorisnik::getId).isPresent())
					sqlUpit.append(" AND korisnik.id = " + poslovniKorisnik.getId());

				if (Optional.ofNullable(poslovniKorisnik.getNaziv()).map(String::isBlank).orElse(true) == false)
					sqlUpit.append(" AND korisnik.ime LIKE '%" + poslovniKorisnik.getNaziv() + "%'");

				if (Optional.ofNullable(poslovniKorisnik.getWeb()).map(String::isBlank).orElse(true) == false)
					sqlUpit.append(" AND korisnik.opis LIKE '%" + poslovniKorisnik.getWeb() + "%'");

				if (Optional.ofNullable(poslovniKorisnik.getTelefon()).map(String::isBlank).orElse(true) == false)
					sqlUpit.append(" AND korisnik.cijena = " + poslovniKorisnik.getTelefon());

				if (Optional.ofNullable(poslovniKorisnik.getEmail()).map(String::isBlank).orElse(true) == false)
					sqlUpit.append(" AND korisnik.cijena = " + poslovniKorisnik.getEmail());

			}

			Statement query = connection.createStatement();
			ResultSet resultSet = query.executeQuery(sqlUpit.toString());

			while (resultSet.next()) {
				Long id = resultSet.getLong("id");
				String naziv = resultSet.getString("naziv");
				String web = resultSet.getString("web");
				String email = resultSet.getString("email");
				String telefon = resultSet.getString("telefon");

				PoslovniKorisnik newPoslovniKorisnik = new PoslovniKorisnik(naziv, web, email, telefon, id);
				listaPoslovnihKorisnika.add(newPoslovniKorisnik);
			}

		} catch (SQLException | IOException e) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, e);
			throw new BazaPodatakaException(poruka, e);
		}
		return listaPoslovnihKorisnika;
	}

	public static List<Prodaja> dohvatiProdajuPremaKriterijima(Prodaja prodaja) throws BazaPodatakaException {
		List<Prodaja> listaProdaje = new ArrayList<>();
		try (Connection connection = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder(
					"select distinct korisnik.id as idKorisnika, tipKorisnika.naziv as tipKorisnika, \r\n"
							+ "korisnik.naziv as nazivKorisnika, web, email, telefon, \r\n"
							+ "korisnik.ime, korisnik.prezime, tipArtikla.naziv as tipArtikla,\r\n"
							+ "artikl.naslov, artikl.opis, artikl.cijena, artikl.kvadratura,\r\n"
							+ "artikl.snaga, stanje.naziv as stanje, prodaja.datumObjave, artikl.id as idArtikla\r\n"
							+ "from korisnik inner join \r\n"
							+ "tipKorisnika on tipKorisnika.id = korisnik.idTipKorisnika inner join\r\n"
							+ "prodaja on prodaja.idKorisnik = korisnik.id inner join\r\n"
							+ "artikl on artikl.id = prodaja.idArtikl inner join\r\n"
							+ "tipArtikla on tipArtikla.id = artikl.idTipArtikla inner join\r\n"
							+ "stanje on stanje.id = artikl.idStanje where 1=1");
			if (Optional.ofNullable(prodaja).isEmpty() == false) {
				if (Optional.ofNullable(prodaja.getArtikl()).isPresent())
					sqlUpit.append(" AND prodaja.idArtikl = " + prodaja.getArtikl().getId());
				if (Optional.ofNullable(prodaja.getKorisnik()).isPresent())
					sqlUpit.append(" AND prodaja.idArtikl = " + prodaja.getKorisnik().getId());
				if (Optional.ofNullable(prodaja.getDatumObjave()).isPresent()) {
					sqlUpit.append(" AND prodaja.datumObjave = '"
							+ prodaja.getDatumObjave().format(DateTimeFormatter.ISO_DATE) + "'");
				}
			}
			Statement query = connection.createStatement();
			ResultSet resultSet = query.executeQuery(sqlUpit.toString());
			while (resultSet.next()) {
				Korisnik korisnik = null;
				if (resultSet.getString("tipKorisnika").equals("PrivatniKorisnik")) {
					korisnik = new PrivatniKorisnik(resultSet.getString("ime"), resultSet.getString("prezime"),
							resultSet.getString("email"), resultSet.getString("telefon"),
							resultSet.getLong("idKorisnika"));
				} else if (resultSet.getString("tipKorisnika").equals("PoslovniKorisnik")) {
					korisnik = new PoslovniKorisnik(resultSet.getString("nazivKorisnika"), resultSet.getString("web"),
							resultSet.getString("telefon"), resultSet.getString("email"),
							resultSet.getLong("idKorisnika"));
				}
				Artikl artikl = null;
				if (resultSet.getString("tipArtikla").equals("Automobil")) {
					artikl = new Automobil(resultSet.getString("naslov"), resultSet.getString("opis"),
							resultSet.getBigDecimal("cijena"), resultSet.getBigDecimal("snaga"),
							Stanje.valueOf(resultSet.getString("stanje")), resultSet.getLong("idArtikla"));
				} else if (resultSet.getString("tipArtikla").equals("Usluga")) {
					artikl = new Usluga(resultSet.getString("naslov"), resultSet.getString("opis"),
							resultSet.getBigDecimal("cijena"), Stanje.valueOf(resultSet.getString("stanje")),
							resultSet.getLong("idArtikla"));
				} else if (resultSet.getString("tipArtikla").equals("Stan")) {
					artikl = new Stan(resultSet.getString("naslov"), resultSet.getString("opis"),
							resultSet.getBigDecimal("cijena"), resultSet.getInt("kvadratura"),
							Stanje.valueOf(resultSet.getString("stanje")), resultSet.getLong("idArtikla"));
				}
				Prodaja novaProdaja = new Prodaja(artikl, korisnik,
						resultSet.getTimestamp("datumObjave").toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				listaProdaje.add(novaProdaja);
			}
		} catch (SQLException | IOException e) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, e);
			throw new BazaPodatakaException(poruka, e);
		}
		return listaProdaje;
	}

	public static List<Artikl> dohvatiArtikle(Artikl artikl) throws SQLException, IOException, BazaPodatakaException{
		List<Artikl> listaArtikala = new ArrayList<>();
		try (Connection connection = spajanjeNaBazu()){
			StringBuilder sqlUpit = new StringBuilder("SELECT distinct artikl.id as idArtikla, naslov, opis, cijena, snaga,\r\n" +
														" kvadratura, stanje.naziv as stanje, tipArtikla.naziv as tipArtikla\r\n" +
														"FROM artikl inner join\r\n" +
														"stanje on stanje.id = artikl.idStanje inner join\r\n" +
														"tipArtikla on tipArtikla.id = artikl.idTipArtikla");
			Statement query = connection.createStatement();
			ResultSet resultSet = query.executeQuery(sqlUpit.toString());
			
			while(resultSet.next()) {
				if(resultSet.getString("tipArtikla").equals("Automobil")) {
					Long id = resultSet.getLong("id");
					String naslov = resultSet.getString("naslov");
					String opis = resultSet.getString("opis");
					BigDecimal cijena = resultSet.getBigDecimal("cijena");
					BigDecimal snaga = resultSet.getBigDecimal("snaga");
					String stanje = resultSet.getString("naziv");

					Automobil auto = new Automobil(naslov, opis, cijena, snaga, Stanje.valueOf(stanje), id);
					listaArtikala.add(auto);
				}
				else if(resultSet.getString("tipArtikla").equals("Usluga")) {
					Long id = resultSet.getLong("id");
					String naslov = resultSet.getString("naslov");
					String opis = resultSet.getString("opis");
					BigDecimal cijena = resultSet.getBigDecimal("cijena");
					String stanje = resultSet.getString("naziv");

					Usluga usluga = new Usluga(naslov, opis, cijena, Stanje.valueOf(stanje), id);
					listaArtikala.add(usluga);
				}
				else if(resultSet.getString("tipArtikla").equals("Stan")) {
					Long id = resultSet.getLong("id");
					String naslov = resultSet.getString("naslov");
					String opis = resultSet.getString("opis");
					BigDecimal cijena = resultSet.getBigDecimal("cijena");
					Integer kvadratura = resultSet.getInt("kvadratura");
					String stanje = resultSet.getString("naziv");

					Stan stan = new Stan(naslov, opis, cijena, kvadratura, Stanje.valueOf(stanje), id);
					listaArtikala.add(stan);
				}
			}
		} catch (SQLException | IOException e) {
			String poruka = "Došlo je do pogreške u dohvatu artikala iz baze podataka!";
			logger.error(poruka, e);
			throw new BazaPodatakaException(poruka, e);
		}
		return listaArtikala;
	}
	
	public static List<Korisnik> dohvatiKorisnike(Korisnik korisnik) throws SQLException, IOException, BazaPodatakaException{
		List<Korisnik> listaKorisnika = new ArrayList<>();
		try (Connection connection = spajanjeNaBazu()){
			StringBuilder sqlUpit = new StringBuilder("SELECT distinct korisnik.id as idKorisnika, korisnik.naziv, korisnik.web, korisnik.email,\r\n" +
														"korisnik.telefon, korisnik.ime, korisnik.prezime, tipKorisnika.naziv as tipKorisnika from korisnik inner join\r\n" +
														"tipKorisnika on tipKorisnika.id = korisnik.idTipKorisnika");
			Statement query = connection.createStatement();
			ResultSet resultSet = query.executeQuery(sqlUpit.toString());
			
			while(resultSet.next()) {
				if (resultSet.getString("tipKorisnika").equals("PrivatniKorisnik")) {
					korisnik = new PrivatniKorisnik(resultSet.getString("ime"), resultSet.getString("prezime"),
							resultSet.getString("email"), resultSet.getString("telefon"),
							resultSet.getLong("idKorisnika"));

					listaKorisnika.add(korisnik);
				}
				else if (resultSet.getString("tipKorisnika").equals("PoslovniKorisnik")) {
					korisnik = new PoslovniKorisnik(resultSet.getString("naziv"), resultSet.getString("web"),
							resultSet.getString("telefon"), resultSet.getString("email"),
							resultSet.getLong("idKorisnika"));
					listaKorisnika.add(korisnik);
				}
			}
		} catch (SQLException | IOException e) {
			String poruka = "Došlo je do pogreške u dohvatu korisnika iz baze podataka!";
			logger.error(poruka, e);
			throw new BazaPodatakaException(poruka, e);
		}
		return listaKorisnika;
	}
	
	public static Prodaja dohvatiZadnjuProdaju(Prodaja prodaja) throws BazaPodatakaException {
		Prodaja zadnjaProdaja = null;
		try (Connection connection = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder(
					"select distinct korisnik.id as idKorisnika, tipKorisnika.naziv as tipKorisnika,\r\n" + 
					"korisnik.naziv as nazivKorisnika, web, email, telefon,\r\n" + 
					"korisnik.ime, korisnik.prezime, tipArtikla.naziv as tipArtikla,\r\n" + 
					"artikl.naslov, artikl.opis, artikl.cijena, artikl.kvadratura,\r\n" + 
					"artikl.snaga, stanje.naziv as stanje, prodaja.datumObjave,\r\n" + 
					"artikl.id as idArtikla\r\n" + 
					"from korisnik inner join\r\n" + 
					"tipKorisnika on tipKorisnika.id = korisnik.idTipKorisnika inner join\r\n" + 
					"prodaja on prodaja.idKorisnik = korisnik.id inner join\r\n" + 
					"artikl on artikl.id = prodaja.idArtikl inner join\r\n" + 
					"tipArtikla on tipArtikla.id = artikl.idTipArtikla inner join\r\n" +
					"stanje on stanje.id = artikl.idStanje\r\n" + 
					"order by datumObjave desc\r\n" + 
					"limit 1");

			Statement query = connection.createStatement();
			ResultSet resultSet = query.executeQuery(sqlUpit.toString());
			while (resultSet.next()) {
				Korisnik korisnik = null;
				if (resultSet.getString("tipKorisnika").equals("PrivatniKorisnik")) {
					korisnik = new PrivatniKorisnik(resultSet.getString("ime"), resultSet.getString("prezime"),
							resultSet.getString("email"), resultSet.getString("telefon"),
							resultSet.getLong("idKorisnika"));
				} else if (resultSet.getString("tipKorisnika").equals("PoslovniKorisnik")) {
					korisnik = new PoslovniKorisnik(resultSet.getString("nazivKorisnika"), resultSet.getString("web"),
							resultSet.getString("telefon"), resultSet.getString("email"),
							resultSet.getLong("idKorisnika"));
				}
				Artikl artikl = null;
				if (resultSet.getString("tipArtikla").equals("Automobil")) {
					artikl = new Automobil(resultSet.getString("naslov"), resultSet.getString("opis"),
							resultSet.getBigDecimal("cijena"), resultSet.getBigDecimal("snaga"),
							Stanje.valueOf(resultSet.getString("stanje")), resultSet.getLong("idArtikla"));
				} else if (resultSet.getString("tipArtikla").equals("Usluga")) {
					artikl = new Usluga(resultSet.getString("naslov"), resultSet.getString("opis"),
							resultSet.getBigDecimal("cijena"), Stanje.valueOf(resultSet.getString("stanje")),
							resultSet.getLong("idArtikla"));
				} else if (resultSet.getString("tipArtikla").equals("Stan")) {
					artikl = new Stan(resultSet.getString("naslov"), resultSet.getString("opis"),
							resultSet.getBigDecimal("cijena"), resultSet.getInt("kvadratura"),
							Stanje.valueOf(resultSet.getString("stanje")), resultSet.getLong("idArtikla"));
				}
				zadnjaProdaja = new Prodaja(artikl, korisnik,
						resultSet.getTimestamp("datumObjave").toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			}
		} catch (SQLException | IOException e) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, e);
			throw new BazaPodatakaException(poruka, e);
		}
		return zadnjaProdaja;
	}
}
