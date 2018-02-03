package intro2.ui;

public final class MethodSubPage implements lahaina.runtime.Page {

	public void MethodSubPage() {}

	public void process(lahaina.runtime.Writer out, lahaina.runtime.State state) throws Exception {
		lahaina.tag.Tag tag = null;

		state.setAttribute("flag","method.ispublic");
		intro2.ui.taglib.Is _tag0 = new intro2.ui.taglib.Is(state);
		tag = _tag0;
		while(_tag0.process(out)) {
			out.write("public&nbsp;");
		}
		state.setAttribute("flag","method.isprotected");
		intro2.ui.taglib.Is _tag1 = new intro2.ui.taglib.Is(state);
		tag = _tag1;
		while(_tag1.process(out)) {
			out.write("protected&nbsp;");
		}
		state.setAttribute("flag","method.isprivate");
		intro2.ui.taglib.Is _tag2 = new intro2.ui.taglib.Is(state);
		tag = _tag2;
		while(_tag2.process(out)) {
			out.write("private&nbsp;");
		}
		state.setAttribute("flag","method.isabstract");
		intro2.ui.taglib.Is _tag3 = new intro2.ui.taglib.Is(state);
		tag = _tag3;
		while(_tag3.process(out)) {
			out.write("abstract&nbsp;");
		}
		state.setAttribute("flag","method.isstatic");
		intro2.ui.taglib.Is _tag4 = new intro2.ui.taglib.Is(state);
		tag = _tag4;
		while(_tag4.process(out)) {
			out.write("static&nbsp;");
		}
		state.setAttribute("flag","method.isfinal");
		intro2.ui.taglib.Is _tag5 = new intro2.ui.taglib.Is(state);
		tag = _tag5;
		while(_tag5.process(out)) {
			out.write("final&nbsp;");
		}
		state.setAttribute("flag","method.isnative");
		intro2.ui.taglib.Is _tag6 = new intro2.ui.taglib.Is(state);
		tag = _tag6;
		while(_tag6.process(out)) {
			out.write("native&nbsp;");
		}
		state.setAttribute("flag","method.isstrict");
		intro2.ui.taglib.Is _tag7 = new intro2.ui.taglib.Is(state);
		tag = _tag7;
		while(_tag7.process(out)) {
			out.write("strict&nbsp;");
		}
		state.setAttribute("flag","method.issynchronized");
		intro2.ui.taglib.Is _tag8 = new intro2.ui.taglib.Is(state);
		tag = _tag8;
		while(_tag8.process(out)) {
			out.write("synchronized&nbsp;");
		}
		state.setAttribute("flag","method.isconstructor");
		intro2.ui.taglib.Not _tag9 = new intro2.ui.taglib.Not(state);
		tag = _tag9;
		while(_tag9.process(out)) {
			intro2.ui.taglib.HyperLink _tag10 = new intro2.ui.taglib.HyperLink(state);
			tag = _tag10;
			while(_tag10.process(out)) {
				out.write(state.getStringAttribute("returnType.displayType"));
			}
			out.write(state.getStringAttribute("returnType.arrayInfo"));
			out.write("&nbsp;");
		}
		out.write(state.getStringAttribute("method.displayName"));
		out.write("(");
		intro2.ui.taglib.MethodArgList _tag11 = new intro2.ui.taglib.MethodArgList(state);
		tag = _tag11;
		while(_tag11.process(out)) {
			intro2.ui.taglib.HyperLink _tag12 = new intro2.ui.taglib.HyperLink(state);
			tag = _tag12;
			while(_tag12.process(out)) {
				out.write(state.getStringAttribute("type.displayType"));
			}
			out.write(state.getStringAttribute("type.arrayInfo"));
			out.write(state.getStringAttribute("comma"));
		}
		out.write(")\r\n");
		state.setAttribute("flag","current.throwsExceptions");
		intro2.ui.taglib.Is _tag13 = new intro2.ui.taglib.Is(state);
		tag = _tag13;
		while(_tag13.process(out)) {
			out.write("&nbsp;throws&nbsp;\r\n");
			state.setAttribute("listName","method.exceptions");
			intro2.ui.taglib.ClassList _tag14 = new intro2.ui.taglib.ClassList(state);
			tag = _tag14;
			while(_tag14.process(out)) {
				intro2.ui.taglib.HyperLink _tag15 = new intro2.ui.taglib.HyperLink(state);
				tag = _tag15;
				while(_tag15.process(out)) {
					out.write(state.getStringAttribute("displayName"));
				}
				out.write(state.getStringAttribute("comma"));
			}
		}
		state.setAttribute("flag","method.isnative");
		intro2.ui.taglib.Is _tag16 = new intro2.ui.taglib.Is(state);
		tag = _tag16;
		while(_tag16.process(out)) {
			out.write("&nbsp;(<FONT CLASS = \"NKey\">N</FONT>)");
		}
	}
}
