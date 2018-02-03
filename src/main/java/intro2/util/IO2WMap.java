package intro2.util;

/**
 * 2-way integer-Object mapping, serialization only saves one half for
 * efficiency.
 */
public final class IO2WMap implements java.io.Serializable {
	static final long serialVersionUID = 2856538143598397302L;

	private I2OMap i2o;
	private transient O2IMap o2i;

	public IO2WMap() {
		this(11, 10, 0.75f);
	}

	public IO2WMap(int size) {
		this(size, 10, 0.75f);
	}

	public IO2WMap(int size, float loadFactor) {
		this(size, 10, loadFactor);
	}

	public IO2WMap(int size, int bucketSize, float loadFactor) {
		if(size<1 || bucketSize<1)
			throw new IllegalArgumentException("Can't create a map / map-bucket smaller than 1 in size");

		i2o = new I2OMap(size, bucketSize, loadFactor);
		o2i = new O2IMap(size, bucketSize, loadFactor);
	}

	public final Object get(int key) {
		return i2o.get(key);
	}

	public final int get(Object key) {
		return o2i.get(key);
	}

	public boolean containsKey(int key) {
		return i2o.containsKey(key);
	}

	public boolean containsKey(Object key) {
		return o2i.containsKey(key);
	}

	public final void put(int key, Object value) {
		i2o.put(key, value);
		o2i.put(value, key);
	}

	public final void remove(int key) {
		Object obj = i2o.remove(key);
		if(obj != null)
			o2i.remove(obj);
	}

	public final void remove(Object key) {
		int i = o2i.remove(key);
		if(i != Integer.MIN_VALUE)
			i2o.remove(i);
	}

	public final int size() {
		return i2o.size();
	}

	private final void readObject(java.io.ObjectInputStream s)
		 throws java.io.IOException, ClassNotFoundException {

		// Read in the int to Object mapping
		s.defaultReadObject();

		o2i = new O2IMap(i2o.size, i2o.origBucketSize, i2o.loadFactor);

		int[] keys = i2o.keys();
		for(int i=0;i<keys.length;i++)
			o2i.put(i2o.get(keys[i]), keys[i]);
	}
}