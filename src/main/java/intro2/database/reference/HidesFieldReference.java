package intro2.database.reference;

import intro2.database.member.Field;

public final class HidesFieldReference extends AbstractInheritanceReference {
	public HidesFieldReference() {}

	HidesFieldReference(int id, Field f1, Field f2) {
		super(id, f1, f2);
	}
}