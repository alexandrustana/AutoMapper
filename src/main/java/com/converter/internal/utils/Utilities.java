package com.converter.internal.utils;

import java.util.ArrayList;
import java.util.List;

public class Utilities {
    private static final List<Class<?>> primitiveTypes;

    static {
        primitiveTypes = new ArrayList<>();

        primitiveTypes.add(Integer.class);
        primitiveTypes.add(Double.class);
        primitiveTypes.add(Float.class);
        primitiveTypes.add(Character.class);
        primitiveTypes.add(Boolean.class);
        primitiveTypes.add(Long.class);
        primitiveTypes.add(Short.class);
        primitiveTypes.add(Byte.class);
        primitiveTypes.add(String.class);
    }

    public static boolean isPrimitive(Class<?> type) {
        return primitiveTypes.contains(type);
    }
}
