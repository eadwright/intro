package intro2.ui.taglib;

import lahaina.runtime.State;
import lahaina.runtime.Writer;
import lahaina.tag.NonEmptyTag;

abstract class LogicGate implements NonEmptyTag {
	private final static String FLAG = "flag";

	protected boolean logicValue;
	protected State state;
	private boolean part1 = true;

	protected LogicGate() {}

	protected abstract void initLogicValue();

	protected abstract void updateLogicValue(boolean b);

	protected abstract boolean getFlag(String varName);

	protected void resetIfNoFlags() {
		logicValue = false;
	}

	public LogicGate(State state) {
		this.state = state;
		int index = 0;
		String flagName, varName;

		initLogicValue();
		while(true) {
			flagName = FLAG+index;
			varName = state.getStringAttribute(flagName);

			if(varName == null)
				break;

			state.remove(flagName);

			updateLogicValue(getFlag(varName));
//			updateLogicValue(state.exists(varName));

			index++;
		}

		if(index == 0)
			resetIfNoFlags();
	}

	public boolean process(Writer out) {
		boolean result = part1 && logicValue;

		state.conditionalSetAttribute(!logicValue, Else.ELSE, true);

		part1 = false;
		return result;
	}
}