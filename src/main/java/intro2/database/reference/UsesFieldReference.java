package intro2.database.reference;

import intro2.database.member.Field;

public final class UsesFieldReference extends OutwardReference {
	private int targetFieldOffset;

	public UsesFieldReference() {}

	UsesFieldReference(int id, int callingClassID, int targetClassID, int methodOffset, Field f) {
		super(id, callingClassID , methodOffset);

		this.targetClassID = targetClassID;
		targetFieldOffset = f.getOffset();
		actualTargetClassID = f.getClassID();
	}

	public int getTargetFieldOffset() {
		return targetFieldOffset;
	}

/*	public void setTargetFieldOffset(int t) { // for XML
		targetFieldOffset = t;
	}*/
}