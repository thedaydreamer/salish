
package schilling.richard.dalvik.model;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import schilling.richard.dalvik.model.Clazz.PlatformClazz;
import android.util.Log;
import android.util.SparseArray;

import com.android.dx.dex.DexFormat;
import com.android.dx.dex.DexOptions;
import com.android.dx.dex.SizeOf;
import com.android.dx.dex.TableOfContents;
import com.android.dx.util.ByteArrayAnnotatedOutput;

/**
 * <p>
 * DEX files are actually very simple structures. A DEX file is a linear
 * representation of a tree structure. This class helps work with that structure
 * by converting between the flat/binary form and an in-memory form.
 * <p>
 * This class builds up a DEX representation one element at a time. It supports
 * the ability to read a definition from a DexBuffer, modify the item and then
 * insert it into another DexBuffer.
 * <p>
 * This also serves as a simplified model of class structure. Most models we see
 * (e.g. DexFile) are more complicated than needed. And, working with a
 * DexBuffer directly forces us to interact with referenced and dereferenced
 * items like strings, types, proto_ids, and method_ids. It's more complicated
 * than it needs to be when you're trying to work with DEX data. Those formats
 * are requirements of the output file, but nothing says we have to stick with
 * that when the data is in memory.
 * <p>
 * string_ids:
 * 
 * @author rschilling
 */
public class DexModel {

    public static final String LOG_TAG = "Finnr.DexBufferBuilder";

    private byte[] magic = null;

    /**
     * Master list of Strings. The string constant pool
     */
    private Vector<String> stringPool = new Vector<String>();

    /**
     * The items in the types list that are identifiers. Every item in this list
     * must also exist in the strings list. A type is just a string.
     */
    private Vector<String> typePool = new Vector<String>();

    /**
     * Method prototypes. A method object cannot be created until a prototype is
     * inserted into this list.
     */
    private Vector<MethodPrototype> prototypePool = new Vector<MethodPrototype>();

    private Vector<Field> fieldPool = new Vector<Field>();

    private Vector<Method> methodPool = new Vector<Method>();

    /* package */final Hashtable<String, Clazz> classTable = new Hashtable<String, Clazz>();

    public void setMagic(byte[] magic) {
        this.magic = magic;
    }

    /**
     * Returns the string constant pool as an ummutable list, sorted.
     * 
     * @return the string pool.
     */
    public List<String> stringPool() {
        Collections.sort(stringPool);
        return Collections.unmodifiableList(stringPool);
    }

    /**
     * Returns the number of strings in the string pool
     * 
     * @return the string pool count.
     */
    public int stringPoolCount() {
        return stringPool.size();
    }

    /**
     * Returns the number of strings in the string pool
     * 
     * @return the string pool count.
     */
    public int typePoolCount() {
        return typePool.size();
    }

    public int protoPoolCount() {
        return prototypePool.size();
    }

    public int fieldPoolCount() {
        return fieldPool.size();
    }

    public int methodPoolCount() {
        return methodPool.size();
    }

    /**
     * Returns the number of classes in this model that would be dumped to a DEX
     * file. Only the count of classes defined in this model are included in the
     * result. Platform classes are not.
     * 
     * @return defined classes count.
     */
    public int classDefPoolCount() {
        return classPool(false).size();
    }

    /**
     * Returns the type constant pool as an ummutable list, sorted.
     * 
     * @return the type pool.
     */
    public List<String> typePool() {
        Collections.sort(typePool);
        return Collections.unmodifiableList(typePool);
    }

    /**
     * Returns the prototype constant pool as an ummutable list, sorted.
     * 
     * @return the prototype pool.
     */
    public List<MethodPrototype> prototypePool() {
        Collections.sort(prototypePool);
        return Collections.unmodifiableList(prototypePool);
    }

    /**
     * Returns the field constant pool as an ummutable list, sorted.
     * 
     * @return the field pool.
     */
    public List<Field> fieldPool() {
        Collections.sort(fieldPool);
        return Collections.unmodifiableList(fieldPool);
    }

