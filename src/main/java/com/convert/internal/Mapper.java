package com.convert.internal;

import com.convert.annotations.Alias;
import com.convert.annotations.Ignore;
import com.convert.exceptions.CommonSuperClassException;
import com.convert.exceptions.UndefinedMethodException;
import com.convert.exceptions.UnmappedType;
import com.convert.internal.utils.TriPredicate;
import com.convert.internal.utils.Tuple;
import com.convert.internal.utils.Utilities;
import com.lambdista.util.Try;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
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
public class Mapper<F, T> {
    private final Map<Tuple<String, String>, Tuple<Method, Method>> map;
    private final Class<T>                                          toClass;

    /**
     * Constructor used to initialize the variables which will be used when the method {@code public T map(F from)} is called
     *
     * @param findCommon specify if the mapping method should stop at the common parent or when it reaches the {@code Object} class
     */
    Mapper(boolean findCommon, Class<F> from, Class<T> to) {
        this.toClass = to;

        if (findCommon) {
            map = common(from, to);
        } else {
            map = different(from, to);
        }
    }

    private Map<Tuple<String, String>, Tuple<Method, Method>> common(Class<F> from, Class<T> to) {
        Class<?>    common = parent(from.getClass(), to.getClass());
        List<Field> fields = Arrays.asList(common.getDeclaredFields());
        List<Tuple<String, String>> commonFields = fields.stream()
                .filter(field -> field.getAnnotation(Ignore.class) == null)
                .map(f -> Tuple.apply(f.getName(), getName(f)))
                .collect(Collectors.toList());

        return properties(from, to, commonFields);
    }

    private String getName(Field field) {
        Alias alias = field.getAnnotation(Alias.class);
        return alias == null ? field.getName() : alias.name();
    }

    private Map<Tuple<String, String>, Tuple<Method, Method>> different(Class<F> from, Class<T> to) {
        Predicate<Field> isIgnored = field -> field.getAnnotation(Ignore.class) != null;

        List<Field> fromFields = getAllFields(from);
        List<Field> toFields   = getAllFields(to);

        return properties(from, to, fromFields.stream()
                .filter(f -> !isIgnored.test(f) && toFields.stream().anyMatch(t -> getName(t).equals(getName(f)) && !isIgnored.test(t)))
                .map(f -> Tuple.apply(f.getName(), getName(f)))
                .collect(Collectors.toList()));
    }

    private Map<Tuple<String, String>, Tuple<Method, Method>> properties(Class<F> from, Class<T> to, List<Tuple<String, String>> fields) {
        return fields.stream().collect(Collectors.toMap(Function.identity(),
                                                        field -> Tuple.apply(find(field, Arrays.asList(from.getMethods()), "get", "is"),
                                                                             find(field, Arrays.asList(to.getMethods()), "set"))));
    }

    private Method find(Tuple<String, String> field, List<Method> methods, String... prefixes) {
        BiPredicate<Method, String>          startsWith = (m, p) -> m.getName().startsWith(p);
        TriPredicate<String, Method, String> isAccessor = (f, m, p) -> m.getName().length() == (f.length() + p.length()) && m.getName().toLowerCase().equals((p + f).toLowerCase());

        List<Method> result = methods.stream()
                .filter(method -> Arrays.stream(prefixes)
                        .anyMatch(prefix -> startsWith.test(method, prefix) &&
                                (isAccessor.test(field._1, method, prefix) || isAccessor.test(field._2, method, prefix))))
                .collect(Collectors.toList());

        return result.get(0);
    }

    /**
     * This methods maps the values from one type of object to another type of
     * object, the values are being get from the getter methods and are being
     * set using the setter methods.The number of getter methods must be equal
     * to the number of setter methods.
     *
     * @param from the object which you want to convert
     * @return the object which has the values set
     * @throws CommonSuperClassException when there is no common super class for the two classes,
     *                                   other than the Object class
     * @throws UndefinedMethodException  when there does not exist a getter for the given field
     */
    public T map(F from) {
        Function<Throwable, Try.Failure<T>> LOG_ERROR = t -> {
            t.printStackTrace();
            return new Try.Failure<>(t);
        };

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
                    Object value = get.invoke(from);
                    if (value == null || Utilities.isPrimitive(value.getClass())) {
                        set.invoke(to, value);
                    } else {
                        Mapper<Object, ?> mapper = TypeMap.getInstance().getMapper(value.getClass());

                        if (mapper == null) {
                            throw new UnmappedType();
                        }
                        Object object = mapper.map(value);
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
     * This methods maps the values from one type of object to another type of
     * object, the values are being get from the getter methods and are being
     * set using the setter methods.The number of getter methods must be equal
     * to the number of setter methods. Modification can be made to ignore the
     * common superclass but then the number
     *
     * @param from the object which you want to convert
     * @return a list of objects which have the values set
     * @throws CommonSuperClassException when there is no common super class for the two classes,
     *                                   other than the Object class
     * @throws UndefinedMethodException  when there does not exist a getter for the given field
     */
    public List<T> map(List<F> from) {
        List<T> t = new ArrayList<>();

        for (F f : from) {
            t.add(map(f));
        }

        return t;
    }

    /**
     * It returns all the objects fields, from its class and all its
     * Superclass's except the Object class
     *
     * @param clazz object from which the fields to be returned
     * @return the list of fields of the given object
     */
    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }

        return fields;
    }

    /**
     * Method which finds the common parent for the classes given as parameters
     *
     * @param from the class from which the values will be mapped
     * @param to   the class on which the values will be mapped
     * @return the common super class for the two class, if there is no common super class then {@code Class<Void>} will be returned
     */
    private Class<?> parent(Class<?> from, Class<?> to) {
        if (from.equals(to)) {
            return from;
        } else if (!from.equals(Object.class) && !to.equals(Object.class)) {
            Class<?> left = parent(from.getSuperclass(), to);
            if (left.equals(Void.TYPE)) {
                return parent(from, to.getSuperclass());
            } else {
                return left;
            }
        }
        return Void.TYPE;
    }
}
