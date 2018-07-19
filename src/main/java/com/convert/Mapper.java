package com.convert;

import com.convert.internal.StrongTypeMap;
import com.core.Converter;
import com.core.TypeMap;

public class Mapper {

    private static Mapper  instance;
    private        TypeMap typeMap;

    private Mapper() {
        typeMap = StrongTypeMap.getInstance();
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
        Converter<F, T> converter = (Converter<F, T>) typeMap.getConverter(from.getClass());

        return converter.map(from);
    }
}
