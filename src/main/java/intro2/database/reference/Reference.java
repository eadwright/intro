package intro2.database.reference;

public abstract class Reference implements java.io.Serializable {
	protected int id;

	public Reference() {}

	Reference(int i) {
		id = i;
	}

	public int getID() {
		return id;
	}

	// These three simplify package / module relationship processing

	public abstract int getSourceClassID();
	public abstract int getTargetClassID();
	public abstract int getActualTargetClassID();

}