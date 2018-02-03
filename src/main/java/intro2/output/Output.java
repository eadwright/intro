package intro2.output;

import intro2.config.Project;
import intro2.database.ClassInfo;
import intro2.database.Database;
import intro2.database.Module;
import intro2.database.Package;
import intro2.ui.*;
import intro2.ui.taglib.*;
import lahaina.runtime.Page;
import lahaina.runtime.State;
import lahaina.runtime.Writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

public final class Output {
	private static Writer writer;

	private Output() {}

	public static void writeHtml() throws Exception {
		System.out.println("writing html");

		long time = System.currentTimeMillis();

		Class clz = State.class; // prevent class gc

		init();

		writeProjectPage();
		writeModulePages();
		writeModuleUsagePages();
		writePackagePages();
		writePackageUsagePages();
		writeClassPages();

		time = System.currentTimeMillis() - time;

		System.out.println("done, writing html time " +time+" ms");
	}

	private static void init() {
		writer = new Writer();
		String outputPath = Database.getProject().getOutputPath();
		createDir(outputPath);
		createDir(outputPath+"/module");
		createDir(outputPath+"/package");
		createDir(outputPath+"/class");
	}

	private static void createDir(String name) {
		File f = new File(name);
		f.mkdirs();

		deleteStuff(f, false);
	}

	private static void deleteStuff(File target, boolean doSelf) {
		if(target.isDirectory()) {
			File[] files = target.listFiles();
			if(files != null)
				for(int i=0;i<files.length;i++)
					deleteStuff(files[i], true);
		}

		if(doSelf) {
			String name = target.getName();
			if((name.indexOf(".html") != -1) || (name.indexOf(".css") != -1))
				target.delete();
		}
	}

	private static void writeProjectPage() throws Exception {
		Project project = Database.getProject();

		Page page = new IndexPage();
		State state = getInitialState(0);
		state.setAttribute(NavbarInit.PAGEID, Constants.ALL_MODULES);
		state.setAttribute(Constants.TITLE, "Project "+project.getName());
		state.setAttribute("this", project);
		state.remove("allModsLink");

		doWritePage(page, state, "index.html");

		page = new CSSPage();
		doWritePage(page, state, "stylesheet.css");
	}

	private static void writeModulePages() throws Exception {
		Project project = Database.getProject();

		Project.Module root = project.getModule(Project.ROOT_MODULE_NAME);

		State state = getInitialState(1);
		state.setAttribute(NavbarInit.PAGEID, Constants.MODULE);
		state.setAttribute("inout", true); // show inward/outward on KeySubPage

		ModulePage page = new ModulePage();
		doWriteModulePage(page, null, null, root, state);
	}

	private static void doWriteModulePage(ModulePage page, Project.Module prev, Project.Module next,
						Project.Module module, State state) throws Exception {

		state.setAttribute("previous", prev);
		state.setAttribute("next", next);
		state.setAttribute("this", module);
		state.setAttribute("real", Database.getModule(module.getName()));
		state.setAttribute(Constants.TITLE, "Module "+module.getName());
		state.setAttribute(ModuleTree.TARGET, module);
		state.conditionalSetAttribute(module.getSubModules() != null, "subModules", "#submodules");
		state.conditionalSetAttribute(module.getPackages() != null, "packages", "#packages");

		state.conditionalSetAttribute(ModuleUsageSummaryList.hasUsage(module), "modUsage", "#module_usage");

		String filename = module.getOutputFilename();
		doWritePage(page, state, filename);

		Project.Module[] children = module.getSubModules();
		if(children != null) {
			Arrays.sort(children);

			Project.Module current = null;
			for(int i=0;i<children.length;i++) {
				prev = current;
				current = children[i];
				if(i<children.length-1)
					next = children[i+1];
				else
					next = null;

				doWriteModulePage(page, prev, next, current, state);
			}
		}
	}

