package mapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import annotations.Id;
import annotations.ManyToMany;
import annotations.MappedSuperclass;

public aspect BeforeManyToMany {

	pointcut beforeAdding() : call(public void Mapper.addManyToMany(Object, Object));
	
	//provere pre dodavanja
	before(): beforeAdding(){
		Object primary = thisJoinPoint.getArgs()[0];
		Object secondary = thisJoinPoint.getArgs()[1];
		
		int primaryId = getId(primary);
		int secondaryId = getId(secondary);
		
		if (primaryId == -1 || secondaryId == -1) {
			try {
				throw new Exception("Objects must be saved first!");
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		
		Class<?> clazz = primary.getClass();
		Field field = null;

		while (clazz != null) {
			Field fields[] = clazz.getDeclaredFields();
			for (Field f : fields) {
				if (f.getAnnotation(ManyToMany.class) != null) {
					field = f;
					break;
				}
			}
			if (clazz.getAnnotation(MappedSuperclass.class) != null) {
				break;
			}
			clazz = clazz.getSuperclass();
		}

		if (field == null) {
			try {
				throw new Exception("Primary object doesn't have a ManyToMany declared field");
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}

		if (!field.getType().equals(ArrayList.class)) {
			try {
				throw new Exception("ManyToMany field must be type of ArrayList");
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}

		Type type = field.getGenericType();
		ParameterizedType pt = (ParameterizedType) type;
		Type t = pt.getActualTypeArguments()[0];
		
		if (t != secondary.getClass()) {
			try {
				throw new Exception("Incompatible types!");
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	private int getId(Object obj) {
		Class<?> clazz = obj.getClass().getSuperclass();
		boolean foundMappedSuperclass = false;
		while (clazz != null) {
			Annotation[] annotations1 = clazz.getAnnotations();
			for (Annotation annotation : annotations1) {
				if (annotation instanceof MappedSuperclass) {
					foundMappedSuperclass = true;
					break;
				}
			}
			if (foundMappedSuperclass)
				break;

			clazz = clazz.getSuperclass();
		}

		if (clazz == null)
			clazz = obj.getClass();

		Field fields[] = clazz.getDeclaredFields();
		for (Field f : fields) {
			if (f.getAnnotation(Id.class) != null) {
				f.setAccessible(true);
				try {
					int id = f.getInt(obj);
					if (Mapper.savedObjects.get(id) != null)
						return id;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return -1;
	}
	
}
