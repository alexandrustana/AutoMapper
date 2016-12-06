package autoMapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class AutoMapper {

	private Map<Class<?>, Mapper<?,?>> typeMapper;
	private static AutoMapper instance;
	private TypeMap typeMap;
	
	private AutoMapper() {
		typeMap = TypeMap.getInstance();
		typeMapper = new ConcurrentHashMap<>();
	}

	public static AutoMapper getInstance() {
		if (instance == null) {
			instance = new AutoMapper();
		}
		return instance;
	}
	
	public void addMapping(Class<?> from, Class<?> to) {
		typeMap.addType(from, to, false, false);
	}

	public<F> Object map(F from) {
		Class to = typeMap.getType(from.getClass());
		(typeMap.getMapper(from.getClass())).map(from, to);
	}
}
