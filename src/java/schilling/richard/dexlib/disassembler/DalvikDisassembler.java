package schilling.richard.dexlib.disassembler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import schilling.richard.dalvik.vm.oo.Clazz;
import schilling.richard.dalvik.vm.oo.PlatformClass;
import schilling.richard.dalvik.vm.oo.util.ClassFileNameHandler;
import schilling.richard.dexlib.Code.Analysis.ClassPath;
import schilling.richard.dexlib.Code.Analysis.ClassPath.ClassPathedClassDef;
import schilling.richard.dexlib.Code.Analysis.CustomInlineMethodResolver;
import schilling.richard.dexlib.Code.Analysis.InlineMethodResolver;
import schilling.richard.dexlib.Code.Analysis.SyntheticAccessorResolver;
import schilling.richard.dexlib.Code.Analysis.ValidationException;
import schilling.richard.dexlib.disassembler.adapter.ClassDefinition;
import schilling.richard.dexlib.io.DexFile;
import schilling.richard.dexlib.io.deserialize.ClassDefItem;
import schilling.richard.dexlib.lang.DexLoader;
import schilling.richard.io.IndentingWriter;
import schilling.richard.util.FileSystemUtil;
import schilling.richard.util.FileUtils;
import android.content.Context;
import android.util.Log;

public class DalvikDisassembler {

	public static final String LOG_TAG = "DalvikDisassembler";

	public static InlineMethodResolver inlineResolver = null;

	public static SyntheticAccessorResolver syntheticAccessorResolver = null;

	private Context context;

	public DalvikDisassembler(Context context) {

		if (context == null)
			throw new IllegalArgumentException("context cannot be null.");

		this.context = context;

	}

	public void disassembleDexFile(DexFile dexFile) {
		try {
			disassembleDexFile((String) null);
		} catch (ValidationException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private void disassembleDexFile(String inlineTable) throws IOException,
			ValidationException, ClassNotFoundException {

		FinnrApp app = FinnrApp.getApp();

		DexLoader loader = app.getDexLoader();
		DexFile dexFile = loader.getDexFile();

		/* create the output directory if it doesn't exist. */
		File outputDirectory = FileSystemUtil.createInternalOutputDirectory();

		if (!FinnrApp.getApp().getPrefNoAccessorComments())
			syntheticAccessorResolver = new SyntheticAccessorResolver(dexFile);

		disassembleClassDefinitions(outputDirectory);

	}

	private void disassembleClassDefinitions(File outputDirectory)
			throws IOException {

		FinnrApp app = FinnrApp.getApp();

		DexLoader loader = app.getDexLoader();
		DexFile dexFile = loader.getDexFile();

		boolean regOrDeodexOrVerify = false;
		if (app.getPrefRegisterInfoSetAsBitmap() != 0 || app.getPrefDeodex()
				|| app.getPrefVerify())
			regOrDeodexOrVerify = true;

		// TODO add disassemble property to control the generation of generate
		// disassembly code.
		boolean outputDisassembly = true; // app.getPrefOutputDisassembly()

		/* sort class definitions */
		ArrayList<ClassDefItem> classDefItems = new ArrayList<ClassDefItem>(
				dexFile.ClassDefsSection.getItems());

		Collections.sort(classDefItems, new Comparator<ClassDefItem>() {
			public int compare(ClassDefItem classDefItem1,
					ClassDefItem classDefItem2) {
				return classDefItem1
						.getClassType()
						.getTypeDescriptor()
						.compareTo(
								classDefItem1.getClassType()
										.getTypeDescriptor());
			}
		});

		ClassFileNameHandler fileNameHandler = new ClassFileNameHandler(
				outputDirectory, ".smali");

		for (ClassDefItem classDefItem : classDefItems) {
			String classSignature = classDefItem.getClassType()
					.getTypeDescriptor();

			if (outputDisassembly) {
				try {
					Clazz cDef = loader.loadClass(classSignature);
					if (cDef instanceof PlatformClass) {
						Log.i(LOG_TAG, "Skipping output of " + classSignature
								+ " because it's a platform class");
						continue;
					}

				} catch (ValidationException e) {
					throw new RuntimeException("uneable to validate class "
							+ classSignature, e);
				} catch (ClassNotFoundException e) {

					// indicates a critical failure in the DexLoader to laod a
					// class.
					throw new IllegalStateException(
							"class "
									+ classSignature
									+ " is not loaded.  Did DexLoader.getFile() properly load all classes");
				}
				File smaliFile = null;
				try {
					smaliFile = fileNameHandler
							.getUniqueFilenameForClass(classSignature);

					Log.i(LOG_TAG, "disassembling " + classSignature
							+ " to file " + smaliFile.getCanonicalPath());

					// create and initialize the top level string template
					ClassDefinition classDefinition = new ClassDefinition(
							classDefItem);

					FileUtils.createFile(smaliFile);

					BufferedWriter bufWriter = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(
									smaliFile), "UTF8"));

					Writer writer = new IndentingWriter(bufWriter);
					classDefinition.writeTo((IndentingWriter) writer);
					writer.flush();
					writer.close();
				} catch (IOException ex) {
					Log.e(LOG_TAG, "unable to write disassembly for class "
							+ classSignature + " to file " + smaliFile
							+ ". skipping.", ex);
				}

			}
		}
	}

