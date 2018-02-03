package intro2.database.pool;

import intro2.database.ByteArrayAccessor;
import intro2.database.Database.DataProcessingException;

public final class FieldRefCPI extends ConstantPoolInfo {
	private String className;
	private String fieldName;
	private String fieldType;

	FieldRefCPI(int offset, ByteArrayAccessor acc, ConstantPoolSupplier cps) throws DataProcessingException {
		int classIndex = acc.getUnsignedShort(offset+1);
		ClassCPI c = (ClassCPI)cps.getCPI(classIndex);
		className = c.getName();

		int ntIndex = acc.getUnsignedShort(offset+3);
		NameAndTypeCPI n = (NameAndTypeCPI)cps.getCPI(ntIndex);

		fieldName = n.getName();
		fieldType = n.getType();
	}

	public String getClassName() {
		return className;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getFieldDescriptor() {
		return fieldType;
	}
}