package entities;

import annotations.CharField;
import annotations.Column;
import annotations.ForeignKey;
import annotations.Table;

@Table(name="student")
public class Student extends BasicEntity{

	@CharField(maxLength=20)
	@Column(name="ime", required=true, notNull=true)
	private String ime;
	
	@CharField(maxLength=20)
	@Column(name="prezime", required=true, notNull=true)
	private String prezime;
	
	@CharField(maxLength=10)
	@Column(name="index", required=true, notNull=true)
	private String index;
	
	@ForeignKey
	private Grupa grupa;
	
	public Student(String ime, String prezime, String index, Grupa grupa) {
		this.ime = ime;
		this.prezime = prezime;
		this.index = index;
		this.grupa = grupa;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
	
	public Grupa getGrupa() {
		return grupa;
	}
	
	public void setGrupa(Grupa grupa) {
		this.grupa = grupa;
	}
	
}
