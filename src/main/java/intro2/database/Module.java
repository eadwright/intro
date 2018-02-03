package intro2.database;

import intro2.util.ISet;

import java.util.*;

public final class Module implements java.io.Serializable {
	private boolean usesReflection;
	private boolean usesNative;
	private String name;
	private SortedSet packages;
	private ISet libraries;

	private transient SortedSet outwardSet;
	private transient SortedSet inwardSet;

	Module(String name) {
		this.name = name;
//		packages = new TreeSet(new Package.PackageComparator());
		packages = new TreeSet();
		libraries = new ISet();
	}

	void setUsesReflection() {
		usesReflection = true;
	}

	public boolean usesReflection() {
		return usesReflection;
	}

	void setUsesNative() {
		usesReflection = true;
	}

	public boolean usesNative() {
		return usesReflection;
	}

	public String getName() {
		return name;
	}

	void addPackage(Package p) {
		packages.add(p);
	}

	public boolean containsPackage(Package p) {
		return packages.contains(p);
	}

	public SortedSet getAllPackages() {
		return packages;
	}

	void addLibrary(int jarID) {
		libraries.add(jarID);
	}

	public ISet getAllLibraries() {
		return libraries;
	}

	public boolean usesOthers() {
		return getOutwardModules().size() > 0;
	}

	public boolean usedByOthers() {
		return getInwardModules().size() > 0;
	}

	public boolean uses(Module other) {
		return getOutwardModules().contains(other);
	}

	public boolean usedBy(Module other) {
		return getInwardModules().contains(other);
	}

	/**
	 * Lists the modules this module refers to
	 */
	public synchronized Set getOutwardModules() {
		if(outwardSet != null)
			return outwardSet;

		outwardSet = new TreeSet(new ModuleComparator());

		Map modules = Database.getAllModules();

		Iterator it = modules.keySet().iterator();
		String n;

		Iterator it2, it3;
		Module m;
		Package p;
		Set s;

		while(it.hasNext()) {
			n = (String)it.next();
			if(n.equals(name))
				continue;

			m = (Module)modules.get(n); // another module

			it2 = packages.iterator();

			here:
			while(it2.hasNext()) {
				p = (Package)it2.next(); // package in this module
				s = p.getOutwardPackages(); // set of packages it refers to

				it3 = s.iterator();
				while(it3.hasNext()) {
					p = (Package)it3.next(); // package in other module

					if(m.containsPackage(p)) {
						outwardSet.add(m);
						continue here;
					}
				}
			}
		}

		return outwardSet;
	}

	/**
	 * Lists the modules refer to this module
	 */
	public synchronized SortedSet getInwardModules() {
		if(inwardSet != null)
			return inwardSet;

		inwardSet = new TreeSet(new ModuleComparator());

		Map modules = Database.getAllModules();

		Module m;
		Package p;
		Set s;
		Iterator it2, it3;
		Iterator it = packages.iterator();

		while(it.hasNext()) {
			p = (Package)it.next(); // package in this module
			it2 = p.getInwardPackages().iterator(); // references to this

			while(it2.hasNext()) {
				p = (Package)it2.next(); // package referring to one of ours

				it3 = modules.keySet().iterator();

				while(it3.hasNext()) {
					m = (Module)modules.get(it3.next()); // other module
					if((m != this) && (m.containsPackage(p))) // found it?
						inwardSet.add(m);
				}
			}
		}

		return inwardSet;
	}

	/**
	 * List the packages in another specified module this module refers to
	 */
	public SortedSet getOutwardPackagesForModule(Module m) {
		Iterator it, it2;
		Package p;
		Set s;
//		SortedSet pset = new TreeSet(new Package.PackageComparator());
		SortedSet pset = new TreeSet();

		it = packages.iterator();

		while(it.hasNext()) {
			p = (Package)it.next(); // package in this module
			s = p.getOutwardPackages(); // set of packages it refers to

			it2 = s.iterator();
			while(it2.hasNext()) {
				p = (Package)it2.next(); // package in other module

				if(m.containsPackage(p))
					pset.add(p);
			}
		}

		return pset;
	}

	/**
	 * List the packages in another specified module which refer to this module
	 */
	public SortedSet getInwardPackagesForModule(Module m) {
		Iterator it, it2;
		Package p;
		Set s;
//		SortedSet pset = new TreeSet(new Package.PackageComparator());
		SortedSet pset = new TreeSet();

		it = packages.iterator();

		while(it.hasNext()) {
			p = (Package)it.next(); // package in this module
			s = p.getInwardPackages(); // set of packages referring to it

			it2 = s.iterator();
			while(it2.hasNext()) {
				p = (Package)it2.next(); // package in other module

				if(m.containsPackage(p))
					pset.add(p);
			}
		}

		return pset;
	}

	static class ModuleComparator implements Comparator, java.io.Serializable {
		public int compare(Object o1, Object o2) {
			Module m1 = (Module)o1;
			Module m2 = (Module)o2;

			return m1.getName().compareTo(m2.getName());
		}
	}
}