	private static void writeModuleUsagePages() throws Exception {
		State state = getInitialState(3);
		state.setAttribute("inout", false); // don't show inward/outward on KeySubPage

		ModuleUsesPage usesPage = new ModuleUsesPage();
		ModuleUsedByPage usedByPage = new ModuleUsedByPage();
		Project project = Database.getProject();

		SortedMap modules = project.getModules();

		Module module, other;
		Project.Module pm, po;
		Set set;
		Iterator it, it2;
		String filename;
		File parent;

		it = modules.keySet().iterator();
		while(it.hasNext()) {
			module = Database.getModule((String)it.next());
			pm = project.getModule(module.getName());
			state.setAttribute(ModuleUsageList.THIS, module);

			set = module.getOutwardModules();

			if(set != null) {
				state.setAttribute(NavbarInit.PAGEID, Constants.MODULE_USES_OUTWARD);
				state.setAttribute(ModuleUsageList.OUTWARD, true);

				it2 = set.iterator();
				while(it2.hasNext()) {
					other = (Module)it2.next();
					po = project.getModule(other.getName());

					state.setAttribute(ModuleUsageList.OTHER, other);
					state.setAttribute(Constants.TITLE, "Module "+module.getName()+" uses "+other.getName());

					filename = Project.Module.getModuleUsesFilename(pm.getNo(), po.getNo());

					doWritePage(usesPage, state, filename, true);
				}
			}

			set = module.getInwardModules();

			if(set != null) {
				state.setAttribute(NavbarInit.PAGEID, Constants.MODULE_USES_INWARD);
				state.setAttribute(ModuleUsageList.OUTWARD, false);

				it2 = set.iterator();
				while(it2.hasNext()) {
					other = (Module)it2.next();
					po = project.getModule(other.getName());

					state.setAttribute(ModuleUsageList.OTHER, other);
					state.setAttribute(Constants.TITLE, "Module "+module.getName()+" used by "+other.getName());

					filename = Project.Module.getModuleUsedByFilename(pm.getNo(), po.getNo());

					doWritePage(usedByPage, state, filename, true);
				}
			}
		}
	}

	private static void writePackageUsagePages() throws Exception {
		State state = getInitialState(3);
		state.setAttribute("inout", false); // don't show inward/outward on KeySubPage

		PackageUsesPage usesPage = new PackageUsesPage();
		PackageUsedByPage usedByPage = new PackageUsedByPage();
		Project project = Database.getProject();

		Module root = Database.getModule(Project.ROOT_MODULE_NAME);

		SortedSet packages = root.getAllPackages();

		Package pkg, other;
		Set set;
		Iterator it, it2;
		String filename;
		File parent;

		it = packages.iterator();
		while(it.hasNext()) {
			pkg = (Package)it.next();

			state.setAttribute(PackageUsageList.THIS, pkg);
			set = pkg.getOutwardPackages();

			if(set != null) {
				state.setAttribute(NavbarInit.PAGEID, Constants.PACKAGE_USES_OUTWARD);
				state.setAttribute(PackageUsageList.OUTWARD, true);

				it2 = set.iterator();
				while(it2.hasNext()) {
					other = (Package)it2.next();

					state.setAttribute(PackageUsageList.OTHER, other);
					state.setAttribute(Constants.TITLE, "Package "+pkg.getDisplayName()+" uses "+other.getDisplayName());

					filename = Package.getUsesFilename(pkg, other);

					doWritePage(usesPage, state, filename, true);
				}
			}

/*			set = pkg.getInwardPackages();

			if(set != null) {
				state.setAttribute(NavbarInit.PAGEID, Constants.MODULE_USES_INWARD);
				state.setAttribute(PackageUsageList.OUTWARD, false);

				it2 = set.iterator();
				while(it2.hasNext()) {
					other = (Package)it2.next();

					state.setAttribute(PackageUsageList.OTHER, other);
					state.setAttribute(Constants.TITLE, "Package "+pkg.getDisplayName()+" used by "+other.getDisplayName());

					filename = Package.getUsedByFilename(pkg, other);

					doWritePage(usedByPage, state, filename, true);
				}
			}*/ // THIS BLOCK ALWAYS COMMENTED
		}
	}

	private static void writePackagePages() throws Exception {
		Set set = Database.getModule(Project.ROOT_MODULE_NAME).getAllPackages();

		Package[] pkgs = new Package[set.size()];
		Iterator it = set.iterator();
		int i=0;
		while(it.hasNext()) {
			pkgs[i] = (Package)it.next();
			i++;
		}

		State state = getInitialState(2);
		state.setAttribute(NavbarInit.PAGEID, Constants.PACKAGE);
//		state.setAttribute("inout", true); // show inward/outward on KeySubPage

		PackagePage page = new PackagePage();
		for(i=0;i<pkgs.length;i++)
			doWritePackagePage(page, pkgs, i, state);
	}

