package intro2.ui.taglib;

import lahaina.runtime.State;
import lahaina.runtime.Writer;
import lahaina.tag.Tag;

public final class Dots implements Tag {
	public static final String LEVELS = "levels";

	private State state;

	public Dots(State state) {
		this.state = state;
	}

	public boolean process(Writer out) throws java.io.IOException {
		printDots(out, state);

		return false;
	}

	static void printDots(Writer out, State state) throws java.io.IOException {
		int levels = state.getIntAttribute(LEVELS);

		int count = 0;
		while(levels > count) {
			out.write("../");
			count++;
//			if(levels > count)
//				out.write("/");
		}
	}
}