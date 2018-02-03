package intro2.database.reference;

import intro2.database.pool.ClassCPI;

public final class CreatesClassReference extends OutwardReference {
	public CreatesClassReference() {}

	CreatesClassReference(int id, int classId, int methodOffset, ClassCPI cpi) {
		super(id, classId, methodOffset);

		targetClassID = descriptorToClassID(cpi.getName());
		actualTargetClassID = targetClassID;
	}
}