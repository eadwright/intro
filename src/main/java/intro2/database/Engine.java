package intro2.database;

import intro2.config.Project;
import intro2.database.reference.NativeFinder;
import intro2.database.reference.Reference;
import intro2.database.reference.ReferenceFactory;
import intro2.database.reference.ReflectionFinder;

import java.io.*;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Entry point for creating / loading a database. Stateless.
 */
public final class Engine {
	private Engine() {}

	/**
	 * Request to get a Database.
	 *
	 * If a database file exists, we load it, otherwise we re-do the processing
	 */
	public static void getDatabase(Project project) throws Database.DataProcessingException {
		String fn = project.getDBFileName();
		if(fn != null) {
			File f = new File(fn);
			if(f.exists())
				try {
					loadDatabase(project);
					return;
				} catch (Database.DataProcessingException ex) {
					System.err.println("Error loading persisent database");
					ex.printStackTrace();
				}
		}

		createDatabase(project);
	}

	private static void loadDatabase(Project project) throws Database.DataProcessingException {
		String dbfn = project.getDBFileName();
		if(dbfn == null)
			return;

		File f = new File(dbfn);

		loadDatabaseSerialized(project, f);

/*		I2OMap map = Database.getDBData().classIDsToClassInfo;
		int[] ids = map.keys();
		for(int i=0;i<ids.length;i++)
			((ClassInfo)map.get(ids[i])).post();*/
	}

	private static void loadDatabaseSerialized(Project project, File f) throws Database.DataProcessingException {
		System.out.println("loadDatabase");
		DBData data = null;
		try {
			ObjectInputStream ois = new ObjectInputStream (new GZIPInputStream(new BufferedInputStream(new FileInputStream(f))));
			data = (DBData)ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			throw new Database.DataProcessingException("Can't find "+f.getName(),e);
		} catch (IOException e2) {
			throw new Database.DataProcessingException("Error reading "+f.getName(),e2);
		} catch (ClassNotFoundException e3) {
			throw new Database.DataProcessingException("Error reading "+f.getName(),e3);
		}
		System.out.println("loaded");

		Database.init(project, data);
	}

	private static void createDatabase(Project project) throws Database.DataProcessingException {
		synchronized(project) {
			DBData data = new DBData();
			data.init();

			Database.init(project, data);

			Cache.setJarFileCaching(true);

			long time = System.currentTimeMillis();

			int[] ids = processClasses1();
			processClasses2(ids);
			processClasses3(ids);
			processClasses4(ids);

			Database.getDBData().refs = Cache.getReferenceArray();

			processClasses5(ids);

			Database.getDBData().printStats();

			Cache.clear(); // stops jar caching too
			// next line now in ReferenceFactory.getInnerClassRelationships()
//			Database.getDBData().doingIntrospection = false;

			long time2 = System.currentTimeMillis();
			System.out.println("Processing time "+(System.currentTimeMillis() - time)+" ms");

			persistDatabase();

			long time3 = System.currentTimeMillis() - time2;

			if(time3 > 1L)
				System.out.println("Persistance time "+time3+" ms");
		}
	}

