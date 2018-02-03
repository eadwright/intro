package lahaina.tag;

import lahaina.runtime.Page;
import lahaina.runtime.State;
import lahaina.runtime.Writer;

public final class Include implements Tag {
	public static final String PAGECLASSNAME = "pageClass";

	private static final Class[] paramTypes = new Class[] { State.class };

	private State state;

	public Include(State state) {
		this.state = state;
	}

	public boolean process(Writer out) throws Exception {
		String clzName = state.getStringAttribute(PAGECLASSNAME);

		Page second = getPageInstance(clzName);

		second.process(out, state);

		return false; // never repeat
	}

	private Page getPageInstance(String clzName) throws Exception {
		Class clz = Class.forName(clzName);

		if(!Page.class.isAssignableFrom(clz))
			throw new ClassCastException("Class "+clzName+" isn't a Page");

		return (Page)clz.newInstance();
	}
}