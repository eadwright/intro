package intro2.ui.taglib;

import lahaina.runtime.State;

public final class BAnd extends And {
	public BAnd(State state) {
		super(state);
	}

	protected boolean getFlag(String varName) {
		return state.getBooleanAttribute(varName);
	}
}