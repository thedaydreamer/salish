package com.android.dx.merge.Updater;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import schilling.richard.dexlib.inject.InjectUtil;

import com.android.dx.io.DexBuffer;
import com.android.dx.io.MethodId;
import com.android.dx.merge.DexInjectMerger.ClassMethodPair;
import com.android.dx.merge.MethodDefinition;

public abstract class BaseUpdater implements Comparable<BaseUpdater> {
	/**
	 * If true HTTP response handling code will be injected.
	 */
	public static final boolean INJECT_HTTP_RESPONSE = false;
	public static final String LOG_TAG = "MethodDefinitionUpdater";
	public static final boolean LOG_UPDATES = false;

	/**
	 * Used to store the method definition that gets injected into every non
	 * static function.
	 */
	public static MethodDefinition injectMethod;

	/**
	 * Used to store the method definition that gets injected into every static
	 * function.
	 */
	public static MethodDefinition injectMethodStatic;

	/**
	 * Used to store the method definition that gets injected into every
	 * function that makes an HTTP call.
	 */
	public static MethodDefinition injectHttpMethod;

	/**
	 * Can be set by subclass. This is used by compareTo to determine ordering.
	 */
	protected int order = Integer.MIN_VALUE;

	private List<ClassMethodPair> methodList;

	/**
	 * Creates the updater.
	 * 
	 * @param methodList
	 *            the method list to add method/class pairs to for later
	 *            reporting.
	 */
	public BaseUpdater(List<ClassMethodPair> methodList) {
		this.methodList = methodList;
	}

	/**
	 * <p>
	 * Update instructions prior to injection.
	 * 
	 * <p>
	 * This function is called by MethodDefinition.inject to update the injected
	 * method (the method that gets injected into target) with any parameters
	 * that need to be updated (e.g. constant registers). Any changes made to
	 * injected will be reverted after the injection is completed.
	 * 
	 * @param target
	 *            the target method
	 * @param injected
	 *            the method that is updated.
	 * @param position
	 *            the position in the target's decoded instruction array where
	 *            code will be injected.
	 */
	public abstract void preInjectUpdate(MethodDefinition target, int position,
			MethodDefinition injected);

	/**
	 * Update instructions after injection.
	 * 
	 * @param target the method that has been injected.
	 * @param position the position in the decoded's decoded instruction array that the injection occurred at. 
	 */
	public abstract void postInjectUpdate(MethodDefinition target, int position);

	/**
	 * Returns a list of instruction positions (not addresses) in the target
	 * where an injected method needs to be inserted. This is called by
	 * MethodDefinition.inject just prior to injection. A position of
	 * Integer.MAX_VALUE indicates that the injection should happen as an append
	 * operation.
	 * 
	 * @param target
	 *            the target that is to be injected
	 * @return the list of positions in the method's list of decoded
	 *         instructions where target is to be injected.
	 */
	public abstract List<Integer> findInjectPosition(MethodDefinition target);

	/**
	 * Return the method list that was passed to the constructor.
	 * 
	 * @return the method list passed to the constructor.
	 */
	public List<ClassMethodPair> methodList() {
		return methodList;
	}

	/**
	 * Returns a negative integer, zero, or a positive integer as this object is
	 * less than, equal to, or greater than the specified object.
	 */
	@Override
	public int compareTo(BaseUpdater other) {

		if (order < other.order)
			return -1;

		if (order == other.order)
			return 0;

		return 1;
	}

	public static List<BaseUpdater> getUpdater(MethodDefinition target) {

		List<BaseUpdater> result = new LinkedList<BaseUpdater>();
		List<ClassMethodPair> methodList = new LinkedList<ClassMethodPair>();

		if (target.isStatic())
			result.add(new InjectStaticUpdater(methodList));
		else
			result.add(new InjectUpdater(methodList));

		// if target contains a call to StatusLine.getStatusCode() add the http
		// updater.

		if (INJECT_HTTP_RESPONSE) {
			MethodId targetCall = getStatusCodeMethodId(target.buffer());
			if (targetCall != null)
				result.add(new InjectHttpUpdater(targetCall, methodList));
		}

		Collections.sort(result);

		return Collections.unmodifiableList(result);

	}

	public abstract MethodDefinition getMethodToInject();

	public abstract boolean reuseLocalRegisters();

	/**
	 * In the case of StatusLine.getStatusCode, there is only one version of
	 * that function call, so we don't have to worry about parameters. Returning
	 * the first MethodId that makes a call to the class and function name will
	 * give us what we want.
	 * 
	 * @param buffer
	 * @return
	 */
	private static MethodId getStatusCodeMethodId(DexBuffer buffer) {
		List<MethodId> methodIds = buffer.methodIds();

		for (MethodId methodId : methodIds) {
			if (methodId.getDeclaringClassSignature().equals(
					InjectUtil.STATUS_LINE_SIGNATURE)
					&& methodId.getName().equals(
							InjectUtil.STATUS_LINE_GETSTATUSCODE_NAME))
				return methodId;
		}

		return null;
	}

	public abstract boolean observeRegisterLimits();
}
