package intro2.ui.taglib;

import lahaina.runtime.State;
import lahaina.runtime.Writer;
import lahaina.tag.Tag;

public final class LookupAssign implements Tag {
	public final static String FROM = "from";
	public final static String TO = "to";

	public LookupAssign(State state) {
		String from = state.getStringAttribute(FROM);
		String to = state.getStringAttribute(TO);

		if(to != null)
			state.remove(to);

		if((from != null) && (to != null)) {
			String second = state.getStringAttribute(from);

			if(second != null)
				state.setAttribute(to, state.getAttribute(second));
		}
	}

	public boolean process(Writer out) {
		return false;
	}
}