package intro2.output;

import intro2.database.ClassInfo;
import intro2.database.Database;
import intro2.database.reference.Reference;
import intro2.database.reference.UsesFieldReference;
import intro2.database.reference.UsesMethodReference;
import intro2.util.IList;

import java.util.Arrays;

public final class ReferenceUtil {
	private ReferenceUtil() {}

	public static boolean isFieldUsed(ClassInfo ci, int fieldOffset, boolean includeSelf)
											throws Database.DataProcessingException {
		IList refList = ci.getReverseRefIDs();

		if(refList.isEmpty())
			return false;

//		Field field = ci.getField(fieldOffset); // needed?

		Reference ref;
		UsesFieldReference ufRef;
		int thisClassID = ci.getID();
		for(int i=0;i<refList.getSize();i++) {
			ref = Database.getReference(refList.get(i));

			if(ref instanceof UsesFieldReference) {
				ufRef = (UsesFieldReference)ref;

				if((ref.getTargetClassID() == thisClassID) && (ufRef.getTargetFieldOffset() == fieldOffset)) {
					if(includeSelf || (ref.getSourceClassID() != thisClassID))
						return true;
//					boolean b =  includeSelf || (ref.getSourceClassID() != thisClassID);
//					if(b) {
//						ClassInfo c2 = Database.getClassInfo(ref.getSourceClassID());
//						Method calling = c2.getMethod(ufRef.getSourceMethodOffset());
//						System.err.println(ci.getName()+"."+field.getName()+" used by "+c2.getName()+"."+calling.getName()+"()");
//					}
				}
			}
		}

		return false;
	}

	public static boolean isMethodUsed(ClassInfo ci, int methodOffset, boolean includeSelf)
											throws Database.DataProcessingException {
		IList refList = ci.getReverseRefIDs();

		if(refList.isEmpty())
			return false;

//		Method method = ci.getMethod(methodOffset); // needed?

		Reference ref;
		UsesMethodReference umRef;
		int thisClassID = ci.getID();
		for(int i=0;i<refList.getSize();i++) {
			ref = Database.getReference(refList.get(i));

			if(ref instanceof UsesMethodReference) {
				umRef = (UsesMethodReference)ref;

				if((ref.getTargetClassID() == thisClassID) && (umRef.getTargetMethodOffset() == methodOffset)) {
					if(includeSelf || (ref.getSourceClassID() != thisClassID))
						return true;
//					boolean b =  includeSelf || (ref.getSourceClassID() != thisClassID);
//					if(b) {
//						ClassInfo c2 = Database.getClassInfo(ref.getSourceClassID());
//						Method calling = c2.getMethod(umRef.getSourceMethodOffset());
//						System.err.println(ci.getName()+"."+method.getName()+"() used by "+c2.getName()+"."+method.getName()+"()");
//					}
				}
			}
		}

		return false;
	}

	/** Sorts class id's into class name order. Note a new IList is returned. */
	public static IList sortClasses(IList input) {
		String[] names = new String[input.getSize()];

		int i;
		for(i=0;i<names.length;i++)
			names[i] = Database.getClassName(input.get(i));

		Arrays.sort(names);

		IList result = new IList();
		for(i=0;i<names.length;i++)
			result.add(Database.getClassID(names[i]));

		return result;
	}
}