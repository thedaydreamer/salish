
package schilling.richard.dexlib.lang.clazz;

import schilling.richard.dexlib.lang.DexLoader.TempClassInfo;

/**
 * Represents classes that were loaded from a dex file, as opposed to being a
 * PletformClassDef.
 * 
 * @author rschilling
 */
public class LoadedClassDef extends ObjectClassDef {

    public LoadedClassDef(String specifier) {
        super(specifier);
    }

    public LoadedClassDef(TempClassInfo tClassInfo) {
        super(tClassInfo);
    }

}
