package intro2.database.pool;

import intro2.database.ByteArrayAccessor;
import intro2.database.Database.DataProcessingException;

public final class NameAndTypeCPI extends ConstantPoolInfo {
	private String name;
	private String type;

	NameAndTypeCPI(int offset, ByteArrayAccessor acc, ConstantPoolSupplier cps) throws DataProcessingException {
		int uIndex = acc.getUnsignedShort(offset+1);
		UTF8CPI u = (UTF8CPI)cps.getCPI(uIndex);
		name = u.getString();

		uIndex = acc.getUnsignedShort(offset+3);
		u = (UTF8CPI)cps.getCPI(uIndex);
		type = u.getString();
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
}