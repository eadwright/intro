package lahaina.fragment;

public abstract class Fragment {
	private Fragment next;

	public boolean hasNext() {
		return next != null;
	}

	public Fragment getNext() {
		return next;
	}

	void setNext(Fragment f) {
		next = f;
	}

	protected void appendNext(StringBuffer sb) {
		if(hasNext())
			appendOther(sb, getNext());
	}

	protected void appendOther(StringBuffer sb, Fragment other) {
		sb.append("\n");
		sb.append(other.toString());
	}
}