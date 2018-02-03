package intro.info.pool;

import intro.io.TrackableDataInputStream;

import java.io.IOException;

public class ClassCPI extends CPInfo {
	public String name;
	private int nameIndex;

	public ClassCPI(TrackableDataInputStream tis) throws IOException {
		nameIndex = tis.readUnsignedShort();
	}

	public void postProcess(CPInfo[] info) {
		name = info[nameIndex-1].toString();
	}

	public String toString() {
		if(name!=null)
			return name;
		else
			return "ClassCPI "+nameIndex;
	}
}