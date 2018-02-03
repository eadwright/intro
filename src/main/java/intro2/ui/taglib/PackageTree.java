package intro2.ui.taglib;

import intro2.database.Database;
import intro2.database.Package;
import lahaina.runtime.State;

public final class PackageTree extends AbstractTree {
	public static final String NAME = "name";

	private boolean doingDirect = false;

	public PackageTree(State state) {
		super(state);

		if(state.exists(TARGET))
			calculateDirect();
		else
			throw new RuntimeException("not implemented yet");
//			calculateAll();
	}

	protected void setCurrentState() {
		super.setCurrentState();

		String name = (String)current.now;

		if(Database.containsPackage(name)) // do not create new blank Package instances!
			setState(state, Database.getPackage(name));
		else
			setState(state, name);

		if(doingDirect && (current.next == null))
			state.remove(HyperLink.HREF);
	}

	private void calculateDirect() {
		doingDirect = true;

		String name = ((Package)state.getAttribute(TARGET)).getName();

		Node n = null, n2, last = null;
		int levelCount = 0;

		do {
			levelCount++;

			n2 = new Node(name);
			n2.up = 1;

			if(n != null)
				n2.next = n;
			else
				last = n2;
			n = n2;

			name = getParentPackage(name);
		} while (name != null);

		last.down = levelCount;

		current = n;
	}

/*	private void calculateAll() {
		Project project = (Project)state.getAttribute(Constants.PROJECT);
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
	}*/

	static String getParentPackage(String name) {
		int i = name.lastIndexOf('/');
		if(i>0)
			return name.substring(0,i);
		else
			return null;
	}

	private static void setState(State state, Package pkg) {
		state.setAttribute(HyperLink.HREF,pkg.getOutputFilename());
		state.setAttribute(CURRENT, pkg);
		state.setAttribute(NAME,pkg.getDisplayName());
	}

	private static void setState(State state, String name) {
		state.remove(HyperLink.HREF);
		state.setAttribute(NAME, Package.getDisplayName(name));
		state.remove(CURRENT);
	}
}