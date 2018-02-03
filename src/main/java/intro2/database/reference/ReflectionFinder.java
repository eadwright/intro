package intro2.database.reference;

import intro2.database.ClassInfo;
import intro2.database.Database;
import intro2.database.member.Method;
import intro2.util.IList;

public abstract class ReflectionFinder {
	private static final String REFLECT = "java/lang/reflect";

	private static int id;
	private static IList reflectionMethodOffsets;

	private ReflectionFinder() {}

	public static void init() throws Database.DataProcessingException {
		id = Database.getClassID("java/lang/Class");
		ClassInfo ci = Database.getClassInfo(id);

		if(ci != null) {
			int[] methods = ci.getMethodOffsets();

			IList l = new IList();
			for(int i=0;i<methods.length;i++) {
				if(methodUsesReflection(ci.getMethod(methods[i])))
					l.add(methods[i]);
			}

			reflectionMethodOffsets = l;
		}
	}

	public static boolean usesReflection(int classID) {
		String name = Database.getClassName(classID);
		if(name.indexOf(REFLECT) == 0)
			return true;

		ClassInfo ci = Database.getClassInfo(classID);
		if(ci !=null) {
			IList refs = ci.getRefIDs();

			for(int i=0;i<refs.getSize();i++)
				if(infersReflectionUsage(Database.getReference(refs.get(i))))
					return true;
		}

		return false;
	}

	private static boolean infersReflectionUsage(Reference ref) {
		if(ref instanceof UsesMethodReference) {
			UsesMethodReference u = (UsesMethodReference)ref;

			int i = u.getTargetClassID();
			int o = u.getTargetMethodOffset();

			if((i == id) && reflectionMethodOffsets.contains(o))
				return true;

			String n = Database.getClassName(i);
			if(n.indexOf(REFLECT) == 0)
				return true;

			n = Database.getClassName(u.getTargetClassID());
			if(n.indexOf(REFLECT) == 0)
				return true;

			n = Database.getClassName(u.getActualTargetClassID());
			if(n.indexOf(REFLECT) == 0)
				return true;
		}

		if(ref instanceof UnresolvedUsesMethodReference) {
			UnresolvedUsesMethodReference u = (UnresolvedUsesMethodReference)ref;

			int i = u.getTargetClassID();
			String n = u.getTargetMethodName();
			String d = u.getTargetMethodDescriptor();

			if(i == id)
				return methodUsesReflection(n, d); // java.lang.Class

			n = Database.getClassName(i);
			if(n.indexOf(REFLECT) == 0)
				return true;

			n = Database.getClassName(u.getTargetClassID());
			if(n.indexOf(REFLECT) == 0)
				return true;
		}

		return false;
	}

	// for java.lang.Class only
	private static boolean methodUsesReflection(Method m) {
		return methodUsesReflection(m.getName(), m.getDescriptor());
	}

	// for java.lang.Class only
	private static boolean methodUsesReflection(String methodName, String methodDesc) {
		if(methodDesc.indexOf("L"+REFLECT+";") != -1)
			return true;

		if(methodName.equals("newInstance"))
			return true;

		return false;
	}
}