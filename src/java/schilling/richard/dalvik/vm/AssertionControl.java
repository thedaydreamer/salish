package schilling.richard.dalvik.vm;

/*
 * One of these for each -ea/-da/-esa/-dsa on the command line.
 */
public class AssertionControl {

	public String pkgOrClass; /* package/class string, or NULL for esa/dsa */
	public int pkgOrClassLen; /* string length, for quick compare */
	public boolean enable; /* enable or disable */
	public boolean isPackage; /* string ended with "..."? */

}
