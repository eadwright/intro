package intro2.ui;

public final class KeySubPage implements lahaina.runtime.Page {

	public void KeySubPage() {}

	public void process(lahaina.runtime.Writer out, lahaina.runtime.State state) throws Exception {
		lahaina.tag.Tag tag = null;

		out.write("<!-- ========= BEGIN KEY ================== -->\r\n");
		out.write("<P>\r\n");
		out.write("<H3>Key:</H3>\r\n");
		out.write("<TABLE BORDER=\"0\" CELLPADDING=\"1\" CELLSPACING=\"0\">\r\n");
		out.write("<TR>\r\n");
		out.write("<TD><FONT CLASS = \"RKey\">R</FONT></TD>\r\n");
		out.write("<TD>&nbsp;&nbsp;<I>uses reflection</I></TD>\r\n");
		out.write("</TR>\r\n");
		out.write("<TR>\r\n");
		out.write("<TD><FONT CLASS = \"NKey\">N</FONT></TD>\r\n");
		out.write("<TD>&nbsp;&nbsp;<I>uses native code</I></TD>\r\n");
		out.write("</TR>\r\n");
		state.setAttribute("flag","inout");
		intro2.ui.taglib.Is _tag0 = new intro2.ui.taglib.Is(state);
		tag = _tag0;
		while(_tag0.process(out)) {
			out.write("<TR>\r\n");
			out.write("<TD><FONT CLASS = \"RefKey\">Outward</FONT></TD>\r\n");
			out.write("<TD>&nbsp;&nbsp;<I>this uses ...</I></TD>\r\n");
			out.write("</TR>\r\n");
			out.write("<TR>\r\n");
			out.write("<TD><FONT CLASS = \"RefKey\">Inward</FONT></TD>\r\n");
			out.write("<TD>&nbsp;&nbsp;<I>this is used by ...</I></TD>\r\n");
			out.write("</TR>");
		}
		out.write("</TABLE>\r\n");
		out.write("<HR>\r\n");
		out.write("<!-- ========= END KEY ==================== -->");
	}
}
