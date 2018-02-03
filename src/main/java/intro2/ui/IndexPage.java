package intro2.ui;

public final class IndexPage implements lahaina.runtime.Page {

	public void IndexPage() {}

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
		out.write("SUMMARY:&nbsp;<A HREF=\"#modules\">MODULES</A>&nbsp;|&nbsp;<A HREF=\"#libraries\">LIBRARIES</A>\r\n");
		out.write("&nbsp;\r\n");
		out.write("</FONT></TD>\r\n");
		out.write("</TR>\r\n");
		out.write("</TABLE>\r\n");
		out.write("<!-- =========== END OF NAVBAR =========== -->\r\n");
		out.write("\r\n");
		out.write("<HR>\r\n");
		out.write("<!-- ======== START OF PROJECT DATA ======== -->\r\n");
		out.write("<H2>Project: ");
		out.write(state.getStringAttribute("this.name"));
		out.write("</H2>\r\n");
		out.write("<P>");
		out.write(state.getStringAttribute("this.description"));
		out.write("<P>\r\n");
		out.write("\r\n");
		out.write("<!-- ======== MODULE SUMMARY ======== -->\r\n");
		out.write("\r\n");
		out.write("<A NAME=\"modules\"><!-- --></A>\r\n");
		out.write("<TABLE BORDER=\"1\" CELLPADDING=\"3\" CELLSPACING=\"0\" WIDTH=\"100%\">\r\n");
		out.write("<TR CLASS=\"TableHeadingColor\">\r\n");
		out.write("<TD><FONT SIZE=\"+2\">\r\n");
		out.write("<B>Module Summary</B></FONT></TD>\r\n");
		out.write("</TR>\r\n");
		out.write("</TABLE>\r\n");
		out.write("<P>\r\n");
		intro2.ui.taglib.ModuleTree _tag2 = new intro2.ui.taglib.ModuleTree(state);
		tag = _tag2;
		while(_tag2.process(out)) {
			state.setAttribute("counterName","up");
			intro2.ui.taglib.Loop _tag3 = new intro2.ui.taglib.Loop(state);
			tag = _tag3;
			while(_tag3.process(out)) {
				out.write("<UL>");
			}
			out.write("<LI type=\"circle\">");
			intro2.ui.taglib.HyperLink _tag4 = new intro2.ui.taglib.HyperLink(state);
			tag = _tag4;
			while(_tag4.process(out)) {
				out.write(state.getStringAttribute("current.name"));
			}
			out.write("&nbsp;( ");
			out.write(state.getStringAttribute("current.description"));
			out.write(")\r\n");
			state.setAttribute("type","real");
			state.setAttribute("pageClass","intro2.ui.NRSubPage");
			lahaina.tag.Include _tag5 = new lahaina.tag.Include(state);
			tag = _tag5;
			while(_tag5.process(out));
			state.setAttribute("counterName","down");
			intro2.ui.taglib.Loop _tag6 = new intro2.ui.taglib.Loop(state);
			tag = _tag6;
			while(_tag6.process(out)) {
				out.write("</UL>");
			}
		}
		out.write("<P>\r\n");
		out.write("\r\n");
		out.write("<!-- ======== LIBRARY SUMMARY ======== -->\r\n");
		out.write("\r\n");
		out.write("<A NAME=\"libraries\"><!-- --></A>\r\n");
		out.write("<TABLE BORDER=\"1\" CELLPADDING=\"3\" CELLSPACING=\"0\" WIDTH=\"100%\">\r\n");
		out.write("<COLGROUP>\r\n");
		out.write("   <COL width=\"30%\">\r\n");
		out.write("   <COL width=\"70%\">\r\n");
		out.write("<TR CLASS=\"TableHeadingColor\">\r\n");
		out.write("<TD COLSPAN=2><FONT SIZE=\"+2\">\r\n");
		out.write("<B>Library Summary</B></FONT></TD>\r\n");
		out.write("</TR>\r\n");
		out.write("<TR CLASS=\"TableSubHeadingColor\">\r\n");
		out.write("<TD><B>Description</B></TD><TD><B>Path</B></TD>\r\n");
		out.write("</TR>\r\n");
		intro2.ui.taglib.LibraryList _tag7 = new intro2.ui.taglib.LibraryList(state);
		tag = _tag7;
		while(_tag7.process(out)) {
			out.write("<TR BGCOLOR=\"white\" CLASS=\"TableRowColor\">\r\n");
			out.write("<TD>");
			out.write(state.getStringAttribute("current.name"));
			out.write("</TD>\r\n");
			out.write("<TD><CODE>");
			out.write(state.getStringAttribute("current.libpath"));
			out.write("</CODE>\r\n");
			out.write("</TR>");
		}
		out.write("</TABLE>\r\n");
		out.write("&nbsp;\r\n");
		out.write("<P>\r\n");
		out.write("\r\n");
		out.write("<!-- ========= END OF PROJECT DATA ========= -->\r\n");
		out.write("\r\n");
		state.setAttribute("pageClass","intro2.ui.KeySubPage");
		lahaina.tag.Include _tag8 = new lahaina.tag.Include(state);
		tag = _tag8;
		while(_tag8.process(out));
		state.setAttribute("pageClass","intro2.ui.NavbarSubPage");
		lahaina.tag.Include _tag9 = new lahaina.tag.Include(state);
		tag = _tag9;
		while(_tag9.process(out));
		out.write("SUMMARY:&nbsp;<A HREF=\"#modules\">MODULES</A>&nbsp;|&nbsp;<A HREF=\"#libraries\">LIBRARIES</A>\r\n");
		out.write("&nbsp;\r\n");
		out.write("</FONT></TD>\r\n");
		out.write("</TR>\r\n");
		out.write("</TABLE>\r\n");
		out.write("<!-- =========== END OF NAVBAR =========== -->\r\n");
		out.write("\r\n");
		state.setAttribute("pageClass","intro2.ui.FooterSubPage");
		lahaina.tag.Include _tag10 = new lahaina.tag.Include(state);
		tag = _tag10;
		while(_tag10.process(out));
	}
}
