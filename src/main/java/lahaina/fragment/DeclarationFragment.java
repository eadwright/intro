package lahaina.fragment;

import java.util.ArrayList;
import java.util.List;

public final class DeclarationFragment extends HasAttributes {
	private List names;
	private List values;

	public DeclarationFragment() {
		names = new ArrayList();
		values = new ArrayList();
	}

	void addAttribute(String name, String value) {
		check(name, value);

		names.add(name);
		values.add(value);
	}

	public List getAttributeNames() {
		return names;
	}

	public String getValue(int index) {
		return (String)values.get(index);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("[<@>][");

		List n = getAttributeNames();
		sb.append(Integer.toString(n.size()));
		sb.append(" attributes ");
		for(int i=0;i<n.size();i++) {
			sb.append((String)n.get(i));
			sb.append(" = \"");
			sb.append(getValue(i));
			sb.append("\" ");
		}

		sb.append("][</@>]");

		appendNext(sb);
		return sb.toString();
	}
}