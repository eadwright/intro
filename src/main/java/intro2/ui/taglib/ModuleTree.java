package intro2.ui.taglib;

import intro2.config.Project;
import intro2.database.Database;
import intro2.database.Module;
import lahaina.runtime.State;

import java.util.Arrays;

public final class ModuleTree extends AbstractTree {
	public static final String REAL = "real";

	private boolean doingDirect = false;

	public ModuleTree(State state) {
		super(state);

		if(state.exists(TARGET))
			calculateDirect();
		else
			calculateAll();
	}

	protected void setCurrentState() {
		super.setCurrentState();

		Project.Module module = (Project.Module)current.now;

		setState(state, module);

		if(doingDirect && (current.next == null))
			state.remove(HyperLink.HREF);
	}

	private void calculateDirect() {
		doingDirect = true;

		Project project = Database.getProject();
		Project.Module module = (Project.Module)state.getAttribute(TARGET);

		Node n = null, n2, last = null;
		int levelCount = 0;

		do {
			levelCount++;

			n2 = new Node(module);
			n2.up = 1;

			if(n != null)
				n2.next = n;
			else
				last = n2;
			n = n2;

			module = module.getParent();
		} while (module != null);

		last.down = levelCount;

		current = n;
	}

	private void calculateAll() {
		Project project = Database.getProject();
		Project.Module root = project.getModule(Project.ROOT_MODULE_NAME);

		current = new Node(root);
		current.up = 1;

		Node last = doCalcAllRecursive(current);
		last.down++;
	}

	private Node doCalcAllRecursive(Node node) {
		Project.Module module = (Project.Module)node.now;
		Project.Module[] sub = module.getSubModules();

		if(sub != null) {
			node.up = 1;
			Arrays.sort(sub);

			Node last;

			for(int i=0;i<sub.length;i++) {
				node.next = new Node(sub[i]);
				node = node.next;
				if(i == 0)
					node.up = 1;

				node = doCalcAllRecursive(node);
			}
			node.down++;
		}

		return node;
	}

	private static void setState(State state, Project.Module module) {
		state.setAttribute(HyperLink.HREF,module.getOutputFilename());
		state.setAttribute(CURRENT, module);

		Module realModule = Database.getModule(module.getName());

		state.setAttribute(REAL, realModule);
	}
}