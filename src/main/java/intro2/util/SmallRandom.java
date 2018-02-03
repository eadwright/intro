package intro2.util;

import java.util.Random;

/**
 * Stores 2^18 random numbers, used in distributing data within maps.
 * Uses 1Mb of memory, but is v. fast. Singleton and thread-safe too.
 */
public final class SmallRandom {
	private final static int[] data;
	private final static int size = 0x3ffff;

	private final static SmallRandom instance;

	private SmallRandom() {} // Inaccessible

	static {
		Random r = new Random();
		data = new int[size+1];

		for(int i=0;i<size+1;i++)
			data[i] = r.nextInt();

		instance = new SmallRandom();
	}

	/**
	 * Contract - will always give the same result for
	 * the same basis, acts like a hashCode for ints.
	 */
	public final static int getInt(int basis) {
		return data[basis & size];
	}

	/**
	 * Allow a strong reference to prevent class garbage collection
	 */
	public final static SmallRandom getSingleton() {
		return instance;
	}
}