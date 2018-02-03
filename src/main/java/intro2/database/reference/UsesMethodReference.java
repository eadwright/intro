package intro2.database.reference;

import intro2.database.member.Method;

public final class UsesMethodReference extends AbstractUsesMethodReference {
	public UsesMethodReference() {}

	UsesMethodReference(int id, int sourceClassID,
						int targetClassID,
						int methodOffset, Method m) {
		super(id, sourceClassID, targetClassID, methodOffset, m);
	}
}