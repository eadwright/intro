package intro2.ui.taglib;

import lahaina.runtime.State;
import lahaina.runtime.Writer;
import lahaina.tag.NonEmptyTag;

public final class Loop implements NonEmptyTag {
	public final static String COUNTERNAME = "counterName";

	private int count = 0;

	public Loop(State state) {
		if(state.exists(COUNTERNAME)) {
			String key = state.getStringAttribute(COUNTERNAME);

			if(state.exists(key))
				count = state.getIntAttribute(key);
		}
	}

	public boolean process(Writer out) {
		return count-- > 0;
	}
}