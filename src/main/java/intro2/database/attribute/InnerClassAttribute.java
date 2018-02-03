package intro2.database.attribute;

import intro2.database.ByteArrayAccessor;
import intro2.database.ClassInfo;
import intro2.database.Database.DataProcessingException;
import intro2.database.pool.ClassCPI;
import intro2.database.pool.UTF8CPI;

public final class InnerClassAttribute {
	private InnerClassAttribute() {}

	static InnerRef[] getData(ClassInfo ci, int offset) throws DataProcessingException {
		ByteArrayAccessor acc = ci.getClassBytes();
		InnerRef[] refs = new InnerRef[acc.getUnsignedShort(offset+6)];

		int ptr = offset+8;
		for(int i=0;i<refs.length;i++) {
			refs[i] = new InnerRef(ci, ptr);
			ptr += 8;
		}

		return refs;
	}

	public static class InnerRef {
		private String innerClass;
		private String outerClass;
		private int accessFlags;
		private String name;

		private InnerRef(ClassInfo ci, int offset) throws DataProcessingException {
			ByteArrayAccessor acc = ci.getClassBytes();

			int i = acc.getUnsignedShort(offset);
			if(i != 0)
				innerClass = ((ClassCPI)ci.getCPI(i)).getName();

			i = acc.getUnsignedShort(offset+2);
			if(i != 0)
				outerClass = ((ClassCPI)ci.getCPI(i)).getName();

			i = acc.getUnsignedShort(offset+4);
			if(i != 0)
				name = ((UTF8CPI)ci.getCPI(i)).getString();

			accessFlags = acc.getUnsignedShort(offset+6);
		}

		public String getInnerClass() {
			return innerClass;
		}

		public String getOuterClass() {
			return outerClass;
		}

		public int getAccessFlags() {
			return accessFlags;
		}

		public String getName() {
			return name;
		}
	}
}