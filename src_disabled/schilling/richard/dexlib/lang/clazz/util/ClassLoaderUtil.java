package schilling.richard.dexlib.lang.clazz.util;

public final class ClassLoaderUtil {
	private ClassLoaderUtil(){}
	
	public static boolean isFoundOnPlatform(String classType){
		String name = convertTypeToClassName(classType);
		try {
			Class<?> c = Class.forName(name);
		} catch (ClassNotFoundException e) {
			return false;
		}
		
		return true;
		
	}
	
	public static String convertTypeToClassName(String typeString){
		if (typeString == null)
			throw new IllegalArgumentException("type string must not be null.");
		
		String type = typeString.trim();
		if (type.length() == 0)
			throw new IllegalArgumentException("type string must not be empty");
		
		if (type.charAt(0) != 'L' || !type.endsWith(";"))
			throw new IllegalArgumentException("type string is not a class specifier");
		
		
		
		int endPosition = type.indexOf(';');
		if (endPosition == -1)
			endPosition = type.length() - 1;
			
		
		String classString = type.substring(1, endPosition);
		classString = classString.replace('/', '.');
		return classString;
		
	}

}
