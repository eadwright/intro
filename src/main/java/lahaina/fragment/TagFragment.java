package lahaina.fragment;

import lahaina.runtime.LahainaException;
import lahaina.tag.ContentProcessTag;
import lahaina.tag.NonEmptyTag;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TagFragment extends HasAttributes {
	private Map attributes;
	private Class tagClass;
	private Fragment child;
	private String content;

	public TagFragment(Class tagClass) {
		attributes = new HashMap();
		this.tagClass = tagClass;
	}

	public Map getAttributes() {
		return Collections.unmodifiableMap(attributes);
	}

	void addAttribute(String name, String value) {
		check(name, value);

		if(attributes.containsKey(name))
			throw new LahainaException("attribute defined twice: "+name);

		attributes.put(name, value);
	}

	public Class getTagClass() {
		return tagClass;
	}

	public boolean hasChild() {
		return child != null;
	}

	public Fragment getChild() {
		return child;
	}

	void setChild(Fragment f) {
		child = f;
	}

	void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		appendTagName(sb, false);

		sb.append("[");
		sb.append(Integer.toString(attributes.size()));
		sb.append(" attributes ");

		if(attributes.size()>0) {
			Iterator it = attributes.keySet().iterator();
			String n;
			while(it.hasNext()) {
				n = (String)it.next();
				sb.append(n);
				sb.append(" = \"");
				sb.append((String)attributes.get(n));
				sb.append("\" ");
			}
		}
		sb.append("]");

		if(content != null)
			sb.append(content);

		if(hasChild()) {
			appendOther(sb, getChild());
			sb.append("\n");
		}

		appendTagName(sb, true);

		appendNext(sb);
		return sb.toString();
	}

	private void appendTagName(StringBuffer sb, boolean close) {
		sb.append("[<");
		if(close)
			sb.append('/');
		sb.append(tagClass.getName());
		sb.append(">]");
	}

	public boolean allowsContent() {
		return allowsContent(tagClass);
	}

	static boolean allowsContent(Class unknown) {
		return implementsInteface(unknown, NonEmptyTag.class);
	}

	public boolean processesContent() {
		return processesContent(tagClass);
	}

	static boolean processesContent(Class unknown) {
		return implementsInteface(unknown, ContentProcessTag.class);
	}

	public static boolean implementsInteface(Class unknown, Class known) {
		return known.isAssignableFrom(unknown);
	}
}