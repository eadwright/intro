package intro2.database.reference;

import intro2.database.ClassInfo;
import intro2.database.Database;
import intro2.database.member.Method;

public abstract class NativeFinder {
	private NativeFinder() {}

	public static boolean usesNative(int classID) throws Database.DataProcessingException {
		ClassInfo ci = Database.getClassInfo(classID);
		if(ci !=null) {
			int[] methods = ci.getMethodOffsets();
			Method m;
			if(methods!=null)
				for(int i=0;i<methods.length;i++) {
					m = ci.getMethod(methods[i]);
					if(m.isNative())
						return true;
				}
		}

		return false;
	}
}