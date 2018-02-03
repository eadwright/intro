package intro2.database.pool;

import intro2.database.ByteArrayAccessor;

public final class FloatCPI extends ConstantPoolInfo {
	private float value;

	FloatCPI(int offset, ByteArrayAccessor acc, ConstantPoolSupplier cps) {
		value = acc.getFloat(offset+1);
	}

	public Float getValue() {
		return new Float(value);
	}
}