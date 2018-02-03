package intro2.database.pool;

import intro2.database.ByteArrayAccessor;
import intro2.database.Database.DataProcessingException;

public final class ClassCPI extends ConstantPoolInfo {
	private String name;

	ClassCPI(int offset, ByteArrayAccessor acc, ConstantPoolSupplier cps) throws DataProcessingException {
		int nameIndex = acc.getUnsignedShort(offset+1);

		UTF8CPI u = (UTF8CPI)cps.getCPI(nameIndex);

		name = u.getString();
	}

	public String getName() {
		return name;
	}
}