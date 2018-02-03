package intro2.database.pool;

import intro2.database.ByteArrayAccessor;
import intro2.database.Database.DataProcessingException;

public final class UTF8CPI extends ConstantPoolInfo {
	private String str;

//	public UTF8CPI() {} // for cache testing only, temp

	UTF8CPI(int offset, ByteArrayAccessor acc, ConstantPoolSupplier cps) throws DataProcessingException {
		int length = acc.getUnsignedShort(offset+1);

		str = acc.getString(offset+3, length);

//		if(str.charAt(str.length()-1) == 'K')
//			System.err.println("Odd string "+str);
	}

	public String getString() {
		return str;
	}
}