package intro2.database.reference;

import intro2.database.Database;
/**
 * Represents references "from" a class "to" another, i.e. creations, uses etc.
 * Not for "used by", "created by" etc, these are InwardReferences
 */
abstract class OutwardReference extends Reference {
	protected int sourceClassID;
	protected int sourceMethodOffset;
	protected int targetClassID;
	protected int actualTargetClassID;

	protected OutwardReference() {}

	protected OutwardReference(int id, int classID, int methodOffset) {
		super(id);
		sourceClassID = classID;
		sourceMethodOffset = methodOffset;
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
		return actualTargetClassID;
	}

	protected int descriptorToClassID(String desc) {
		int x = desc.indexOf('L');
		int y = desc.indexOf(';');

		String cn;
		if(y == -1)
			cn = desc;
		else
			cn = desc.substring(x+1, y);

//		System.out.println(getClass().toString()+" looking up "+cn);

		return Database.getClassID(cn);
	}

	// The rest here for XML

/*	public void setSourceClassID(int i) {
		sourceClassID = i;
	}

	public void setSourceMethodOffset(int n) {
		sourceMethodOffset = n;
	}

	public void setTargetClassID(int i) {
		targetClassID = i;
	}

	public void setActualTargetClassID(int i) {
		actualTargetClassID = i;
	}*/
}