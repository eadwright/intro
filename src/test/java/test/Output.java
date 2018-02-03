package test;

public final class Output implements lahaina.runtime.Page {

	public void Output() {}

private void aMethod() {
	int i = 3;
}

	public void process(lahaina.runtime.Writer out, lahaina.runtime.State state) throws Exception {
		lahaina.tag.Tag tag = null;

		out.write("<tag1 someattr=\"1\\a2\\>3\" another=\"abc\">some text\n");
		int i = 7;
i++;
		out.write("<tag2/>\n");
		test.Tag3 _tag0 = new test.Tag3(state);
		tag = _tag0;
		while(_tag0.process(out)) {
		}
		out.write("Wibble\n");
		test.Tag3 _tag1 = new test.Tag3(state);
		tag = _tag1;
		state.setAttribute("attribute","xyz");
		state.setAttribute("fred","123");
		while(_tag1.process(out)) {
			out.write("Data for the special tag");
		}
		out.write("Just some free text here\n</tag1>\n");
		out.write(state.getStringAttribute("a.value"));
		out.write("<tag4>\n<tag5/>\n</tag4>");
	}
}
