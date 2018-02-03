package intro2.ui.taglib;

import lahaina.runtime.State;
import lahaina.runtime.Writer;
import lahaina.tag.NonEmptyTag;

public final class Not implements NonEmptyTag {
	public final static String FLAGNAME = "flag";

	private boolean includeContent;
	private boolean part1 = true;
	private State state;

	public Not(State state) {
		this.state = state;
		includeContent = !state.getBooleanAttribute(state.getStringAttribute(FLAGNAME));
	}

	public boolean process(Writer out) {
		boolean result = part1 && includeContent;

		if(!result)
			state.conditionalSetAttribute(!includeContent, Else.ELSE, true);

		part1 = false;
		return result;
	}
}