	private void disassembleDexFile_backup(DexFile dexFile, String inlineTable)
			throws IOException, ValidationException, ClassNotFoundException {
		FinnrApp app = FinnrApp.getApp();

		boolean regOrDeodexOrVerify = false;
		if (app.getPrefRegisterInfoSetAsBitmap() != 0 || app.getPrefDeodex()
				|| app.getPrefVerify())
			regOrDeodexOrVerify = true;

		// TODO figure out the side effects of each block of code in this
		// function. It's not clear.

		if (regOrDeodexOrVerify) {

			ClassPath.InitializeClassPath(dexFile);
			if (inlineTable != null) {
				inlineResolver = new CustomInlineMethodResolver(inlineTable);
			}

		}

		File outputDirectory = FinnrApp.getApp().getExternalOutputDirectory();
		Log.i(LOG_TAG, "External output directory is " + outputDirectory);
		if (!outputDirectory.exists()) {
			Log.i(LOG_TAG, "Creating " + outputDirectory);
			if (!outputDirectory.mkdirs()) {
				throw new IllegalStateException(
						"Can't create the output directory " + outputDirectory);

				// TODO: remove all System.exit calls
				// System.exit(1);
			}
		}

		if (!FinnrApp.getApp().getPrefNoAccessorComments()) {
			syntheticAccessorResolver = new SyntheticAccessorResolver(dexFile);
		}

		// sort the classes, so that if we're on a case-insensitive file system
		// and need to handle classes with file
		// name collisions, then we'll use the same name for each class, if the
		// dex file goes through multiple
		// baksmali/smali cycles for some reason. If a class with a colliding
		// name is added or removed, the filenames
		// may still change of course
		ArrayList<ClassDefItem> classDefItems = new ArrayList<ClassDefItem>(
				dexFile.ClassDefsSection.getItems());
		Collections.sort(classDefItems, new Comparator<ClassDefItem>() {
			public int compare(ClassDefItem classDefItem1,
					ClassDefItem classDefItem2) {
				return classDefItem1
						.getClassType()
						.getTypeDescriptor()
						.compareTo(
								classDefItem1.getClassType()
										.getTypeDescriptor());
			}
		});

		ClassFileNameHandler fileNameHandler = new ClassFileNameHandler(
				outputDirectory, ".smali");

		for (ClassDefItem classDefItem : classDefItems) {
			/**
			 * The path for the disassembly file is based on the package name
			 * The class descriptor will look something like: Ljava/lang/Object;
			 * Where the there is leading 'L' and a trailing ';', and the parts
			 * of the package name are separated by '/'
			 */

			if (regOrDeodexOrVerify) {
				// If we are analyzing the bytecode, make sure that this class
				// is loaded into the ClassPath. If it isn't
				// then there was some error while loading it, and we should
				// skip it
				ClassPathedClassDef classDef = ClassPath.getClassDef(
						classDefItem.getClassType(), false);
				if (classDef == null
						|| classDef instanceof ClassPath.UnresolvedClassDef) {
					continue;
				}
			}

			String classDescriptor = classDefItem.getClassType()
					.getTypeDescriptor();

			// validate that the descriptor is formatted like we expect
			if (classDescriptor.charAt(0) != 'L'
					|| classDescriptor.charAt(classDescriptor.length() - 1) != ';') {
				System.err.println("Unrecognized class descriptor - "
						+ classDescriptor + " - skipping class");
				continue;
			}

			File smaliFile = fileNameHandler
					.getUniqueFilenameForClass(classDescriptor);
			Log.i(LOG_TAG,
					"current smali file is " + smaliFile.getCanonicalPath());

			// create and initialize the top level string template
			ClassDefinition classDefinition = new ClassDefinition(classDefItem);

			// write the disassembly
			Writer writer = null;
			try {
				File smaliParent = smaliFile.getParentFile();
				if (!smaliParent.exists()) {
					if (!smaliParent.mkdirs()) {
						System.err.println("Unable to create directory "
								+ smaliParent.toString() + " - skipping class");
						continue;
					} else {
						Log.i(LOG_TAG,
								"Created directory "
										+ smaliParent.getCanonicalPath());
					}
				}

				if (!smaliFile.exists()) {
					if (!smaliFile.createNewFile()) {
						System.err.println("Unable to create file "
								+ smaliFile.toString() + " - skipping class");
						continue;
					} else {
						Log.i(LOG_TAG,
								"Created file " + smaliFile.getCanonicalPath());
					}
				}

				BufferedWriter bufWriter = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(smaliFile),
								"UTF8"));

				writer = new IndentingWriter(bufWriter);
				classDefinition.writeTo((IndentingWriter) writer);

			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException ex) {
						// ignore
					}
				}

			}

		}
	}

	private static final Pattern extJarPattern = Pattern
			.compile("(?:^|\\\\|/)ext.(?:jar|odex)$");

	private static boolean isExtJar(String dexFilePath) {
		Matcher m = extJarPattern.matcher(dexFilePath);
		return m.find();
	}
}
