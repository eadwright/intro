package intro2.ui.taglib;

import lahaina.runtime.State;

public class And extends LogicGate {
	public And(State state) {
		super(state);
	}

	protected boolean getFlag(String varName) {
		return state.exists(varName);
	}

	protected void initLogicValue() {
		logicValue = true;
	}

	protected void updateLogicValue(boolean b) {
		logicValue &= b;
	}
}