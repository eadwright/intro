package intro2.database;

import intro2.util.UniqueIList;

import java.util.*;

public final class Package implements java.io.Serializable, Comparable {
	private boolean usesReflection = false;
	private boolean usesNative = false;
	private String name;
	private UniqueIList classIDs;
	private transient UniqueIList classOnlyIDs, interfaceOnlyIDs;
	private SortedMap destPkgToRefIDLists;
	private SortedMap srcPkgToRefIDLists;
	private transient boolean noSet = false;
	private transient long no;
	private transient String displayName;
	private transient boolean filenameValueSet = false;
	private transient long filenameValue;

	Package(String name) {
		this.name = name;
		classIDs = new UniqueIList();
//		destPkgToRefIDLists = new TreeMap(new PackageComparator());
//		srcPkgToRefIDLists = new TreeMap(new PackageComparator());
		destPkgToRefIDLists = new TreeMap();
		srcPkgToRefIDLists = new TreeMap();
	}

	public int compareTo(Object obj) {
		return name.compareTo(((Package)obj).getName());
	}

	public long getNo() {
		if(!noSet) {
			no = Cache.getUniqueFilenameValue(name);
			noSet = true;
		}
		return no;
	}

	void setUsesReflection() {
		usesReflection = true;
	}

	void setUsesNative() {
		usesNative = true;
	}

	public boolean usesReflection() {
		return usesReflection;
	}

	public boolean usesNative() {
		return usesReflection;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		if(displayName == null)
			displayName = getDisplayName(name);

		return displayName;
	}

	public static String getDisplayName(String name) {
		return name.replaceAll("/","."); // needs Java 1.4
	}

	private long getFilenameValue() {
		if(!filenameValueSet) {
			filenameValueSet = true;

			filenameValue = Cache.getUniqueFilenameValue(name);
		}

		return filenameValue;
	}

	public String getOutputFilename() {
		return getOutputFilename(null);
	}

	public String getOutputFilename(String ending) {
		getFilenameValue();

		long part1 = filenameValue % 100L;
		long part2 = filenameValue / 100L;

		StringBuffer sb = new StringBuffer("package/");
		sb.append(Long.toString(part1, 32));
		sb.append('/');
		sb.append(Long.toString(part2, 32));

		if(ending != null)
			sb.append(ending);

		sb.append(".html");

		return sb.toString();
	}

	private static String getFilename(Package p1, Package p2, String middle) {
		StringBuffer sb = new StringBuffer("package/");
		sb.append(middle);
		sb.append('/');

		long v1 = p1.getFilenameValue();
		long v2 = p2.getFilenameValue();

		long v1p1 = v1 % 100L;
		long v1p2 = v1 / 100L;
		long v2p1 = v2 % 100L;
		long v2p2 = v2 / 100L;

		sb.append(Long.toString(v1p1, 32));
		sb.append('/');
		sb.append(Long.toString(v2p1, 32));
		sb.append('/');
		sb.append(Long.toString(v1p2, 32));
		sb.append('/');
		sb.append(Long.toString(v2p2, 32));
		sb.append(".html");
		return sb.toString();
	}

	public static String getUsesFilename(Package p1, Package p2) {
		return getFilename(p1, p2, "uses");
	}

	public static String getUsedByFilename(Package p1, Package p2) {
		return getFilename(p1, p2, "usedby");
	}

	void addClassID(int id) {
		classIDs.add(id);
	}

	public void addOutwardReference(Package destinationPackage, int referenceID) {
		NamingUtil.addToListInMap(destinationPackage, referenceID, destPkgToRefIDLists);
	}

	public void addInwardReference(Package sourcePackage, int referenceID) {
		NamingUtil.addToListInMap(sourcePackage, referenceID, srcPkgToRefIDLists);
	}

	public UniqueIList getAllClassIDs() {
		return classIDs;
	}

	public UniqueIList getAllInterfaceOnlyIDs() {
		if(interfaceOnlyIDs == null)
			interfaceOnlyIDs = getClassesOrInterfaces(true, classIDs);

		return interfaceOnlyIDs;
	}

	public UniqueIList getAllClassOnlyIDs() {
		if(classOnlyIDs == null)
			classOnlyIDs = getClassesOrInterfaces(false, classIDs);

		return classOnlyIDs;
	}

	UniqueIList getClassesOrInterfaces(boolean lookForInterfaces, UniqueIList classIDs) {
		TreeSet names = new TreeSet();

		ClassInfo ci;

		for(int i=0;i<classIDs.getSize();i++) {
			ci = Database.getClassInfo(classIDs.get(i));

			if(ci.isInterface() == lookForInterfaces)
				names.add(Database.getClassName(ci.getID()));
		}

		UniqueIList ids = new UniqueIList(names.size());
		Iterator it = names.iterator();
		while(it.hasNext())
			ids.add(Database.getClassID((String)it.next()));

		return ids;
	}

	public boolean usesOthers() {
		return destPkgToRefIDLists.size() > 0;
	}

	public boolean usedByOthers() {
		return srcPkgToRefIDLists.size() > 0;
	}

	/**
	 * This doesn't return a "SortedSet", but the Set's Iterator will always
	 * return things in order
	 */
	public Set getOutwardPackages() {
		return destPkgToRefIDLists.keySet();
	}

	/**
	 * This doesn't return a "SortedSet", but the Set's Iterator will always
	 * return things in order
	 */
	public Set getInwardPackages() {
		return srcPkgToRefIDLists.keySet();
	}

	public UniqueIList getReferencesByDestPackage(Package destinationPackage) {
		return (UniqueIList)destPkgToRefIDLists.get(destinationPackage);
	}

	public UniqueIList getReferencesBySrcPackage(Package sourcePackage) {
		return (UniqueIList)srcPkgToRefIDLists.get(sourcePackage);
	}

/*	static class PackageComparator implements Comparator, java.io.Serializable {
		public int compare(Object o1, Object o2) {
			Package p1 = (Package)o1;
			Package p2 = (Package)o2;

			return p1.getName().compareTo(p2.getName());
		}
	}*/
}