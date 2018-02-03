package intro2.ui.taglib;

import lahaina.runtime.State;

public final class BOr extends Or {
	public BOr(State state) {
		super(state);
	}

	protected boolean getFlag(String varName) {
		return state.getBooleanAttribute(varName);
	}
}