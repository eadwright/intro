package intro2.ui.taglib;

import lahaina.runtime.State;

public class Or extends LogicGate {
	public Or(State state) {
		super(state);
	}

	protected boolean getFlag(String varName) {
		return state.exists(varName);
	}

	protected void initLogicValue() {
		logicValue = false;
	}

	protected void updateLogicValue(boolean b) {
		logicValue |= b;
	}
}