package intro2.ui.taglib;

import intro2.config.Project;
import intro2.database.Database;
import lahaina.runtime.State;

import java.util.Arrays;

public final class LibraryList extends AbstractList {
	public LibraryList(State state) {
		super(state);

		Project project = Database.getProject();

		Project.Jar[] jars = project.getLibraries();
		Arrays.sort(jars);

		list = jars;
	}

	protected void setCurrentState(Object arg) {
		state.setAttribute(CURRENT, arg);
	}
}