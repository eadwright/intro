package intro2.database.pool;

import intro2.database.ByteArrayAccessor;
import intro2.database.Database.DataProcessingException;

public final class MethodRefCPI extends AbstractMethodRefCPI {
	MethodRefCPI(int offset, ByteArrayAccessor acc, ConstantPoolSupplier cps) throws DataProcessingException {
		super(offset, acc, cps);
	}

	public String getClassName() {
		return className;
	}
}