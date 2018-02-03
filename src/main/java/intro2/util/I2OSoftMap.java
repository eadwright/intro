package intro2.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Map;

/**
 * A Map using Soft references to values (not the keys), having a
 * strong reference to a key won't prevent data being garbage collected
 * when memory is low.
 *
 * Note this behaviour is different to WeakHashMap, which holds weak references
 * to the keys, not the values.
 *
 * This Map does not allow nulls as entry values.
 *
 * The top recently used "interval" entries are protected from garbage
 * collection for efficiency.
 */
public final class I2OSoftMap {
	private volatile ISet strongKeys;
	private volatile I2OMap hash;
	private long idCounter = 0;
	private int interval;
	private int strongSize;
	private ReferenceQueue queue = new ReferenceQueue();

	public I2OSoftMap(int strongSize) { // interval = strongsize
		this(strongSize, strongSize);
	}

	public I2OSoftMap(int interval, int strongSize) {
		if((interval < 1) && (strongSize > 0))
			throw new IllegalArgumentException("Must specify a positive interval to check strong references");

		if((interval < 0) || (strongSize < 0))
			throw new IllegalArgumentException("Negative parameters invalid");

		strongKeys = new ISet();
		hash = new I2OMap(strongSize+interval+1);

		this.interval = interval;
		this.strongSize = strongSize;
	}

	public final Object get(int key) {
		Value v = (Value)hash.get(key);

		if(v == null)
			return null;

		Object obj = v.getStrong(idCounter);

		if(obj != null) {
			if(interval > 0) {
				idCounter++;
				strongKeys.add(key);
				soften();
			} else
				v.goSoft();
		} else {
			strongKeys.remove(key);
			hash.remove(key);
		}

		return obj;
	}

	public final Object put(int key, Object value) {
		processQueue();
		if(value == null)
			throw new NullPointerException("Null value not allowed");

		Object old = null;
		Value v = (Value)hash.get(key);
		if(v != null)
			old = v.get();

		v = new Value(idCounter++, key, value, queue);

		if(interval > 0)
			strongKeys.add(key);
		else
			v.goSoft();

		hash.put(key, v);

		soften();

		return old;
	}

	public final Object remove(int key) {
		processQueue();

		Object old = null;
		Value v = (Value)hash.get(key);
		if(v != null) {
			old = v.get();
			strongKeys.remove(key);
			hash.remove(key);
		}

		return old;
	}

	public final void clear() {
		processQueue();
		strongKeys.clear();
		hash.clear();
		idCounter = 0;
	}

	/* Internal class for entries */
	private final static class Entry implements Map.Entry {
		private Map.Entry ent;
		private Object value;	/* Strong reference to value, so that the GC
					   will leave it alone as long as this Entry
					   exists */

		Entry(Map.Entry ent, Object value) {
			this.ent = ent;
			this.value = value;
		}

		public final Object getKey() {
			return ent.getKey();
		}

		public final Object getValue() {
			return value;
		}

		public final Object setValue(Object value) {
			this.value = value;
			return ent.setValue(value);
		}

		private final static boolean valEquals(Object o1, Object o2) {
			return (o1 == null) ? (o2 == null) : o1.equals(o2);
		}

		public final boolean equals(Object o) {
			if (! (o instanceof Map.Entry)) return false;
			Map.Entry e = (Map.Entry)o;
			return (valEquals(ent.getKey(), e.getKey())
				&& valEquals(value, e.getValue()));
		}

		public final int hashCode() {
			Object v;
			return ent.getKey().hashCode() ^ (((v = value) == null) ? 0 : v.hashCode());
		}
	}

	private final class Value extends SoftReference {
		private volatile Object value; // optional strong reference
		long id; // records when accessed
		int key; // key, used when processing ref queue

		volatile boolean strong;
		volatile boolean goSoftCalled;

		public Value(long id, int key, Object value, ReferenceQueue q) {
			super(value, q);
			this.id = id;
			this.key = key;
			this.value = value;

			strong = true;
			goSoftCalled = false;
		}

		/**
		 * Destroys the internal strong reference, now only
		 * got a soft one.
		 */
		public final void goSoft() {
			value = null;

			strong = false;

			goSoftCalled = true;

//			if(strongKeys.contains(key))
//				System.out.println("goSoft() just called, but still in strongKeys, key: "+key);

/*			boolean b = strongKeys.remove(key); // remove anyway

			if(b)
				System.out.println("goSoft() just called, strong key removed");*/
		}

		/**
		 * Returns the soft reference, if it's not been forgotten,
		 * the reference is made strong again, and the id # updated
		 */
		public final Object getStrong(long id) {
//			if((value!=null) && (super.get()==null))
//				System.out.println("Got soft, but no hard!!");

			value = super.get();

//			if(strong && (value==null))
//				System.out.println("Got no soft, but strong flag true");

			if(value != null)
				this.id = id;

			return value;
		}
	}

	private final void processQueue() {
		Value v;
		while ((v = (Value)queue.poll()) != null) {
			hash.remove(v.key);

//			if(strongKeys.contains(v.key)) {
//				System.out.println("in queue & strongkeys, key "+v.key+" hashcode "+v.key.hashCode());
//				strongKeys.remove(v.key);
//			}
		}
	}

	private final void soften() {
		if(interval == 0)
			return;

		if(idCounter % interval != 0)
			return;

		int[] keys = strongKeys.keys();

		int key;
		Value v;
		for(int i=0;i<keys.length;i++) {
			key = keys[i];
			v = (Value)hash.get(key);
			if(v != null) {
				if(idCounter - v.id > strongSize) {
					strongKeys.remove(key);
					v.goSoft();
				}
			} else
				strongKeys.remove(key);
//				System.out.println("soften(), key "+key.toString()+" not in hash");
		}
	}
}
