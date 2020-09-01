package app;

import entities.Grupa;
import entities.Student;
import entities.Termin;
import mapper.Mapper;

public class Main {
	
	//ugledao sam se na nacin rada DJANGO ORM-a
	
	public static void main(String[] args) {
		
		Grupa gr = new Grupa("403");
		Student st = new Student("Stefan", "Ginic", "RN43/15", gr);
		
		Mapper mapper = new Mapper();
		
		mapper.save(gr);
		
		mapper.save(st);
		
		Termin termin = new Termin("16:15", "18:00");
		
		mapper.save(termin);
		
		mapper.addManyToMany(termin, gr);
		
	}

}
