package hr.java.vjezbe.entitet;

import java.util.ArrayList;
import java.util.List;

public class Kategorija extends Entitet {

	private String naziv;
	private List<Artikl> artikli = new ArrayList<>();
	private List<Artikl> listaArtikala = new ArrayList<>();

	public Kategorija(Long id, String naziv, List<Artikl> artikli) {
		super(id);
		this.naziv = naziv;
		this.artikli = artikli;
	}

	public Kategorija(Long id, String naziv, List<Artikl> artikli, List<Artikl> listaArtikala) {
		super(id);
		this.naziv = naziv;
		this.artikli = artikli;
		this.listaArtikala = listaArtikala;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public List<Artikl> getArtikli() {
		return artikli;
	}

	public void setArtikli(List<Artikl> artikli) {
		this.artikli = artikli;
	}

	public List<Artikl> getListaArtikala() {
		return listaArtikala;
	}

	public void setListaArtikala(List<Artikl> listaArtikala) {
		this.listaArtikala = listaArtikala;
	}
	
	

}
