/*
 * [The "BSD licence"]
 * Copyright (c) 2010 Ben Gruver (JesusFreke)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package schilling.richard.dexlib.disassembler.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import schilling.richard.dexlib.Code.Instruction;
import schilling.richard.dexlib.Code.InstructionWithReference;
import schilling.richard.dexlib.Code.OffsetInstruction;
import schilling.richard.dexlib.Code.Opcode;
import schilling.richard.dexlib.Code.Analysis.AnalyzedInstruction;
import schilling.richard.dexlib.Code.Analysis.MethodAnalyzer;
import schilling.richard.dexlib.Code.Analysis.SyntheticAccessorResolver;
import schilling.richard.dexlib.Code.Analysis.ValidationException;
import schilling.richard.dexlib.Code.Format.Format;
import schilling.richard.dexlib.debug.DebugInstructionIterator;
import schilling.richard.dexlib.disassembler.DalvikDisassembler;
import schilling.richard.dexlib.disassembler.adapter.format.InstructionMethodItemFactory;
import schilling.richard.dexlib.io.AnnotationFormatter;
import schilling.richard.dexlib.io.deserialize.AnnotationSetItem;
import schilling.richard.dexlib.io.deserialize.AnnotationSetRefList;
import schilling.richard.dexlib.io.deserialize.ClassDataItem;
import schilling.richard.dexlib.io.deserialize.CodeItem;
import schilling.richard.dexlib.io.deserialize.DebugInfoItem;
import schilling.richard.dexlib.io.deserialize.MethodIdItem;
import schilling.richard.dexlib.io.deserialize.StringIdItem;
import schilling.richard.dexlib.io.deserialize.TypeIdItem;
import schilling.richard.dexlib.io.util.AccessFlags;
import schilling.richard.io.IndentingWriter;
import android.util.SparseIntArray;

public class MethodDefinition {
	private final ClassDataItem.EncodedMethod encodedMethod;
	private MethodAnalyzer methodAnalyzer;

	private final LabelCache labelCache = new LabelCache();

	private final SparseIntArray packedSwitchMap;
	private final SparseIntArray sparseSwitchMap;
	private final SparseIntArray instructionMap;

	public MethodDefinition(ClassDataItem.EncodedMethod encodedMethod) {

		this.encodedMethod = encodedMethod;

		// TODO: what about try/catch blocks inside the dead code? those
		// will need to be commented out too. ugh.

		if (encodedMethod.codeItem != null) {
			Instruction[] instructions = encodedMethod.codeItem
					.getInstructions();

			packedSwitchMap = new SparseIntArray(1);
			sparseSwitchMap = new SparseIntArray(1);
			instructionMap = new SparseIntArray(instructions.length);

			int currentCodeAddress = 0;
			for (int i = 0; i < instructions.length; i++) {
				Instruction instruction = instructions[i];
				if (instruction.getOpcode() == Opcode.PACKED_SWITCH) {
					packedSwitchMap.append(
							currentCodeAddress
									+ ((OffsetInstruction) instruction)
											.getTargetAddressOffset(),
							currentCodeAddress);
				} else if (instruction.getOpcode() == Opcode.SPARSE_SWITCH) {
					sparseSwitchMap.append(
							currentCodeAddress
									+ ((OffsetInstruction) instruction)
											.getTargetAddressOffset(),
							currentCodeAddress);
				}
				instructionMap.append(currentCodeAddress, i);
				currentCodeAddress += instruction.getSize(currentCodeAddress);
			}
		} else {
			packedSwitchMap = null;
			sparseSwitchMap = null;
			instructionMap = null;
			methodAnalyzer = null;
		}

	}

	public void writeTo(IndentingWriter writer,
			AnnotationSetItem annotationSet,
			AnnotationSetRefList parameterAnnotations) throws IOException {
		final CodeItem codeItem = encodedMethod.codeItem;

		writer.write(".method ");
		writeAccessFlags(writer, encodedMethod);
		writer.write(encodedMethod.method.getMethodName().getStringValue());
		writer.write(encodedMethod.method.getPrototype().getPrototypeString());
		writer.write('\n');

		writer.indent(4);
		if (codeItem != null) {
			if (FinnrApp.getApp().getPrefUseLocalsDirective()) {
				writer.write(".locals ");
			} else {
				writer.write(".registers ");
			}
			writer.printSignedIntAsDec(getRegisterCount(encodedMethod));
			writer.write('\n');
			writeParameters(writer, codeItem, parameterAnnotations);
			if (annotationSet != null) {
				AnnotationFormatter.writeTo(writer, annotationSet);
			}

			writer.write('\n');

			for (MethodItem methodItem : getMethodItems()) {
				if (methodItem.writeTo(writer)) {
					writer.write('\n');
				}
			}
		} else {
			writeParameters(writer, codeItem, parameterAnnotations);
			if (annotationSet != null) {
				AnnotationFormatter.writeTo(writer, annotationSet);
			}
		}
		writer.deindent(4);
		writer.write(".end method\n");
	}

	private static int getRegisterCount(
			ClassDataItem.EncodedMethod encodedMethod) {
		int totalRegisters = encodedMethod.codeItem.getRegisterCount();
		if (FinnrApp.getApp().getPrefUseLocalsDirective()) {
			int parameterRegisters = encodedMethod.method.getPrototype()
					.getParameterRegisterCount();
			if ((encodedMethod.accessFlags & AccessFlags.STATIC.getValue()) == 0) {
				parameterRegisters++;
			}
			return totalRegisters - parameterRegisters;
		}
		return totalRegisters;
	}

	private static void writeAccessFlags(IndentingWriter writer,
			ClassDataItem.EncodedMethod encodedMethod) throws IOException {
		for (AccessFlags accessFlag : AccessFlags
				.getAccessFlagsForMethod(encodedMethod.accessFlags)) {
			writer.write(accessFlag.toString());
			writer.write(' ');
		}
	}

	private static void writeParameters(IndentingWriter writer,
			CodeItem codeItem, AnnotationSetRefList parameterAnnotations)
			throws IOException {
		DebugInfoItem debugInfoItem = null;
		if (FinnrApp.getApp().getPrefOutputDebugInfo() && codeItem != null) {
			debugInfoItem = codeItem.getDebugInfo();
		}

		int parameterCount = 0;
		AnnotationSetItem[] annotations;
		StringIdItem[] parameterNames = null;

		if (parameterAnnotations != null) {
			annotations = parameterAnnotations.getAnnotationSets();
			parameterCount = annotations.length;
		} else {
			annotations = new AnnotationSetItem[0];
		}

		if (debugInfoItem != null) {
			parameterNames = debugInfoItem.getParameterNames();
		}
		if (parameterNames == null) {
			parameterNames = new StringIdItem[0];
		}

		if (parameterCount < parameterNames.length) {
			parameterCount = parameterNames.length;
		}

		for (int i = 0; i < parameterCount; i++) {
			AnnotationSetItem annotationSet = null;
			if (i < annotations.length) {
				annotationSet = annotations[i];
			}

			StringIdItem parameterName = null;
			if (i < parameterNames.length) {
				parameterName = parameterNames[i];
			}

			writer.write(".parameter");

			if (parameterName != null) {
				writer.write(" \"");
				writer.write(parameterName.getStringValue());
				writer.write('"');
			}

			writer.write('\n');
			if (annotationSet != null) {
				writer.indent(4);
				AnnotationFormatter.writeTo(writer, annotationSet);
				writer.deindent(4);

				writer.write(".end parameter\n");
			}
		}
	}

	public LabelCache getLabelCache() {
		return labelCache;
	}

	public ValidationException getValidationException() {
		if (methodAnalyzer == null) {
			return null;
		}

		return methodAnalyzer.getValidationException();
	}

	public int getPackedSwitchBaseAddress(int packedSwitchDataAddress) {
		int packedSwitchBaseAddress = this.packedSwitchMap.get(
				packedSwitchDataAddress, -1);

		if (packedSwitchBaseAddress == -1) {
			Instruction[] instructions = encodedMethod.codeItem
					.getInstructions();
			int index = instructionMap.get(packedSwitchDataAddress);

			if (instructions[index].getOpcode() == Opcode.NOP) {
				packedSwitchBaseAddress = this.packedSwitchMap.get(
						packedSwitchDataAddress + 2, -1);
			}
		}

		return packedSwitchBaseAddress;
	}

	public int getSparseSwitchBaseAddress(int sparseSwitchDataAddress) {
		int sparseSwitchBaseAddress = this.sparseSwitchMap.get(
				sparseSwitchDataAddress, -1);

		if (sparseSwitchBaseAddress == -1) {
			Instruction[] instructions = encodedMethod.codeItem
					.getInstructions();
			int index = instructionMap.get(sparseSwitchDataAddress);

			if (instructions[index].getOpcode() == Opcode.NOP) {
				sparseSwitchBaseAddress = this.packedSwitchMap.get(
						sparseSwitchDataAddress + 2, -1);
			}
		}

		return sparseSwitchBaseAddress;
	}

	/**
	 * @param instructions
	 *            The instructions array for this method
	 * @param instruction
	 *            The instruction
	 * @return true if the specified instruction is a NOP, and the next
	 *         instruction is one of the variable sized switch/array data
	 *         structures
	 */
	private boolean isInstructionPaddingNop(
			List<AnalyzedInstruction> instructions,
			AnalyzedInstruction instruction) {
		if (instruction.getInstruction().getOpcode() != Opcode.NOP
				|| instruction.getInstruction().getFormat().variableSizeFormat) {

			return false;
		}

		if (instruction.getInstructionIndex() == instructions.size() - 1) {
			return false;
		}

		AnalyzedInstruction nextInstruction = instructions.get(instruction
				.getInstructionIndex() + 1);
		if (nextInstruction.getInstruction().getFormat().variableSizeFormat) {
			return true;
		}
		return false;
	}

	private List<MethodItem> getMethodItems() {
		ArrayList<MethodItem> methodItems = new ArrayList<MethodItem>();

		if (encodedMethod.codeItem == null) {
			return methodItems;
		}

		if (FinnrApp.getApp().getPrefRegisterInfoSetAsBitmap() != 0
				|| FinnrApp.getApp().getPrefDeodex()
				|| FinnrApp.getApp().getPrefVerify()) {
			addAnalyzedInstructionMethodItems(methodItems);
		} else {
			addInstructionMethodItems(methodItems);
		}

		addTries(methodItems);
		if (FinnrApp.getApp().getPrefOutputDebugInfo()) {
			addDebugInfo(methodItems);
		}

		if (FinnrApp.getApp().getPrefUseSequentialLabels()) {
			setLabelSequentialNumbers();
		}

		for (LabelMethodItem labelMethodItem : labelCache.getLabels()) {
			methodItems.add(labelMethodItem);
		}

		Collections.sort(methodItems);

		return methodItems;
	}

	private void addInstructionMethodItems(List<MethodItem> methodItems) {
		Instruction[] instructions = encodedMethod.codeItem.getInstructions();

		int currentCodeAddress = 0;
		for (int i = 0; i < instructions.length; i++) {
			Instruction instruction = instructions[i];

			MethodItem methodItem = InstructionMethodItemFactory
					.makeInstructionFormatMethodItem(this,
							encodedMethod.codeItem, currentCodeAddress,
							instruction);

			methodItems.add(methodItem);

			if (i != instructions.length - 1) {
				methodItems.add(new BlankMethodItem(currentCodeAddress));
			}

			if (FinnrApp.getApp().getPrefAddCodeOffsets()) {
				methodItems.add(new MethodItem(currentCodeAddress) {

					@Override
					public double getSortOrder() {
						return -1000;
					}

					@Override
					public boolean writeTo(IndentingWriter writer)
							throws IOException {
						writer.write("#@");
						writer.printUnsignedLongAsHex(codeAddress & 0xFFFFFFFF);
						return true;
					}
				});
			}

			if (!FinnrApp.getApp().getPrefNoAccessorComments()
					&& (instruction instanceof InstructionWithReference)) {
				if (instruction.getOpcode() == Opcode.INVOKE_STATIC
						|| instruction.getOpcode() == Opcode.INVOKE_STATIC_RANGE) {
					MethodIdItem methodIdItem = (MethodIdItem) ((InstructionWithReference) instruction)
							.getReferencedItem();

					if (SyntheticAccessorResolver
							.looksLikeSyntheticAccessor(methodIdItem)) {
						SyntheticAccessorResolver.AccessedMember accessedMember = DalvikDisassembler.syntheticAccessorResolver
								.getAccessedMember(methodIdItem);
						if (accessedMember != null) {
							methodItems
									.add(new SyntheticAccessCommentMethodItem(
											accessedMember, currentCodeAddress));
						}
					}
				}
			}

			currentCodeAddress += instruction.getSize(currentCodeAddress);
		}
	}

	private void addAnalyzedInstructionMethodItems(List<MethodItem> methodItems) {
		methodAnalyzer = new MethodAnalyzer(encodedMethod, FinnrApp.getApp()
				.getPrefDeodex(), DalvikDisassembler.inlineResolver);

		methodAnalyzer.analyze();

		ValidationException validationException = methodAnalyzer
				.getValidationException();
		if (validationException != null) {
			methodItems.add(new CommentMethodItem(
					String.format("ValidationException: %s",
							validationException.getMessage()),
					validationException.getCodeAddress(), Integer.MIN_VALUE));
		} else if (FinnrApp.getApp().getPrefVerify()) {
			methodAnalyzer.verify();

			validationException = methodAnalyzer.getValidationException();
			if (validationException != null) {
				methodItems.add(new CommentMethodItem(String.format(
						"ValidationException: %s",
						validationException.getMessage()), validationException
						.getCodeAddress(), Integer.MIN_VALUE));
			}
		}

		List<AnalyzedInstruction> instructions = methodAnalyzer
				.getInstructions();

		int currentCodeAddress = 0;
		for (int i = 0; i < instructions.size(); i++) {
			AnalyzedInstruction instruction = instructions.get(i);

			MethodItem methodItem = InstructionMethodItemFactory
					.makeInstructionFormatMethodItem(this,
							encodedMethod.codeItem, currentCodeAddress,
							instruction.getInstruction());

			methodItems.add(methodItem);

			if (instruction.getInstruction().getFormat() == Format.UnresolvedOdexInstruction) {
				methodItems.add(new CommentedOutMethodItem(
						InstructionMethodItemFactory
								.makeInstructionFormatMethodItem(this,
										encodedMethod.codeItem,
										currentCodeAddress,
										instruction.getOriginalInstruction())));
			}

			if (i != instructions.size() - 1) {
				methodItems.add(new BlankMethodItem(currentCodeAddress));
			}

			if (FinnrApp.getApp().getPrefAddCodeOffsets()) {
				methodItems.add(new MethodItem(currentCodeAddress) {

					@Override
					public double getSortOrder() {
						return -1000;
					}

					@Override
					public boolean writeTo(IndentingWriter writer)
							throws IOException {
						writer.write("#@");
						writer.printUnsignedLongAsHex(codeAddress & 0xFFFFFFFF);
						return true;
					}
				});
			}

			if (FinnrApp.getApp().getPrefRegisterInfoSetAsBitmap() != 0
					&& !instruction.getInstruction().getFormat().variableSizeFormat) {
				methodItems.add(new PreInstructionRegisterInfoMethodItem(
						instruction, methodAnalyzer, currentCodeAddress));

				methodItems.add(new PostInstructionRegisterInfoMethodItem(
						instruction, methodAnalyzer, currentCodeAddress));
			}

			currentCodeAddress += instruction.getInstruction().getSize(
					currentCodeAddress);
		}
	}

	private void addTries(List<MethodItem> methodItems) {
		if (encodedMethod.codeItem == null
				|| encodedMethod.codeItem.getTries() == null) {
			return;
		}

		Instruction[] instructions = encodedMethod.codeItem.getInstructions();

		for (CodeItem.TryItem tryItem : encodedMethod.codeItem.getTries()) {
			int startAddress = tryItem.getStartCodeAddress();
			int endAddress = tryItem.getStartCodeAddress()
					+ tryItem.getTryLength();

			/**
			 * The end address points to the address immediately after the end
			 * of the last instruction that the try block covers. We want the
			 * .catch directive and end_try label to be associated with the last
			 * covered instruction, so we need to get the address for that
			 * instruction
			 */

			int index = instructionMap.get(endAddress, -1);
			int lastInstructionAddress;

			/**
			 * If we couldn't find the index, then the try block probably
			 * extends to the last instruction in the method, and so endAddress
			 * would be the address immediately after the end of the last
			 * instruction. Check to make sure this is the case, if not, throw
			 * an exception.
			 */
			if (index == -1) {
				Instruction lastInstruction = instructions[instructions.length - 1];
				lastInstructionAddress = instructionMap.keyAt(instructionMap
						.size() - 1);

				if (endAddress != lastInstructionAddress
						+ lastInstruction.getSize(lastInstructionAddress)) {
					throw new RuntimeException("Invalid code offset "
							+ endAddress + " for the try block end address");
				}
			} else {
				if (index == 0) {
					throw new RuntimeException("Unexpected instruction index");
				}
				Instruction lastInstruction = instructions[index - 1];

				if (lastInstruction.getFormat().variableSizeFormat) {
					throw new RuntimeException(
							"This try block unexpectedly ends on a switch/array data block.");
				}

				// getSize for non-variable size formats should return the same
				// size regardless of code address, so just
				// use a dummy address of "0"
				lastInstructionAddress = endAddress
						- lastInstruction.getSize(0);
			}

			// add the catch all handler if it exists
			int catchAllAddress = tryItem.encodedCatchHandler
					.getCatchAllHandlerAddress();
			if (catchAllAddress != -1) {
				CatchMethodItem catchAllMethodItem = new CatchMethodItem(
						labelCache, lastInstructionAddress, null, startAddress,
						endAddress, catchAllAddress);
				methodItems.add(catchAllMethodItem);
			}

			// add the rest of the handlers
			for (CodeItem.EncodedTypeAddrPair handler : tryItem.encodedCatchHandler.handlers) {
				// use the address from the last covered instruction
				CatchMethodItem catchMethodItem = new CatchMethodItem(
						labelCache, lastInstructionAddress,
						handler.exceptionType, startAddress, endAddress,
						handler.getHandlerAddress());
				methodItems.add(catchMethodItem);
			}
		}
	}

	private void addDebugInfo(final List<MethodItem> methodItems) {
		if (encodedMethod.codeItem == null
				|| encodedMethod.codeItem.getDebugInfo() == null) {
			return;
		}

		final CodeItem codeItem = encodedMethod.codeItem;
		DebugInfoItem debugInfoItem = codeItem.getDebugInfo();

		DebugInstructionIterator
				.DecodeInstructions(
						debugInfoItem,
						codeItem.getRegisterCount(),
						new DebugInstructionIterator.ProcessDecodedDebugInstructionDelegate() {
							@Override
							public void ProcessStartLocal(
									final int codeAddress, final int length,
									final int registerNum,
									final StringIdItem name,
									final TypeIdItem type) {
								methodItems.add(new DebugMethodItem(
										codeAddress, -1) {
									@Override
									public boolean writeTo(
											IndentingWriter writer)
											throws IOException {
										writeStartLocal(writer, codeItem,
												registerNum, name, type, null);
										return true;
									}
								});
							}

							@Override
							public void ProcessStartLocalExtended(
									final int codeAddress, final int length,
									final int registerNum,
									final StringIdItem name,
									final TypeIdItem type,
									final StringIdItem signature) {
								methodItems.add(new DebugMethodItem(
										codeAddress, -1) {
									@Override
									public boolean writeTo(
											IndentingWriter writer)
											throws IOException {
										writeStartLocal(writer, codeItem,
												registerNum, name, type,
												signature);
										return true;
									}
								});
							}

							@Override
							public void ProcessEndLocal(final int codeAddress,
									final int length, final int registerNum,
									final StringIdItem name,
									final TypeIdItem type,
									final StringIdItem signature) {
								methodItems.add(new DebugMethodItem(
										codeAddress, -1) {
									@Override
									public boolean writeTo(
											IndentingWriter writer)
											throws IOException {
										writeEndLocal(writer, codeItem,
												registerNum, name, type,
												signature);
										return true;
									}
								});
							}

							@Override
							public void ProcessRestartLocal(
									final int codeAddress, final int length,
									final int registerNum,
									final StringIdItem name,
									final TypeIdItem type,
									final StringIdItem signature) {
								methodItems.add(new DebugMethodItem(
										codeAddress, -1) {
									@Override
									public boolean writeTo(
											IndentingWriter writer)
											throws IOException {
										writeRestartLocal(writer, codeItem,
												registerNum, name, type,
												signature);
										return true;
									}
								});
							}

							@Override
							public void ProcessSetPrologueEnd(int codeAddress) {
								methodItems.add(new DebugMethodItem(
										codeAddress, -4) {
									@Override
									public boolean writeTo(
											IndentingWriter writer)
											throws IOException {
										writeEndPrologue(writer);
										return true;
									}
								});
							}

							@Override
							public void ProcessSetEpilogueBegin(int codeAddress) {
								methodItems.add(new DebugMethodItem(
										codeAddress, -4) {
									@Override
									public boolean writeTo(
											IndentingWriter writer)
											throws IOException {
										writeBeginEpilogue(writer);
										return true;
									}
								});
							}

							@Override
							public void ProcessSetFile(int codeAddress,
									int length, final StringIdItem name) {
								methodItems.add(new DebugMethodItem(
										codeAddress, -3) {
									@Override
									public boolean writeTo(
											IndentingWriter writer)
											throws IOException {
										writeSetFile(writer,
												name.getStringValue());
										return true;
									}
								});
							}

							@Override
							public void ProcessLineEmit(int codeAddress,
									final int line) {
								methodItems.add(new DebugMethodItem(
										codeAddress, -2) {
									@Override
									public boolean writeTo(
											IndentingWriter writer)
											throws IOException {
										writeLine(writer, line);
										return true;
									}
								});
							}
						});
	}

	private void setLabelSequentialNumbers() {
		HashMap<String, Integer> nextLabelSequenceByType = new HashMap<String, Integer>();
		ArrayList<LabelMethodItem> sortedLabels = new ArrayList<LabelMethodItem>(
				labelCache.getLabels());

		// sort the labels by their location in the method
		Collections.sort(sortedLabels);

		for (LabelMethodItem labelMethodItem : sortedLabels) {
			Integer labelSequence = nextLabelSequenceByType.get(labelMethodItem
					.getLabelPrefix());
			if (labelSequence == null) {
				labelSequence = 0;
			}
			labelMethodItem.setLabelSequence(labelSequence);
			nextLabelSequenceByType.put(labelMethodItem.getLabelPrefix(),
					labelSequence + 1);
		}
	}

	public static class LabelCache {
		protected HashMap<LabelMethodItem, LabelMethodItem> labels = new HashMap<LabelMethodItem, LabelMethodItem>();

		public LabelCache() {
		}

		public LabelMethodItem internLabel(LabelMethodItem labelMethodItem) {
			LabelMethodItem internedLabelMethodItem = labels
					.get(labelMethodItem);
			if (internedLabelMethodItem != null) {
				return internedLabelMethodItem;
			}
			labels.put(labelMethodItem, labelMethodItem);
			return labelMethodItem;
		}

		public Collection<LabelMethodItem> getLabels() {
			return labels.values();
		}
	}

}
