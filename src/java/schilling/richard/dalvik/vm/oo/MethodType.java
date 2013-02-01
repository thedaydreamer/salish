package schilling.richard.dalvik.vm.oo;

/**
 * "Direct" and "virtual" methods are stored independently. The type of call
 * used to invoke the method determines which list we search, and whether we
 * travel up into superclasses.
 * 
 * (<clinit>, <init>, and methods declared "private" or "static" are stored in
 * the "direct" list. All others are stored in the "virtual" list.)
 */
public enum MethodType {

	METHOD_UNKNOWN, METHOD_DIRECT, // <init>, private
	METHOD_STATIC, // static
	METHOD_VIRTUAL, // virtual, super
	METHOD_INTERFACE // interface
}