    /**
     * Returns the method constant pool as an ummutable list, sorted.
     * 
     * @return the method pool.
     */
    public List<Method> methodPool() {
        Collections.sort(methodPool);
        return Collections.unmodifiableList(methodPool);
    }

    /**
     * Adds a method to the method pool if it is not already present.
     * 
     * @param m the method to add
     * @return true if the method pool was changed or false otherwise.
     */
    public boolean addToMethodPool(Method m) {
        if (methodPool.contains(m))
            return false;

        methodPool.add(m);
        return true;

    }

    /**
     * Adds a field to the field pool if it is not already present.
     * 
     * @param m the field to add
     * @return true if the field pool was changed or false otherwise.
     */
    public boolean addToFieldPool(Field f) {
        if (fieldPool.contains(f))
            return false;

        fieldPool.add(f);
        return true;

    }

    /**
     * Adds a prototype to the prototype pool if it is not already present.
     * 
     * @param m the prototype to add
     * @return true if the prototype pool was changed or false otherwise.
     */
    public boolean addToPrototypePool(MethodPrototype p) {
        if (prototypePool.contains(p))
            return false;

        prototypePool.add(p);
        return true;

    }

    public List<Clazz> classPool(boolean includePlatform) {

        List<Clazz> result = new LinkedList<Clazz>();
        for (Enumeration<Clazz> cEnum = classTable.elements(); cEnum.hasMoreElements();) {
            Clazz c = cEnum.nextElement();
            if (!includePlatform && (c instanceof PlatformClazz))
                continue;
            result.add(c);

        }

        Collections.sort(result);

        return Collections.unmodifiableList(result);

    }

    public byte[] getMagic() {
        if (magic == null) {
            String m = DexFormat.apiToMagic(DexFormat.API_CURRENT);
            magic = new byte[8];
            // Write the magic number.
            for (int i = 0; i < 8; i++) {
                magic[i] = (byte) m.charAt(i);
            }

        }
        return magic;
    }

    public DexOptions getOptions() {

        DexOptions result = new DexOptions();
        result.targetApiLevel = DexFormat.magicToApi(getMagic());

        return result;

    }

    public int getApiLevel() {
        return DexFormat.magicToApi(getMagic());
    }

    /**
     * Finds the index of the specified string.
     * 
     * @param s
     *            the string to look for.
     * @return the position of the string.
     * @throws IllegalArgumentException
     *             if the string is not in the list.
     */
    private int indexOfString(String s) {
        String s0 = s.intern();
        if (!stringPool.contains(s0))
            throw new IllegalArgumentException("string " + s
                    + " is not part of the string constant pool.");

        return stringPool.indexOf(s0);

    }

    private int indexOfType(String t) {
        String t0 = t.intern();
        if (!typePool.contains(t0))
            throw new IllegalArgumentException("string " + t
                    + " is not part of the master list of strings.");

        return typePool.indexOf(t0);
    }

    public boolean isInStringPool(String s) {
        if (s == null)
            return false;

        String s0 = s.intern();
        return stringPool.contains(s0);

    }

    public boolean isInTypePool(String t) {
        if (t == null)
            return false;

        String t0 = t.intern();
        return typePool.contains(t0);
    }

    public void addToStringPool(String s) {
        verifyString(s);

        String s0 = s.intern();

        if (stringPool.contains(s0)) {
            Log.i(LOG_TAG, "import of string " + s0 + " skipped (duplicate)");
            return;
        }

        stringPool.add(s0);
        Log.i(LOG_TAG, "import of string " + s0 + " successful");
        Collections.sort(stringPool);
    }

    public void addToStringPool(List<String> strings) {
        for (String s : strings) {
            addToStringPool(s);
        }
    }

    public void addToTypePool(String t) {
        verifyString(t);

        String t0 = t.intern();

        if (!stringPool.contains(t0))
            throw new IllegalArgumentException("type " + t0 + " must be added as a string first");

        if (typePool.contains(t0)) {
            Log.i(LOG_TAG, "import of type " + t0 + " skipped (duplicate)");
            return;
        }

        typePool.add(t0);
        Log.i(LOG_TAG, "import of type " + t0 + " successful");
        Collections.sort(typePool);

    }

