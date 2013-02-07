import android.util.Log;

import com.android.dx.dex.DexModel;
import com.android.dx.dex.ModelVisitor;
import com.android.dx.dex.SizeOf;



public class TableOfContents {
	/**
     * Loads the table of contents values from a model. DebugInfo data is
     * ignored. The current version does not work with debug info structures.
     * 
     * @param model
     *            the model to read values from
     */
    public void readFrom(DexModel model) {

        Log.i(LOG_TAG, "Loading Table of Contents from a DexModel ...");

        readConstantPool(model);
        readDataSection(model);

    }

    /**
     * Read in data about all the sections that will go into the data section.
     * 
     * @param model
     *            the model to read from.
     */
    private void readDataSection(DexModel model) {

        int offset = dataOff;

        mapList.off = offset;
        mapList.byteCount = SizeOf.mapList();
        mapList.size = SECTION_COUNT;
        offset += mapList.byteCount;

        ModelVisitor.AllSectionsVisitor visitor = new ModelVisitor.AllSectionsVisitor();
        ModelVisitor.visit(model, visitor);

        typeLists.off = offset;
        typeLists.byteCount = visitor.typeListByteCount();
        typeLists.size = visitor.typeListsFound();
        offset += typeLists.byteCount;

        annotationSetRefLists.off = offset;
        annotationSetRefLists.byteCount = visitor.annotationSetRefListsByteCount();
        annotationSetRefLists.size = visitor.annotationSetRefListsFound();
        offset += annotationSetRefLists.byteCount;

        annotationSets.off = offset;
        annotationSets.byteCount = visitor.annotationSetByteCount();
        annotationSets.size = visitor.annotationSetsFound();
        offset += annotationSets.byteCount;

        classDatas.off = offset;
        classDatas.byteCount = visitor.classDataByteCount();
        classDatas.size = visitor.classDatasFound();
        offset += classDatas.byteCount;

        codes.off = offset;
        codes.byteCount = visitor.codeBytes();
        codes.size = visitor.codeItemsFound();
        offset += codes.byteCount;

        stringDatas.off = offset;
        stringDatas.byteCount = SizeOf.stringDataBytes(model);
        stringDatas.size = model.stringPoolCount();
        offset += stringDatas.byteCount;

        annotations.off = offset;
        annotations.byteCount = visitor.annotationsByteCount();
        annotations.size = visitor.annotationsFound();
        offset += annotations.byteCount;

        encodedArrays.off = offset;
        encodedArrays.byteCount = visitor.encodedArrayBytes();
        encodedArrays.size = visitor.encodedArrayCount();
        offset += encodedArrays.byteCount;

        annotationsDirectories.off = offset;
        annotationsDirectories.byteCount = visitor.annotationDirectoryBytes();
        annotationsDirectories.size = visitor.annotationDirectoryCount();
        offset += annotationsDirectories.byteCount;

        dataOff = getDataOffset(model);
        dataSize = offset - dataOff;
    }
    
    /**
     * Calculates the byte position in the DEX file of the data section.
     * 
     * @return the offset of the start of the data section.
     */
    public static int getDataOffset(DexModel model) {

        int result = SizeOf.HEADER_ITEM;
        result += SizeOf.stringPoolSize(model);
        result += SizeOf.typePoolSize(model);
        result += SizeOf.protoPoolSize(model);
        result += SizeOf.fieldPoolSize(model);
        result += SizeOf.methodPoolSize(model);
        result += SizeOf.classDefPoolSize(model);

        return result;
    }
    
    /**
     * read in all the constant pool sections.
     * 
     * @param model
     *            the model to read from.
     */
    private void readConstantPool(DexModel model) {
        int offset = 0;
        header.byteCount = SizeOf.HEADER_ITEM;
        header.off = offset;
        headerSize = SizeOf.HEADER_ITEM;
        offset += header.byteCount;

        stringIds.byteCount = SizeOf.stringPoolSize(model);
        stringIds.size = model.stringPoolCount();
        stringIds.off = offset;
        offset += stringIds.byteCount;

        typeIds.byteCount = SizeOf.typePoolSize(model);
        typeIds.size = model.typePoolCount();
        typeIds.off = offset;
        offset += typeIds.byteCount;

        protoIds.byteCount = SizeOf.protoPoolSize(model);
        protoIds.size = model.protoPoolCount();
        protoIds.off = offset;
        offset += protoIds.byteCount;

        fieldIds.byteCount = SizeOf.fieldPoolSize(model);
        fieldIds.size = model.fieldPoolCount();
        fieldIds.off = offset;
        offset += fieldIds.byteCount;

        methodIds.byteCount = SizeOf.methodPoolSize(model);
        methodIds.size = model.methodPoolCount();
        methodIds.off = offset;
        offset += methodIds.byteCount;

        classDefs.byteCount = SizeOf.classDefPoolSize(model);
        classDefs.size = model.classDefPoolCount();
        classDefs.off = offset;
        offset += classDefs.byteCount;

    }


}
