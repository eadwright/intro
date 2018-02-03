package intro2.util;

/**
 * Mega efficient Set-like object which creates far fewer objects than usual. The
 * full Map interface in not implemented, and only integers can be used as keys,
 * apart from Integer.MIN_VALUE .
 */
public final class ISet implements java.io.Serializable {
	static final long serialVersionUID = -2734167093850052066L;

	private transient int size;
	private int origSize;
	private int origBucketSize;
	private int threshold;
	private float loadFactor;

	private int[][] keys;

	public ISet() {
		this(11, 10, 0.75f);
	}

	public ISet(int size) {
		this(size, 10, 0.75f);
	}

	public ISet(int size, float loadFactor) {
		this(size, 10, loadFactor);
	}

	public ISet(int size, int bucketSize, float loadFactor) {
		if(size<1)
			throw new IllegalArgumentException("Can't create a set smaller than 1 in size");

		origSize = size;
		origBucketSize = bucketSize;
		this.loadFactor = loadFactor;

		init();
	}

	public final void clear() {
		init();
	}

	public final boolean isEmpty() {
		return size == 0;
	}

	public final int[] keys() {
		int[][] z = keys;

		int[] k = new int[size];

		int pos = 0;
		int i,j;
		int[] b;

		for(i=0;i<z.length;i++) {
			b = z[i];
			if(b!=null)
				for(j=0;j<b.length;j++)
					if(b[j] != 0) {
						k[pos] = decodeKey(b[j]);
						pos++;
					}
		}

		return k;
	}

	/**
	 * Returns true if key already present
	 */
	public final boolean add(int key) {
		key = checkKey(key);

		int index = getIndex(key);
		int[] k = getKeys(index, true);
		int i;

		int zero = -1;
		for(i=0;i<k.length;i++) {
			if(k[i] == key)
				return true;
			else
				if((zero == -1) && (k[i] == 0))
					zero = i; // note first unused slot
		}

		if(zero != -1) { // use unused slot
			size++;
			k[zero] = key;
			return false;
		}

		size++;
		k = growKeys(index); // otherwise grow
		k[i] = key;

		// It's not there; grow the hash table if necessary...
		if (size >= threshold)
			rehash();

		return false;
	}

	/**
	 * Returns true if key found
	 */
	public final boolean contains(int key) {
		key = checkKey(key);

		int index = getIndex(key);
		int[] k = getKeys(index, false);

		if(k == null)
			return false;

		for(int i=0;i<k.length;i++)
			if(k[i] == key)
				return true;

		return false;
	}

	/**
	 * Returns true if key removed, false if not present
	 */
	public final boolean remove(int key) {
		if(size == 0)
			return false;

		key = checkKey(key);

		int index = getIndex(key);
		int[] k = getKeys(index, false);

		if(k == null)
			return false;

		boolean foundSomething = false;
		boolean removedKey = false;

		for(int i=0;i<k.length;i++) {
			if(k[i] == key) {
				k[i] = 0;
				removedKey = true;
				size--;
			} else
				if(k[i] != 0)
					foundSomething = true;
		}

		if(!foundSomething)
			keys[index] = null;

		return removedKey;
	}

	private final int[] getKeys(int index, boolean create) {
		int[][] k = keys;
		int[] ks = k[index];

		if(create && (ks == null)) {
			ks = new int[origBucketSize];
			k[index] = ks;
		}

		return ks;
	}

	private final int[] growKeys(int index) {
//		System.out.println("growKeys("+index+")");
		int[][] k = keys;
		int[] ks = k[index];

		if(ks == null)
			return getKeys(index, false);
		else {
			int[] ks2 = new int[ks.length*2];
			System.arraycopy(ks,0,ks2,0,ks.length);
			k[index] = ks2;

			return ks2;
		}
	}

	private final int checkKey(int key) {
		if(key == Integer.MIN_VALUE)
			throw new IllegalArgumentException("Can't use Integer.MIN_VALUE as a key, reserved");

		if(key == 0)
			key = Integer.MIN_VALUE;

		return key;
	}

	private final int decodeKey(int key) {
		if(key == Integer.MIN_VALUE)
			key = 0;

		return key;
	}

	private final void init() {
		size = 0;

		threshold = (int)(origSize * loadFactor);
		keys = new int[origSize][];
	}

	private final void rehash() {
//		System.out.println("rehash()");
		int[][] oldKeys = keys;

		int oldCapacity = oldKeys.length;
		int newCapacity = oldCapacity * 2 + 1;

		int[][] newKeys = new int[newCapacity][];

		threshold = (int)(newCapacity * loadFactor);

		keys = newKeys;

		int[] k;
		int i,j;

		for(i=0;i<oldKeys.length;i++) {
			k = oldKeys[i];
			if(k!=null)
				for(j=0;j<k.length;j++)
					if(k[j] != 0)
						internalPut(k[j]);
		}
	}

	/**
	 * Special method for rehash, doesn't alter the size,
	 * never checks for duplicates, never rehashs, key is
	 * already converted
	 */
	private final void internalPut(int key) {
		int index = getIndex(key);
		int[] k = getKeys(index, true);

		int i;
		for(i=0;i<k.length;i++)
			if(k[i] == 0) { // i.e. unused
				k[i] = key;
				return;
			}

		k = growKeys(index); // grow if no free slots found
		k[i] = key;
	}

	private final int getIndex(int key) {
		int r = SmallRandom.getInt(key);
		int index =  (r & 0x7FFFFFFF) % keys.length;
//		System.out.println("getIndex("+key+") = "+index);
		return index;
	}


	private final void readObject(java.io.ObjectInputStream s)
		 throws java.io.IOException, ClassNotFoundException {

		// Read in the threshold, loadfactor, and any hidden stuff
		s.defaultReadObject();

		int[] z = (int[])s.readObject();

		for(int i=0;i<z.length;i++)
			add(z[i]);
	}

	private final void writeObject(java.io.ObjectOutputStream s)
		throws java.io.IOException {

		// Write out the threshold, loadfactor, and any hidden stuff
		s.defaultWriteObject();

		init();

		int[] z = keys();

		// Write out all the keys as a simple array
		s.writeObject(z);
	}
}