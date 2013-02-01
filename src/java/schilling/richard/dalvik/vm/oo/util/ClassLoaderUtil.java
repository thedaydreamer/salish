package schilling.richard.dalvik.vm.oo.util;

public final class ClassLoaderUtil {
	private ClassLoaderUtil() {
	}

	public static boolean isFoundOnPlatform(String classType) {
		String type = classType;
		if (type.startsWith("[")) {
			int last = type.lastIndexOf("[");
			type = type.substring(last + 1);
		}
		String name = convertTypeToClassName(type);
		try {
			Class<?> c = Class.forName(name);
		} catch (ClassNotFoundException e) {
			return false;
		}

		return true;

	}

	/**
	 * Converts a signature string to a qualified class name. Array class names
	 * are not supported since they always resolve to Object.
	 * 
	 * @param typeString
	 *            the type to convert in the form of 'L/package/name/ClassName;'
	 * @return the type converted to dot notation such as
	 *         'package.name.ClassName';
	 */
	public static String convertTypeToClassName(String typeString) {
		if (typeString == null)
			throw new IllegalArgumentException("type string must not be null.");

		String type = typeString.trim();
		if (type.length() == 0)
			throw new IllegalArgumentException("type string must not be empty");

		if (type.charAt(0) != 'L' || !type.endsWith(";"))
			throw new IllegalArgumentException(String.format(
					"type string %s is not a class specifier", typeString));

		int endPosition = type.indexOf(';');
		if (endPosition == -1)
			endPosition = type.length() - 1;

		String classString = type.substring(1, endPosition);
		classString = classString.replace('/', '.');
		return classString;

	}

	/**
	 * Returns the class depth of a class on the platform.
	 * 
	 * @param c
	 *            the class to calculate the depth for.
	 * @return the number of superclasses the specified class has.
	 */
	public static int getClassDepth(Class<?> c) {
		if (c == null)
			throw new IllegalArgumentException("argument cannot be null");

		int result = 0;

		Class<?> supClass = c.getSuperclass();
		while (supClass != null) {
			result++;
			supClass = supClass.getSuperclass();
		}

		return result;

	}

}
