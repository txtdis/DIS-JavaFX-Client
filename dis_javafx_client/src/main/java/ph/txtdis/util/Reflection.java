package ph.txtdis.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflection {

	public static <T> T instantiateClass(String name) {
		return instantiateClass(name, null, null);
	}

	public static <T> T instantiateClass(String name, Object[] parameters, Class<?>[] parameterTypes) {
		try {
			return instantiateClass(Class.forName(name), parameters, parameterTypes);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> T instantiateClass(Object object, Object[] parameters, Class<?>[] parameterTypes) {
		return instantiateClass(object.getClass(), parameters, parameterTypes);
	}

	public static <T> T instantiateClass(Class<?> cls) {
		return instantiateClass(cls, null, null);
	}

	@SuppressWarnings("unchecked")
	public static <T> T instantiateClass(Class<?> cls, Object[] parameters, Class<?>[] parameterTypes) {
		try {
			Constructor<?> constructor = cls.getConstructor(parameterTypes);
			return (T) constructor.newInstance(parameters);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> T invokeMethod(Object object, String name) {
		return invokeMethod(object, name, null, null);
	}

	public static <T> T invokeOneParameterMethod(Object object, String name, Object parameter, Class<?> parameterType) {
		return invokeMethod(object, name, new Object[] { parameter }, new Class<?>[] { parameterType });
	}

	public static <T> T invokeMethod(Object object, String name, Object[] parameters, Class<?>[] parameterTypes) {
		try {
			return invoke(object, name, parameters, parameterTypes);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T invoke(Object object, String name, Object[] parameters, Class<?>[] parameterTypes)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Class<?> cls = object.getClass();
		Method method = cls.getMethod(name, parameterTypes);
		return (T) method.invoke(object, parameters);
	}
}
