package com.core;

import com.convert.exceptions.UnmappedType;
import com.convert.internal.utils.TriPredicate;
import com.convert.internal.utils.tuple.Arity2;
import com.convert.internal.utils.tuple.Arity2;
import com.convert.internal.utils.Utilities;
import com.core.annotations.Alias;
import com.core.annotations.Ignore;
import com.lambdista.util.Try;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Handles mapping properties from one class to the other
 *
 * @param <F> From
 * @param <T> To
 */
public final class Converter<F, T> {
    private final Map<Arity2<String, String>, Arity2<Method, Method>> map;
    private final Class<T>                                            toClass;
    private final TypeMap                                             typeMap;

    /**
     * Constructor used to initialize the variables which will be used when the method {@code public T convert(F from)} is called
     */
    Converter(Class<F> from, Class<T> to, TypeMap typeMap) {
        this.toClass = to;
        this.typeMap = typeMap;

        map = associate(from, to);
    }

    private String getName(Field field) {
        Alias alias = field.getAnnotation(Alias.class);
        return alias == null ? field.getName() : alias.name();
    }

    private Map<Arity2<String, String>, Arity2<Method, Method>> associate(Class<F> from, Class<T> to) {
        Predicate<Field> isIgnored = field -> field.getAnnotation(Ignore.class) != null;

        Set<Field> fromFields = getAllFields(from);
        Set<Field> toFields   = getAllFields(to);

        return properties(from, to, fromFields.stream()
                .filter(f -> !isIgnored.test(f) && toFields.stream().anyMatch(t -> getName(t).equals(getName(f)) && !isIgnored.test(t)))
                .map(f -> Arity2.apply(f.getName(), getName(f)))
                .collect(Collectors.toList()));
    }

    private Map<Arity2<String, String>, Arity2<Method, Method>> properties(Class<F> from, Class<T> to, List<Arity2<String, String>> fields) {
        return fields.stream().collect(Collectors.toMap(Function.identity(),
                                                        field -> Arity2.apply(find(field, Arrays.asList(from.getMethods()), "get", "is"),
                                                                              find(field, Arrays.asList(to.getMethods()), "set"))));
    }

    private Method find(Arity2<String, String> field, List<Method> methods, String... prefixes) {
        BiPredicate<Method, String>          startsWith = (m, p) -> m.getName().startsWith(p);
        TriPredicate<String, Method, String> isAccessor = (f, m, p) -> m.getName().length() == (f.length() + p.length()) && m.getName().toLowerCase().equals((p + f).toLowerCase());

        List<Method> result = methods.stream()
                .filter(method -> Arrays.stream(prefixes)
                        .anyMatch(prefix -> startsWith.test(method, prefix) &&
                                (isAccessor.test(field._1, method, prefix) || isAccessor.test(field._2, method, prefix))))
                .collect(Collectors.toList());

        return result.get(0);
    }

    public T convert(F from) {
        return convert(from, null);
    }

    /**
     * This methods maps the values from one type of object to another type of
     * object, the values are being get from the getter methods and are being
     * set using the setter methods.The number of getter methods must be equal
     * to the number of setter methods.
     *
     * @param from    the object which you want to convert
     * @param replace map which holds as key variable names and as values the value which should be put in the variable
     * @return the object which has the values set
     */
    public T convert(F from, Set<Arity2<String, Object>> replace) {
        Function<Throwable, Try.Failure<T>> LOG_ERROR = t -> {
            t.printStackTrace();
            return new Try.Failure<>(t);
        };
        BiFunction<Set<Arity2<String, Object>>, String, Optional<Object>> contains = (s, f) -> {
            if (s == null) { return Optional.empty(); }
            return s.stream().filter(v -> v._1.equals(f)).map(v -> v._2).findFirst();
        };
        BiFunction<Set<Arity2<String, Object>>, Arity2<String, String>, Optional<Object>> find = (s, t) -> contains.apply(s, t._1).or(() -> contains.apply(s, t._2));

        return Try.apply(() -> {
            T to = toClass.getConstructor().newInstance();
            if (history.containsKey(from)) {
                return history.get(from);
            }
            history.put(from, to);

            map.forEach((field, accessors) -> {
                Method get = accessors._1;
                Method set = accessors._2;

                Try.apply(() -> {
                    Object value = find.apply(replace, field).orElse(get.invoke(from));
                    if (value == null || Utilities.isPrimitive(value.getClass())) {
                        set.invoke(to, value);
                    } else {
                        Converter<Object, ?> converter = typeMap.getConverter(value.getClass());

                        if (converter == null) {
                            throw new UnmappedType();
                        }
                        Object object = converter.convert(value);
                        set.invoke(to, object);
                    }
                    return Void.TYPE;
                }).recoverWith(LOG_ERROR::apply).get();
            });
            return to;
        }).transform(result -> {
            history.clear();
            return new Try.Success<>(result);
        }, t -> {
            history.clear();
            return new Try.Failure<>(t);
        }).recoverWith(LOG_ERROR::apply).get();
    }

    private Map<F, T> history = new HashMap<>();

    /**
     * It returns all the objects fields, from its class and all its
     * Superclass's except the Object class
     *
     * @param clazz object from which the fields to be returned
     * @return the list of fields of the given object
     */
    private Set<Field> getAllFields(Class<?> clazz) {
        Set<Field> fields = new HashSet<>();

        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }

        return fields;
    }
}
