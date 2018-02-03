package intro2.ui;

public final class NRSubPage implements lahaina.runtime.Page {

	public void NRSubPage() {}

	public void process(lahaina.runtime.Writer out, lahaina.runtime.State state) throws Exception {
		lahaina.tag.Tag tag = null;

		state.setAttribute("to","nrtest");
		state.setAttribute("from","type");
		intro2.ui.taglib.LookupAssign _tag0 = new intro2.ui.taglib.LookupAssign(state);
		tag = _tag0;
		while(_tag0.process(out));
		out.write("&nbsp;");
		state.setAttribute("flag0","nrtest.usesreflection");
		state.setAttribute("flag1","nrtest.usesnative");
		intro2.ui.taglib.BOr _tag1 = new intro2.ui.taglib.BOr(state);
		tag = _tag1;
		while(_tag1.process(out)) {
			out.write("(");
			state.setAttribute("flag0","nrtest.usesreflection");
			intro2.ui.taglib.BOr _tag2 = new intro2.ui.taglib.BOr(state);
			tag = _tag2;
			while(_tag2.process(out)) {
				out.write("<FONT CLASS = \"RKey\">R</FONT>");
			}
			state.setAttribute("flag0","nrtest.usesnative");
			intro2.ui.taglib.BOr _tag3 = new intro2.ui.taglib.BOr(state);
			tag = _tag3;
			while(_tag3.process(out)) {
				out.write("<FONT CLASS = \"NKey\">N</FONT>");
			}
			out.write(")");
		}
	}
}
