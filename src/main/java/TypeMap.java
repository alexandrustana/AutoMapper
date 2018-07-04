import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeMap {

	private Map<Class<?>, Mapper<?,?>> typeMapper;
	
	private static TypeMap instance;

	private TypeMap() {
		typeMapper = new ConcurrentHashMap<>();
	}

	public static TypeMap getInstance() {
		if (instance == null) {
			instance = new TypeMap();
		}
		return instance;
	}

	public <F,T> void addType(Class<F> from, Class<T> to, boolean bothways, boolean findCommonParent) {
		typeMapper.put(from, new Mapper<F, T>(findCommonParent, from, to));
		if (bothways) {
			typeMapper.put(to, new Mapper<T, F>(findCommonParent, to, from));
		}
	}
	
	public<F,T> Mapper<F, T> getMapper(Class<T> from) {
		@SuppressWarnings("unchecked")
		Mapper<F, T> mapper = ((Mapper<F, T>) typeMapper.get(from));
		return mapper;
	}

}
