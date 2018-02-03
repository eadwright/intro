package intro2.database;

import intro2.database.reference.Reference;
import intro2.util.I2OSoftMap;
import intro2.util.LSet;
import intro2.util.SoftHashMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public final class Cache {
//	private long objectsAddedCount = 0;
//	private long accessCount = 0;
//	private long cacheHitCount = 0;

//	private long bcObjectsAddedCount = 0;
//	private long bcAccessCount = 0;
//	private long bcCacheHitCount = 0;

//	private Map objectClassCount = new HashMap();

	private static Map bytecode; // soft
//	private Map objects; // soft
	private static I2OSoftMap objects; // soft
	private static Map jarFiles; // normal
	private static volatile int objKeyCounter;
	private static volatile int refKeyCounter;
	private static boolean cacheJarFiles = false;
	private static List refs;
	private static LSet filenameValues;

	static {
		bytecode = new SoftHashMap(1333,4000);
		objects = new I2OSoftMap(10000,30000);
		jarFiles = new HashMap();
		refs = new ArrayList(20000);
		filenameValues = new LSet();
		objKeyCounter = 0;
		refKeyCounter = 0;
	}

	private Cache() {}

	public static long getUniqueFilenameValue(String name) {
		long value = 0L;
		for(int i=0;i<name.length();i++)
			value ^= (((long)name.charAt(i)) & 0xFFL) << (6 * (i%11));

		value = Math.abs(value);

		do {
			if(!filenameValues.add(value))
				return value;

			if(value == Long.MAX_VALUE)
				value = Long.MIN_VALUE;
			else
				value++;
		} while(true);
	}

	public static void addReference(Reference ref) { // only for db creation
		refs.add(ref);

//		if(ref.getID() > refs.size())
//			new Exception().printStackTrace();
	}

	static Reference[] getReferenceArray() { // only for db creation, call before clear()
		Reference[] r = new Reference[refs.size()];

		for(int n=0;n<r.length;n++)
			r[n] = (Reference)refs.get(n);

		return r;
	}

	static List getReferenceList() { // only for db creation, call before clear()
		return refs;
	}

	static void setJarFileCaching(boolean b) {
		cacheJarFiles = b;
		if(!cacheJarFiles) {
			Iterator it = jarFiles.values().iterator();
			while(it.hasNext()) {
				try {
					((JarFile)it.next()).close();
				} catch (IOException e) {} // don't care
			}
			jarFiles.clear();
		}
	}

	static void clear() {
		setJarFileCaching(false);
		bytecode.clear();
		objects.clear();
		refs = null;
	}

	static int getNewKey() {
		return objKeyCounter++;
	}

	public static int getNewReferenceID() {
		return refKeyCounter++;
	}

	static void putObject(int key, Object value) {
//		objectsAddedCount++;
		objects.put(key,value);

/*		String cl = value.getClass().toString();
		Integer i = (Integer)objectClassCount.get(cl);

		if(i == null)
			i = new Integer(1);
		else
			i = new Integer(i.intValue()+1);

		objectClassCount.put(cl,i);

		if(objectsAddedCount%100000 == 0)
			printStatistics();*/
	}

	static Object getObject(int key) {
		return objects.get(key);
/*		Object obj = objects.get(key);

		accessCount++;
		if(obj != null)
			cacheHitCount++;

		return obj;*/
	}

	static ByteArrayAccessor getClassBytes(String className, String jarName) throws Database.DataProcessingException {
		ByteArrayAccessor acc = (ByteArrayAccessor)bytecode.get(className);

/*		bcAccessCount++;
		if(acc != null)
			bcCacheHitCount++;*/

		if(acc == null) {
			byte[] data = null;
			try {
				JarFile jf = getJarFile(jarName);

				ZipEntry ze = jf.getEntry(className+".class");

				long size = ze.getSize();
				data = new byte[(int)size];
				InputStream is = jf.getInputStream(ze);

				long alreadyRead = 0;
				do {
					alreadyRead += is.read(data,(int)alreadyRead,(int)(size-alreadyRead));
//					if(alreadyRead<size)
//						System.out.println("want "+size+" bytes, got "+alreadyRead);
				} while (alreadyRead < size);

				is.close();

				if(!cacheJarFiles)
					jf.close();
			} catch (IOException e) {
				throw new Database.DataProcessingException("Unable to load class data for "+className+" in "+jarName, e);
			}

			acc = new ByteArrayAccessor(data);

//			bcObjectsAddedCount++;
			bytecode.put(className, acc);
		}

		return acc;
	}

	static Set getJarFileNames(String jarName) throws Database.DataProcessingException {
		try {
			HashSet set = new HashSet();

			JarFile jf = getJarFile(jarName);

			Enumeration e = jf.entries();
			while(e.hasMoreElements())
				set.add(((JarEntry)e.nextElement()).getName());

			if(!cacheJarFiles)
				jf.close();

			return set;
		} catch (IOException e) {
			throw new Database.DataProcessingException("Error reading jar file "+jarName, e);
		}
	}

	private static JarFile getJarFile(String jarName) throws IOException  {
		JarFile jf = null;
		if(cacheJarFiles)
			jf = (JarFile)jarFiles.get(jarName);

		if(jf == null) {
			jf = new JarFile(jarName);

			if(cacheJarFiles)
				jarFiles.put(jarName, jf);
		}

		return jf;
	}

/*	public void printStatistics() {
		System.out.println();
		System.out.println("Object cache accessed "+accessCount+" times");
		System.out.println("Object cache hits "+cacheHitCount);
		System.out.println("Object cache added "+objectsAddedCount+" current size "+objects.size());

		float eff = ((float)cacheHitCount)/((float)accessCount);
		System.out.println("Efficiency "+eff);

		System.out.println();
		System.out.println("Bytecode cache accessed "+bcAccessCount+" times");
		System.out.println("Bytecode cache hits "+bcCacheHitCount);
		System.out.println("Bytecode cache added "+bcObjectsAddedCount+" current size "+bytecode.size());

		eff = ((float)bcCacheHitCount)/((float)bcAccessCount);
		System.out.println("Efficiency "+eff);

		System.out.println();
//		Iterator it = objectClassCount.keySet().iterator();
//		String cl;
//		while(it.hasNext()) {
//			cl = (String)it.next();
//			System.out.println(cl+" has "+objectClassCount.get(cl).toString()+" entries");
//		}
	}*/
}