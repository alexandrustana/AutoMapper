package com.convert;

import com.convert.exceptions.UnmappedType;
import com.convert.internal.Converter;
import com.convert.internal.TypeMap;

public class Mapper {

    private static Mapper  instance;
    private        TypeMap typeMap;

    private Mapper() {
        typeMap = TypeMap.getInstance();
    }

    public static Mapper instance() {
        if (instance == null) {
            instance = new Mapper();
        }
        return instance;
    }

    public <F, T> void addMapping(Class<F> from, Class<T> to, boolean reverse) {
        typeMap.addType(from, to, reverse);
    }

    public <F, T> void addMapping(Class<F> from, Class<T> to) {
        typeMap.addType(from, to, false);
    }

    @SuppressWarnings("unchecked")
    public <F, T> T map(F from) {
        Converter<F, T> converter = (Converter<F, T>) typeMap.getMapper(from.getClass());
        if (converter == null) {
            throw new UnmappedType();
        }
        return converter.map(from);
    }
}
