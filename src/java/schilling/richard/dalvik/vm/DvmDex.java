package schilling.richard.dalvik.vm;

import schilling.richard.dexlib.io.DexFile;

public class DvmDex {
	/* pointer to the DexFile we're associated with */
	DexFile pDexFile;

	/* clone of pDexFile->pHeader (it's used frequently enough) */
	// const DexHeader* pHeader;

	/* interned strings; parallel to "stringIds" */
	// struct StringObject** pResStrings;

	/* resolved classes; parallel to "typeIds" */
	// struct ClassObject** pResClasses;

	/* resolved methods; parallel to "methodIds" */
	// struct Method** pResMethods;

	/* resolved instance fields; parallel to "fieldIds" */
	/* (this holds both InstField and StaticField) */
	// struct Field** pResFields;

	/* interface method lookup cache */
	// struct AtomicCache* pInterfaceCache;

	/* shared memory region with file contents */
	boolean isMappedReadOnly;
	// MemMapping memMap; // TODO : implement later - what are the semantics of
	// MemMapping

	/* lock ensuring mutual exclusion during updates */
	static Object modLock = new Object();
}
