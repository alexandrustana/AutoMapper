package com.clone;

import com.clone.internal.WeakTypeMap;
import com.convert.internal.utils.tuple.Arity2;
import com.convert.internal.utils.tuple.Tuple;
import com.core.Converter;
import com.core.TypeMap;

import java.util.Set;

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

    @SuppressWarnings("unchecked")
    public <F> F copy(F object) {
        Converter<F, F> converter = (Converter<F, F>) typeMap.getConverter(object.getClass());
        return converter.convert(object);
    }

    @SuppressWarnings("unchecked")
    public <F> F copy(F object, Set<Arity2<String, Object>> replace) {
        Converter<F, F> converter = (Converter<F, F>) typeMap.getConverter(object.getClass());
        return converter.convert(object, replace);
    }
}
