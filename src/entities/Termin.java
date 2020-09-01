package entities;

import java.util.ArrayList;

import annotations.CharField;
import annotations.Column;
import annotations.ManyToMany;
import annotations.Table;

@Table(name="termin")
public class Termin extends BasicEntity{
	
	@CharField(maxLength=10)
	@Column(name="pocetak", required=true, notNull=true)
	private String pocetak;
	
	@CharField(maxLength=10)
	@Column(name="kraj", required=true, notNull=true)
	private String kraj;
	
	@ManyToMany
	private ArrayList<Grupa> grupe;
	
	public Termin() {
		this.grupe = new ArrayList<>();
	}
	
	public Termin(String pocetak, String kraj) {
		this.pocetak = pocetak;
		this.kraj = kraj;
		this.grupe = new ArrayList<>();
	}

	public String getPocetak() {
		return pocetak;
	}

	public void setPocetak(String pocetak) {
		this.pocetak = pocetak;
	}

	public String getKraj() {
		return kraj;
	}

	public void setKraj(String kraj) {
		this.kraj = kraj;
	}
	
}
