package intro2.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.*;

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
public final class SoftHashMap extends AbstractMap implements Map {
	private volatile Set strongKeys;
	private volatile Map hash;
	private long idCounter = 0;
	private int interval;
	private int strongSize;
	private ReferenceQueue queue = new ReferenceQueue();

	public SoftHashMap(int strongSize) { // interval = strongsize
		this(strongSize, strongSize);
	}

	public SoftHashMap(int interval, int strongSize) {
		if((interval < 1) && (strongSize > 0))
			throw new IllegalArgumentException("Must specify a positive interval to check strong references");

		if((interval < 0) || (strongSize < 0))
			throw new IllegalArgumentException("Negative parameters invalid");

		strongKeys = new HashSet();
		hash = new HashMap(strongSize+interval+1);

		this.interval = interval;
		this.strongSize = strongSize;
	}

	public int size() {
		return entrySet().size();
	}

	public boolean isEmpty() {
		return entrySet().isEmpty();
	}

	public boolean containsKey(Object key) {
		return hash.containsKey(key);
	}

	public Object get(Object key) {
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

	public Object put(Object key, Object value) {
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

	public Object remove(Object key) {
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

	public void clear() {
		processQueue();
		strongKeys.clear();
		hash.clear();
		idCounter = 0;
	}

	public Set entrySet() {
		return new EntrySet();
	}

	/* Internal class for entries */
	private static class Entry implements Map.Entry {
		private Map.Entry ent;
		private Object value;	/* Strong reference to value, so that the GC
					   will leave it alone as long as this Entry
					   exists */

		Entry(Map.Entry ent, Object value) {
			this.ent = ent;
			this.value = value;
		}

		public Object getKey() {
			return ent.getKey();
		}

		public Object getValue() {
			return value;
		}

		public Object setValue(Object value) {
			this.value = value;
			return ent.setValue(value);
		}

		private static boolean valEquals(Object o1, Object o2) {
			return (o1 == null) ? (o2 == null) : o1.equals(o2);
		}

		public boolean equals(Object o) {
			if (! (o instanceof Map.Entry)) return false;
			Map.Entry e = (Map.Entry)o;
			return (valEquals(ent.getKey(), e.getKey())
				&& valEquals(value, e.getValue()));
		}

		public int hashCode() {
			Object v;
			return ent.getKey().hashCode() ^ (((v = value) == null) ? 0 : v.hashCode());
		}
	}

	private class EntrySet extends AbstractSet {
		Set hashEntrySet = hash.entrySet();

		public Iterator iterator() {
			return new Iterator() {
				Iterator hashIterator = hashEntrySet.iterator();
				Entry next = null;

				public boolean hasNext() {
					Value v;
					Object obj;
					Map.Entry ent;

					while (hashIterator.hasNext()) {
						ent = (Map.Entry)hashIterator.next();
						v = (Value)ent.getValue();
						obj = v.get();

						if(obj == null)
							continue; // value has been cleared by GC

						next = new Entry(ent, obj);
						return true;
					}
					return false;
				}

				public Object next() {
					if ((next == null) && !hasNext())
						throw new NoSuchElementException();
					Entry e = next;
					next = null;
					return e;
				}

				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}

		public boolean isEmpty() {
			return !(iterator().hasNext());
		}

		public int size() {
			int j = 0;
			for (Iterator i = iterator(); i.hasNext(); i.next())
				j++;
			return j;
		}

		public boolean remove(Object o) {
			throw new UnsupportedOperationException();
		}

		public int hashCode() {
			Map.Entry ent;
			Value v;
			Object obj;
			Iterator i;

			int h = 0;

			for (i = hashEntrySet.iterator(); i.hasNext();) {
				ent = (Map.Entry)i.next();
				v = (Value)ent.getValue();
				obj = v.get();
				if (obj == null)
					continue;
				else
					h+= v.hashCode();
			}

			return h;
		}
	}

	private class Value extends SoftReference {
		private volatile Object value; // optional strong reference
		long id; // records when accessed
		Object key; // key, used when processing ref queue

		volatile boolean strong;
		volatile boolean goSoftCalled;

		public Value(long id, Object key, Object value, ReferenceQueue q) {
			super(value, q);
			this.id = id;
			this.key = key;
			this.value = value;

			strong = true;
			goSoftCalled = false;
		}

/*		public boolean enqueue() {
			if(value!=null)
				System.out.println("GC called enqueue(), but value non-null");
			if(strongKeys.contains(key))
				System.out.println("GC called enqueue(), but key in strongKeys");
			new Exception().printStackTrace();
			return super.enqueue();
		}*/

		/**
		 * Destroys the internal strong reference, now only
		 * got a soft one.
		 */
		public void goSoft() {
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
		public Object getStrong(long id) {
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

	private void processQueue() {
		Value v;
		while ((v = (Value)queue.poll()) != null) {
			hash.remove(v.key);

//			if(strongKeys.contains(v.key)) {
//				System.out.println("in queue & strongkeys, key "+v.key+" hashcode "+v.key.hashCode());
//				strongKeys.remove(v.key);
//			}
		}
	}

	private void soften() {
		if(interval == 0)
			return;

		if(idCounter % interval != 0)
			return;

		Set strongKeysCopy = new HashSet(strongKeys);
		Iterator it = strongKeysCopy.iterator();

		Object key;
		Value v;
		while(it.hasNext()) {
			key = it.next();
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
