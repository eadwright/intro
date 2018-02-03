package intro2.database.reference;

import intro2.database.ClassInfo;
import intro2.database.Database;
import intro2.database.Database.DataProcessingException;
import intro2.database.member.Field;
import intro2.database.member.Member;
import intro2.database.member.Method;
import intro2.database.pool.FieldRefCPI;
import intro2.database.pool.InterfaceMethodRefCPI;
import intro2.database.pool.MethodRefCPI;

abstract class Resolver {
	/**
	 * Searches for a field, doesn't search super classes
	 */
	static Field findField(FieldRefCPI cpi) throws DataProcessingException {
		String className = cpi.getClassName();
		String fieldName = cpi.getFieldName();
		String fieldDescriptor = cpi.getFieldDescriptor();

		return (Field)findMember(className, fieldName, fieldDescriptor, true);
	}

	/**
	 * Searches for a method, doesn't search super classes
	 */
	static Method findMethod(MethodRefCPI cpi) throws DataProcessingException {
		String className = cpi.getClassName();
		String methodName = cpi.getMethodName();
		String methodDescriptor = cpi.getMethodDescriptor();

		return (Method)findMember(className, methodName, methodDescriptor, false);
	}

	/**
	 * Searches for an interface method, doesn't search super classes
	 */
	static Method findMethod(InterfaceMethodRefCPI cpi) throws DataProcessingException {
		String interfaceName = cpi.getInterfaceName();
		String methodName = cpi.getMethodName();
		String methodDescriptor = cpi.getMethodDescriptor();

		return (Method)findMember(interfaceName, methodName, methodDescriptor, false);
	}

	private static Member findMember(String className, String memberName, String memberDescriptor, boolean doField) throws DataProcessingException {
		ClassInfo ci = Database.getClassInfo(className);

		if(ci != null)
			return findMember(ci, memberName, memberDescriptor, doField);
		else
			return null;
	}

	private static Member findMember(int classID, String memberName, String memberDescriptor, boolean doField) throws DataProcessingException {
		ClassInfo ci = Database.getClassInfo(classID);

		if(ci != null)
			return findMember(ci, memberName, memberDescriptor, doField);
		else
			return null;
	}

	private static Member findMember(ClassInfo ci, String memberName, String memberDescriptor, boolean doField) throws DataProcessingException {
		int[] offsets;
		if(doField)
			offsets = ci.getFieldOffsets();
		else
			offsets = ci.getMethodOffsets();

		if(offsets != null) {
			Member m;
			for(int n=0;n<offsets.length;n++) {
				if(doField)
					m = ci.getField(offsets[n]);
				else
					m = ci.getMethod(offsets[n]);

				if(m.getName().equals(memberName) && m.getDescriptor().equals(memberDescriptor))
					return m;
			}
		}

		int superClass = ci.getSuperClassID();
		if(superClass > 0)
			return findMember(superClass, memberName, memberDescriptor, doField);
		else
			return null;
	}
}