	private static void doWritePackagePage(PackagePage page, Package[] pkgs, int i,
										State state) throws Exception {

		if(i>0)
			state.setAttribute("previous", pkgs[i-1]);

		state.setAttribute("this", pkgs[i]);

		if(i<pkgs.length-1)
			state.setAttribute("next", pkgs[i+1]);

		state.setAttribute(Constants.TITLE, "Package "+pkgs[i].getDisplayName());
		state.setAttribute(PackageTree.TARGET, pkgs[i]);
		state.conditionalSetAttribute(Database.hasSubPackages(pkgs[i]), "subPackages", "#subpackages");
		state.conditionalSetAttribute(pkgs[i].getAllInterfaceOnlyIDs().getSize() > 0, "interfaces", "#interfaces");
		state.conditionalSetAttribute(pkgs[i].getAllClassOnlyIDs().getSize() > 0, "classes", "#classes");

		state.conditionalSetAttribute(PackageUsageSummaryList.hasUsage(pkgs[i]), "pkgUsage", "#package_usage");

		String filename = pkgs[i].getOutputFilename();

		doWritePage(page, state, filename, true);
	}

	private static void writeClassPages() throws Exception {
		int[] classIDs = Database.getAllClassIDsInOrder();

		State state = getInitialState(2);
		state.setAttribute(NavbarInit.PAGEID, Constants.CLASS);
		state.setAttribute("inout", true); // show inward/outward on KeySubPage

		ClassPage page = new ClassPage();
		for(int i=0;i<classIDs.length;i++)
			doWriteClassPage(page, classIDs, i, state);
	}

	private static void doWriteClassPage(ClassPage page, int[] classIDs, int i,
										State state) throws Exception {

		ClassInfo prev = null, next = null, current;

		current = Database.getClassInfo(classIDs[i]);
		if(i>0)
			prev = Database.getClassInfo(classIDs[i-1]);
		if(i<classIDs.length-1)
			next = Database.getClassInfo(classIDs[i+1]);

		state.setAttribute("previous", prev);
		state.setAttribute("next", next);
		state.setAttribute("this", current);

		if(current.isInterface())
			state.setAttribute(Constants.TITLE, "Interface "+current.getDisplayName());
		else
			state.setAttribute(Constants.TITLE, "Class "+current.getDisplayName());

		state.setAttribute(ClassTree.TARGET, current);

		Package pkg = Database.getPackage(current.getPackage());
		state.setAttribute("packageLink", pkg.getOutputFilename());
		state.conditionalSetAttribute(current.getFieldOffsets() != null, "fields", "#fields");
		state.conditionalSetAttribute(current.getMethodOffsets() != null, "methods", "#methods");
		state.conditionalSetAttribute(current.hasInnerClasses(), "innerClasses", "#innerClasses");

		doWritePage(page, state, current.getOutputFilename(), true);
	}

	private static State getInitialState(int levels) {
		State state = new State();

		Project.Module root = Database.getProject().getModule("root");

		state.setAttribute(Dots.LEVELS, levels);
		state.setAttribute("allModsLink", "index.html");
		state.setAttribute("allPkgsLink", root.getOutputFilename("#packages"));
		state.setAttribute("allClzsLink", "class/index.html");
		state.setAttribute(Constants.PROJECTNAME, Database.getProject().getName());
		return state;
	}

	private static void doWritePage(Page page, State state, String filename) throws Exception {
		doWritePage(page, state, filename, false);
	}

	private static void doWritePage(Page page, State state, String filename, boolean mkdirs) throws Exception {
		filename = Database.getProject().getOutputPath()+'/'+filename;

		if(mkdirs)
			doMkdirs(filename);

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filename));
			writer.setInternalWriter(bw);

			page.process(writer, state);
		} finally {
			if(bw != null)
				bw.close();
		}
	}

	private static void doMkdirs(String filename) {
		File parent = new File(filename).getParentFile();
		if(!parent.exists())
			parent.mkdirs();
	}
}