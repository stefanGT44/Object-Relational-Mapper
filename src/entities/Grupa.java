package entities;

import annotations.CharField;
import annotations.Column;
import annotations.Table;

@Table(name="grupa")
public class Grupa extends BasicEntity{
	
	@CharField(maxLength=5)
	@Column(name="oznaka", required=true, notNull=true)
	private String oznaka;
	
	public Grupa(String oznaka) {
		this.oznaka = oznaka;
	}
	
	public String getOznaka() {
		return oznaka;
	}
	
	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}

}
