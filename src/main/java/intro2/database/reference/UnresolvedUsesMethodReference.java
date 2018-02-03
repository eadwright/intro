package intro2.database.reference;

import intro2.database.Database;
import intro2.database.pool.MethodRefCPI;

public final class UnresolvedUsesMethodReference extends AbstractUnresolvedReference {
	public UnresolvedUsesMethodReference() {}

	UnresolvedUsesMethodReference(int id, int classID, int methodOffset, MethodRefCPI cpi) {
		super(id, classID, methodOffset, Database.getClassID(cpi.getClassName()), cpi.getMethodName(), cpi.getMethodDescriptor());
	}

	public String getTargetMethodName() {
		return targetMemberName;
	}

	public String getTargetMethodDescriptor() {
		return targetDescriptor;
	}
}