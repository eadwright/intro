package intro2.util;

public final class UniqueList extends java.util.ArrayList implements java.io.Serializable {
	public UniqueList() {}

	public final boolean add(Object obj) {
		if(contains(obj))
			return false;
		else
			return super.add(obj);
	}
}