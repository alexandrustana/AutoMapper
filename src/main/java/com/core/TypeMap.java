package com.core;

import java.util.Map;

public abstract class TypeMap {

    private final Map<Class<?>, Converter<?, ?>> typeMapper;

    protected TypeMap(Map<Class<?>, Converter<?, ?>> mapper) {
        typeMapper = mapper;
    }

    public <F, T> void addType(Class<F> from, Class<T> to, boolean reverse) {
        typeMapper.put(from, new Converter<>(from, to, this));
        if (reverse) {
            typeMapper.put(to, new Converter<>(to, from, this));
        }
    }

    public <F, T> Converter<F, T> getMapper(Class<T> from) {
        @SuppressWarnings("unchecked")
        Converter<F, T> converter = ((Converter<F, T>) typeMapper.get(from));
        return converter;
    }
}
