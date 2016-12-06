package autoMapper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Constants {

	public static final List<Class<?>> primitiveTypes;
	
	static{
		primitiveTypes = new ArrayList<>();
		
		primitiveTypes.add(Date.class);
		primitiveTypes.add(Integer.class);
		primitiveTypes.add(Double.class);
		primitiveTypes.add(Character.class);
		primitiveTypes.add(Long.class);
		primitiveTypes.add(Short.class);
		primitiveTypes.add(Byte.class);
		primitiveTypes.add(String.class);
	}
}
