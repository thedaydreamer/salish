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
 *      <code> 	public static final Info INVOKE_INTERFACE_JUMBO = new Info(Opcodes.INVOKE_INTERFACE_JUMBO, "invoke-interface/jumbo", InstructionCodec.FORMAT_5RC, IndexType.METHOD_REF); </code>
 * </pre>
 * 
 */
public final class INVOKE_INTERFACE_JUMBO extends OpcodeInfo.Info {
	public INVOKE_INTERFACE_JUMBO() {
		super(Opcodes.INVOKE_INTERFACE_JUMBO, "invoke-interface/jumbo",
				InstructionCodec.FORMAT_5RC, IndexType.METHOD_REF);
		this.verifier = new NoVerification();
		this.regTypeFactory = new RegisterTypeInvokeInterface();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
