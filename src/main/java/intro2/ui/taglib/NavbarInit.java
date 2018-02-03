package intro2.ui.taglib;

import lahaina.runtime.State;
import lahaina.runtime.Writer;
import lahaina.tag.Tag;

public final class NavbarInit implements Tag {
	public static final String PROJECT = "revProject";
	public static final String MODULE = "revModule";
	public static final String PACKAGE = "revPackage";
	public static final String CLASS = "revClass";
	public static final String USES = "revUses";
	public static final String USEDBY = "revUsedBy";
	public static final String BYTECODE = "revBytecode";

	public static final String PAGEID = "pageId";

	public NavbarInit(State state) {
		int pid = state.getIntAttribute(PAGEID);

		switch(pid) {
			case Constants.ALL_MODULES:
				state.setAttribute(PROJECT, state);
				break;

			case Constants.MODULE:
				state.setAttribute(MODULE, state);
				break;

			case Constants.PACKAGE:
			case Constants.ALL_PACKAGES:
				state.setAttribute(PACKAGE, state);
				break;

			case Constants.CLASS:
			case Constants.ALL_CLASSES:
				state.setAttribute(CLASS, state);
				break;

			case Constants.MODULE_USES_OUTWARD:
			case Constants.PACKAGE_USES_OUTWARD:
			case Constants.PACKAGE_USES_OUTWARD_COMPLETE:
			case Constants.CLASS_USES_OUTWARD:
			case Constants.CLASS_USES_OUTWARD_SELFREF:
				state.setAttribute(USES, state);
				break;

			case Constants.MODULE_USES_INWARD:
			case Constants.PACKAGE_USES_INWARD:
			case Constants.PACKAGE_USES_INWARD_COMPLETE:
			case Constants.CLASS_USES_INWARD:
			case Constants.CLASS_USES_INWARD_SELFREF:
				state.setAttribute(USEDBY, state);
				break;

			case Constants.BYTECODE:
				state.setAttribute(BYTECODE, state);
				break;

			default:
				throw new RuntimeException("bad page id "+pid);
		}
	}

	public boolean process(Writer out) throws Exception {
		return false;
	}
}