	private static void persistDatabase() throws Database.DataProcessingException {
		Project project = Database.getProject();
		String dbfn = project.getDBFileName();

		if(dbfn == null) // no file name specified, so do nothing
			return;

		System.out.println("persistDatabase");

		File f = new File(dbfn);
		DBData data = Database.getDBData();

		try {
			ObjectOutputStream oos = new ObjectOutputStream(new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(f))));
			oos.writeObject(data);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new Database.DataProcessingException("Error writing "+f.getName(),e);
		}
	}

	/**
	 * Creates ClassInfo objects, and adds mappings for
	 * class names, packages, modules etc.
	 */
	private static int[] processClasses1() throws Database.DataProcessingException {
		System.out.println("processClasses1");

		Project.Jar[] jars = Database.getProject().getLibraries();
		DBData data = Database.getDBData();

		String path, classFileName, className;
		Set fileNames;
		Iterator it;
		ClassInfo ci;
		int id;
		int jarIdCounter = 0;
		int jarId;

		for(int n=0;n<jars.length;n++) {
			path = jars[n].getLibPath();
			jarId = jarIdCounter++;
			data.jarIDsToJarNames.put(jarId, path);

			fileNames = Cache.getJarFileNames(path);

			it = fileNames.iterator();
			while(it.hasNext()) {
				classFileName = (String)it.next();

				if(NamingUtil.isClassFile(classFileName)) {
					className = NamingUtil.getClassName(classFileName);

					if(!data.classIDsToClassNames.containsKey(className)) {
//						int s1a = data.classIDsToClassInfo.size();
//						int s1b = data.classIDsToClassNames.size();

						id = Database.getClassID(className);
						ci = new ClassInfo(id, jarId);

						NamingUtil.addMappings(ci, className, jarId);

/*						boolean b1 = data.classIDsToClassInfo.containsKey(id);
						boolean b2 = data.classIDsToClassNames.containsKey(id);
						boolean b3 = data.classIDsToClassNames.containsKey(className);

						if(!(b1 && b2 && b3))
							System.out.println("b1 "+b1+" b2 "+b2+" b3 "+b3);

						int s2a = data.classIDsToClassInfo.size();
						int s2b = data.classIDsToClassNames.size();

						if(s1a == s2a)
							System.out.println("class info not added "+className);

						if(s1b == s2b)
							System.out.println("class name not added "+className);

						if(data.classIDsToClassInfo.size()< data.classIDsToClassNames.size())
							System.out.println("size mismatch s1a "+s1a+" s1b "+s1b+" s2a "+s2a+" s2b "+s2b);*/
					}
				}
			}
		}

//		cache.printStatistics();

		// sanity check

/*		int[] k = data.classIDsToClassInfo.keys();
		System.out.println("class info objects "+k.length);
		System.out.println("class names "+data.classIDsToClassNames.size());
		for(int q=0;q<k.length;q++)
			if(data.classIDsToClassInfo.get(k[q]) == null)
				System.out.println("fail for "+q);*/

		return data.classIDsToClassInfo.keys();
	}

	/**
	 * Finds direct references between classes
	 */
	private static void processClasses2(int[] classIDs) throws Database.DataProcessingException {
		System.out.println("processClasses2");

		ClassInfo ci;
		Reference ref;
		int n;
		int[] offsets;

		DBData data = Database.getDBData();
		for(int i=0;i<classIDs.length;i++) {
			ci = (ClassInfo)data.classIDsToClassInfo.get(classIDs[i]);

			offsets = ci.getMethodOffsets();
			if(offsets!=null)
				for(n=0;n<offsets.length;n++)
					ci.getMethod(offsets[n]).addReferences();
		}

//		Database.getCache().printStatistics();
	}

	/**
	 * Finds class-level inheritance relationships, then class-member-level ones
	 */
	private static void processClasses3(int[] classIDs) throws Database.DataProcessingException {
		System.out.println("processClasses3");

		ReferenceFactory.processClassRelationships(classIDs);
		ReferenceFactory.processMemberRelationships(classIDs);

//		Database.getDBData().createNewIDs = false;

		ReferenceFactory.processInnerClassRelationships(classIDs);
	}

	/**
	 * Finds package-level & module-level relationships
	 */
	private static void processClasses4(int[] classIDs) throws Database.DataProcessingException {
		System.out.println("processClasses4");

		ReferenceFactory.processPackageRelationships(Cache.getReferenceList(), classIDs);
	}

	/**
	 * Finds reflection / native usage at class, package and module level
	 */
	private static void processClasses5(int[] classIDs) throws Database.DataProcessingException {
		System.out.println("processClasses5");

		ReflectionFinder.init();

		ClassInfo ci;
		Package p;
		for(int i=0;i<classIDs.length;i++) {
			if(ReflectionFinder.usesReflection(classIDs[i])) {
				ci = Database.getClassInfo(classIDs[i]);
				if(ci != null) {
					ci.setUsesReflection();
//					System.out.println(Database.getClassName(classIDs[i])+" uses reflection");
				}

				p = Database.getPackage(NamingUtil.getPackage(Database.getClassName(classIDs[i])));
				p.setUsesReflection();
//				System.out.println("package "+p.getName()+" uses reflection");
			}

			if(NativeFinder.usesNative(classIDs[i])) {
				ci = Database.getClassInfo(classIDs[i]);
				if(ci != null) {
					ci.setUsesNative();
//					System.out.println(Database.getClassName(classIDs[i])+" uses reflection");
				}

				p = Database.getPackage(NamingUtil.getPackage(Database.getClassName(classIDs[i])));
				p.setUsesNative();
//				System.out.println("package "+p.getName()+" uses reflection");
			}
		}

		Iterator it = Database.getAllModules().values().iterator();
		Iterator it2;
		Module m;
		while(it.hasNext()) {
			m = (Module)it.next();

			it2 = m.getAllPackages().iterator();
			here:
			while(it2.hasNext()) {
				p = (Package)it2.next();

				if(p.usesReflection())
					m.usesReflection();

				if(p.usesNative())
					m.setUsesNative();
			}
		}
	}

/*	private static void cacheTest(Database db) throws Database.DataProcessingException {
		Cache cache = Database.getCache();
		ClassInfo ci = Database.getClassInfo("javax/swing/JTable");

		Long key;
		do {
			key = cache.getNewKey();

			cache.putObject(key, ci.getCPI(1));
		} while(true);
	}*/
}