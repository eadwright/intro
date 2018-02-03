package intro2.database.reference;

import intro2.database.pool.ClassCPI;

public final class UsesClassReference extends OutwardReference {
	public UsesClassReference() {}

	UsesClassReference(int id, int classID, int methodOffset, String className) {
		super(id, classID, methodOffset);

		targetClassID = descriptorToClassID(className);
		actualTargetClassID = targetClassID;
	}

	UsesClassReference(int id, int classID, int methodOffset, ClassCPI cpi) {
		this(id, classID, methodOffset, cpi.getName());
	}
}