package intro2.database.reference;

public final class ThrowsReference extends OutwardReference {
	ThrowsReference(int id, int sourceID, int methodOffset, int thrownClassID) {
		super(id, sourceID, methodOffset);

		this.targetClassID = thrownClassID;
		actualTargetClassID = thrownClassID;
	}
}