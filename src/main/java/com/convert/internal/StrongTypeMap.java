package com.convert.internal;

import com.core.TypeMap;

import java.util.HashMap;
import java.util.Map;

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
}
