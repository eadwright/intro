package intro2.ui.taglib;

import intro2.database.ClassInfo;
import intro2.database.Database;
import lahaina.runtime.State;

public final class ClassTree extends AbstractTree {
	public static final String NAME = "name";

	private static final String OBJECT = "java/lang/Object";

	private boolean doingDirect = false;

	public ClassTree(State state) throws Database.DataProcessingException {
		super(state);

		if(state.exists(TARGET))
			calculateDirect();
		else
			throw new RuntimeException("not implemented yet");
//			calculateAll();
	}

	protected void setCurrentState() {
		super.setCurrentState();

		if(current.now instanceof String)
			setState(state, (String)current.now);
		else
			setState(state, (ClassInfo)current.now);

		if(doingDirect && (current.next == null))
			state.remove(HyperLink.HREF);
	}

	private void calculateDirect() throws Database.DataProcessingException {
		doingDirect = true;

		ClassInfo ci = (ClassInfo)state.getAttribute(TARGET);

		boolean doingInterface = ci.isInterface();

		String name = ci.getName();
		Node n = null, n2, last = null;
		int levelCount = 0;
		int id;

		here:
		do {
			levelCount++;

			if(ci != null)
				n2 = new Node(ci);
			else
				n2 = new Node(name);
			n2.up = 1;

			if(n != null)
				n2.next = n;
			else
				last = n2;

			n = n2;

			if(ci == null) {
				if(doingInterface || name.equals(OBJECT))
					break here;
				else
					name = OBJECT;
			} else {
				if(ci.getName().equals(OBJECT))
					break here;
				else {
					id = ci.getSuperClassID();
					name = Database.getClassName(id);
					ci = Database.getClassInfo(id);
				}
			}
		} while (true);

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

	private static void setState(State state, ClassInfo ci) {
		state.setAttribute(HyperLink.HREF, ci.getOutputFilename());
		state.setAttribute(CURRENT, ci);
		state.setAttribute(NAME,ci.getDisplayName());
	}

	private static void setState(State state, String name) {
		state.remove(HyperLink.HREF);
		state.setAttribute(NAME, ClassInfo.getDisplayName(name));
		state.remove(CURRENT);
	}
}