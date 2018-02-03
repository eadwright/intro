package intro2.ui.taglib;

import intro2.config.Project;
import intro2.database.Database;
import intro2.database.Package;
import lahaina.runtime.State;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedSet;

public final class PackageUsageSummaryList extends AbstractList {
	public static final String OUTWARDREF = "outwardref";
	public static final String INWARDREF = "inwardref";
	public static final String NO = "no";

	public PackageUsageSummaryList(State state) {
		super(state);

		SortedSet packages = Database.getModule(Project.ROOT_MODULE_NAME).getAllPackages();

		Iterator it = packages.iterator();

		if(!it.hasNext())
			return;

		Package pkg;
		ArrayList packages2 = new ArrayList();

		while(it.hasNext()) {
			pkg = (Package)it.next();

			if(pkg.usesOthers() || pkg.usedByOthers())
				packages2.add(pkg);
		}

		list = packages2.iterator();
	}

	public static boolean hasUsage(Package pkg) {
		return pkg.getOutwardPackages().size() + pkg.getInwardPackages().size() > 0;
	}

	protected void setCurrentState(Object arg) {
		Package pkg = (Package)arg;

		state.setAttribute(CURRENT, pkg);
		state.setAttribute(HyperLink.HREF, pkg.getOutputFilename());

		state.conditionalSetAttribute(pkg.usesOthers(), OUTWARDREF, pkg.getOutputFilename("_uses"));
		state.conditionalSetAttribute(pkg.usedByOthers(), INWARDREF, pkg.getOutputFilename("_usedby"));
	}
}