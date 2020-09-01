package mapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import annotations.Id;
import annotations.MappedSuperclass;

public aspect BeforeSave {

	pointcut beforeSaving() : call(public void Mapper.save(Object));
	
	before(): beforeSaving(){
		Object object = thisJoinPoint.getArgs()[0];
		
		Class<?> clazz = object.getClass();
		
		int idCounter = 0;
		
		while (clazz != null) {
			Field fields[] = clazz.getDeclaredFields();
	        for (Field f:fields) {
	        	if (f.getAnnotation(Id.class)!=null)
	        		idCounter++;
	        }
	        clazz = clazz.getSuperclass();
		}
        
        if (idCounter == 0 || idCounter > 1) {
        	try {
				throw new Exception("An entity must have strictly one ID field");
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
     
	}
	
}
