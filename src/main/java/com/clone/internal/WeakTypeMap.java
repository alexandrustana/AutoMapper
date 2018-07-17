package com.clone.internal;

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

    public static TypeMap getInstance() {
        if (instance == null) {
            instance = new WeakTypeMap();
        }
        return instance;
    }
}
