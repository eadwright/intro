package lahaina.fragment;

public final class CommentFragment extends Fragment {
	private String comment;

	CommentFragment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("[<!>]");
		sb.append(comment);
		sb.append("[</!>]");

		appendNext(sb);
		return sb.toString();
	}
}