package intro2.ui;

public final class InnerClassSubPage implements lahaina.runtime.Page {

	public void InnerClassSubPage() {}

	public void process(lahaina.runtime.Writer out, lahaina.runtime.State state) throws Exception {
		lahaina.tag.Tag tag = null;

		state.setAttribute("flag","innerref.ispublic");
		intro2.ui.taglib.Is _tag0 = new intro2.ui.taglib.Is(state);
		tag = _tag0;
		while(_tag0.process(out)) {
			out.write("public&nbsp;");
		}
		state.setAttribute("flag","innerref.isprotected");
		intro2.ui.taglib.Is _tag1 = new intro2.ui.taglib.Is(state);
		tag = _tag1;
		while(_tag1.process(out)) {
			out.write("protected&nbsp;");
		}
		state.setAttribute("flag","innerref.isprivate");
		intro2.ui.taglib.Is _tag2 = new intro2.ui.taglib.Is(state);
		tag = _tag2;
		while(_tag2.process(out)) {
			out.write("private&nbsp;");
		}
		state.setAttribute("flag","innerref.isstatic");
		intro2.ui.taglib.Is _tag3 = new intro2.ui.taglib.Is(state);
		tag = _tag3;
		while(_tag3.process(out)) {
			out.write("static&nbsp;");
		}
		state.setAttribute("flag","innerref.isfinal");
		intro2.ui.taglib.Is _tag4 = new intro2.ui.taglib.Is(state);
		tag = _tag4;
		while(_tag4.process(out)) {
			out.write("final&nbsp;");
		}
		state.setAttribute("flag","innerref.isinterface");
		intro2.ui.taglib.Not _tag5 = new intro2.ui.taglib.Not(state);
		tag = _tag5;
		while(_tag5.process(out)) {
			state.setAttribute("flag","innerref.isabstract");
			intro2.ui.taglib.Is _tag6 = new intro2.ui.taglib.Is(state);
			tag = _tag6;
			while(_tag6.process(out)) {
				out.write("abstract&nbsp");
			}
		}
		state.setAttribute("flag","innerref.isinterface");
		intro2.ui.taglib.Is _tag7 = new intro2.ui.taglib.Is(state);
		tag = _tag7;
		while(_tag7.process(out)) {
			out.write("interface");
		}
		intro2.ui.taglib.Else _tag8 = new intro2.ui.taglib.Else(state);
		tag = _tag8;
		while(_tag8.process(out)) {
			out.write("class");
		}
		out.write("&nbsp;\r\n");
		state.setAttribute("flag","innerref.isanonymous");
		intro2.ui.taglib.Is _tag9 = new intro2.ui.taglib.Is(state);
		tag = _tag9;
		while(_tag9.process(out)) {
			out.write("[anonymous]");
		}
		intro2.ui.taglib.Else _tag10 = new intro2.ui.taglib.Else(state);
		tag = _tag10;
		while(_tag10.process(out)) {
			out.write(state.getStringAttribute("innerref.declaredname"));
		}
	}
}
