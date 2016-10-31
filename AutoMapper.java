package Utilities;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @param <F>
 *            the abbreviation means From
 * @param <T>
 *            the abbreviation means To
 */
public class AutoMapper<F, T> {

	/**
	 * This methods maps the values from one type of object to another type of object. 
	 * Modification can be made to ignore the common superclass but then the number of getters and the number of setters must be the same 
	 * 
	 * @throws CommonSuperClassException when the from and to do not have a common super class
	 * @throws IllegalAccessException when the methods from the classes could not be accessed
	 * @throws InvocationTargetException when the methods from the classes could not be invoked
	 * 
	 * @param from
	 *            the object which you want to convert
	 * @param to
	 *            the class which you want your values mapped
	 * @return the object which has the mapped values
	 */
	public T map(F from, Class<T> to) {
		try {
			T t = to.newInstance();
			
			Class<?> common = getCommonParrent(from.getClass(), to);
			List<Field> fields = Arrays.asList(common.getDeclaredFields());

			for (int i = 0; i < fields.size(); i++) {
				runSetter(fields.get(i), t, runGetter(fields.get(i), from));
			}

			return t;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public Class<?> getCommonParrent(Class<?> from, Class<?> to) {
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

	public Object runGetter(Field field, F o) {
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

		return null;
	}

	public void runSetter(Field field, T o, Object value) {
		for (Method method : o.getClass().getMethods()) {
			if ((method.getName().startsWith("set"))
					&& (method.getName().length() == (field.getName().length() + 3))) {
				if (method.getName().toLowerCase()
						.endsWith(field.getName().toLowerCase())) {
					try {
						method.invoke(o, value);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}

				}
			}
		}
	}
}
