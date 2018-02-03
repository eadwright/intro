package intro2.ui;

public final class PackageUsesPage implements lahaina.runtime.Page {

	public void PackageUsesPage() {}

	public void process(lahaina.runtime.Writer out, lahaina.runtime.State state) throws Exception {
		lahaina.tag.Tag tag = null;

		state.setAttribute("pageClass","intro2.ui.HeaderSubPage");
		lahaina.tag.Include _tag0 = new lahaina.tag.Include(state);
		tag = _tag0;
		while(_tag0.process(out));
		state.setAttribute("pageClass","intro2.ui.NavbarSubPage");
		lahaina.tag.Include _tag1 = new lahaina.tag.Include(state);
		tag = _tag1;
		while(_tag1.process(out));
		out.write("</FONT></TD>\r\n");
		out.write("</TR>\r\n");
		out.write("</TABLE>\r\n");
		out.write("<!-- =========== END OF NAVBAR =========== -->\r\n");
		out.write("\r\n");
		out.write("<HR>\r\n");
		out.write("<!-- ======== START OF MODULE DATA ======== -->\r\n");
		out.write("<H2>Module: ");
		out.write(state.getStringAttribute("this.name"));
		out.write("&nbsp;uses&nbsp;");
		out.write(state.getStringAttribute("other.name"));
		out.write("</H2>\r\n");
		out.write("\r\n");
		out.write("<!-- ======== CLASS-CLASS SUMMARY ======== -->\r\n");
		out.write("\r\n");
		out.write("<P>\r\n");
		out.write("<TABLE BORDER=\"1\" CELLPADDING=\"3\" CELLSPACING=\"0\" WIDTH=\"100%\">\r\n");
		out.write("<COLGROUP>\r\n");
		out.write("   <COL width=\"50%\">\r\n");
		out.write("   <COL width=\"50%\">\r\n");
		out.write("</COLGROUP>\r\n");
		out.write("<TR CLASS=\"TableHeadingColor\">\r\n");
		out.write("<TD COLSPAN=3><FONT SIZE=\"+2\">\r\n");
		out.write("<B>Outward References</B></FONT></TD>\r\n");
		out.write("</TR>\r\n");
		out.write("<TR CLASS=\"TableSubHeadingColor\">\r\n");
		out.write("<TD><B>From (Package ");
		out.write(state.getStringAttribute("this.displayName"));
		out.write(")</B></TD><TD><B>To (Package ");
		out.write(state.getStringAttribute("other.displayName"));
		out.write(")</B></TD>\r\n");
		out.write("</TR>\r\n");
		intro2.ui.taglib.PackageUsageList _tag2 = new intro2.ui.taglib.PackageUsageList(state);
		tag = _tag2;
		while(_tag2.process(out)) {
			out.write("<TR BGCOLOR=\"white\" CLASS=\"TableRowColor\">\r\n");
			out.write("<TD>");
			intro2.ui.taglib.HyperLink _tag3 = new intro2.ui.taglib.HyperLink(state);
			tag = _tag3;
			while(_tag3.process(out)) {
				out.write("<CODE><B>");
				out.write(state.getStringAttribute("left.name"));
				out.write("</B></CODE>");
			}
			state.setAttribute("type","left");
			state.setAttribute("pageClass","intro2.ui.NRSubPage");
			lahaina.tag.Include _tag4 = new lahaina.tag.Include(state);
			tag = _tag4;
			while(_tag4.process(out));
			out.write("</TD>\r\n");
			out.write("<TD>");
			intro2.ui.taglib.HyperLink _tag5 = new intro2.ui.taglib.HyperLink(state);
			tag = _tag5;
			while(_tag5.process(out)) {
				out.write("<CODE><B>");
				out.write(state.getStringAttribute("right.name"));
				out.write("</B></CODE>");
			}
			state.setAttribute("type","right");
			state.setAttribute("pageClass","intro2.ui.NRSubPage");
			lahaina.tag.Include _tag6 = new lahaina.tag.Include(state);
			tag = _tag6;
			while(_tag6.process(out));
			out.write("</TD>\r\n");
			out.write("</TR>");
		}
		out.write("</TABLE>\r\n");
		out.write("&nbsp;\r\n");
		out.write("<P>\r\n");
		out.write("<!-- ========= END OF CLASS-CLASS DATA ========= -->\r\n");
		out.write("\r\n");
		out.write("\r\n");
		state.setAttribute("pageClass","intro2.ui.KeySubPage");
		lahaina.tag.Include _tag7 = new lahaina.tag.Include(state);
		tag = _tag7;
		while(_tag7.process(out));
		state.setAttribute("pageClass","intro2.ui.NavbarSubPage");
		lahaina.tag.Include _tag8 = new lahaina.tag.Include(state);
		tag = _tag8;
		while(_tag8.process(out));
		out.write("</FONT></TD>\r\n");
		out.write("</TR>\r\n");
		out.write("</TABLE>\r\n");
		out.write("<!-- =========== END OF NAVBAR =========== -->\r\n");
		out.write("\r\n");
		state.setAttribute("pageClass","intro2.ui.FooterSubPage");
		lahaina.tag.Include _tag9 = new lahaina.tag.Include(state);
		tag = _tag9;
		while(_tag9.process(out));
	}
}
