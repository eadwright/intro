package lahaina.runtime;

public class LahainaException extends RuntimeException {
	public LahainaException() {}

	public LahainaException(String msg) {
		super(msg);
	}

	public LahainaException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public LahainaException(Throwable cause) {
		super(cause);
	}
}