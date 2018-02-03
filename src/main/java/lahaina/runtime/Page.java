package lahaina.runtime;

public interface Page {
	public void process(Writer out, lahaina.runtime.State state) throws Exception;
}