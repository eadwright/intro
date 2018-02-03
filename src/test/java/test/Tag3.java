package test;

import lahaina.runtime.State;
import lahaina.runtime.Writer;
import lahaina.tag.NonEmptyTag;

public class Tag3 implements NonEmptyTag {
	public Tag3(State state) {
	}

	public boolean process(Writer out) {
		return false;
	}
}