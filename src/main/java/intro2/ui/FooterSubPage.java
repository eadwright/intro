package intro2.ui;

public final class FooterSubPage implements lahaina.runtime.Page {

	public void FooterSubPage() {}

	public void process(lahaina.runtime.Writer out, lahaina.runtime.State state) throws Exception {
		lahaina.tag.Tag tag = null;

		out.write("<HR>\r\n");
		out.write("<font size=\"-1\">Document generated on ");
		intro2.ui.taglib.Date _tag0 = new intro2.ui.taglib.Date(state);
		tag = _tag0;
		while(_tag0.process(out));
		out.write("<br>\r\n");
		out.write("Intro &szlig; Copyright 2001-2002 Edward Wright, All Rights Reserved.</font>\r\n");
		out.write("</BODY>\r\n");
		out.write("</HTML>");
	}
}
