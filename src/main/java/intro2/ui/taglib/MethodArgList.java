package intro2.ui.taglib;

import intro2.output.Type;
import lahaina.runtime.State;

public final class MethodArgList extends AbstractList {
	public static final String TYPE = "type";
	public static final String COMMA = "comma";

	public MethodArgList(State state) {
		super(state);

		list = state.getAttribute(MethodList.ARGS);
	}

	public void setCurrentState(Object arg) {
		Type type = (Type)arg;
		state.setAttribute(TYPE, type);
		state.conditionalSetAttribute(!type.isPrimative(), HyperLink.HREF, type.getHref());
		state.conditionalSetAttribute(hasNext(), COMMA, ", ");
	}
}