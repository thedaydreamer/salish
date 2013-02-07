package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;
import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.format.InstructionCodec;
/**
 * processed by opcode-generator.awk.  Original line from OpcodeInfo:
 * <pre>
 *      <code> Three </code>
 * </pre>
 *
 */
public final class  extends OpcodeInfo.Info{
     public  (){
          super(     );
     }
 
     @Override
     public RegisterType getRegisterType(RegisterReference ref) {
          throw new UnsupportedOperationException("TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
     }
}
