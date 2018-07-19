package com.convert.internal;

import com.convert.exceptions.UnmappedType;
import com.core.Converter;
import com.core.TypeMap;

import java.util.HashMap;

public class StrongTypeMap extends TypeMap {

    private static StrongTypeMap instance;

    private StrongTypeMap() {
        super(new HashMap<>());
    }

    public static StrongTypeMap getInstance() {
        if (instance == null) {
            instance = new StrongTypeMap();
        }
        return instance;
    }

    @Override
    public <F, T> Converter<F, T> getConverter(Class<T> from) {
        @SuppressWarnings("unchecked")
        Converter<F, T> converter = ((Converter<F, T>) map.get(from));
        if (converter == null) {
            throw new UnmappedType();
        }
        return converter;
    }
}
