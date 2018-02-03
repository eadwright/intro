package intro2.database.member;

import intro2.database.ByteArrayAccessor;
import intro2.database.attribute.AttributeUtil;

public abstract class MemberUtil {
	public static int[] getMemberOffsets(int countIndex, ByteArrayAccessor acc) {
		int num = acc.getUnsignedShort(countIndex);
		int index = countIndex+2;

		if(num>0) {
			int[] offsets = new int[num];
			offsets[0] = index;

			for(int n=1;n<num;n++) {
				index = skipMember(index, acc);
				offsets[n] = index;
			}
			return offsets;
		} else
			return null;
	}

	/**
	 * If no offsets are found (no members), this method must not
	 * be called.
	 */
	public static int skipLast(int[] offsets, ByteArrayAccessor acc) {
		return skipMember(offsets[offsets.length-1], acc);
	}

	private static int skipMember(int index, ByteArrayAccessor acc) {
//		System.out.println("member skip from "+index);
		int no = AttributeUtil.skipAttributes(index+6, acc);
//		System.out.println("... to "+no);
		return no;
	}
}