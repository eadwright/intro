package intro2.database.pool;

import intro2.database.ByteArrayAccessor;
import intro2.database.Database.DataProcessingException;

public final class InterfaceMethodRefCPI extends AbstractMethodRefCPI {
	InterfaceMethodRefCPI(int offset, ByteArrayAccessor acc, ConstantPoolSupplier cps) throws DataProcessingException {
		super(offset, acc, cps);
	}

	public String getInterfaceName() {
		return className;
	}
}