package intro2.database.pool;

import intro2.database.ByteArrayAccessor;
import intro2.database.Database.DataProcessingException;

abstract class AbstractMethodRefCPI extends ConstantPoolInfo {
	protected String className;
	protected String fieldName;
	protected String fieldType;

	AbstractMethodRefCPI(int offset, ByteArrayAccessor acc, ConstantPoolSupplier cps) throws DataProcessingException {
		int classIndex = acc.getUnsignedShort(offset+1);
		ClassCPI c = (ClassCPI)cps.getCPI(classIndex);
		className = c.getName();

		int ntIndex = acc.getUnsignedShort(offset+3);
		NameAndTypeCPI n = (NameAndTypeCPI)cps.getCPI(ntIndex);

		fieldName = n.getName();
		fieldType = n.getType();
	}

	public String getMethodName() {
		return fieldName;
	}

	public String getMethodDescriptor() {
		return fieldType;
	}
}