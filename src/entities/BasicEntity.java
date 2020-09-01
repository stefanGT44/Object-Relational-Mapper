package entities;

import java.io.Serializable;

import annotations.Column;
import annotations.Id;
import annotations.IntegerField;
import annotations.MappedSuperclass;

@MappedSuperclass
public class BasicEntity implements Serializable{

	@Id
	@Column(name="id", required=true, notNull=true)
	private int id;
	
	public BasicEntity() {
		this.id = Integer.MIN_VALUE;
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicEntity other = (BasicEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
