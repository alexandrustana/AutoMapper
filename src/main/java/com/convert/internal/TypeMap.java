package com.convert.internal;

import java.util.HashMap;
import java.util.Map;

public class TypeMap {

    private final Map<Class<?>, Converter<?, ?>> typeMapper;

    private static TypeMap instance;

    private TypeMap() {
        typeMapper = new HashMap<>();
    }

    public static TypeMap getInstance() {
        if (instance == null) {
            instance = new TypeMap();
        }
        return instance;
    }

    public <F, T> void addType(Class<F> from, Class<T> to, boolean reverse) {
        typeMapper.put(from, new Converter<>(from, to));
        if (reverse) {
            typeMapper.put(to, new Converter<>(to, from));
        }
    }

    public <F, T> Converter<F, T> getMapper(Class<T> from) {
        @SuppressWarnings("unchecked")
        Converter<F, T> converter = ((Converter<F, T>) typeMapper.get(from));
        return converter;
    }

}
