package autoMapper;


public class AutoMapper {

	private static AutoMapper instance;
	private TypeMap typeMap;
	
	private AutoMapper() {
		typeMap = TypeMap.getInstance();
	}

	public static AutoMapper getInstance() {
		if (instance == null) {
			instance = new AutoMapper();
		}
		return instance;
	}
	
	public<F,T> void addMapping(Class<F> from, Class<T> to) {
		typeMap.addType(from, to, false, false);
	}

	@SuppressWarnings("unchecked")
	public<F,T> T map(F from) {
		return (T) (typeMap.getMapper(from.getClass())).map(from);
	}
}
