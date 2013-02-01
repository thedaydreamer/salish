
package schilling.richard.dexlib.visitors;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.gnu.salish.visitors.DexFileListener;

import com.android.dx.io.ClassDef;

/**
 * Builds up a list of classes defined in the enhancer DEX file. The strings in
 * classList are interned.
 * 
 * @author rschilling
 */
public class DefinedClassListener extends DexFileListener {

    private List<String> classList = new LinkedList<String>();

    public List<String> getClassList() {

        Collections.sort(classList);
        return classList;

    }

    @Override
    public boolean shouldVisit(ClassDef cDef) {
        return (cDef.getClassDataOffset() > 0);
    }

    public void onClassDefFound(ClassDef cDef) {
        classList.add(cDef.getSignature().intern());

    }

}
