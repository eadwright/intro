package intro2.database.reference;

import intro2.database.member.Method;

public final class ImplementsMethodReference extends AbstractInheritanceReference {
	public ImplementsMethodReference() {}

	ImplementsMethodReference(int id, Method m1, Method m2) {
		super(id, m1, m2);
	}
}