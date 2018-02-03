package intro2.database.attribute;

import intro2.database.ByteArrayAccessor;
import intro2.database.ClassInfo;
import intro2.database.Database.DataProcessingException;
import intro2.database.pool.UTF8CPI;

public abstract class AttributeUtil {
	public static int skipAttributes(int countIndex, ByteArrayAccessor acc) {
		int num = acc.getUnsignedShort(countIndex);
		return skipAttributes(countIndex, num, acc);
	}

	public static int skipAttributes(int countIndex, int num, ByteArrayAccessor acc) {
		int index = countIndex+2; // start of attributes
		int count = 1;

		long len;
		while(count<=num) {
			len = acc.getUnsignedInt(index+2);
			index += 6 + len;
			count++;
		}
		return index;
	}

	public static boolean isDeprecated(int countIndex, ClassInfo ci) throws DataProcessingException {
		ByteArrayAccessor acc = ci.getClassBytes();

		int index = countIndex + 2;
		long len;
		int nameIndex;
		String name;

		for(int count = acc.getUnsignedShort(countIndex);count>0;count--) {
			nameIndex = acc.getUnsignedShort(index);
			name = ((UTF8CPI)ci.getCPI(nameIndex)).getString();

			if(name.equals("Deprecated"))
				return true;

			len = acc.getUnsignedInt(index+2);
			index += 6 + len;
		}

		return false;
	}

	public static InnerClassAttribute.InnerRef[] getInnerClassAttributeData(int countIndex, ClassInfo ci) throws DataProcessingException {
//		System.err.println("get inner classes");
		ByteArrayAccessor acc = ci.getClassBytes();

		int index = countIndex + 2;
		long len;
		int nameIndex;
		String name;

		for(int count = acc.getUnsignedShort(countIndex);count>0;count--) {
			nameIndex = acc.getUnsignedShort(index);
			name = ((UTF8CPI)ci.getCPI(nameIndex)).getString();
//			System.err.println("checking "+name);

			if(name.equals("InnerClasses")) {
//				System.err.println("found InnerClasses attribute");
				return InnerClassAttribute.getData(ci, index);
			}

			len = acc.getUnsignedInt(index+2);
			index += 6 + len;
		}

		return null;
	}
}