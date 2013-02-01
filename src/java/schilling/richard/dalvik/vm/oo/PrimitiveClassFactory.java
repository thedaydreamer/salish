package schilling.richard.dalvik.vm.oo;

public class PrimitiveClassFactory {
	private DexModel model;

	public PrimitiveClassFactory(DexModel model) {
		if (model == null)
			throw new IllegalArgumentException("model cannot be null.");

		this.model = model;
	}

	public PrimitiveClass getByteClass() {
		return PrimitiveClass.getByteClass(model);
	}

	public PrimitiveClass getShortClass() {
		return PrimitiveClass.getShortClass(model);
	}

	public PrimitiveClass getIntClass() {
		return PrimitiveClass.getIntClass(model);
	}

	public PrimitiveClass getLongClass() {
		return PrimitiveClass.getLongClass(model);
	}

	public PrimitiveClass getFloatClass() {
		return PrimitiveClass.getFloatClass(model);
	}

	public PrimitiveClass getDoubleClass() {
		return PrimitiveClass.getDoubleClass(model);
	}

	public PrimitiveClass getCharClass() {
		return PrimitiveClass.getCharClass(model);
	}

	public PrimitiveClass getBooleanClass() {
		return PrimitiveClass.getBooleanClass(model);
	}

	public PrimitiveClass getVoidClass() {
		return PrimitiveClass.getVoidClass(model);
	}

}
