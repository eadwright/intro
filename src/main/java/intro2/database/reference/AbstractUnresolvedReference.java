package intro2.database.reference;

abstract class AbstractUnresolvedReference extends Reference {
	protected int sourceClassID;
	protected int sourceMethodOffset;
	protected int targetClassID;
	protected String targetMemberName;
	protected String targetDescriptor;

	protected AbstractUnresolvedReference() {}

	protected AbstractUnresolvedReference(int id, int classID, int methodOffset,
											int targetClassID, String targetMemberName,
											String targetDescriptor) {
		super(id);
		sourceClassID = classID;
		sourceMethodOffset = methodOffset;
		this.targetClassID = targetClassID;
		this.targetMemberName = targetMemberName;
		this.targetDescriptor = targetDescriptor;
	}

	public int getSourceClassID() {
		return sourceClassID;
	}

	public int getSourceMethodOffset() {
		return sourceMethodOffset;
	}

	public int getTargetClassID() {
		return targetClassID;
	}

	public int getActualTargetClassID() {
		return Integer.MIN_VALUE;
	}
}