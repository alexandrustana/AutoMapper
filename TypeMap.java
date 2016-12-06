package autoMapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeMap {

	private Map<Class<?>, Class<?>> fromTo;
	private Map<Class<?>, Mapper<?,?>> typeMapper;
	private static TypeMap instance;

	private TypeMap() {
		fromTo = new ConcurrentHashMap<>();
		typeMapper = new ConcurrentHashMap<>();
	}

	public static TypeMap getInstance() {
		if (instance == null) {
			instance = new TypeMap();
		}
		return instance;
	}

	public <F,T> void addType(Class<F> from, Class<T> to, boolean bothways, boolean findCommonParent) {
		fromTo.put(from, to);
		typeMapper.put(from, new Mapper<F, T>(findCommonParent));
		if (bothways) {
			fromTo.put(to, from);
			typeMapper.put(to, new Mapper<T, F>(findCommonParent));
		}
	}
	
	public Class<?> getType(Class<?> from) {
		return fromTo.get(from);
	}
	
	public Mapper<?,?> getMapper(Class<?> from) {
		return typeMapper.get(from);
	}

	public void removeType(Class<?> type) {
		fromTo.remove(type);
	}
	
	public boolean contains(Class<?> type) {
		return fromTo.containsKey(type);
	}
	
}
