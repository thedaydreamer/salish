package schilling.richard.dalvik.vm.analysis;

import schilling.richard.dalvik.vm.oo.Clazz;
import android.util.SparseArray;

/**
 * A sparse array of uninitialized instances. See CodeVerify.verifyInstruction
 * for more information.
 * 
 * @author rschilling
 * 
 */
public class UninitInstanceMap extends SparseArray<Clazz> {

}
