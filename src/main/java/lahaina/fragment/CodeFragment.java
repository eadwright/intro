package lahaina.fragment;

public final class CodeFragment extends Fragment {
	private String code;
	private boolean isAssignment;
	private boolean isInline;

	CodeFragment(boolean isAssignment, boolean isInline, String code) {
		this.isAssignment = isAssignment;
		this.isInline = isInline;
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public boolean isAssignment() {
		return isAssignment;
	}

	public boolean isInline() {
		return isInline;
	}

	public String toString() {
		char c;
		if(isAssignment)
			c = '=';
		else
			c = '%';

		StringBuffer sb = new StringBuffer("[<");
		sb.append(c);
		sb.append(">]");
		sb.append(code);
		sb.append("[</");
		sb.append(c);
		sb.append(">]");

		appendNext(sb);
		return sb.toString();
	}
}