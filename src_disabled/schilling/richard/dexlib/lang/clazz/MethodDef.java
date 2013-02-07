package schilling.richard.dexlib.lang.clazz;

import java.util.EnumSet;

/**
 * Represents a method definition.
 * @author rschilling
 *
 */
public class MethodDef {
	
	private String name;
	private ClassDef parent;
	private EnumSet<ModifierEnum> modifiers = EnumSet.noneOf(ModifierEnum.class);
	
	public MethodDef(ClassDef parent, String name){
		this.name = name;
		this.parent = parent;
	}
	
	public String toString(){
		return getName();
	}
	
	public String getName(){
		return name;
	}
	
	public ClassDef getParent(){
		return parent;
	}
	
	public boolean setModifier(ModifierEnum me){
		if (modifiers.contains(me))
			return true;
		
		if (!modifiers.add(me))
			throw new IllegalStateException("unable to set the modifier.");
		
		return true;
	}
	
	public boolean clearModifier(ModifierEnum me){
		return modifiers.remove(me);
	}
	
}
