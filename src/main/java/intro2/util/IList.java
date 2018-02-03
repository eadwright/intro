package intro2.util;

public class IList implements java.io.Serializable {
	static final long serialVersionUID = -3615276259919099575L;

	protected int size;
	private int origSize;
	protected transient int[] array;

	public IList() {
		this(20);
	}

	public IList(int size) {
		if(size<0)
			throw new IllegalArgumentException("Can't create a list smaller than 0 in size");

		origSize = size;
		array = new int[size];
		this.size = 0; // size;
	}

	public final int getSize() {
		return size;
	}

	public final boolean isEmpty() {
		return size == 0;
	}

	public final int[] getInts() {
		if(size == 0)
			return new int[0];

		int[] i = new int[size];
		System.arraycopy(array,0,i,0,size);
		return i;
	}

	public void add(int value) {
		if(size == array.length)
			growArray();

		array[size++] = value;
	}

	public void add(int[] values) {
		if(values == null) // well why not!
			return;

		while(array.length - size < values.length)
			growArray();

		System.arraycopy(values, 0, array, size, values.length);
		size += values.length;

//		for(int i=0;i<values.length;i++)
//			array[size++] = values[i];
	}

	public final boolean contains(int value) {
		for(int i=0;i<size;i++)
			if(array[i] == value)
				return true;

		return false;
	}

	public final int get(int index) {
		if(index<0 || index>=size)
			throw new ArrayIndexOutOfBoundsException();

		return array[index];
	}

	private final void growArray() {
		if(array.length == 0)
			array = new int[origSize];
		else {
			int[] a2 = new int[array.length*2];
			System.arraycopy(array,0,a2,0,array.length);
			array = a2;
		}
	}

	private final void readObject(java.io.ObjectInputStream s)
		 throws java.io.IOException, ClassNotFoundException {

		// Read in the threshold, loadfactor, and any hidden stuff
		s.defaultReadObject();

		array = (int[])s.readObject();
	}

	private final void writeObject(java.io.ObjectOutputStream s)
		throws java.io.IOException {

		// Write out the threshold, loadfactor, and any hidden stuff
		s.defaultWriteObject();

		// Write out all the data as a simple array
		s.writeObject(getInts());
	}
}