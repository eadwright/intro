package lahaina.fragment;

public final class TextFragment extends Fragment {
	private String text;

	TextFragment(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("[raw]");
		sb.append(text);
		sb.append("[/raw]");

		appendNext(sb);
		return sb.toString();
	}
}