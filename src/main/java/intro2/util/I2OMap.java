package intro2.util;

/**
 * Mega efficient Map-like object which creates far fewer objects than usual. The
 * full Map interface in not implemented, and only integers can be used as keys,
 * apart from Integer.MIN_VALUE .
 */
public final class I2OMap implements java.io.Serializable {
	static final long serialVersionUID = -1752393564319573901L;

	transient int size;
	int origBucketSize;
	float loadFactor;
	private int origSize;
	private int threshold;

	private transient int[][] keys;
	private transient Object[][] objects;

	public I2OMap() {
		this(11, 10, 0.75f);
	}

	public I2OMap(int size) {
		this(size, 10, 0.75f);
	}

	public I2OMap(int size, float loadFactor) {
		this(size, 10, loadFactor);
	}

	public I2OMap(int size, int bucketSize, float loadFactor) {
		if(size<1 || bucketSize<1)
			throw new IllegalArgumentException("Can't create a map / map-bucket smaller than 1 in size");

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
//						if(b[j] != Integer.MIN_VALUE) {
							k[pos] = decodeKey(b[j]);
							pos++;
//						}
					}
		}

		return k;
	}

	public final int size() {
		return keys().length;
	}

	public final Object put(int key, Object value) {
		key = checkKey(key);

		int index = getIndex(key);
		int[] k = getKeys(index, true);
		int i;

		int zero = -1;
		for(i=0;i<k.length;i++) {
			if(k[i] == key) {
				Object old = objects[index][i];
				objects[index][i] = value;
				return old;
			} else
				if((zero == -1) && (k[i] == 0))
					zero = i; // note first unused slot
		}

		if(zero != -1) { // use unused slot
			size++;
			k[zero] = key;
			objects[index][zero] = value;
			return null;
		}

		size++;
		k = growKeys(index); // otherwise grow
		k[i] = key;
		objects[index][i] = value;

		// It's not there; grow the hash table if necessary...
		if (size >= threshold)
			rehash();

		return null;
	}

	public final Object get(int key) {
		key = checkKey(key);

		int index = getIndex(key);
		int[] k = getKeys(index, false);

		if(k == null)
			return null;

		for(int i=0;i<k.length;i++)
			if(k[i] == key) {
//				System.out.println("get() found key "+key);
				return objects[index][i];
			}

		return null;
	}

	public final boolean containsKey(int key) {
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

	public final Object remove(int key) {
		if(size == 0)
			return null;

		key = checkKey(key);

		int index = getIndex(key);
		int[] k = getKeys(index, false);

		if(k == null)
			return null;

		Object obj = null;
		boolean foundSomething = false;

		for(int i=0;i<k.length;i++) {
			if(k[i] == key) {
				k[i] = 0;
				obj = objects[index][i];
				objects[index][i] = null;
				size--;
			} else
				if(k[i] != 0)
					foundSomething = true;
		}

		if(!foundSomething) {
			keys[index] = null;
			objects[index] = null;
		}

		return obj;
	}

	private final int[] getKeys(int index, boolean create) {
		int[][] k = keys;
		int[] ks = k[index];

		if(create && (ks == null)) {
			ks = new int[origBucketSize];
			k[index] = ks;

			objects[index] = new Object[origBucketSize];
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

			Object[] o = objects[index];
			Object[] o2 = new Object[ks2.length];
			System.arraycopy(o,0,o2,0,o.length);
			objects[index] = o2;

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
		objects = new Object[origSize][];
	}

	private final void rehash() {
//		System.out.println("rehash()");
		int[][] oldKeys = keys;
		Object[][] oldObjects = objects;

		int oldCapacity = oldObjects.length;
		int newCapacity = oldCapacity * 2 + 1;

		int[][] newKeys = new int[newCapacity][];
		Object[][] newObjects = new Object[newCapacity][];

		threshold = (int)(newCapacity * loadFactor);

		keys = newKeys;
		objects = newObjects;

		int[] k;
		int i,j;

		for(i=0;i<oldKeys.length;i++) {
			k = oldKeys[i];
			if(k!=null)
				for(j=0;j<k.length;j++)
					if(k[j] != 0)
						internalPut(k[j], oldObjects[i][j]);
		}
	}

	/**
	 * Special method for rehash, doesn't alter the size,
	 * never checks for duplicates, never rehashs, key is
	 * already converted
	 */
	private final void internalPut(int key, Object value) {
		int index = getIndex(key);
		int[] k = getKeys(index, true);

		int i;
		for(i=0;i<k.length;i++)
			if(k[i] == 0) { // i.e. unused
				k[i] = key;
				objects[index][i] = value;
				return;
			}

		k = growKeys(index); // grow if no free slots found
		k[i] = key;
		objects[index][i] = value;
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

		// Read in number of buckets and allocate the bucket array;
		int numBuckets = s.readInt();

		keys = new int[numBuckets][];
		objects = new Object[numBuckets][];

		// Read in size (number of Mappings)
		int z = s.readInt();

		// Read the keys and values, and put the mappings in the HashMap
		int key;
		Object value;
		for (int i=0; i<z; i++) {
			key = s.readInt();
			value = s.readObject();
			put(key, value);
		}
	}

	private final void writeObject(java.io.ObjectOutputStream s)
		throws java.io.IOException {

		// Write out the threshold, loadfactor, and any hidden stuff
		s.defaultWriteObject();

		int[][] z = keys;

		// Write out number of buckets
		s.writeInt(z.length);

		// Write out size (number of Mappings)
		s.writeInt(size);

		// Write out keys and values (alternating)

		int i,j;
		int[] b;

		for(i=0;i<z.length;i++) {
			b = z[i];
			if(b!=null)
				for(j=0;j<b.length;j++)
					if(b[j] != 0) {
						s.writeInt(decodeKey(b[j]));
						s.writeObject(objects[i][j]);
					}
		}
	}
}