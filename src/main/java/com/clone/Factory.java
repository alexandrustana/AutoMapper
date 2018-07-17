package com.clone;

import com.clone.internal.WeakTypeMap;
import com.core.Converter;
import com.core.TypeMap;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 17/07/2018
 */
public class Factory {

    private static Factory instance;
    private        TypeMap typeMap;

    private Factory() {
        typeMap = WeakTypeMap.getInstance();
    }

    public static Factory instance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    private <F, T> void addMapping(Class<F> from, Class<T> to) {
        typeMap.addType(from, to, false);
    }

    @SuppressWarnings("unchecked")
    public <F> F copy(F object) {
        Converter<F, F> converter = (Converter<F, F>) typeMap.getConverter(object.getClass());
        return converter.map(object);
    }
}
