package intro2.database;

import intro2.database.reference.Reference;
import intro2.util.I2OMap;
import intro2.util.IO2WMap;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Persistent data bean
 */
public final class DBData implements java.io.Serializable {
	Reference[] refs;
	IO2WMap classIDsToClassNames;
	I2OMap classIDsToClassInfo;
	I2OMap jarIDsToJarNames;
//	transient boolean createNewIDs = false;

	SortedMap modules; // name to Module object
	SortedMap packages; // name to Package object

	void init() { // creating from scratch
//		createNewIDs = true;

		classIDsToClassNames = new IO2WMap();
		classIDsToClassInfo = new I2OMap();
		jarIDsToJarNames = new I2OMap();

		modules = new TreeMap(); // new Module.ModuleComparator());
		packages = new TreeMap(); // new Package.PackageComparator());
	}

	void printStats() {
		System.out.println();
		if(refs!=null)
			System.out.println("refs size "+refs.length);
		System.out.println("classIDsToClassNames size "+classIDsToClassNames.size());
//		System.out.println("classNamesToClassInfo size "+classNamesToClassInfo.size());

//		System.out.println("packageNamesToClassLists size "+packageNamesToClassLists.size());
//		System.out.println("moduleNamesToClassLists size "+moduleNamesToClassLists.size());
//		System.out.println("moduleNamesToPackageLists size "+moduleNamesToPackageLists.size());
		System.out.println();
	}
}