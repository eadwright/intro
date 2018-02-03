package intro2.ui.taglib;

import lahaina.runtime.State;
import lahaina.runtime.Writer;
import lahaina.tag.ContentProcessTag;

// Nothing gets processed inside an <ignore> tag
public final class Ignore implements ContentProcessTag {
	private static boolean warningDisplayed = false;

	public Ignore(State state) {}

	public void setContent(String content) {}

	public boolean process(Writer out) {
		if(!warningDisplayed) {
			System.err.println("Warning, using <ignore> tag");
			warningDisplayed = true;
		}
		return false;
	}
}