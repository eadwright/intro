package intro2.ui.taglib;

import lahaina.runtime.State;
import lahaina.runtime.Writer;
import lahaina.tag.NonEmptyTag;

import java.io.IOException;

abstract class AbstractTree implements NonEmptyTag {
	public static final String CURRENT = "current";
	public static final String TARGET = "target";
	public static final String UP = "up";
	public static final String DOWN = "down";

	protected State state;
	protected Node current;
	private boolean first = true;

	protected AbstractTree(State state) {
		this.state = state;
	}

	protected void setCurrentState() {
		state.setAttribute(UP, current.up);
		state.setAttribute(DOWN, current.down);
	}

	public final boolean process(Writer out) throws IOException {
		if(current == null)
			return false;

		if(first)
			first = false;
		else
			current = current.next;

		if(current != null) {
			setCurrentState();
			return true;
		} else {
			out.write("\n");
			return false;
		}
	}

	protected static class Node {
		public Object now;
		public Node next;
		public int up = 0; // for tree use
		public int down = 0; // for tree use

		public Node(Object now) {
			this.now = now;
		}
	}
}