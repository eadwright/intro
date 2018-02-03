package lahaina.compile;

import lahaina.fragment.*;
import lahaina.runtime.LahainaException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class Source {
	private static final String CRLF;

	static {
		if(System.getProperty("line.separator").length() == 2)
			CRLF = "\\r\\n";
		else
			CRLF = null;
	}

	private static Pattern findQuote = Pattern.compile("\\\"", Pattern.MULTILINE);
//	private static Pattern findCR = Pattern.compile("$", Pattern.MULTILINE);
	private static Pattern findCR = Pattern.compile("[\\x0a\\x0d]", Pattern.MULTILINE);
	private static Pattern findESC = Pattern.compile("\\\\");

	// find declarations data
	private String classname;
	private String superclass;
	private String pkg;
	private List importsList = new ArrayList();
	private List implementsList = new ArrayList();
	private StringBuffer nonInlineCode = new StringBuffer();

	// create header data
	private StringBuffer header = new StringBuffer();

	// create body data
	private StringBuffer body = new StringBuffer();
	private int tagNumberCounter = 0;

	Source(Fragment first) {
		findDeclarations(first);
		createHeader();
		createBody(first);
		body.append("\t}\n}\n");
	}

	String getClassName() {
		return classname;
	}

	String getPackage() {
		return pkg;
	}

	public String toString() {
		StringBuffer total = new StringBuffer();

		total.append(header);
		total.append(body);

		return total.toString();
	}

	 // look for declarations & non-inline code, process them
	private void findDeclarations(Fragment f) {
		do {
			if(f instanceof DeclarationFragment)
				processDeclaration((DeclarationFragment)f);

			if((f instanceof CodeFragment) && (!((CodeFragment)f).isInline()))
				processNonInlineCode((CodeFragment)f);

			if(f instanceof TagFragment) {
				Fragment f2 = ((TagFragment)f).getChild();

				if(f2 != null)
					findDeclarations(f2);
			}

			f = f.getNext();
		} while (f != null);
	}

	private void processDeclaration(DeclarationFragment d) { // <@> tags
		int i,j;
		String name, value;
		List attrNames;
		boolean found;

		attrNames = d.getAttributeNames();
		for(j=0;j<attrNames.size();j++) {
			name = ((String)attrNames.get(j)).toLowerCase();
			value = d.getValue(j);

			found = false;
			if(name.equals("classname")) {
				if(classname != null)
					throw new LahainaException("classname defined twice");

				classname = value;
				found = true;
			}

			if(name.equals("extends")) {
				if(superclass != null)
					throw new LahainaException("extends defined twice");

				superclass = value;
				found = true;
			}

			if(name.equals("import")) {
				importsList.add(value);
				found = true;
			}

			if(name.equals("implements")) {
				implementsList.add(value);
				found = true;
			}

			if(!found)
				throw new LahainaException("declartion attribute "+name+" not understood");
		}
	}

	private void processNonInlineCode(CodeFragment c) { // <#> tags
		nonInlineCode.append(c.getCode());
		nonInlineCode.append('\n');
	}

	private void createHeader() { // create header
		if(classname == null)
			throw new LahainaException("classname not defined");

		pkg = null;
		int i = classname.lastIndexOf('.');
		if(i != -1) {
			if(i == classname.length()-1)
				throw new LahainaException("classname can't end in '.'");

			pkg = classname.substring(0, i);
			classname = classname.substring(i+1);
		}

		if(pkg != null) {
			header.append("package ");
			header.append(pkg);
			header.append(";\n\n");
		}

		for(i=0;i<importsList.size();i++) {
			header.append("import ");
			header.append((String)importsList.get(i));
			header.append(";\n");
		}

		if(importsList.size()>0)
			header.append('\n');

		implementsList.add("lahaina.runtime.Page");

		header.append("public final class ");
		header.append(classname);

		if(superclass != null) {
			header.append(" extends ");
			header.append(superclass);
		}

		header.append(" implements ");
		for(i=0;i<implementsList.size();i++) {
			header.append((String)implementsList.get(i));
			if((i>0) && (i<implementsList.size()-1))
				header.append(", ");
		}

		header.append(" {\n\n");

		header.append("\tpublic void ");
		header.append(classname);
		header.append("() {}\n\n");

		if(nonInlineCode.length()>0) {
			header.append(nonInlineCode);
			header.append("\n");
		}

		header.append("\tpublic void process(lahaina.runtime.Writer out, lahaina.runtime.State state) throws Exception {\n");
		header.append("\t\tlahaina.tag.Tag tag = null;\n\n");
	}

	private void createBody(Fragment f) {
		createBody(f, 2);
	}

	private void createBody(Fragment f, int tabs) {
		while(f != null) {

			if(f instanceof CommentFragment) {
				insertTabs(body, tabs);
				body.append("out.write(\"<!-- ");
				body.append(escapeString(((CommentFragment)f).getComment()));
				body.append(" -->\");\n");
			}

			if(f instanceof TextFragment)
				insertTextFragment((TextFragment)f, tabs);

			if((f instanceof CodeFragment) && (((CodeFragment)f).isInline())) {
				insertTabs(body, tabs);
				CodeFragment c = (CodeFragment)f;

				if(c.isAssignment()) {
					body.append("out.write(state.getStringAttribute(\"");
					body.append(escapeString(c.getCode()));
					body.append("\"));\n");
				} else {
					body.append(c.getCode());
					body.append("\n");
				}
			}

			if(f instanceof TagFragment)
				insertTagFragment((TagFragment)f, tabs);

			f = f.getNext();
		}
	}

	private void insertTextFragment(TextFragment t, int tabs) {
		String str = escapeString(t.getText());

		int index;
		int pos = 0;
		String str2;

		do {
			index = str.indexOf("\\n", pos);

			if(index != -1) {
				if(CRLF != null)
					insertTextFragmentPart(str.substring(pos, index) + CRLF, tabs);
				else
					insertTextFragmentPart(str.substring(pos, index+2), tabs);

				pos = index+2;
			} else
				insertTextFragmentPart(str.substring(pos), tabs);
		} while (index != -1);
	}

	private void insertTextFragmentPart(String text, int tabs) {
		if(text.length()>0) {
			insertTabs(body, tabs);
			body.append("out.write(\"");
			body.append(text);
			body.append("\");\n");
		}
	}

	private void insertTagFragment(TagFragment t, int tabs) {
		boolean nonEmpty = t.allowsContent();
		boolean processes = t.processesContent();

		Map m = t.getAttributes();
		String key, value;
		Iterator it = m.keySet().iterator();
		while(it.hasNext()) {
			key = (String)it.next();
			value = (String)m.get(key);

			insertTabs(body, tabs);
			body.append("state.setAttribute(\"");
			body.append(escapeString(key));
			body.append("\",\"");
			body.append(escapeString(value));
			body.append("\");\n");
		}

		insertTabs(body, tabs);

		int tagNum = tagNumberCounter++;
		String name = "_tag"+tagNum;

		body.append(t.getTagClass().getName());
		body.append(' ');

		body.append(name);
		body.append(" = new ");
		body.append(t.getTagClass().getName());
		body.append("(state);\n");

		insertTabs(body, tabs);

		body.append("tag = ");
		body.append(name);
		body.append(";\n");

		if(processes) {
			insertTabs(body, tabs);
			body.append(name);
			body.append(".setContent(\"");
			body.append(escapeString(t.getContent()));
			body.append("\");\n");
		}

		insertTabs(body, tabs);
		if(nonEmpty) {
			body.append("while(");
			body.append(name);
			body.append(".process(out)) {\n");

			Fragment child = t.getChild();
			if(child != null)
				createBody(child, tabs+1);

			insertTabs(body, tabs);
			body.append("}\n");
		} else {
			body.append("while(");
			body.append(name);
			body.append(".process(out));\n");
		}
	}

	private void insertTabs(StringBuffer sb, int num) {
		for(int i=0;i<num;i++)
			sb.append('\t');
	}

	private String escapeString(String str) {
		String f = str;

		Matcher matcher = findESC.matcher(str);
//		System.out.println("FE "+matcher.find());
		str = matcher.replaceAll("\\\\\\\\");

		matcher = findQuote.matcher(str);
//		System.out.println("FQ "+matcher.find());
		str = matcher.replaceAll("\\\\\"");

		matcher = findCR.matcher(str);
//		System.out.println("CR "+matcher.find());
		str = matcher.replaceAll("\\\\n");

//		System.out.println("before "+f);
//		System.out.println("after "+str);

		// test

//		if(str.length() > 2)
//			return str.substring(0, str.length()-2);
//		else
			return str;

//		return str.substring(0, str.length()-2);
	}

// Have a method to write the source out to a file, given a dir

// Have another method to get the source as a String, we can use this for the
// above and for debug
}