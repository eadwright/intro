package intro2.ui.taglib;

import intro2.config.Project;
import intro2.database.Database;
import intro2.database.Module;
import intro2.database.Package;
import lahaina.runtime.State;

import java.util.SortedSet;

public final class PackageList extends AbstractList {
	public PackageList(State state) {
		super(state);

		Object target = state.getAttribute(TARGET);
		SortedSet packages;

		if(target instanceof Project.Module) {
			Project.Module m = (Project.Module)target;
			Module module = Database.getModule(m.getName());
			packages = module.getAllPackages();
		} else {
			packages = Database.getAllSubPackages((Package)target);
		}

		list = packages.iterator();
	}

	protected void setCurrentState(Object arg) {
		state.setAttribute(CURRENT, arg);
		state.setAttribute(HyperLink.HREF,((Package)arg).getOutputFilename());
	}
}