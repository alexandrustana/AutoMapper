package com.converter.internal.utils;

/**
 * @author Alexandru Stana, alexandru.stana@busymachines.com
 * @since 10/07/2018
 */
@FunctionalInterface
public interface TriPredicate<T, U, S> {
    Boolean test(T t, U u, S s);
}
