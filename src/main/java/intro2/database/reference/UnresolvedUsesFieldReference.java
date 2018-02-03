package intro2.database.reference;

import intro2.database.Database;
import intro2.database.pool.FieldRefCPI;

public final class UnresolvedUsesFieldReference extends AbstractUnresolvedReference {
	public UnresolvedUsesFieldReference() {}

	UnresolvedUsesFieldReference(int id, int classID, int methodOffset, FieldRefCPI cpi) {
		super(id, classID, methodOffset, Database.getClassID(cpi.getClassName()), cpi.getFieldName(), cpi.getFieldDescriptor());
	}

	public String getTargetFieldName() {
		return targetMemberName;
	}

	public String getTargetFieldDescriptor() {
		return targetDescriptor;
	}

	// For XML

/*	public void setTargetFieldName(String s) {
		targetMemberName = s;
	}

	public void setTargetFieldDescriptor(String s) {
		targetDescriptor = s;
	}*/
}