package autoMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public class AutoMapper<F, T> {

	private boolean findCommon;

	/**
	 * Constructor which must have specified if while mapping to look for a
	 * common super class
	 * 
	 * @param findCommon
	 *            specify if the common parent of the two classes if to be found
	 */
	public AutoMapper(boolean findCommon) {
		this.findCommon = findCommon;
	}

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
	public T map(F from, Class<T> toType) {

		try {
			T to = toType.newInstance();

			if (findCommon) {
				mapCommon(from, to, toType);
			} else {
				mapDifferent(from, to);
			}

			return to;
		} catch (InstantiationException | IllegalAccessException
				| SecurityException e) {
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
	public List<T> map(List<F> from, Class<T> to) {
		List<T> t = new ArrayList<>();

		for (F f : from) {
			t.add(map(f, to));
		}

		return t;
	}

	public void mapCommon(F from, T to, Class<T> toType) {
		Class<?> common = getCommonParrent(from.getClass(), toType);
		List<Field> fields = Arrays.asList(common.getDeclaredFields());

		for (int i = 0; i < fields.size(); i++) {
			Object value = runGetter(fields.get(i), from);

			runSetter(fields.get(i), to, value);
		}
	}

	public void mapDifferent(F from, T to) {
		List<Field> fromFields = getAllFields(from);
		
		List<Field> toFields = getAllFields(to);
		
		for (int i = 0; i < fromFields.size(); i++) {
			Object value = runGetter(fromFields.get(i), from);

			runSetter(toFields.get(i), to, value);
		}
	}
	
	private List<Field> getAllFields(Object o) {
		List<Field> fields = new ArrayList<>();

		Class<?> clazz = o.getClass();
		while(clazz.getSuperclass() != null) {
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

	/**
	 * The method finds the getter for a given field and invokes it's
	 * appropriate getter
	 * 
	 * @throws IllegalAccessException
	 *             when the class on which this is invoked it does not have the
	 *             public modifier
	 * @throws InvocationTargetException
	 *             when the methods from the class could not be invoked
	 * @throws UndefinedMethodException
	 *             when there does not exist a getter for the given field
	 * 
	 * @param field
	 *            finds the getter for the given field and invokes it, thus
	 *            getting the value
	 * @param o
	 *            the object on which the looking for the getter method will be
	 *            run
	 * @return the value that is obtained after invoking the getter
	 */
	private Object runGetter(Field field, F o) {
		for (Method method : o.getClass().getMethods()) {
			if ((method.getName().startsWith("get"))
					&& (method.getName().length() == (field.getName().length() + 3))) {
				if (method.getName().toLowerCase()
						.endsWith(field.getName().toLowerCase())) {
					try {
						return method.invoke(o);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}

				}
			} else if ((method.getName().startsWith("is"))
					&& (method.getName().length() == (field.getName().length() + 2))) {
				if (method.getName().toLowerCase()
						.endsWith(field.getName().toLowerCase())) {
					try {
						return method.invoke(o);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}

				}
			}
		}

		throw new UndefinedMethodException();
	}

	/**
	 * The method finds the setter for a given field and invokes it's
	 * appropriate setter
	 * 
	 * @throws IllegalAccessException
	 *             when the class on which this is invoked it does not have the
	 *             public modifier
	 * @throws InvocationTargetException
	 *             when the methods from the class could not be invoked
	 * @throws UndefinedMethodException
	 *             when there does not exist a getter for the given field
	 * 
	 * @param field
	 *            finds the setter for the given field and invokes it giving the
	 *            value, thus setting the value
	 * @param o
	 *            the object on which the looking for the setter method will be
	 *            run
	 * @param value
	 *            the value that will be given to the setter value
	 */
	private void runSetter(Field field, T o, Object value) {
		for (Method method : o.getClass().getMethods()) {
			if ((method.getName().startsWith("set"))
					&& (method.getName().length() == (field.getName().length() + 3))) {
				if (method.getName().toLowerCase()
						.endsWith(field.getName().toLowerCase())) {
					try {
						method.invoke(o, value);
						
						return;
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}

				}
			}
		}

		throw new UndefinedMethodException();
	}
}
