package schilling.richard.dexlib.lang.clazz;

import java.util.EnumSet;

public class FieldDef {

	public static enum FieldCategoryEnum{
		
		REFERENCE(0), WIDE(1), OTHER(2);
		
		private int type;
		
		private FieldCategoryEnum(int t){
			type = t;
		}
		
		
		public int asInt(){
			return type;
		}
		
		public static FieldCategoryEnum getFieldType(String fieldType) {
			switch (fieldType.charAt(0)) {
			case '[':
			case 'L':
				return REFERENCE;
				
			case 'J':
			case 'D':
				return WIDE;
				
			default:
				return OTHER;
			}
		}
		
	}
	
	/**
	 * The class that defines this field.  It's parent.
	 */
	private ClassDef declaringClass;
	private String name;
	private ClassDef fieldClass;
	private EnumSet<ModifierEnum> modifiers = EnumSet.noneOf(ModifierEnum.class);
	private FieldCategoryEnum category = null;
	
	/**
	 * Creates a new field definition.  Call setDeclaringClass to indicate what class 
	 * this field is defined in.
	 * 
	 * @param type the class that this field is defined to be.
	 * @param name the name of the field.
	 * @param category the overall category of the field.
	 */
	public FieldDef(ClassDef type, String name, FieldCategoryEnum category){
		this.name = name;
		this.category = category;
		this.fieldClass = type;
	}
	
	public void setDeclaringClass(ClassDef declaredIn){
		this.declaringClass = declaredIn;
	}
	
	
	public boolean setModifier(ModifierEnum mod){
		if (modifiers.contains(mod))
			return true;
		
		if (!modifiers.add(mod))
			throw new IllegalStateException("unable to set modifier.");
		
		return true;
	}
	
	/**
	 * Returns the class that declares this field.
	 * @return
	 */
	public ClassDef getDeclaringClass(){
		return declaringClass;
	}
	
	/**
	 * Returns the name of this field.
	 * @return
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns the class associated with this field.
	 * @return the class associated with this field.
	 */
	public ClassDef getType(){
		return fieldClass;
		
	}
	
	/**
	 * Returns the general category that this field fits into : REFERENCE, WIDE, or OTHER
	 * @return the field's category.
	 */
	public FieldCategoryEnum getCategory(){
		return category;
	}

}
