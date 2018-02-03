package intro2.database;

import intro2.config.Project;
import intro2.util.UniqueIList;
import intro2.util.UniqueList;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class NamingUtil {
	static boolean isClassFile(String fileName) {
//		if(fileName.indexOf("java/awt") == -1)
//		if((fileName.indexOf("java/awt") == -1) ||
//					(fileName.lastIndexOf('/') > 8))
//			return false; // for development

//		return fileName.equals("java/security/PrivateKey.class");
		int i = fileName.indexOf(".class");
		return ((i!=-1) && (i==fileName.length()-6));
	}

	static String getClassName(String fileName) {
		return fileName.substring(0,fileName.length()-6);
	}

	public static String getPackage(String className) {
		if(className == null)
			return null;

		int i = className.lastIndexOf('/');
		if(i == -1)
			return "/"; // default package
		else
			return className.substring(0,i);
	}

	static void addMappings(ClassInfo ci, String className, int jarID) {
		DBData data = Database.getDBData();
		int id = ci.getID();
		data.classIDsToClassInfo.put(id, ci);

		String pkg = getPackage(className);
		String[] p;

		int n,i;
		Project.Module m;
		Package pp;
		Module mm;

		Iterator it = Database.getProject().getModules().values().iterator();

		while(it.hasNext()) {
			m = (Project.Module)it.next();
			mm = Database.getModule(m.getName());
			p = m.getPackages();
			for(i=0;i<p.length;i++) {
				if(isSubPackage(p[i],pkg)) {
					pp = Database.getPackage(pkg);
					pp.addClassID(id);

					mm.addPackage(pp);
					mm.addLibrary(jarID);

/*					addToListInMap(m.getName(),id,data.moduleNamesToClassLists);
					addToListInMap(m.getName(),pkg,data.moduleNamesToPackageLists);
					addToListInMap(pkg,id,data.packageNamesToClassLists);*/
				}
			}
		}
	}

	static void addToListInMap(Object key, Object value, Map map) {
		List list = (List)map.get(key);
		if(list == null) {
			list = new UniqueList();
			map.put(key,list);
		}
		list.add(value);
	}

	static void addToListInMap(Object key, int value, Map map) {
		UniqueIList list = (UniqueIList)map.get(key);
		if(list == null) {
			list = new UniqueIList();
			map.put(key,list);
		}
		list.add(value);
	}

	public static boolean isSubPackage(String sup, String sub) {
		if(sup.equals("/"))
			return true;

		return sub.indexOf(sup+'/') == 0;
	}
}