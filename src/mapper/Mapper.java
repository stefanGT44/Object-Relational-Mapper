package mapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import annotations.CharField;
import annotations.Column;
import annotations.ForeignKey;
import annotations.Id;
import annotations.IntegerField;
import annotations.ManyToMany;
import annotations.MappedSuperclass;
import annotations.Table;

public class Mapper {

	public static HashMap<Integer, Object> savedObjects;

	public Mapper() {
		savedObjects = new HashMap<>();
	}

	public void addManyToMany(Object primary, Object secondary) {
		int primaryId = getId(primary);
		int secondaryId = getId(secondary);
		
		Annotation[] annotations = primary.getClass().getAnnotations();
		String primaryName = "", secondaryName = "";
		
		for (Annotation annotation : annotations) {
			if (!(annotation instanceof Table))
				continue;
			primaryName = ((Table) annotation).name();
		}
		
		annotations = secondary.getClass().getAnnotations();
		for (Annotation annotation : annotations) {
			if (!(annotation instanceof Table))
				continue;
			secondaryName = ((Table) annotation).name();
		}
		
		String query = "INSERT INTO %s (%s) VALUES (%s)";
		
		String tableName = primaryName + "_has_" + secondaryName;
		String columnNames = primaryName + "_id, " + secondaryName + "_id";
		String values = "?, ?";
		List<Object> columnsObject = new ArrayList<Object>();
		columnsObject.add(new Integer(primaryId));
		columnsObject.add(new Integer(secondaryId));
		
		query = String.format(query, tableName, columnNames, values);
		
		System.out.println(query);
		System.out.println(columnsObject);
		System.out.println();

	}

	public void save(Object object) {
		String query = "INSERT INTO %s (%s) VALUES (%s)";
		String tableName = "";

		Map<String, Object> hashMapa = new HashMap<String, Object>();

		Class<?> clazz = object.getClass();
		Annotation[] annotations = clazz.getAnnotations();

		for (Annotation annotation : annotations) {
			if (!(annotation instanceof Table))
				continue;
			tableName = ((Table) annotation).name();
		}

		Class<?> superClazz = clazz.getSuperclass();
        boolean foundMappedSuperclass = false;
        while( superClazz != null ){
            Annotation[] annotations1 = superClazz.getAnnotations();
            for(Annotation annotation : annotations1){
                if( annotation instanceof MappedSuperclass){
                    foundMappedSuperclass = true;
                    break;
                }
            }
            if( foundMappedSuperclass ) break;

            superClazz = superClazz.getSuperclass();
        }

		if (foundMappedSuperclass) {
			fillMap(superClazz, hashMapa, object);
		}
		fillMap(clazz, hashMapa, object);

		int id = -1 * (int) System.currentTimeMillis();
		Field f = findIdField(!foundMappedSuperclass ? object.getClass() : superClazz);
		try {
			f.setAccessible(true);
			f.set(object, id);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		savedObjects.put(id, object);

		String columnsName = "";
		String columnsValues = "";
		List<Object> columnsObject = new ArrayList<Object>();

		for (String k : hashMapa.keySet()) {
			columnsName = columnsName == "" ? k : String.format("%s, %s", columnsName, k);
			columnsObject.add(hashMapa.get(k));
			columnsValues = columnsValues == "" ? "?" : String.format("%s, %s", columnsValues, "?");
		}

		query = String.format(query, tableName, columnsName, columnsValues);

		System.out.println(query);
		System.out.println(columnsObject);
		System.out.println("Generated ID: " + id);
		System.out.println();

	}

	private void fillMap(Class<?> clazz, Map<String, Object> map, Object object) {
		Field fields[] = clazz.getDeclaredFields();

		for (Field f : fields) {
			Annotation annotations[] = f.getAnnotations();
			boolean id = false, column = false, charField = false, integerField = false, fk = false, manyToMany = false;
			boolean required = false, notNull = false;
			String fieldName = "", fkField = "";
			for (Annotation ann : annotations) {
				if (ann instanceof Id)
					id = true;
				if (ann instanceof Column) {
					column = true;
					fieldName = ((Column) ann).name();
					required = ((Column) ann).required();
					notNull = ((Column) ann).notNull();
				}
				if (ann instanceof CharField) {
					charField = true;
					f.setAccessible(true);
					try {
						if (((String) f.get(object)).length() > ((CharField) ann).maxLength())
							throw new Exception("CharField " + f.getName() + "exceeds maximum char length of "
									+ ((CharField) ann).maxLength());
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}
				if (ann instanceof IntegerField)
					integerField = true;
				if (ann instanceof ForeignKey) {
					fk = true;
					try {
						f.setAccessible(true);
						Class<?> cl = f.get(object).getClass();
						String name = "";
						for (Annotation as:cl.getAnnotations()) {
							if (!(as instanceof Table))
								continue;
							name = ((Table) as).name();
						}
						fkField = name + "_id";
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}
				if (ann instanceof ManyToMany)
					manyToMany = true;
			}
			if (id && (charField || integerField || fk || manyToMany)) {
				try {
					throw new Exception("Invalid field description.");
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}

			if (fk && (charField || integerField || manyToMany)) {
				try {
					throw new Exception("Invalid field description.");
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}

			if (manyToMany && (integerField || charField || fk)) {
				try {
					throw new Exception("Invalid field description.");
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}

			if ((id || integerField || charField) && !column) {
				try {
					throw new Exception("Invalid field description.");
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}

			if (!id && column) {
				f.setAccessible(true);
				if (required || notNull) {
					Object o = null;
					try {
						o = f.get(object);
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
					if (o == null)
						try {
							throw new Exception("Field " + f.getName() + " is required or notNull");
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}
				}
				try {
					boolean nuull = false;
					if (!required && !notNull) {
						Object o;
						o = f.get(object);
						if (o == null)
							nuull = false;
					}
					if (!nuull)
						map.put(fieldName, f.get(object));
					else
						map.put(fieldName, "NULL");
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}

			if (fk) {
				f.setAccessible(true);
				try {
					if (f.get(object) == null) {
						throw new Exception("Foreign key required");
					}
					int fk_id = getId(f.get(object));
					if (fk_id == -1) {
						throw new Exception("Invalid foreign key");
					}
					map.put(fkField, fk_id);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}

		}
	}

	private Field findIdField(Class<?> clazz) {

		Field fields[] = clazz.getDeclaredFields();
		for (Field f : fields) {
			if (f.getAnnotation(Id.class) != null) {
				return f;
			}
		}
		return null;
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
					if (savedObjects.get(id) != null)
						return id;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return -1;
	}

}
