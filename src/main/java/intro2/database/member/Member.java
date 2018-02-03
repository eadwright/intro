package intro2.database.member;

import intro2.database.ByteArrayAccessor;
import intro2.database.Database.DataProcessingException;
import intro2.database.pool.ConstantPoolSupplier;
import intro2.database.pool.UTF8CPI;

public abstract class Member implements Comparable {
	protected int accessFlags;
	protected int classID;
	protected String name;
	protected String descriptor;
	protected boolean deprecated;
	protected boolean synthetic;
	protected int memberOffset;

	protected Member(int classID, int offset, ByteArrayAccessor acc, ConstantPoolSupplier cps) throws DataProcessingException {
		this.classID = classID;
		memberOffset = offset;
		accessFlags = acc.getUnsignedShort(offset);

		int index = acc.getUnsignedShort(offset+2);
		UTF8CPI u = (UTF8CPI)cps.getCPI(index);
		name = u.getString();

		index = acc.getUnsignedShort(offset+4);
		u = (UTF8CPI)cps.getCPI(index);
		descriptor = u.getString();
	}

	protected void checkSyntheticDeprecated(String attrName) {
		if(attrName.equals("Synthetic"))
			synthetic = true;

		if(attrName.equals("Deprecated"))
			deprecated = true;
	}

	public int getClassID() {
		return classID;
	}

	public int getAccessFlags() {
		return accessFlags;
	}

	public String getName() {
		return name;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public boolean isDeprecated() {
		return deprecated;
	}

	public boolean isSynthetic() {
		return synthetic;
	}

	public int getOffset() {
		return memberOffset;
	}

	public abstract boolean isPublic();
	public abstract boolean isPrivate();
	public abstract boolean isStatic();
	public abstract boolean isFinal();

	public boolean equals(Object other) {
		return other == this;
	}

	public int compareTo(Object other) {
		Member mOther = (Member)other;

		if(isPublic() && !mOther.isPublic())
			return -1;

		if(!isPublic() && mOther.isPublic())
			return 1;

		if(isPrivate() && !mOther.isPrivate())
			return 1;

		if(!isPrivate() && mOther.isPrivate())
			return -1;

		int result = getName().compareTo(mOther.getName());

		if(result == 0)
			result = getDescriptor().compareTo(mOther.getDescriptor());

		if(result == 0)
			return getDescriptor().length()-mOther.getDescriptor().length();
		else
			return result;
	}
}