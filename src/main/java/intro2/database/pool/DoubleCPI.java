package intro2.database.pool;

import intro2.database.ByteArrayAccessor;

public final class DoubleCPI extends ConstantPoolInfo {
	private double value;

	DoubleCPI(int offset, ByteArrayAccessor acc, ConstantPoolSupplier cps) {
		value = acc.getDouble(offset+1);
	}

	public Double getValue() {
		return new Double(value);
	}
}