package intro2.ui;

public final class ModulePage implements lahaina.runtime.Page {

	public void ModulePage() {}

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
		out.write("SUMMARY:&nbsp;");
		state.setAttribute("href","subModules");
		intro2.ui.taglib.HyperLink _tag2 = new intro2.ui.taglib.HyperLink(state);
		tag = _tag2;
		while(_tag2.process(out)) {
			out.write("SUB-MODULES");
		}
		out.write("&nbsp;|&nbsp;");
		state.setAttribute("href","packages");
		intro2.ui.taglib.HyperLink _tag3 = new intro2.ui.taglib.HyperLink(state);
		tag = _tag3;
		while(_tag3.process(out)) {
			out.write("PACKAGES");
		}
		out.write("&nbsp;|&nbsp;");
		state.setAttribute("href","modUsage");
		intro2.ui.taglib.HyperLink _tag4 = new intro2.ui.taglib.HyperLink(state);
		tag = _tag4;
		while(_tag4.process(out)) {
			out.write("MODULE USAGE");
		}
		out.write("&nbsp;\r\n");
		out.write("</FONT></TD>\r\n");
		out.write("</TR>\r\n");
		out.write("</TABLE>\r\n");
		out.write("<!-- =========== END OF NAVBAR =========== -->\r\n");
		out.write("\r\n");
		out.write("<HR>\r\n");
		out.write("<!-- ======== START OF MODULE DATA ======== -->\r\n");
		out.write("<H2>Module: ");
		out.write(state.getStringAttribute("this.name"));
		out.write("</H2>\r\n");
		out.write("\r\n");
		intro2.ui.taglib.ModuleTree _tag5 = new intro2.ui.taglib.ModuleTree(state);
		tag = _tag5;
		while(_tag5.process(out)) {
			state.setAttribute("counterName","up");
			intro2.ui.taglib.Loop _tag6 = new intro2.ui.taglib.Loop(state);
			tag = _tag6;
			while(_tag6.process(out)) {
				out.write("<UL>");
			}
			out.write("<LI type=\"circle\">");
			intro2.ui.taglib.HyperLink _tag7 = new intro2.ui.taglib.HyperLink(state);
			tag = _tag7;
			while(_tag7.process(out)) {
				out.write(state.getStringAttribute("current.name"));
			}
			state.setAttribute("counterName","down");
			intro2.ui.taglib.Loop _tag8 = new intro2.ui.taglib.Loop(state);
			tag = _tag8;
			while(_tag8.process(out)) {
				out.write("</UL>");
			}
		}
		out.write("<P>");
		out.write(state.getStringAttribute("this.description"));
		state.setAttribute("type","real");
		state.setAttribute("pageClass","intro2.ui.NRSubPage");
		lahaina.tag.Include _tag9 = new lahaina.tag.Include(state);
		tag = _tag9;
		while(_tag9.process(out));
		out.write("<P>\r\n");
		out.write("\r\n");
		state.setAttribute("flag","subModules");
		intro2.ui.taglib.Exists _tag10 = new intro2.ui.taglib.Exists(state);
		tag = _tag10;
		while(_tag10.process(out)) {
			out.write("<!-- ======== SUB-MODULE SUMMARY ======== -->\r\n");
			out.write("\r\n");
			out.write("<A NAME=\"submodule\"></A>\r\n");
			out.write("<TABLE BORDER=\"1\" CELLPADDING=\"3\" CELLSPACING=\"0\" WIDTH=\"100%\">\r\n");
			out.write("<TR CLASS=\"TableHeadingColor\">\r\n");
			out.write("<TD><FONT SIZE=\"+2\">\r\n");
			out.write("<B>Sub-Module Summary</B></FONT></TD>\r\n");
			out.write("</TR>\r\n");
			intro2.ui.taglib.ModuleList _tag11 = new intro2.ui.taglib.ModuleList(state);
			tag = _tag11;
			while(_tag11.process(out)) {
				out.write("<TR BGCOLOR=\"white\" CLASS=\"TableRowColor\">\r\n");
				out.write("<TD>");
				intro2.ui.taglib.HyperLink _tag12 = new intro2.ui.taglib.HyperLink(state);
				tag = _tag12;
				while(_tag12.process(out)) {
					out.write("<CODE><B>");
					out.write(state.getStringAttribute("current.name"));
					out.write("</B></CODE>");
				}
				out.write("<BR>\r\n");
				out.write("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				out.write(state.getStringAttribute("current.description"));
				state.setAttribute("type","real");
				state.setAttribute("pageClass","intro2.ui.NRSubPage");
				lahaina.tag.Include _tag13 = new lahaina.tag.Include(state);
				tag = _tag13;
				while(_tag13.process(out));
				out.write("</TD>\r\n");
				out.write("</TR>");
			}
			out.write("</TABLE>\r\n");
			out.write("<P>");
		}
		state.setAttribute("flag","packages");
		intro2.ui.taglib.Exists _tag14 = new intro2.ui.taglib.Exists(state);
		tag = _tag14;
		while(_tag14.process(out)) {
			out.write("<!-- ======== PACKAGE SUMMARY ======== -->\r\n");
			out.write("\r\n");
			out.write("<A NAME=\"packages\"></A>\r\n");
			out.write("<TABLE BORDER=\"1\" CELLPADDING=\"3\" CELLSPACING=\"0\" WIDTH=\"100%\">\r\n");
			out.write("<TR CLASS=\"TableHeadingColor\">\r\n");
			out.write("<TD><FONT SIZE=\"+2\">\r\n");
			out.write("<B>Package Summary</B></FONT></TD>\r\n");
			out.write("</TR>\r\n");
			out.write("<TR CLASS=\"TableSubHeadingColor\">\r\n");
			out.write("<TD><B>Package</B></TD>\r\n");
			out.write("</TR>\r\n");
			intro2.ui.taglib.PackageList _tag15 = new intro2.ui.taglib.PackageList(state);
			tag = _tag15;
			while(_tag15.process(out)) {
				out.write("<TR BGCOLOR=\"white\" CLASS=\"TableRowColor\">\r\n");
				out.write("<TD>");
				intro2.ui.taglib.HyperLink _tag16 = new intro2.ui.taglib.HyperLink(state);
				tag = _tag16;
				while(_tag16.process(out)) {
					out.write("<CODE><B>");
					out.write(state.getStringAttribute("current.displayName"));
					out.write("</B></CODE>");
				}
				state.setAttribute("type","current");
				state.setAttribute("pageClass","intro2.ui.NRSubPage");
				lahaina.tag.Include _tag17 = new lahaina.tag.Include(state);
				tag = _tag17;
				while(_tag17.process(out));
				out.write("</TD>\r\n");
				out.write("</TR>");
			}
			out.write("</TABLE>\r\n");
			out.write("&nbsp;<P>");
		}
		state.setAttribute("flag","modUsage");
		intro2.ui.taglib.Exists _tag18 = new intro2.ui.taglib.Exists(state);
		tag = _tag18;
		while(_tag18.process(out)) {
			out.write("<!-- ======== MODULE USAGE SUMMARY ======== -->\r\n");
			out.write("<A NAME=\"module_usage\"></A>\r\n");
			out.write("<H2>Usage</H2>\r\n");
			out.write("<P>\r\n");
			out.write("<TABLE BORDER=\"1\" CELLPADDING=\"3\" CELLSPACING=\"0\" WIDTH=\"100%\">\r\n");
			out.write("<COLGROUP>\r\n");
			out.write("   <COL width=\"70%\">\r\n");
			out.write("   <COL width=\"15%\">\r\n");
			out.write("   <COL width=\"15%\">\r\n");
			out.write("</COLGROUP>\r\n");
			out.write("<TR CLASS=\"TableHeadingColor\">\r\n");
			out.write("<TD COLSPAN=3><FONT SIZE=\"+2\">\r\n");
			out.write("<B>Module Usage Summary</B></FONT></TD>\r\n");
			out.write("</TR>\r\n");
			out.write("<TR CLASS=\"TableSubHeadingColor\">\r\n");
			out.write("<TD><B>Module</B></TD><TD><B>Outward</B></TD><TD><B>Inward</B></TD>\r\n");
			out.write("</TR>\r\n");
			intro2.ui.taglib.ModuleUsageSummaryList _tag19 = new intro2.ui.taglib.ModuleUsageSummaryList(state);
			tag = _tag19;
			while(_tag19.process(out)) {
				out.write("<TR BGCOLOR=\"white\" CLASS=\"TableRowColor\">\r\n");
				out.write("<TD><CODE><B>");
				out.write(state.getStringAttribute("current.name"));
				out.write("</B></CODE>\r\n");
				out.write("<BR>\r\n");
				out.write("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\r\n");
				out.write(state.getStringAttribute("current.description"));
				out.write("</TD>\r\n");
				out.write("<TD>");
				state.setAttribute("href","outwardref");
				intro2.ui.taglib.HyperLink2 _tag20 = new intro2.ui.taglib.HyperLink2(state);
				tag = _tag20;
				while(_tag20.process(out)) {
					out.write("Yes");
				}
				out.write("&nbsp;</TD>\r\n");
				out.write("<TD>");
				state.setAttribute("href","inwardref");
				intro2.ui.taglib.HyperLink2 _tag21 = new intro2.ui.taglib.HyperLink2(state);
				tag = _tag21;
				while(_tag21.process(out)) {
					out.write("Yes");
				}
				out.write("&nbsp;</TD>\r\n");
				out.write("</TR>");
			}
			out.write("</TABLE>\r\n");
			out.write("&nbsp;\r\n");
			out.write("<P>");
		}
		out.write("<!-- ========= END OF MODULE DATA ========= -->\r\n");
		out.write("\r\n");
		state.setAttribute("pageClass","intro2.ui.KeySubPage");
		lahaina.tag.Include _tag22 = new lahaina.tag.Include(state);
		tag = _tag22;
		while(_tag22.process(out));
		state.setAttribute("pageClass","intro2.ui.NavbarSubPage");
		lahaina.tag.Include _tag23 = new lahaina.tag.Include(state);
		tag = _tag23;
		while(_tag23.process(out));
		out.write("SUMMARY:&nbsp;<A HREF=\"#submodules\">SUB-MODULES</A>&nbsp;|&nbsp;<A HREF=\"#packages\">PACKAGES</A>\r\n");
		out.write("&nbsp;|&nbsp;");
		state.setAttribute("href","modUsage");
		intro2.ui.taglib.HyperLink _tag24 = new intro2.ui.taglib.HyperLink(state);
		tag = _tag24;
		while(_tag24.process(out)) {
			out.write("MODULE USAGE");
		}
		out.write("&nbsp;\r\n");
		out.write("</FONT></TD>\r\n");
		out.write("</TR>\r\n");
		out.write("</TABLE>\r\n");
		out.write("<!-- =========== END OF NAVBAR =========== -->\r\n");
		out.write("\r\n");
		state.setAttribute("pageClass","intro2.ui.FooterSubPage");
		lahaina.tag.Include _tag25 = new lahaina.tag.Include(state);
		tag = _tag25;
		while(_tag25.process(out));
	}
}
