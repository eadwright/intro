package lahaina.tag;

public interface Tag {
//	public void init(lahaina.runtime.State state); // replace with constructor
	public boolean process(lahaina.runtime.Writer out) throws Exception;
}