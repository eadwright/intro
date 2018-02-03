package intro2.database.reference;

import intro2.database.member.Member;

abstract class AbstractInheritanceReference extends Reference {
	protected int subClassID; // the class doing the overriding, implementing, hiding etc
	protected int subMemberOffset;
	protected int superClassID;
	protected int superMemberOffset;

	public AbstractInheritanceReference() {}

	AbstractInheritanceReference(int id, Member m1, Member m2) {
		super(id);

		subClassID = m1.getClassID();
		subMemberOffset = m1.getOffset();

		superClassID = m2.getClassID();
		superMemberOffset = m2.getOffset();
	}

	public int getSubClassID() {
		return subClassID;
	}

	public int getSubMemberOffset() {
		return subMemberOffset;
	}

	public int getSuperClassID() {
		return superClassID;
	}

	public int getSuperMemberOffset() {
		return superMemberOffset;
	}

	public int getSourceClassID () {
		return subClassID;
	}

	public int getTargetClassID() {
		return superClassID;
	}

	public int getActualTargetClassID() {
		return Integer.MIN_VALUE;
	}

/*	public void setSubClassID(int i) {
		subClassID = i;
	}

	public void setSubMemberOffset(int i) {
		subMemberOffset = i;
	}

	public void setSuperClassID(int i) {
		superClassID = i;
	}

	public void setSuperMemberOffset(int i) {
		superMemberOffset = i;
	}*/
}