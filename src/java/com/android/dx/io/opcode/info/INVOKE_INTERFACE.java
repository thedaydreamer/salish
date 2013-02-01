package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterTypeInvokeInterface;
import com.android.dx.io.opcode.verify.NoVerification;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info INVOKE_INTERFACE = new Info(Opcodes.INVOKE_INTERFACE, "invoke-interface", InstructionCodec.FORMAT_35C, IndexType.METHOD_REF); </code>
 * </pre>
 * 
 */
public final class INVOKE_INTERFACE extends OpcodeInfo.Info {
	public INVOKE_INTERFACE() {
		super(Opcodes.INVOKE_INTERFACE, "invoke-interface",
				InstructionCodec.FORMAT_35C, IndexType.METHOD_REF);
		this.verifier = new NoVerification();
		this.regTypeFactory = new RegisterTypeInvokeInterface();

	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
