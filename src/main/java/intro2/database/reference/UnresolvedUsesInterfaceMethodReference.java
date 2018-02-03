package intro2.database.reference;

import intro2.database.Database;
import intro2.database.pool.InterfaceMethodRefCPI;

public final class UnresolvedUsesInterfaceMethodReference extends AbstractUnresolvedReference {
	public UnresolvedUsesInterfaceMethodReference() {}

	UnresolvedUsesInterfaceMethodReference(int id, int classID, int methodOffset, InterfaceMethodRefCPI cpi) {
		super(id, classID, methodOffset, Database.getClassID(cpi.getInterfaceName()), cpi.getMethodName(), cpi.getMethodDescriptor());
	}

	public String getTargetMethodName() {
		return targetMemberName;
	}

	public String getTargetMethodDescriptor() {
		return targetDescriptor;
	}

	// For XML

/*	public void setTargetMethodName(String s) {
		targetMemberName = s;
	}

	public void setTargetMethodDescriptor(String s) {
		targetDescriptor = s;
	}*/
}