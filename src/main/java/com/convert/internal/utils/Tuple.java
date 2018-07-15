package com.convert.internal.utils;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 10/07/2018
 */
public class Tuple<F, S> {
    public final F _1;
    public final S _2;

    private Tuple(F _1, S _2) {
        this._1 = _1;
        this._2 = _2;
    }

    public static <F, S> Tuple<F, S> apply(F _1, S _2) {
        return new Tuple<>(_1, _2);
    }
}
