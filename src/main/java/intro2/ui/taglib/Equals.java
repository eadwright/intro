package intro2.ui.taglib;

import lahaina.runtime.State;
import lahaina.runtime.Writer;
import lahaina.tag.NonEmptyTag;

public final class Equals implements NonEmptyTag {
	public final static String FLAGNAME = "flag";
	public final static String VALUENAME = "value";

	private boolean match = false;
	private boolean part1 = true;
	private State state;

	public Equals(State state) {
		this.state = state;

		String value = state.getStringAttribute(state.getStringAttribute(FLAGNAME));
		String testValue = state.getStringAttribute(VALUENAME);

		if((value != null) && (testValue != null))
			match = value.equalsIgnoreCase(testValue);
	}

	public boolean process(Writer out) {
		boolean result = part1 && match;

		if(!match)
			state.setAttribute(Else.ELSE, true);

		part1 = false;
		return result;
	}
}