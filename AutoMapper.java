package autoMapper.mapperInterface;

import autoMapper.exceptions.UnmappedType;
import autoMapper.mapper.Mapper;


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
	
	public<F,T> void addMapping(Class<F> from, Class<T> to, boolean mapBothways, boolean findCommonSuper) {
		typeMap.addType(from, to, mapBothways, findCommonSuper);
	}

	@SuppressWarnings("unchecked")
	public<F,T> T map(F from) {
		Mapper<F,T> mapper = (Mapper<F, T>) typeMap.getMapper(from.getClass());
		if(mapper == null) {
			throw new UnmappedType();
		}
		mapper.emptyHistory();
		
		return mapper.map(from);
	}
}
