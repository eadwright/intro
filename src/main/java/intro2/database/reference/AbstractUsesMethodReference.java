package intro2.database.reference;

import intro2.database.member.Method;

class AbstractUsesMethodReference extends OutwardReference {
	protected int targetMethodOffset;

	protected AbstractUsesMethodReference() {}

	protected AbstractUsesMethodReference(int id, int sourceClassID,
											int targetClassID,
											int methodOffset, Method m) {
		super(id, sourceClassID, methodOffset);

		this.targetClassID = targetClassID;
		targetMethodOffset = m.getOffset();
		actualTargetClassID = m.getClassID();
	}

	public int getTargetMethodOffset() {
		return targetMethodOffset;
	}
}