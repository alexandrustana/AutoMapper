package com.converter.internal;

import com.converter.annotations.Ignore;
import com.converter.exceptions.CommonSuperClassException;
import com.converter.exceptions.UndefinedMethodException;
import com.converter.exceptions.UnmappedType;
import com.converter.internal.utils.Utilities;
import com.lambdista.util.Try;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Handles mapping properties from one class to the other
 *
 * @param <F> From
 * @param <T> To
 */
public class Mapper<F, T> {
    private        Map<String, Map<Method, Method>> map;
    private        Map<Method, Method>              getterSetter;
    private static Set<Class<?>>                    history;
    private        Class<T>                         toClass;

    /**
     * Constructor used to initialize the variables which will be used when the method {@code public T map(F from)} is called
     *
     * @param findCommon specify if the mapping method should stop at the common parent or when it reaches the {@code Object} class
     */
    public Mapper(boolean findCommon, Class<F> from, Class<T> to) {
        this.toClass = to;

        history = new HashSet<>();
        getterSetter = new HashMap<>();

        if (findCommon) {
            common(from, to);
        } else {
            different(from, to);
        }
    }

    private void common(Class<F> from, Class<T> to) {
        Class<?>    common       = getCommonParrent(from.getClass(), to.getClass());
        List<Field> fields       = Arrays.asList(common.getDeclaredFields());
        List<Field> commonFields = new ArrayList<>();

        for (Field field : fields) {
            if (field.getAnnotation(Ignore.class) != null) {
                continue;
            }

            commonFields.add(field);
        }

        saveFields(from, to, commonFields);
    }

    private void different(Class<F> from, Class<T> to) {
        List<Field> fromFields   = getAllFields(from);
        List<Field> toFields     = getAllFields(to);
        List<Field> commonFields = new ArrayList<Field>();

        List<Field> maxFields, minFields;
        if (fromFields.size() < toFields.size()) {
            maxFields = toFields;
            minFields = fromFields;
        } else {
            maxFields = fromFields;
            minFields = toFields;
        }

        for (Field i : maxFields) {
            for (Field j : minFields) {
                if (i.getName().equals(j.getName())) {
                    if (i.getAnnotation(Ignore.class) != null
                            || j.getAnnotation(Ignore.class) != null) {
                        continue;
                    }

                    commonFields.add(i);
                }
            }
        }

        saveFields(from, to, commonFields);
    }

    public void saveFields(Class<F> from, Class<T> to, List<Field> fields) {
        for (Field field : fields) {
            try {
                Method getter = getterForField(field, from);
                Method setter = setterForField(field, to);

                getterSetter.put(getter, setter);
            } catch (UndefinedMethodException ignore) {
                // this means that the field does not have a getter or a setter
                // and will not be mapped;
            }
        }
    }

    private Method getterForField(Field field, Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            if ((method.getName().startsWith("get"))
                    && (method.getName().length() == (field.getName().length() + 3))) {
                if (method.getName().toLowerCase()
                        .endsWith(field.getName().toLowerCase())) {
                    return method;
                }
            } else if ((method.getName().startsWith("is"))
                    && (method.getName().length() == (field.getName().length() + 2))) {
                if (method.getName().toLowerCase()
                        .endsWith(field.getName().toLowerCase())) {
                    return method;
                }
            }
        }

        throw new UndefinedMethodException();
    }

    private Method setterForField(Field field, Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            if ((method.getName().startsWith("set"))
                    && (method.getName().length() == (field.getName().length() + 3))) {
                if (method.getName().toLowerCase()
                        .endsWith(field.getName().toLowerCase())) {
                    return method;
                }
            }
        }

        throw new UndefinedMethodException();
    }

    /**
     * Specify if the mapping of the object should look for a common class
     * between the two objects
     *
     * @param findCommon
     *            boolean which specifies if the mapping should look for a
     *            common class
     */

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
        return Try.apply(() -> {
            T to = toClass.getConstructor().newInstance();

            if (history.contains(toClass)) {
                return null;
            }

            history.add(toClass);

            for (Method getter : getterSetter.keySet()) {
                Method setter = getterSetter.get(getter);

                Object result = getter.invoke(from);
                if (Utilities.isPrimitive(result.getClass())) {
                    setter.invoke(to, result);
                } else {
                    Mapper<Object, ?> mapper = TypeMap.getInstance().getMapper(result.getClass());

                    if (mapper == null) {
                        throw new UnmappedType();
                    }

                    setter.invoke(to, mapper.map(result));
                }
            }
            return to;
        }).get();
    }

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
     * @return the common super class for the two class
     * @throws CommonSuperClassException when there is no common super class for the two classes,
     *                                   other than the Object class
     */
    private Class<?> getCommonParrent(Class<?> from, Class<?> to) {
        if (from.getCanonicalName().equals(to.getCanonicalName())) {
            return to;
        }

        if (from.getCanonicalName().equals(Object.class.getCanonicalName())
                || to.getCanonicalName()
                .equals(Object.class.getCanonicalName())) {
            throw new CommonSuperClassException();
        }
        return getCommonParrent(from.getSuperclass(), to.getSuperclass());
    }

    public void emptyHistory() {
        history.clear();
    }
}
