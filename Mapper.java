package autoMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import autoMapper.exceptions.CommonSuperClassException;
import autoMapper.exceptions.UndefinedMethodException;

/**
 * Handles mapping values from one class to the other
 * 
 * @param <F>
 *            the abbreviation means From
 * @param <T>
 *            the abbreviation means To
 */
public class Mapper<F, T> {

	private Map<Method, Method> getterSetter;
	private Class<T> toClass;

	/**
	 * Constructor which must have specified if while mapping to look for a
	 * common super class
	 * 
	 * @param findCommon
	 *            specify if the common parent of the two classes is to be found
	 */
	public Mapper(boolean findCommon, Class<F> from, Class<T> to) {
		this.toClass = to;

		getterSetter = new ConcurrentHashMap<>();

		if (findCommon) {
			mapCommon(from, to);
		} else {
			mapDifferent(from, to);
		}
	}

	private void mapCommon(Class<F> from, Class<T> to) {
		Class<?> common = getCommonParrent(from.getClass(), to.getClass());
		List<Field> commonFields = Arrays.asList(common.getDeclaredFields());

		saveFields(from, to, commonFields);
	}

	private void mapDifferent(Class<F> from, Class<T> to) {
		List<Field> fromFields = getAllFields(from);
		List<Field> toFields = getAllFields(to);
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
				//this means that the field does not have a getter or a setter and will not be mapped;
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
	 * @throws CommonSuperClassException
	 *             when there is no common super class for the two classes,
	 *             other than the Object class
	 * @throws UndefinedMethodException
	 *             when there does not exist a getter for the given field
	 * 
	 * @param from
	 *            the object which you want to convert
	 * @param to
	 *            the class which you want your values mapped
	 * @return the object which has the values set
	 */
	public T map(F from) {

		try {
			T to = toClass.newInstance();

			for (Method getter : getterSetter.keySet()) {
				Method setter = getterSetter.get(getter);

				Object result = getter.invoke(from);

				setter.invoke(to, result);
			}
			return to;
		} catch (InstantiationException | IllegalAccessException
				| SecurityException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * This methods maps the values from one type of object to another type of
	 * object, the values are being get from the getter methods and are being
	 * set using the setter methods.The number of getter methods must be equal
	 * to the number of setter methods. Modification can be made to ignore the
	 * common superclass but then the number
	 * 
	 * @throws CommonSuperClassException
	 *             when there is no common super class for the two classes,
	 *             other than the Object class
	 * @throws UndefinedMethodException
	 *             when there does not exist a getter for the given field
	 * 
	 * @param from
	 *            the object which you want to convert
	 * @param to
	 *            the class which you want your values mapped
	 * @return a list of objects which have the values set
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
	 * @param o
	 *            object from which the fields to be returned
	 * @return the list of fields of the givven object
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
	 * @throws CommonSuperClassException
	 *             when there is no common super class for the two classes,
	 *             other than the Object class
	 * 
	 * @param from
	 *            the class from which the values will be mapped
	 * @param to
	 *            the class on which the values will be mapped
	 * @return the common super class for the two class
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

}
