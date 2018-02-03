package intro2.database.reference;

public final class UnresolvedThrowsReference extends Reference {
	private int sourceID;
	private int methodOffset;
	private String thrownClass;

	UnresolvedThrowsReference(int id, int sourceID, int methodOffset, String thrownClass) {
		super(id);

		this.sourceID = sourceID;
		this.thrownClass = thrownClass;
		this.methodOffset = methodOffset;
	}

	public int getSourceMethodOffset() {
		return methodOffset;
	}

	public int getSourceClassID() {
		return sourceID;
	}

	public String getTargetClassName() {
		return thrownClass;
	}

	public int getTargetClassID() {
		return Integer.MIN_VALUE;
	}

	public int getActualTargetClassID() {
		return Integer.MIN_VALUE;
	}
}