    public void addToTypePool(List<String> strings) {

        // TODO add a check for a properly formatted type string. See
        // TypeDescriptor section in DEX file doc.
        for (String s : strings) {
            addToTypePool(s);
        }
    }

    private void verifyString(String s) {
        if (s == null)
            throw new IllegalArgumentException("s cannot be null");

        if (s.trim().length() == 0)
            throw new IllegalArgumentException("s cannot be empty");

    }

    public Clazz getFromClassTable(String signature) {
        return classTable.get(signature.intern());
    }

    public boolean isInClassPool(Clazz c) {

        return classTable.containsValue(c);
    }

    public boolean isInClassPool(String specifier) {
        return classTable.containsKey(specifier.intern());
    }

    public void dump(boolean verbose) {

        // string

        StringBuilder builder = new StringBuilder();
        builder.append("---------------- DexModel Dump ----------------\n");
        builder.append("string pool count ").append(stringPool.size()).append(" \n");

        if (verbose) {
            int i = 0;
            for (String s : stringPool) {
                builder.append("  ").append(Integer.toString(i)).append(" ");
                builder.append(s).append("\n");
                i++;
            }
        }
        builder.append("\n");
        Log.i(LOG_TAG, builder.toString());

        // type
        builder = new StringBuilder();
        builder.append("type pool count ").append(typePool.size()).append("\n");

        if (verbose) {
            int i = 0;
            for (String s : typePool) {
                builder.append("  ").append(Integer.toString(i)).append(" ");
                builder.append(s).append("\n");
                i++;
            }
        }
        builder.append("\n");
        Log.i(LOG_TAG, builder.toString());

        // proto
        builder = new StringBuilder();
        builder.append("prototype count ").append(prototypePool.size()).append("\n");
        if (verbose) {
            int i = 0;
            for (MethodPrototype mp : prototypePool) {
                builder.append("  ").append(Integer.toString(i)).append(" ");
                builder.append(mp.toString()).append("\n");
                i++;
            }
        }
        Log.i(LOG_TAG, builder.toString());

        // field
        builder = new StringBuilder();
        builder.append("field count ").append(fieldPool.size()).append("\n");
        if (verbose) {
            int i = 0;
            for (Field f : fieldPool) {
                builder.append("  ").append(Integer.toString(i)).append(" ");
                builder.append(f.toString()).append("\n");
                i++;
            }
        }

        // method
        builder = new StringBuilder();
        builder.append("method count ").append(fieldPool.size()).append("\n");
        if (verbose) {
            int i = 0;
            for (Method m : methodPool) {
                builder.append("  ").append(Integer.toString(i)).append(" ");
                builder.append(m.toString()).append("\n");
                i++;
            }
        }

        // class
        builder = new StringBuilder();
        builder.append("class count ").append(classTable.size()).append("\n");

        if (verbose) {
            int i = 0;
            for (Enumeration<Clazz> values = classTable.elements(); values.hasMoreElements();) {

                builder.append("  ").append(Integer.toString(i)).append(" ");
                builder.append(values.nextElement().toString()).append("\n");
                i++;
            }
        }

        builder.append("\n");

        Log.i(LOG_TAG, builder.toString());

    }

    /**
     * Generates a sparse array of strings where the index is the byte position
     * of each string in the file. The first index value is the value returned
     * from getDataOffset(). Once this item is retrieved, other lists can refer
     * to this by either index or position, which is what the DEX file will
     * require.
     * 
     * @return a sparse array of strings that appear in the data section.
     */
    public SparseArray<String> positionStrings(int startingAt) {

        SparseArray<String> result = new SparseArray<String>();

        Collections.sort(stringPool);

        ByteArrayAnnotatedOutput out = new ByteArrayAnnotatedOutput(0);

        for (String s : stringPool) {
            int pos = out.getCursor();

        }

        return result;

    }

    public int mapListCount() {
        return 0;
    }

    /**
     * Return the number of type lists in the model.
     * 
     * @return
     */
    public int typeListCount() {
        return 0;
    }

}
