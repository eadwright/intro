package intro2.ui.taglib;

import lahaina.runtime.State;
import lahaina.runtime.Writer;
import lahaina.tag.NonEmptyTag;

public final class NotExists implements NonEmptyTag {
	public final static String FLAGNAME = "flag";

	private boolean flagExists;
	private boolean part1 = true;
	private State state;

	public NotExists(State state) {
		this.state = state;

		flagExists = state.exists(state.getStringAttribute(FLAGNAME));
	}

	public boolean process(Writer out) {
		boolean result = part1 && !flagExists;

		if(!result)
			state.conditionalSetAttribute(flagExists, Else.ELSE, true);

		part1 = false;
		return result;
	}
}