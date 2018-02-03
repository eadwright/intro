package intro2.ui.taglib;

import intro2.config.Project;
import intro2.database.Database;
import intro2.database.Module;
import lahaina.runtime.State;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;

public final class ModuleUsageSummaryList extends AbstractList {
	public static final String OUTWARDREF = "outwardref";
	public static final String INWARDREF = "inwardref";

	private Project.Module ourModule;
	private Module ourRealModule;

	public ModuleUsageSummaryList(State state) {
		super(state);

		ourModule = (Project.Module)state.getAttribute(TARGET);
		ourRealModule = Database.getModule(ourModule.getName());

		list = getModulesUsed(ourModule);
		position = 0;
	}

	private static List getModulesUsed(Project.Module ourModule) {
		Module ourRealModule = Database.getModule(ourModule.getName());
		Project project = Database.getProject();

		SortedMap map = project.getModules();
		Iterator it = map.keySet().iterator();

		Project.Module other;
		Module realOther;
		String name;

		ArrayList list = new ArrayList();
		while(it.hasNext()) {
			other = (Project.Module)map.get(it.next());

			if(!ourModule.hasSubModule(other)) {
				name = other.getName();
				realOther = Database.getModule(name);

				if(ourRealModule.uses(realOther) ||
						ourRealModule.usedBy(realOther))
					list.add(project.getModule(name));
			}
		}

		return list;
	}

	public static boolean hasUsage(Project.Module ourModule) {
		return getModulesUsed(ourModule).size() > 0;
	}

	protected void setCurrentState(Object current) {
		Project.Module module = (Project.Module)current;

		state.setAttribute(CURRENT, module);
		state.setAttribute(HyperLink.HREF, module.getOutputFilename());

		Module realModule = Database.getModule(module.getName());

		state.conditionalSetAttribute(ourRealModule.uses(realModule), OUTWARDREF, Project.Module.getModuleUsesFilename(ourModule.getNo(), module.getNo()));
		state.conditionalSetAttribute(ourRealModule.usedBy(realModule), INWARDREF, Project.Module.getModuleUsedByFilename(ourModule.getNo(), module.getNo()));
	}
}