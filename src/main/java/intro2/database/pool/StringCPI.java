package intro2.database.pool;

import intro2.database.ByteArrayAccessor;
import intro2.database.Database.DataProcessingException;

public final class StringCPI extends ConstantPoolInfo {
	private String str;

	StringCPI(int offset, ByteArrayAccessor acc, ConstantPoolSupplier cps) throws DataProcessingException {
		int uIndex = acc.getUnsignedShort(offset+1);

		UTF8CPI u = (UTF8CPI)cps.getCPI(uIndex);

		str = u.getString();
	}

	public String getString() {
		return str;
	}
}