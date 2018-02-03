package intro2.database.reference;

import intro2.database.member.Method;

public final class UsesInterfaceMethodReference extends AbstractUsesMethodReference {
	public UsesInterfaceMethodReference() {}

	UsesInterfaceMethodReference(int id, int sourceClassID,
						int targetInterfaceID,
						int methodOffset, Method m) {
		super(id, sourceClassID, targetInterfaceID, methodOffset, m);
	}
}