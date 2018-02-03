package intro2.ui.taglib;

import intro2.config.Project;
import intro2.database.Database;
import lahaina.runtime.State;

import java.util.Arrays;

public final class ModuleList extends AbstractList {
	public ModuleList(State state) {
		super(state);

		Project project = Database.getProject();
		Project.Module module = (Project.Module)state.getAttribute(TARGET);

		Project.Module[] children = module.getSubModules();
		Arrays.sort(children);

		list = children;
	}

	protected void setCurrentState(Object arg) {
		state.setAttribute(CURRENT, arg);
		state.setAttribute(HyperLink.HREF,((Project.Module)arg).getOutputFilename());
	}
}