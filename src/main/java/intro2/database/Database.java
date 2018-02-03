package intro2.database;

import intro2.config.Project;
import intro2.database.reference.Reference;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * The public API for databases, a singleton
 */
public final class Database {
	private static Project project; // config stuff
	private static DBData data; // persisent stuff

	private static int classIDCounter = 0;

	private Database() {}

	static void init(Project prj, DBData d) {
		project = prj;
		data = d;
	}

	public static ClassInfo getClassInfo(String className) {
		return getClassInfo(data.classIDsToClassNames.get(className));
	}

	public static boolean isClassInfoDefined(String className) {
		return data.classIDsToClassNames.containsKey(className);
	}

	public static boolean isClassInfoDefined(int classID) {
		if(classID == Integer.MIN_VALUE)
			return false;
		else
			return data.classIDsToClassInfo.containsKey(classID);
	}

	public static ClassInfo getClassInfo(int classID) {
		if(classID == Integer.MIN_VALUE)
			return null;
		else
			return (ClassInfo)data.classIDsToClassInfo.get(classID);
	}

	public static int getClassID(String className) {
		if(className == null)
			return Integer.MIN_VALUE;

		int id = data.classIDsToClassNames.get(className);
		if(id == Integer.MIN_VALUE) {
//		if((id == Integer.MIN_VALUE) && data.createNewIDs) {
			id = classIDCounter++;
			data.classIDsToClassNames.put(id, className);
//			if(className.indexOf("PropertyChangeListener")!=-1)
//				System.out.println("new id for "+className);
		}
		return id;
	}

	public static String getClassName(int classID) {
		if(classID == Integer.MIN_VALUE)
			return null;
		else
			return (String)data.classIDsToClassNames.get(classID);
	}

	public static Reference getReference(int refID) {
//		try {
			return data.refs[refID];
//		} catch (Throwable t) {
//			System.out.println("Can't find ref "+refID+" size "+data.refs.length);
//			t.printStackTrace();
//			throw new RuntimeException();
//		}
	}

	public static Package getPackage(String name) {
		Package p = (Package)data.packages.get(name);
		if(p == null) {
			p = new Package(name);
			data.packages.put(name, p);
		}
		return p;
	}

	public static boolean containsPackage(String name) {
		return data.packages.containsKey(name);
	}

	public static SortedSet getAllSubPackages(Package pkg) {
		TreeSet results = new TreeSet();
		Iterator it = data.packages.keySet().iterator();
		String name = pkg.getName();
		String second;
		while(it.hasNext()) {
			second = (String)it.next();
			if((second.length() > name.length()) &&
						(second.indexOf(name) == 0))
				results.add(data.packages.get(second));
		}

		return results;
	}

	public static int[] getAllClassIDsInOrder() { // sorts into classname order
		int[] ids = data.classIDsToClassInfo.keys();

		SortedSet set = new TreeSet();

		int i;
		for(i=0;i<ids.length;i++)
			set.add(data.classIDsToClassNames.get(ids[i]));

		String[] names = new String[ids.length];
		set.toArray(names);

		int[] results  = new int[ids.length];

		for(i=0;i<results.length;i++)
			results[i] = data.classIDsToClassNames.get(names[i]);

		return results;
	}

	public static boolean hasSubPackages(Package pkg) {
		Iterator it = data.packages.keySet().iterator();
		String name = pkg.getName();
		String second;
		while(it.hasNext()) {
			second = (String)it.next();
			if((second.length() > name.length()) &&
						(second.indexOf(name) == 0))
				return true;
		}

		return false;
	}

	public static Module getModule(String name) {
		Module m = (Module)data.modules.get(name);
		if(m == null) {
			m = new Module(name);
			data.modules.put(name, m);
		}
		return m;
	}

	static SortedMap getAllModules() {
		return data.modules;
	}

	public static Project getProject() {
		return project;
	}

	static DBData getDBData() {
		return data;
	}

/*	public Cache getCache() {
		return cache;
	}*/

	public static class DataProcessingException extends Exception {
		public DataProcessingException(String msg) {
			super(msg);
		}

		public DataProcessingException(String msg, Throwable rootCause) {
			super(msg, rootCause);
		}
	}
}