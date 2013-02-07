package schilling.richard.dexlib.lang.clazz;

import java.util.EnumSet;
import java.util.HashMap;

import schilling.richard.dexlib.lang.DexLoader.TempClassInfo;

import static schilling.richard.dexlib.lang.clazz.ModifierEnum.*;

/**
 * All class definitions in the libarary extend this class. Has package private
 * scope.
 * 
 * @author rschilling
 * 
 */
public abstract class ClassDef {
	public static final String CLASS_SPECIFIER_PREFIX = "class definition for class ";

	private String signature;
	
	private EnumSet<ModifierEnum> classModifiers = EnumSet.noneOf(ModifierEnum.class);

	/**
	 * Create a new class definition. The specifier is the internal name of the
	 * class as seen by the class loader (e.g. "Ljava/lang/Objet"). See the VM
	 * spec for more informaton on class specifiers (or the Javadoc for
	 * java.lang.Class<T>).
	 * 
	 * @param specifier
	 *            the specifier of the class
	 * @throws IllegalArgumentException
	 *             if specifier is null.
	 */
	public ClassDef(String signature) {
		if (signature == null)
			throw new IllegalArgumentException(
					"class specifier cannot be null.");

		this.signature = signature;
	}
	
	

	/**		
	 * Returns the class specifier
	 * 
	 * @return the class specifier that was passed to the constructor.
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * Defined to return the value of the class specifier with a prefix so that
	 * it is easy to see that this class represents a class definition.
	 */
	public String toString() {
		return CLASS_SPECIFIER_PREFIX + signature;
	}
	

	public boolean isInterface(){
		if (!(this instanceof ObjectClassDef))
			return false;
		
		for (ModifierEnum en : classModifiers){
			if (en == INTERFACE)
				return true;
		}
		
		return false;
	}
	
	protected void setModifier(ModifierEnum mEnum){
		if (classModifiers.contains(mEnum))
			return;
		
		if (!classModifiers.add(mEnum))
			throw new RuntimeException("Unable to set modifier.");
		
	}

}
