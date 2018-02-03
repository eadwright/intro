package intro2.ui.taglib;

import intro2.database.Module;
import intro2.database.Package;
import lahaina.runtime.State;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

public final class ModuleUsageList extends AbstractList {
	public static final String THIS = "this";
	public static final String OTHER = "other";
	public static final String OUTWARD = "outward";
	public static final String LEFT = "left";
	public static final String RIGHT = "right";

	private Module module, other;
	private boolean outward;

	public ModuleUsageList(State state) {
		super(state);

		module = (Module)state.getAttribute(THIS);
		other = (Module)state.getAttribute(OTHER);
		outward = state.getBooleanAttribute(OUTWARD);

		list = process();
		position = 0;
	}

	private List process() {
		SortedSet allOurPkgs = module.getAllPackages();
		Iterator ourPkgs = allOurPkgs.iterator();

		Iterator otherPkgs;
		Package pkg, otherPkg;

		List al = new ArrayList();
		while(ourPkgs.hasNext()) {
			pkg = (Package)ourPkgs.next();

			if(outward)
				otherPkgs = pkg.getOutwardPackages().iterator();
			else
				otherPkgs = pkg.getInwardPackages().iterator();

			while(otherPkgs.hasNext()) {
				otherPkg = (Package)otherPkgs.next();

				if(!allOurPkgs.contains(otherPkg))
					al.add(new Package[] { pkg, otherPkg });
			}
		}

		return al;
	}

	protected void setCurrentState(Object current) {
		Package[] p = (Package[])current;

		state.setAttribute("left", p[0]);
		state.setAttribute("right", p[1]);

		if(outward)
			state.setAttribute(HyperLink.HREF, Package.getUsesFilename(p[0], p[1]));
		else
			state.setAttribute(HyperLink.HREF, Package.getUsedByFilename(p[0], p[1]));
	}
}