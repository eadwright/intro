package intro2.database.pool;

import intro2.database.ByteArrayAccessor;

public final class IntegerCPI extends ConstantPoolInfo {
	private int raw;

	IntegerCPI(int offset, ByteArrayAccessor acc, ConstantPoolSupplier cps) {
		raw = acc.getInt(offset+1);
	}

	public Integer getIntValue() {
		return new Integer(raw);
	}

	public Byte getByteValue() {
		return new Byte((byte)raw);
	}

	public Character getCharValue() {
		return new Character((char)(raw&0xFFFF));
	}

	public Short getShortValue() {
		return new Short((short)raw);
	}

	public Boolean getBooleanValue() {
		return new Boolean(raw != 0);
	}
}