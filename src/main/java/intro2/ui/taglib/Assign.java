package intro2.ui.taglib;

import lahaina.runtime.State;
import lahaina.runtime.Writer;
import lahaina.tag.Tag;

public final class Assign implements Tag {
	public final static String FROM = "from";
	public final static String TO = "to";

	public Assign(State state) {
		String from = state.getStringAttribute(FROM);
		String to = state.getStringAttribute(TO);

		if((from != null) && (to != null)) {
			Object obj = state.getAttribute(from);

//			System.err.println("from: "+from+" to "+to+" obj "+obj);

			state.setAttribute(to, obj);
		}
	}

	public boolean process(Writer out) {
		return false;
	}
}