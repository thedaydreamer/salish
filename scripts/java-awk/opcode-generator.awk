# awk to generate classes
# example statement:
# public static final Info INVOKE_STATIC_JUMBO = new Info(Opcodes.INVOKE_STATIC_JUMBO, "invoke-static/jumbo", InstructionCodec.FORMAT_5RC, IndexType.METHOD_REF);
#

BEGIN{ 
	package="com.android.dx.io.opcode.info"; 
	FS = "[ ()]"
	OPCODEFILE = "OpcodeInfo.java"
	print "// insert into OpcodeInfo.java" > OPCODEFILE;
}
{
	if (NF > 0){
	OUTFILE = ($5".java")
	
	print "File: ", OUTFILE;
	print "public static final Info", $5, "= new", $5, "();" >> OPCODEFILE;
	print "package", (package ";") > OUTFILE;
	print "" >> OUTFILE;
	print "import schilling.richard.dalvik.vm.analysis.RegisterType;" >> OUTFILE;
	print "import com.android.dx.io.IndexType;" >> OUTFILE;
	print "import com.android.dx.io.opcode.OpcodeInfo;" >> OUTFILE;
	print "import com.android.dx.io.opcode.Opcodes;" >> OUTFILE;
	print "import com.android.dx.io.opcode.RegisterReference;" >> OUTFILE;
	print "import com.android.dx.io.opcode.format.InstructionCodec;" >> OUTFILE;
	print "" >> OUTFILE;
	print "" >> OUTFILE;
	print "/**" >> OUTFILE;
	print " * processed by opcode-generator.awk.  Original line from OpcodeInfo:" >> OUTFILE;
	print " * <pre>" >> OUTFILE;
	print " *      <code>", $0, "</code>" >> OUTFILE;
	print " * </pre>" >> OUTFILE;
	print " *" >> OUTFILE;
	print " */" >> OUTFILE;
	print "public final class", $5, "extends OpcodeInfo.Info{" >> OUTFILE;
	print "     public", $5, "(){" >> OUTFILE;
	print "          super(", $9, $10, $11, $12, ");" >> OUTFILE;
	print "     }" >> OUTFILE;
	print " " >> OUTFILE;
	print "     @Override" >> OUTFILE;
	print "     public RegisterType getRegisterType(RegisterReference ref) {" >> OUTFILE;
	print "          throw new UnsupportedOperationException(\"TODO define, but call return super.getRegisterType(ref); first\");" >> OUTFILE;
	print "     }" >> OUTFILE;
	print "}" >> OUTFILE;

	close(OUTFILE);
	}
}
