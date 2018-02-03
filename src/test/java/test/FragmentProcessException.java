package test;

public class FragmentProcessException extends Exception {
	public FragmentProcessException() {
		super();
	}

	public FragmentProcessException(String s) {
		super(s);
	}

	public FragmentProcessException(Throwable t) {
		super(t);
	}

	public FragmentProcessException(String s, Throwable t) {
		super(s,t);
	}
}