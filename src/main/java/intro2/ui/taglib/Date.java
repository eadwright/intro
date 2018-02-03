package intro2.ui.taglib;

import lahaina.runtime.State;
import lahaina.runtime.Writer;
import lahaina.tag.Tag;

import java.text.DateFormat;

public final class Date implements Tag {
	public Date(State state) {}

	public boolean process(Writer out) throws java.io.IOException {
		out.write(DateFormat.getDateTimeInstance().format(new java.util.Date()));
		return false;
	}
}