package com.core;

import java.util.Map;

public abstract class TypeMap {

    protected final Map<Class<?>, Converter<?, ?>> map;

    protected TypeMap(Map<Class<?>, Converter<?, ?>> mapper) {
        map = mapper;
    }

    public <F, T> void addType(Class<F> from, Class<T> to, boolean reverse) {
        map.put(from, new Converter<>(from, to, this));
        if (reverse) {
            map.put(to, new Converter<>(to, from, this));
        }
    }

    public abstract <F, T> Converter<F, T> getConverter(Class<T> from);

}
