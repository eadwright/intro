package intro2.database.pool;

import intro2.database.ByteArrayAccessor;

public final class LongCPI extends ConstantPoolInfo {
	private long value;

	LongCPI(int offset, ByteArrayAccessor acc, ConstantPoolSupplier cps) {
		value = acc.getLong(offset+1);
	}

	public Long getValue() {
		return new Long(value);
	}
}