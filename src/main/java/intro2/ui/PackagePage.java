package intro2.ui;

import intro2.ui.taglib.*;

public final class PackagePage implements lahaina.runtime.Page {

	public void PackagePage() {}

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
		state.setAttribute("href","subPackages");
		HyperLink _tag2 = new HyperLink(state);
		tag = _tag2;
		while(_tag2.process(out)) {
			out.write("SUB-PACKAGES");
		}
		out.write("&nbsp;|&nbsp;");
		state.setAttribute("href","interfaces");
		HyperLink _tag3 = new HyperLink(state);
		tag = _tag3;
		while(_tag3.process(out)) {
			out.write("INTERFACES");
		}
		out.write("&nbsp;|&nbsp;");
		state.setAttribute("href","classes");
		HyperLink _tag4 = new HyperLink(state);
		tag = _tag4;
		while(_tag4.process(out)) {
			out.write("CLASSES");
		}
		out.write("&nbsp;|&nbsp;");
		state.setAttribute("href","pkgUsage");
		HyperLink _tag5 = new HyperLink(state);
		tag = _tag5;
		while(_tag5.process(out)) {
			out.write("PACKAGE USAGE");
		}
		out.write("&nbsp;\r\n");
		out.write("</FONT></TD>\r\n");
		out.write("</TR>\r\n");
		out.write("</TABLE>\r\n");
		out.write("<!-- =========== END OF NAVBAR =========== -->\r\n");
		out.write("<HR>\r\n");
		out.write("<!-- ======== START OF PACKAGE DATA ======== -->\r\n");
		out.write("<H2>Package: ");
		out.write(state.getStringAttribute("this.displayName"));
		out.write("</H2>\r\n");
		out.write("\r\n");
		PackageTree _tag6 = new PackageTree(state);
		tag = _tag6;
		while(_tag6.process(out)) {
			state.setAttribute("counterName","up");
			Loop _tag7 = new Loop(state);
			tag = _tag7;
			while(_tag7.process(out)) {
				out.write("<UL>");
			}
			out.write("<LI type=\"circle\">");
			HyperLink _tag8 = new HyperLink(state);
			tag = _tag8;
			while(_tag8.process(out)) {
				out.write(state.getStringAttribute("name"));
			}
			state.setAttribute("counterName","down");
			Loop _tag9 = new Loop(state);
			tag = _tag9;
			while(_tag9.process(out)) {
				out.write("</UL>");
			}
		}
		out.write("<P>\r\n");
		state.setAttribute("type","current");
		state.setAttribute("pageClass","intro2.ui.NRSubPage");
		lahaina.tag.Include _tag10 = new lahaina.tag.Include(state);
		tag = _tag10;
		while(_tag10.process(out));
		out.write("<P>\r\n");
		out.write("\r\n");
		state.setAttribute("flag","subPackages");
		Exists _tag11 = new Exists(state);
		tag = _tag11;
		while(_tag11.process(out)) {
			out.write("<!-- ======== SUB-PACKAGE SUMMARY ======== -->\r\n");
			out.write("\r\n");
			out.write("<A NAME=\"subpackages\"></A>\r\n");
			out.write("<TABLE BORDER=\"1\" CELLPADDING=\"3\" CELLSPACING=\"0\" WIDTH=\"100%\">\r\n");
			out.write("<TR CLASS=\"TableHeadingColor\">\r\n");
			out.write("<TD><FONT SIZE=\"+2\">\r\n");
			out.write("<B>Sub-Package Summary</B></FONT></TD>\r\n");
			out.write("</TR>\r\n");
			out.write("<TR CLASS=\"TableSubHeadingColor\">\r\n");
			out.write("<TD><B>Package</B></TD>\r\n");
			out.write("</TR>\r\n");
			PackageList _tag12 = new PackageList(state);
			tag = _tag12;
			while(_tag12.process(out)) {
				out.write("<TR BGCOLOR=\"white\" CLASS=\"TableRowColor\">\r\n");
				out.write("<TD>");
				HyperLink _tag13 = new HyperLink(state);
				tag = _tag13;
				while(_tag13.process(out)) {
					out.write("<CODE><B>");
					out.write(state.getStringAttribute("current.displayName"));
					out.write("</B></CODE>");
				}
				state.setAttribute("type","current");
				state.setAttribute("pageClass","intro2.ui.NRSubPage");
				lahaina.tag.Include _tag14 = new lahaina.tag.Include(state);
				tag = _tag14;
				while(_tag14.process(out));
				out.write("</TD>\r\n");
				out.write("</TR>");
			}
			out.write("</TABLE>\r\n");
			out.write("&nbsp;<P>");
		}
		state.setAttribute("flag","interfaces");
		Exists _tag15 = new Exists(state);
		tag = _tag15;
		while(_tag15.process(out)) {
			out.write("<!-- ======== INTERFACE SUMMARY ======== -->\r\n");
			out.write("\r\n");
			out.write("<A NAME=\"interfaces\"></A>\r\n");
			out.write("<TABLE BORDER=\"1\" CELLPADDING=\"3\" CELLSPACING=\"0\" WIDTH=\"100%\">\r\n");
			out.write("<TR CLASS=\"TableHeadingColor\">\r\n");
			out.write("<TD><FONT SIZE=\"+2\">\r\n");
			out.write("<B>Interface Summary</B></FONT></TD>\r\n");
			out.write("</TR>\r\n");
			out.write("<TR CLASS=\"TableSubHeadingColor\">\r\n");
			out.write("<TD><B>Interface</B></TD>\r\n");
			out.write("</TR>\r\n");
			state.setAttribute("listName","target.getAllInterfaceOnlyIDs");
			ClassList _tag16 = new ClassList(state);
			tag = _tag16;
			while(_tag16.process(out)) {
				out.write("<TR BGCOLOR=\"white\" CLASS=\"TableRowColor\">\r\n");
				out.write("<TD>");
				HyperLink _tag17 = new HyperLink(state);
				tag = _tag17;
				while(_tag17.process(out)) {
					out.write("<CODE><B>");
					out.write(state.getStringAttribute("displayName"));
					out.write("</B></CODE>");
				}
				state.setAttribute("type","ci");
				state.setAttribute("pageClass","intro2.ui.NRSubPage");
				lahaina.tag.Include _tag18 = new lahaina.tag.Include(state);
				tag = _tag18;
				while(_tag18.process(out));
				out.write("</TD>\r\n");
				out.write("</TR>");
			}
			out.write("</TABLE>\r\n");
			out.write("&nbsp;<P>");
		}
		state.setAttribute("flag","classes");
		Exists _tag19 = new Exists(state);
		tag = _tag19;
		while(_tag19.process(out)) {
			out.write("<!-- ======== CLASS SUMMARY ======== -->\r\n");
			out.write("\r\n");
			out.write("<A NAME=\"classes\"></A>\r\n");
			out.write("<TABLE BORDER=\"1\" CELLPADDING=\"3\" CELLSPACING=\"0\" WIDTH=\"100%\">\r\n");
			out.write("<TR CLASS=\"TableHeadingColor\">\r\n");
			out.write("<TD><FONT SIZE=\"+2\">\r\n");
			out.write("<B>Class Summary</B></FONT></TD>\r\n");
			out.write("</TR>\r\n");
			out.write("<TR CLASS=\"TableSubHeadingColor\">\r\n");
			out.write("<TD><B>Class</B></TD>\r\n");
			out.write("</TR>\r\n");
			state.setAttribute("listName","target.getAllClassOnlyIDs");
			ClassList _tag20 = new ClassList(state);
			tag = _tag20;
			while(_tag20.process(out)) {
				out.write("<TR BGCOLOR=\"white\" CLASS=\"TableRowColor\">\r\n");
				out.write("<TD>");
				HyperLink _tag21 = new HyperLink(state);
				tag = _tag21;
				while(_tag21.process(out)) {
					out.write("<CODE><B>");
					out.write(state.getStringAttribute("displayName"));
					out.write("</B></CODE>");
				}
				state.setAttribute("type","ci");
				state.setAttribute("pageClass","intro2.ui.NRSubPage");
				lahaina.tag.Include _tag22 = new lahaina.tag.Include(state);
				tag = _tag22;
				while(_tag22.process(out));
				out.write("</TD>\r\n");
				out.write("</TR>");
			}
			out.write("</TABLE>\r\n");
			out.write("&nbsp;<P>");
		}
		state.setAttribute("flag","pkgUsage");
		Exists _tag23 = new Exists(state);
		tag = _tag23;
		while(_tag23.process(out)) {
			out.write("<!-- ======== PACKAGE USAGE SUMMARY ======== -->\r\n");
			out.write("<A NAME=\"package_usage\"></A>\r\n");
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
			out.write("<B>Package Usage Summary</B></FONT></TD>\r\n");
			out.write("</TR>\r\n");
			out.write("<TR CLASS=\"TableSubHeadingColor\">\r\n");
			out.write("<TD><B>Package</B></TD><TD><B>Outward</B></TD><TD><B>Inward</B></TD>\r\n");
			out.write("</TR>\r\n");
			PackageUsageSummaryList _tag24 = new PackageUsageSummaryList(state);
			tag = _tag24;
			while(_tag24.process(out)) {
				out.write("<TR BGCOLOR=\"white\" CLASS=\"TableRowColor\">\r\n");
				out.write("<TD><CODE><B>");
				out.write(state.getStringAttribute("current.displayName"));
				out.write("</B></CODE>\r\n");
				state.setAttribute("type","current");
				state.setAttribute("pageClass","intro2.ui.NRSubPage");
				lahaina.tag.Include _tag25 = new lahaina.tag.Include(state);
				tag = _tag25;
				while(_tag25.process(out));
				out.write("</TD>\r\n");
				out.write("<TD>");
				state.setAttribute("href","outwardref");
				HyperLink2 _tag26 = new HyperLink2(state);
				tag = _tag26;
				while(_tag26.process(out)) {
					out.write("Yes");
				}
				out.write("&nbsp;</TD>\r\n");
				out.write("<TD>");
				state.setAttribute("href","inwardref");
				HyperLink2 _tag27 = new HyperLink2(state);
				tag = _tag27;
				while(_tag27.process(out)) {
					out.write("Yes");
				}
				out.write("&nbsp;</TD>\r\n");
				out.write("</TR>");
			}
			out.write("</TABLE>\r\n");
			out.write("&nbsp;\r\n");
			out.write("<P>");
		}
		out.write("<!-- ========= END OF PACKAGE DATA ========= -->\r\n");
		out.write("\r\n");
		state.setAttribute("pageClass","intro2.ui.KeySubPage");
		lahaina.tag.Include _tag28 = new lahaina.tag.Include(state);
		tag = _tag28;
		while(_tag28.process(out));
		state.setAttribute("pageClass","intro2.ui.NavbarSubPage");
		lahaina.tag.Include _tag29 = new lahaina.tag.Include(state);
		tag = _tag29;
		while(_tag29.process(out));
		out.write("SUMMARY:&nbsp;");
		state.setAttribute("href","subPackages");
		HyperLink _tag30 = new HyperLink(state);
		tag = _tag30;
		while(_tag30.process(out)) {
			out.write("SUB-PACKAGES");
		}
		out.write("&nbsp;|&nbsp;");
		state.setAttribute("href","interfaces");
		HyperLink _tag31 = new HyperLink(state);
		tag = _tag31;
		while(_tag31.process(out)) {
			out.write("INTERFACES");
		}
		out.write("&nbsp;|&nbsp;");
		state.setAttribute("href","classes");
		HyperLink _tag32 = new HyperLink(state);
		tag = _tag32;
		while(_tag32.process(out)) {
			out.write("CLASSES");
		}
		out.write("&nbsp;\r\n");
		out.write("</FONT></TD>\r\n");
		out.write("</TR>\r\n");
		out.write("</TABLE>\r\n");
		out.write("<!-- =========== END OF NAVBAR =========== -->\r\n");
		out.write("\r\n");
		state.setAttribute("pageClass","intro2.ui.FooterSubPage");
		lahaina.tag.Include _tag33 = new lahaina.tag.Include(state);
		tag = _tag33;
		while(_tag33.process(out));
	}
}
