package schilling.richard.dalvik.vm.oo.util;

import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;

public class ClassPathUtil {
	/**
	 * Checks a class signature and validates it. If the signature is valid this
	 * function does nothing. If the signature is invalid this function throws
	 * an exception. A valid signature is
	 * <ul>
	 * <li>Lpackage/name/ClassName;
	 * <li>[Lpackage/nake/ClassName; (for arrays)
	 * <li>[B (array of a primitive)
	 * </ul>
	 * 
	 * @param signature
	 *            the signature to check.
	 * @throws VerifyException
	 *             if the signature is invalid.
	 */
	public static void verifySignatureOrThrow(String signature,
			boolean allowVoid) throws VerifyException {
		if (signature == null)
			throw new IllegalArgumentException("signature must not be null");

		if (signature.trim().length() == 0)
			throw new IllegalArgumentException(
					"signature cannot be an empty string");

		switch (signature.charAt(0)) {
		case 'I':
		case 'C':
		case 'S':
		case 'B':
		case 'Z':
		case 'F':
		case 'D':
			break;
		case 'V':
			if (!allowVoid)
				throw new VerifyException(
						VerifyErrorCause.VERIFY_ERROR_GENERIC,
						String.format(
								"VFY: bad signature '%s'. Void type not allowed.",
								signature));
		case 'J':
			if (signature.length() != 1)
				throw new VerifyException(
						VerifyErrorCause.VERIFY_ERROR_GENERIC,
						String.format(
								"VFY: bad signature '%s'. Primitive signatures must have only one character.",
								signature));
			break;
		case '[':
			/* single/multi, object/primitive */
			try {
				int signaturePos = signature.lastIndexOf('[') + 1;
				if (signature.charAt(signaturePos) == 'L') {
					if (!signature.endsWith(";"))
						throw new VerifyException(
								VerifyErrorCause.VERIFY_ERROR_GENERIC,
								String.format(
										"VFY: bad signature '%s'. Object signatures must end with a ';'.",
										signature));
				} else if (signaturePos != (signature.length() - 1)) {
					throw new VerifyException(
							VerifyErrorCause.VERIFY_ERROR_GENERIC,
							String.format(
									"VFY: bad signature '%s'. Primitive signatures can only be one character.",
									signature));
				} else {
					char endChar = signature.charAt(signaturePos);
					switch (endChar) {
					case 'I':
					case 'C':
					case 'S':
					case 'B':
					case 'Z':
					case 'F':
					case 'D':
					case 'V':
					case 'J':
					case 'L':
						break;
					default:
						throw new VerifyException(
								VerifyErrorCause.VERIFY_ERROR_GENERIC,
								String.format(
										"VFY: bad signature '%s'. The character %c is not valid for primitive arrays.",
										signature, endChar));
					}
				}
			} catch (IndexOutOfBoundsException ex) {
				throw new VerifyException(
						VerifyErrorCause.VERIFY_ERROR_GENERIC,
						String.format(
								"VFY: bad signature '%s'. Array signature contains no object reference.",
								signature));
			}

			break;
		case 'L':

			if (signature.indexOf(';') == -1
					|| (signature.indexOf(';') != signature.lastIndexOf(';')))
				throw new VerifyException(
						VerifyErrorCause.VERIFY_ERROR_GENERIC,
						String.format(
								"VFY: bad signature '%s'. Class signatures must terminate with a ';' character. Multiple ';' characters not allowed",
								signature));

			break;

		default:
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					String.format("VFY: unexpected signature type char '%c'",
							signature.charAt(0)));
		}

	}
	
	/**
	 * Given a complete Dalvik signature, extract the name of the class.
	 * @param signature the signature to process
	 * @return the name of the class only.
	 */
	public static String getClassName(String signature){
		if (signature == null)
			throw new IllegalArgumentException("signature cannot be null");
		
		if (ClassDefFactory.isPrimitiveClassSpecifier(signature))
			return signature;
		
		int index = 0;
		if (signature.indexOf('/') >= 0)
			index = signature.lastIndexOf('/') + 1;
		
		return signature.substring(index);
		
		
	}
}
