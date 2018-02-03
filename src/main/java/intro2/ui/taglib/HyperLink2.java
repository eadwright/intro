package intro2.ui.taglib;

import lahaina.runtime.State;

public final class HyperLink2 extends HyperLink {
	public HyperLink2(State state) {
		super(state);
	}

	protected boolean showNonLinks() {
		return false;
	}
}