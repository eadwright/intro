package lahaina.runtime;

import java.io.IOException;

public final class Writer {
	private java.io.Writer innerWriter;

	public void setInternalWriter(java.io.Writer w) {
		innerWriter = w;
	}

	public void write(String str) throws IOException {
		if(str != null)
			innerWriter.write(str);
	}
}