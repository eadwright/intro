package intro2.database.member;

import intro2.database.ByteArrayAccessor;
import intro2.database.Database.DataProcessingException;
import intro2.database.pool.*;
import intro2.output.Type;

public final class Field extends Member {
	private Object constantValue;
	private Type type;

	public Field(int classID, int offset, ByteArrayAccessor acc, ConstantPoolSupplier cps) throws DataProcessingException {
		super(classID, offset, acc, cps);

		int attrCount = acc.getUnsignedShort(offset+6);
		offset+=8;

		String attrName;
		UTF8CPI u;
		for(int n=0;n<attrCount;n++) {
			u = (UTF8CPI)cps.getCPI(acc.getUnsignedShort(offset));
			attrName = u.getString();

			if(attrName.equals("ConstantValue")) {
				ConstantPoolInfo cpi = cps.getCPI(acc.getUnsignedShort(offset+6));
				char type = descriptor.charAt(descriptor.length()-1);
				switch(type) {
					case 'B':
						constantValue = ((IntegerCPI)cpi).getByteValue();
						break;
					case 'C':
						constantValue = ((IntegerCPI)cpi).getCharValue();
						break;
					case 'D':
						constantValue = ((DoubleCPI)cpi).getValue();
						break;
					case 'F':
						constantValue = ((FloatCPI)cpi).getValue();
						break;
					case 'I':
						constantValue = ((IntegerCPI)cpi).getIntValue();
						break;
					case 'J':
						constantValue = ((LongCPI)cpi).getValue();
						break;
					case 'S':
						constantValue = ((IntegerCPI)cpi).getShortValue();
						break;
					case 'Z':
						constantValue = ((IntegerCPI)cpi).getBooleanValue();
						break;
					case ';':
						if(descriptor.equals("Ljava/lang/String;"))
							constantValue = ((StringCPI)cpi).getString();
						else
							throw new DataProcessingException("Invalid field descriptor "+descriptor+" with constant value");
						break;
					default:
						throw new DataProcessingException("Invalid field descriptor "+descriptor);
				}
			}

			checkSyntheticDeprecated(attrName);

			offset +=  acc.getUnsignedInt(offset+2) + 6;
		}
	}

	public Object getConstantValue() {
		return constantValue;
	}

	public boolean isPublic() {
		return (accessFlags & 0x01) != 0;
	}

	public boolean isPrivate() {
		return (accessFlags & 0x02) != 0;
	}

	public boolean isProtected() {
		return (accessFlags & 0x04) != 0;
	}

	public boolean isStatic() {
		return (accessFlags & 0x08) != 0;
	}

	public boolean isFinal() {
		return (accessFlags & 0x010) != 0;
	}

	public Type getType() {
		if(type == null)
			type = new Type(descriptor, 0);

		return type;
	}
}