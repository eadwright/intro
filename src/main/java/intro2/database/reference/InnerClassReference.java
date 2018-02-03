package intro2.database.reference;

public final class InnerClassReference extends Reference {
	private int outerClassID;
	private int innerClassID;
	private String name;
	private int accessFlags;
	private boolean innerIsChild;
	private boolean member;

	public InnerClassReference(int id, int innerClassID, int outerClassID, String name,
								int flags, boolean innerIsChild, boolean member) {
		super(id);

		this.outerClassID = outerClassID;
		this.innerClassID = innerClassID;
		this.name = name;
		this.accessFlags = accessFlags;
		this.innerIsChild = innerIsChild;
		this.member = member;
	}

	public boolean isAnonymous() {
		return name == null;
	}

	public String getDeclaredName() {
		return name;
	}

	public boolean isPublic() {
		return (accessFlags & 0x01) != 0;
	}

	public boolean isPrivate() {
		return (accessFlags & 0x02) != 0;
	}

	public boolean isProtected() {
		return (accessFlags & 0x04) != 0;
	}

	public boolean isStatic() {
		return (accessFlags & 0x018) != 0;
	}

	public boolean isFinal() {
		return (accessFlags & 0x010) != 0;
	}

	public boolean isInterface() {
		return (accessFlags & 0x0200) != 0;
	}

	public boolean isAbstract() {
		return (accessFlags & 0x0400) != 0;
	}

	public boolean isReferringToInnerOfSourceClass() {
		return innerIsChild;
	}

	public int getSourceClassID() {
		if(innerIsChild)
			return outerClassID;
		else
			return innerClassID;
	}

	public int getTargetClassID() {
		if(innerIsChild)
			return innerClassID;
		else
			return outerClassID;
	}

	public int getActualTargetClassID() {
		return getTargetClassID();
	}

	public int getInnerClassID() {
		return innerClassID;
	}

	public int getOuterClassID() {
		return outerClassID;
	}
}