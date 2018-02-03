package intro2.util;

public class UniqueIList extends IList {
	public UniqueIList() {}

	public UniqueIList(int size) {
		super(size);
	}

	public void add(int value) {
		if(!contains(value))
			super.add(value);
	}
}