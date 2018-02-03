package intro2.ui.taglib;

import intro2.database.ClassInfo;
import intro2.database.Database;
import lahaina.runtime.State;
import lahaina.runtime.Writer;
import lahaina.tag.Tag;

public final class GetClass implements Tag {
	public final static String ID = "id";
	public final static String CI = "ci";
	public final static String NAME = "displayName";

	public GetClass(State state) {
		String idVar = state.getStringAttribute(ID);

		if((idVar == null) || (!state.exists(idVar))) {
			state.remove(HyperLink.HREF);
			state.remove(CI);
			state.remove(NAME);
			return;
		}

		int id = state.getIntAttribute(idVar);

		if(id == Integer.MIN_VALUE) {
			state.remove(HyperLink.HREF);
			state.remove(CI);
			state.remove(NAME);
			return;
		}

		ClassInfo ci = Database.getClassInfo(id);

		if(ci != null) {
			state.setAttribute(HyperLink.HREF, ci.getOutputFilename());
			state.setAttribute(NAME, ci.getDisplayName());
			state.setAttribute(CI, ci);
		} else {
			state.setAttribute(NAME, ClassInfo.getDisplayName(Database.getClassName(id)));
			state.remove(HyperLink.HREF);
			state.remove(CI);
		}
	}

	public boolean process(Writer out) {
		return false;
	}
}