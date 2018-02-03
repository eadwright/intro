package intro2.ui;

public final class NavbarSubPage implements lahaina.runtime.Page {

	public void NavbarSubPage() {}

	public void process(lahaina.runtime.Writer out, lahaina.runtime.State state) throws Exception {
		lahaina.tag.Tag tag = null;

		out.write("<!-- ========== START OF NAVBAR ========== -->\r\n");
		out.write("<A NAME=\"navbar_top\"><!-- --></A>\r\n");
		out.write("<TABLE BORDER=\"0\" WIDTH=\"100%\" CELLPADDING=\"1\" CELLSPACING=\"0\">\r\n");
		out.write("<TR>\r\n");
		out.write("<TD COLSPAN=3 BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1\">\r\n");
		out.write("<TABLE BORDER=\"0\" CELLPADDING=\"0\" CELLSPACING=\"3\">\r\n");
		out.write("<TR ALIGN=\"center\" VALIGN=\"top\">\r\n");
		state.setAttribute("flag","revProject");
		intro2.ui.taglib.Exists _tag0 = new intro2.ui.taglib.Exists(state);
		tag = _tag0;
		while(_tag0.process(out)) {
			out.write("<TD BGCOLOR=\"#FFFFFF\" CLASS=\"NavBarCell1Rev\">&nbsp;<FONT CLASS=\"NavBarFont1Rev\"><B>");
			state.setAttribute("href","projectLink");
			intro2.ui.taglib.HyperLink _tag1 = new intro2.ui.taglib.HyperLink(state);
			tag = _tag1;
			while(_tag1.process(out)) {
				out.write("Project");
			}
			out.write("</B>");
		}
		intro2.ui.taglib.Else _tag2 = new intro2.ui.taglib.Else(state);
		tag = _tag2;
		while(_tag2.process(out)) {
			out.write("<TD BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1\">&nbsp;<FONT CLASS=\"NavBarFont1\">");
			state.setAttribute("href","projectLink");
			intro2.ui.taglib.HyperLink _tag3 = new intro2.ui.taglib.HyperLink(state);
			tag = _tag3;
			while(_tag3.process(out)) {
				out.write("Project");
			}
		}
		out.write("</FONT>&nbsp;</TD>\r\n");
		out.write("\r\n");
		state.setAttribute("flag","revModule");
		intro2.ui.taglib.Exists _tag4 = new intro2.ui.taglib.Exists(state);
		tag = _tag4;
		while(_tag4.process(out)) {
			out.write("<TD BGCOLOR=\"#FFFFFF\" CLASS=\"NavBarCell1Rev\">&nbsp;<FONT CLASS=\"NavBarFont1Rev\"><B>");
			state.setAttribute("href","moduleLink");
			intro2.ui.taglib.HyperLink _tag5 = new intro2.ui.taglib.HyperLink(state);
			tag = _tag5;
			while(_tag5.process(out)) {
				out.write("Module");
			}
			out.write("</B>");
		}
		intro2.ui.taglib.Else _tag6 = new intro2.ui.taglib.Else(state);
		tag = _tag6;
		while(_tag6.process(out)) {
			out.write("<TD BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1\">&nbsp;<FONT CLASS=\"NavBarFont1\">");
			state.setAttribute("href","moduleLink");
			intro2.ui.taglib.HyperLink _tag7 = new intro2.ui.taglib.HyperLink(state);
			tag = _tag7;
			while(_tag7.process(out)) {
				out.write("Module");
			}
		}
		out.write("</FONT>&nbsp;</TD>\r\n");
		out.write("\r\n");
		state.setAttribute("flag","revPackage");
		intro2.ui.taglib.Exists _tag8 = new intro2.ui.taglib.Exists(state);
		tag = _tag8;
		while(_tag8.process(out)) {
			out.write("<TD BGCOLOR=\"#FFFFFF\" CLASS=\"NavBarCell1Rev\">&nbsp;<FONT CLASS=\"NavBarFont1Rev\"><B>");
			state.setAttribute("href","packageLink");
			intro2.ui.taglib.HyperLink _tag9 = new intro2.ui.taglib.HyperLink(state);
			tag = _tag9;
			while(_tag9.process(out)) {
				out.write("Package");
			}
			out.write("</B>");
		}
		intro2.ui.taglib.Else _tag10 = new intro2.ui.taglib.Else(state);
		tag = _tag10;
		while(_tag10.process(out)) {
			out.write("<TD BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1\">&nbsp;<FONT CLASS=\"NavBarFont1\">");
			state.setAttribute("href","packageLink");
			intro2.ui.taglib.HyperLink _tag11 = new intro2.ui.taglib.HyperLink(state);
			tag = _tag11;
			while(_tag11.process(out)) {
				out.write("Package");
			}
		}
		out.write("</FONT>&nbsp;</TD>\r\n");
		out.write("\r\n");
		state.setAttribute("flag","revClass");
		intro2.ui.taglib.Exists _tag12 = new intro2.ui.taglib.Exists(state);
		tag = _tag12;
		while(_tag12.process(out)) {
			out.write("<TD BGCOLOR=\"#FFFFFF\" CLASS=\"NavBarCell1Rev\">&nbsp;<FONT CLASS=\"NavBarFont1Rev\"><B>");
			state.setAttribute("href","classLink");
			intro2.ui.taglib.HyperLink _tag13 = new intro2.ui.taglib.HyperLink(state);
			tag = _tag13;
			while(_tag13.process(out)) {
				out.write("Class");
			}
			out.write("</B>");
		}
		intro2.ui.taglib.Else _tag14 = new intro2.ui.taglib.Else(state);
		tag = _tag14;
		while(_tag14.process(out)) {
			out.write("<TD BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1\">&nbsp;<FONT CLASS=\"NavBarFont1\">");
			state.setAttribute("href","classLink");
			intro2.ui.taglib.HyperLink _tag15 = new intro2.ui.taglib.HyperLink(state);
			tag = _tag15;
			while(_tag15.process(out)) {
				out.write("Class");
			}
		}
		out.write("</FONT>&nbsp;</TD>\r\n");
		out.write("\r\n");
		state.setAttribute("flag","revUses");
		intro2.ui.taglib.Exists _tag16 = new intro2.ui.taglib.Exists(state);
		tag = _tag16;
		while(_tag16.process(out)) {
			out.write("<TD BGCOLOR=\"#FFFFFF\" CLASS=\"NavBarCell1Rev\">&nbsp;<FONT CLASS=\"NavBarFont1Rev\"><B>");
			state.setAttribute("href","usesLink");
			intro2.ui.taglib.HyperLink _tag17 = new intro2.ui.taglib.HyperLink(state);
			tag = _tag17;
			while(_tag17.process(out)) {
				out.write("Uses");
			}
			out.write("</B>");
		}
		intro2.ui.taglib.Else _tag18 = new intro2.ui.taglib.Else(state);
		tag = _tag18;
		while(_tag18.process(out)) {
			out.write("<TD BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1\">&nbsp;<FONT CLASS=\"NavBarFont1\">");
			state.setAttribute("href","usesLink");
			intro2.ui.taglib.HyperLink _tag19 = new intro2.ui.taglib.HyperLink(state);
			tag = _tag19;
			while(_tag19.process(out)) {
				out.write("Uses");
			}
		}
		out.write("</FONT>&nbsp;</TD>\r\n");
		out.write("\r\n");
		state.setAttribute("flag","revUsedby");
		intro2.ui.taglib.Exists _tag20 = new intro2.ui.taglib.Exists(state);
		tag = _tag20;
		while(_tag20.process(out)) {
			out.write("<TD BGCOLOR=\"#FFFFFF\" CLASS=\"NavBarCell1Rev\">&nbsp;<FONT CLASS=\"NavBarFont1Rev\"><B>");
			state.setAttribute("href","usedbyLink");
			intro2.ui.taglib.HyperLink _tag21 = new intro2.ui.taglib.HyperLink(state);
			tag = _tag21;
			while(_tag21.process(out)) {
				out.write("Used-by");
			}
			out.write("</B>");
		}
		intro2.ui.taglib.Else _tag22 = new intro2.ui.taglib.Else(state);
		tag = _tag22;
		while(_tag22.process(out)) {
			out.write("<TD BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1\">&nbsp;<FONT CLASS=\"NavBarFont1\">");
			state.setAttribute("href","usedbyLink");
			intro2.ui.taglib.HyperLink _tag23 = new intro2.ui.taglib.HyperLink(state);
			tag = _tag23;
			while(_tag23.process(out)) {
				out.write("Used-by");
			}
		}
		out.write("</FONT>&nbsp;</TD>\r\n");
		out.write("\r\n");
		intro2.ui.taglib.Ignore _tag24 = new intro2.ui.taglib.Ignore(state);
		tag = _tag24;
		_tag24.setContent("\n<exists flag=\"revBytecode\"><TD BGCOLOR=\"#FFFFFF\" CLASS=\"NavBarCell1Rev\">&nbsp;<FONT CLASS=\"NavBarFont1Rev\"><B><hyperlink href=\"bytecodeLink\">Bytecode</hyperlink></B></exists>\n<else><TD BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1\">&nbsp;<FONT CLASS=\"NavBarFont1\"><hyperlink href=\"bytecodeLink\">Bytecode</hyperlink></else></FONT>&nbsp;</TD>\n");
		while(_tag24.process(out)) {
		}
		out.write("</TR>\r\n");
		out.write("</TABLE>\r\n");
		out.write("</TD>\r\n");
		out.write("<TD ALIGN=\"right\" VALIGN=\"top\" ROWSPAN=3><EM>\r\n");
		out.write("<B>\r\n");
		out.write(state.getStringAttribute("projectName"));
		out.write("</B></EM>\r\n");
		out.write("</TD>\r\n");
		out.write("<TD ALIGN=\"right\" VALIGN=\"top\" ROWSPAN=3>\r\n");
		out.write("<FONT CLASS=\"Logo\">\r\n");
		out.write("Intro &szlig;\r\n");
		out.write("</FONT>\r\n");
		out.write("</TD>\r\n");
		out.write("</TR>\r\n");
		out.write("\r\n");
		out.write("<TR>\r\n");
		out.write("<TD BGCOLOR=\"white\" CLASS=\"NavBarCell2\"><FONT SIZE=\"-2\">\r\n");
		state.setAttribute("href","allModsLink");
		intro2.ui.taglib.HyperLink _tag25 = new intro2.ui.taglib.HyperLink(state);
		tag = _tag25;
		while(_tag25.process(out)) {
			out.write("<B>ALL MODULES</B>");
		}
		out.write("&nbsp;&nbsp;\r\n");
		state.setAttribute("href","allPkgsLink");
		intro2.ui.taglib.HyperLink _tag26 = new intro2.ui.taglib.HyperLink(state);
		tag = _tag26;
		while(_tag26.process(out)) {
			out.write("<B>ALL PACKAGES</B>");
		}
		out.write("&nbsp;&nbsp;\r\n");
		state.setAttribute("href","allClzsLink");
		intro2.ui.taglib.HyperLink _tag27 = new intro2.ui.taglib.HyperLink(state);
		tag = _tag27;
		while(_tag27.process(out)) {
			out.write("<B>ALL CLASSES</B>");
		}
		out.write("</FONT></TD>\r\n");
		out.write("</TR>\r\n");
		out.write("<TR>\r\n");
		out.write("<TD COLSPAN=2 VALIGN=\"top\" CLASS=\"NavBarCell3\"><FONT SIZE=\"-2\">");
	}
}
