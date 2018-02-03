package intro2.database.reference;

import intro2.database.member.Method;

public final class HidesMethodReference extends AbstractInheritanceReference {
	public HidesMethodReference() {}

	HidesMethodReference(int id, Method m1, Method m2) {
		super(id, m1, m2);
	}
}