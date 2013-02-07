package schilling.richard.dexlib.lang.clazz;

import java.util.EnumSet;
import static schilling.richard.dexlib.lang.clazz.PrimitiveClassDef.*;

public enum PrimitiveEnum {
	
	BYTE("byte", 'B', BYTE_DEF), 		SHORT("short", 'S', SHORT_DEF), 
	INT("int", 'I', INT_DEF), 		LONG("long", 'J', LONG_DEF), 
	FLOAT("float", 'F', FLOAT_DEF), 	DOUBLE("double", 'D', DOUBLE_DEF),
	CHAR("char", 'C', CHAR_DEF), 		BOOLEAN("boolean", 'Z', BOOLEAN_DEF);
	
	
	private PrimitiveEnum(String resWord, char specifier, PrimitiveClassDef pClass){
		reservedWord = resWord;
		typeSpecifier = specifier;
		primitiveClass = pClass;
	}
	
	private String reservedWord;
	private char typeSpecifier;
	private PrimitiveClassDef primitiveClass;
	
	public String toString(){
		return asReservedWord();
	}
	
	public String asReservedWord(){
		return reservedWord;
	}
	
	public char asSpecifier(){
		return typeSpecifier;
	}
	
	public PrimitiveClassDef getPrimitiveClass(){
		return primitiveClass;
	}
	
	/**
	 * Converts a primitive signature to its enumerated equivalent.
	 * @param sig the signature to retrieve an enum from.
	 * @return the enumeration associated with the signature
	 * @throws IllegalArgumentException is sig is not one of the primitive type signatures or is an empty string..
	 */
	public static PrimitiveEnum fromSignature(String sig){
		
		if (sig == null || sig.trim().length() == 0)
			throw new IllegalArgumentException("sig cannot be null.");
		
		char sigChar = sig.charAt(0);
		EnumSet<PrimitiveEnum> eSet = EnumSet.allOf(PrimitiveEnum.class);
		
		for (PrimitiveEnum pe : eSet){
			if (pe.asSpecifier() == sigChar)
				return pe;
		}
		
		throw new IllegalArgumentException("signature '" + sig + "' is not a primitive type.");
		
	}

}
