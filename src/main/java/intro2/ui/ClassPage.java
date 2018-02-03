package intro2.ui;

public final class ClassPage implements lahaina.runtime.Page {

	public void ClassPage() {}

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
		out.write("DETAILS:&nbsp;");
		state.setAttribute("href","fields");
		intro2.ui.taglib.HyperLink _tag2 = new intro2.ui.taglib.HyperLink(state);
		tag = _tag2;
		while(_tag2.process(out)) {
			out.write("FIELDS");
		}
		out.write("&nbsp;|&nbsp;");
		state.setAttribute("href","methods");
		intro2.ui.taglib.HyperLink _tag3 = new intro2.ui.taglib.HyperLink(state);
		tag = _tag3;
		while(_tag3.process(out)) {
			out.write("METHODS");
		}
		out.write("&nbsp;|&nbsp;");
		state.setAttribute("href","innerClasses");
		intro2.ui.taglib.HyperLink _tag4 = new intro2.ui.taglib.HyperLink(state);
		tag = _tag4;
		while(_tag4.process(out)) {
			out.write("INNER-CLASSES");
		}
		out.write("&nbsp;\r\n");
		out.write("</FONT></TD>\r\n");
		out.write("</TR>\r\n");
		out.write("</TABLE>\r\n");
		out.write("<!-- =========== END OF NAVBAR =========== -->\r\n");
		out.write("<HR>\r\n");
		out.write("<!-- ======== START OF CLASS DATA ======== -->\r\n");
		out.write("<H2><FONT SIZE=\"-1\">");
		out.write(state.getStringAttribute("this.displayPackage"));
		out.write("</FONT><br>\r\n");
		state.setAttribute("flag","this.isinterface");
		intro2.ui.taglib.Is _tag5 = new intro2.ui.taglib.Is(state);
		tag = _tag5;
		while(_tag5.process(out)) {
			out.write("Interface");
		}
		intro2.ui.taglib.Else _tag6 = new intro2.ui.taglib.Else(state);
		tag = _tag6;
		while(_tag6.process(out)) {
			out.write("Class");
		}
		out.write("&nbsp;");
		out.write(state.getStringAttribute("this.shortName"));
		out.write("</H2>\r\n");
		out.write("\r\n");
		state.setAttribute("flag","this.ispublic");
		intro2.ui.taglib.Is _tag7 = new intro2.ui.taglib.Is(state);
		tag = _tag7;
		while(_tag7.process(out)) {
			out.write("public&nbsp;");
		}
		state.setAttribute("flag","this.isinterface");
		intro2.ui.taglib.Not _tag8 = new intro2.ui.taglib.Not(state);
		tag = _tag8;
		while(_tag8.process(out)) {
			state.setAttribute("flag","this.isabstract");
			intro2.ui.taglib.Is _tag9 = new intro2.ui.taglib.Is(state);
			tag = _tag9;
			while(_tag9.process(out)) {
				out.write("abstract&nbsp");
			}
		}
		state.setAttribute("flag","this.isfinal");
		intro2.ui.taglib.Is _tag10 = new intro2.ui.taglib.Is(state);
		tag = _tag10;
		while(_tag10.process(out)) {
			out.write("final&nbsp;");
		}
		state.setAttribute("flag","this.isinterface");
		intro2.ui.taglib.Is _tag11 = new intro2.ui.taglib.Is(state);
		tag = _tag11;
		while(_tag11.process(out)) {
			out.write("interface");
		}
		intro2.ui.taglib.Else _tag12 = new intro2.ui.taglib.Else(state);
		tag = _tag12;
		while(_tag12.process(out)) {
			out.write("class");
		}
		out.write("&nbsp;");
		out.write(state.getStringAttribute("this.shortName"));
		state.setAttribute("id","this.superclassid");
		intro2.ui.taglib.GetClass _tag13 = new intro2.ui.taglib.GetClass(state);
		tag = _tag13;
		while(_tag13.process(out));
		state.setAttribute("flag","this.interface");
		intro2.ui.taglib.Not _tag14 = new intro2.ui.taglib.Not(state);
		tag = _tag14;
		while(_tag14.process(out)) {
			state.setAttribute("flag","displayName");
			intro2.ui.taglib.Exists _tag15 = new intro2.ui.taglib.Exists(state);
			tag = _tag15;
			while(_tag15.process(out)) {
				out.write("&nbsp;extends&nbsp;");
				intro2.ui.taglib.HyperLink _tag16 = new intro2.ui.taglib.HyperLink(state);
				tag = _tag16;
				while(_tag16.process(out)) {
					out.write(state.getStringAttribute("displayName"));
				}
			}
		}
		state.setAttribute("flag","this.implementsinterfaces");
		intro2.ui.taglib.Is _tag17 = new intro2.ui.taglib.Is(state);
		tag = _tag17;
		while(_tag17.process(out)) {
			out.write("&nbsp;implements&nbsp;\r\n");
			state.setAttribute("listName","this.interfaceids");
			intro2.ui.taglib.ClassList _tag18 = new intro2.ui.taglib.ClassList(state);
			tag = _tag18;
			while(_tag18.process(out)) {
				intro2.ui.taglib.HyperLink _tag19 = new intro2.ui.taglib.HyperLink(state);
				tag = _tag19;
				while(_tag19.process(out)) {
					out.write(state.getStringAttribute("displayName"));
				}
				out.write(state.getStringAttribute("comma"));
			}
		}
		out.write("<br>\r\n");
		state.setAttribute("type","this");
		state.setAttribute("pageClass","intro2.ui.NRSubPage");
		lahaina.tag.Include _tag20 = new lahaina.tag.Include(state);
		tag = _tag20;
		while(_tag20.process(out));
		state.setAttribute("flag","this.isdeprecated");
		intro2.ui.taglib.Is _tag21 = new intro2.ui.taglib.Is(state);
		tag = _tag21;
		while(_tag21.process(out)) {
			out.write("<b>Deprecated</b>&nbsp;");
		}
		out.write("<P>\r\n");
		intro2.ui.taglib.ClassTree _tag22 = new intro2.ui.taglib.ClassTree(state);
		tag = _tag22;
		while(_tag22.process(out)) {
			state.setAttribute("counterName","up");
			intro2.ui.taglib.Loop _tag23 = new intro2.ui.taglib.Loop(state);
			tag = _tag23;
			while(_tag23.process(out)) {
				out.write("<UL>");
			}
			out.write("<LI type=\"circle\">");
			intro2.ui.taglib.HyperLink _tag24 = new intro2.ui.taglib.HyperLink(state);
			tag = _tag24;
			while(_tag24.process(out)) {
				out.write(state.getStringAttribute("name"));
			}
			state.setAttribute("counterName","down");
			intro2.ui.taglib.Loop _tag25 = new intro2.ui.taglib.Loop(state);
			tag = _tag25;
			while(_tag25.process(out)) {
				out.write("</UL>");
			}
		}
		out.write("<P>\r\n");
		out.write("\r\n");
		state.setAttribute("flag","this.isInnerClass");
		intro2.ui.taglib.Is _tag26 = new intro2.ui.taglib.Is(state);
		tag = _tag26;
		while(_tag26.process(out)) {
			out.write("Inner&nbsp;");
			state.setAttribute("flag","this.isinterface");
			intro2.ui.taglib.Is _tag27 = new intro2.ui.taglib.Is(state);
			tag = _tag27;
			while(_tag27.process(out)) {
				out.write("interface");
			}
			intro2.ui.taglib.Else _tag28 = new intro2.ui.taglib.Else(state);
			tag = _tag28;
			while(_tag28.process(out)) {
				out.write("class");
			}
			out.write("&nbsp;defined as:\r\n");
			state.setAttribute("to","innerref");
			state.setAttribute("from","this.innerClassSelfReference");
			intro2.ui.taglib.Assign _tag29 = new intro2.ui.taglib.Assign(state);
			tag = _tag29;
			while(_tag29.process(out));
			state.setAttribute("pageClass","intro2.ui.InnerClassSubPage");
			lahaina.tag.Include _tag30 = new lahaina.tag.Include(state);
			tag = _tag30;
			while(_tag30.process(out));
			out.write("<br>\r\n");
			state.setAttribute("id","innerref.targetclassid");
			intro2.ui.taglib.GetClass _tag31 = new intro2.ui.taglib.GetClass(state);
			tag = _tag31;
			while(_tag31.process(out));
			state.setAttribute("flag","ci");
			intro2.ui.taglib.Exists _tag32 = new intro2.ui.taglib.Exists(state);
			tag = _tag32;
			while(_tag32.process(out)) {
				state.setAttribute("flag","ci.interface");
				intro2.ui.taglib.Is _tag33 = new intro2.ui.taglib.Is(state);
				tag = _tag33;
				while(_tag33.process(out)) {
					out.write("Outer interface:&nbsp;");
				}
				intro2.ui.taglib.Else _tag34 = new intro2.ui.taglib.Else(state);
				tag = _tag34;
				while(_tag34.process(out)) {
					out.write("Outer class:&nbsp;");
				}
			}
			intro2.ui.taglib.Else _tag35 = new intro2.ui.taglib.Else(state);
			tag = _tag35;
			while(_tag35.process(out)) {
				out.write("Outer class:&nbsp;");
			}
			intro2.ui.taglib.HyperLink _tag36 = new intro2.ui.taglib.HyperLink(state);
			tag = _tag36;
			while(_tag36.process(out)) {
				out.write(state.getStringAttribute("displayName"));
			}
			out.write("<br>");
		}
		out.write("<p>\r\n");
		state.setAttribute("flag","this.fieldoffsets");
		intro2.ui.taglib.Exists _tag37 = new intro2.ui.taglib.Exists(state);
		tag = _tag37;
		while(_tag37.process(out)) {
			out.write("<!-- ======== FIELD SUMMARY ======== -->\r\n");
			out.write("\r\n");
			out.write("<A NAME=\"fields\"></A>\r\n");
			out.write("<TABLE BORDER=\"1\" CELLPADDING=\"3\" CELLSPACING=\"0\" WIDTH=\"100%\">\r\n");
			out.write("<TR CLASS=\"TableHeadingColor\">\r\n");
			out.write("<TD><FONT SIZE=\"+2\">\r\n");
			out.write("<B>Fields</B></FONT></TD>\r\n");
			out.write("</TR>\r\n");
			intro2.ui.taglib.FieldList _tag38 = new intro2.ui.taglib.FieldList(state);
			tag = _tag38;
			while(_tag38.process(out)) {
				out.write("<TR BGCOLOR=\"white\" CLASS=\"TableRowColor\">\r\n");
				out.write("<TD>\r\n");
				out.write(state.getStringAttribute("current.name"));
				out.write("<br>\r\n");
				out.write("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\r\n");
				state.setAttribute("flag","current.ispublic");
				intro2.ui.taglib.Is _tag39 = new intro2.ui.taglib.Is(state);
				tag = _tag39;
				while(_tag39.process(out)) {
					out.write("public&nbsp;");
				}
				state.setAttribute("flag","current.isprotected");
				intro2.ui.taglib.Is _tag40 = new intro2.ui.taglib.Is(state);
				tag = _tag40;
				while(_tag40.process(out)) {
					out.write("protected&nbsp;");
				}
				state.setAttribute("flag","current.isprivate");
				intro2.ui.taglib.Is _tag41 = new intro2.ui.taglib.Is(state);
				tag = _tag41;
				while(_tag41.process(out)) {
					out.write("private&nbsp;");
				}
				state.setAttribute("flag","current.isstatic");
				intro2.ui.taglib.Is _tag42 = new intro2.ui.taglib.Is(state);
				tag = _tag42;
				while(_tag42.process(out)) {
					out.write("static&nbsp;");
				}
				state.setAttribute("flag","current.isfinal");
				intro2.ui.taglib.Is _tag43 = new intro2.ui.taglib.Is(state);
				tag = _tag43;
				while(_tag43.process(out)) {
					out.write("final&nbsp;");
				}
				intro2.ui.taglib.HyperLink _tag44 = new intro2.ui.taglib.HyperLink(state);
				tag = _tag44;
				while(_tag44.process(out)) {
					out.write(state.getStringAttribute("type.type"));
				}
				out.write(state.getStringAttribute("type.arrayInfo"));
				out.write("&nbsp;\r\n");
				out.write(state.getStringAttribute("current.name"));
				state.setAttribute("flag","current.constantValue");
				intro2.ui.taglib.Exists _tag45 = new intro2.ui.taglib.Exists(state);
				tag = _tag45;
				while(_tag45.process(out)) {
					out.write("&nbsp;=&nbsp;");
					out.write(state.getStringAttribute("current.constantValue"));
				}
				out.write("</TD>\r\n");
				out.write("</TR>");
			}
			out.write("</TABLE>\r\n");
			out.write("<P>");
		}
		state.setAttribute("flag","this.methodoffsets");
		intro2.ui.taglib.Exists _tag46 = new intro2.ui.taglib.Exists(state);
		tag = _tag46;
		while(_tag46.process(out)) {
			out.write("<!-- ======== METHOD SUMMARY ======== -->\r\n");
			out.write("\r\n");
			out.write("<A NAME=\"methods\"></A>\r\n");
			out.write("<TABLE BORDER=\"1\" CELLPADDING=\"3\" CELLSPACING=\"0\" WIDTH=\"100%\">\r\n");
			out.write("<TR CLASS=\"TableHeadingColor\">\r\n");
			out.write("<TD><FONT SIZE=\"+2\">\r\n");
			out.write("<B>Methods</B></FONT></TD>\r\n");
			out.write("</TR>\r\n");
			intro2.ui.taglib.MethodList _tag47 = new intro2.ui.taglib.MethodList(state);
			tag = _tag47;
			while(_tag47.process(out)) {
				out.write("<TR BGCOLOR=\"white\" CLASS=\"TableRowColor\">\r\n");
				out.write("<TD>\r\n");
				out.write(state.getStringAttribute("current.displayName"));
				out.write("<br>\r\n");
				out.write("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\r\n");
				state.setAttribute("to","method");
				state.setAttribute("from","current");
				intro2.ui.taglib.Assign _tag48 = new intro2.ui.taglib.Assign(state);
				tag = _tag48;
				while(_tag48.process(out));
				state.setAttribute("pageClass","intro2.ui.MethodSubPage");
				lahaina.tag.Include _tag49 = new lahaina.tag.Include(state);
				tag = _tag49;
				while(_tag49.process(out));
				out.write("</TD>\r\n");
				out.write("</TR>");
			}
			out.write("</TABLE>\r\n");
			out.write("<P>");
		}
		state.setAttribute("flag","this.hasInnerClasses");
		intro2.ui.taglib.Is _tag50 = new intro2.ui.taglib.Is(state);
		tag = _tag50;
		while(_tag50.process(out)) {
			out.write("<!-- ======== INNER CLASS SUMMARY ======== -->\r\n");
			out.write("\r\n");
			out.write("<A NAME=\"innerClasses\"></A>\r\n");
			out.write("<TABLE BORDER=\"1\" CELLPADDING=\"3\" CELLSPACING=\"0\" WIDTH=\"100%\">\r\n");
			out.write("<TR CLASS=\"TableHeadingColor\">\r\n");
			out.write("<TD><FONT SIZE=\"+2\">\r\n");
			out.write("<B>Inner Classes</B></FONT></TD>\r\n");
			out.write("</TR>\r\n");
			intro2.ui.taglib.InnerClassListInit _tag51 = new intro2.ui.taglib.InnerClassListInit(state);
			tag = _tag51;
			while(_tag51.process(out));
			state.setAttribute("listName","innerClasses");
			intro2.ui.taglib.ClassList _tag52 = new intro2.ui.taglib.ClassList(state);
			tag = _tag52;
			while(_tag52.process(out)) {
				out.write("<TR BGCOLOR=\"white\" CLASS=\"TableRowColor\">\r\n");
				out.write("<TD>");
				state.setAttribute("pageClass","intro2.ui.InnerClassSubPage");
				lahaina.tag.Include _tag53 = new lahaina.tag.Include(state);
				tag = _tag53;
				while(_tag53.process(out));
				out.write("<br>\r\n");
				intro2.ui.taglib.HyperLink _tag54 = new intro2.ui.taglib.HyperLink(state);
				tag = _tag54;
				while(_tag54.process(out)) {
					out.write("<CODE><B>");
					out.write(state.getStringAttribute("displayName"));
					out.write("</B></CODE>");
				}
				state.setAttribute("type","ci");
				state.setAttribute("pageClass","intro2.ui.NRSubPage");
				lahaina.tag.Include _tag55 = new lahaina.tag.Include(state);
				tag = _tag55;
				while(_tag55.process(out));
				out.write("</TD>\r\n");
				out.write("</TR>");
			}
			out.write("</TABLE>\r\n");
			out.write("<P>");
		}
		out.write("<!-- ========= END OF CLASS DATA ========= -->\r\n");
		out.write("\r\n");
		state.setAttribute("pageClass","intro2.ui.KeySubPage");
		lahaina.tag.Include _tag56 = new lahaina.tag.Include(state);
		tag = _tag56;
		while(_tag56.process(out));
		state.setAttribute("pageClass","intro2.ui.NavbarSubPage");
		lahaina.tag.Include _tag57 = new lahaina.tag.Include(state);
		tag = _tag57;
		while(_tag57.process(out));
		out.write("SUMMARY:&nbsp;");
		state.setAttribute("href","subPackages");
		intro2.ui.taglib.HyperLink _tag58 = new intro2.ui.taglib.HyperLink(state);
		tag = _tag58;
		while(_tag58.process(out)) {
			out.write("SUB-PACKAGES");
		}
		out.write("&nbsp;|&nbsp;");
		state.setAttribute("href","interfaces");
		intro2.ui.taglib.HyperLink _tag59 = new intro2.ui.taglib.HyperLink(state);
		tag = _tag59;
		while(_tag59.process(out)) {
			out.write("INTERFACES");
		}
		out.write("&nbsp;|&nbsp;");
		state.setAttribute("href","classes");
		intro2.ui.taglib.HyperLink _tag60 = new intro2.ui.taglib.HyperLink(state);
		tag = _tag60;
		while(_tag60.process(out)) {
			out.write("CLASSES");
		}
		out.write("&nbsp;\r\n");
		out.write("</FONT></TD>\r\n");
		out.write("</TR>\r\n");
		out.write("</TABLE>\r\n");
		out.write("<!-- =========== END OF NAVBAR =========== -->\r\n");
		out.write("\r\n");
		state.setAttribute("pageClass","intro2.ui.FooterSubPage");
		lahaina.tag.Include _tag61 = new lahaina.tag.Include(state);
		tag = _tag61;
		while(_tag61.process(out));
	}
}
