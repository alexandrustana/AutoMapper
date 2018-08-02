package com.convert.internal.utils.tuple;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 02/08/2018
 */
public class Arity2<F, S> extends Tuple {
    public final F _1;
    public final S _2;

    private Arity2(F _1, S _2) {
        this._1 = _1;
        this._2 = _2;
    }

    public static <F, S> Arity2<F, S> apply(F _1, S _2) {
        return new Arity2<>(_1, _2);
    }
}
