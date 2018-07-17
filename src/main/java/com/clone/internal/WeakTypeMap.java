package com.clone.internal;

import com.core.Converter;
import com.core.TypeMap;

import java.util.WeakHashMap;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 17/07/2018
 */
public class WeakTypeMap extends TypeMap {

    private static WeakTypeMap instance;

    private WeakTypeMap() {
        super(new WeakHashMap<>());
    }

    public static WeakTypeMap getInstance() {
        if (instance == null) {
            instance = new WeakTypeMap();
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F, T> Converter<F, T> getConverter(Class<T> from) {
        Converter<F, T> converter = ((Converter<F, T>) map.get(from));
        if (converter == null) {
            addType(from.getClass(), from.getClass(), false);
            converter = (Converter<F, T>) getConverter(from.getClass());
        }
        return converter;
    }
}
