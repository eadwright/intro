package intro2.ui.taglib;

import lahaina.runtime.State;
import lahaina.runtime.Writer;
import lahaina.tag.NonEmptyTag;

public final class Else implements NonEmptyTag {
	public final static String ELSE = "else";

	private boolean proceed;
	private boolean part1 = true;

	public Else(State state) {
		proceed = state.getBooleanAttribute(ELSE); // was exists()
		state.remove(ELSE);
	}

	public boolean process(Writer out) {
		boolean result = part1 && proceed;

		part1 = false;
		return result;
	}
}