package schilling.richard.dalvik.vm;

/**
 * Types of indexed reference that are associated with opcodes whose formats
 * include such an indexed reference (e.g., 21c and 35c).
 */
public enum InstructionIndexType {
	kIndexUnknown, kIndexNone, // has no index
	kIndexVaries, // "It depends." Used for throw-verification-error
	kIndexTypeRef, // type reference index
	kIndexStringRef, // string reference index
	kIndexMethodRef, // method reference index
	kIndexFieldRef, // field reference index
	kIndexInlineMethod, // inline method index (for inline linked methods)
	kIndexVtableOffset, // vtable offset (for static linked methods)
	kIndexFieldOffset // field offset (for static linked fields)

}
