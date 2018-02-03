package intro.info.pool;

import intro.io.TrackableDataInputStream;

import java.io.IOException;

public class NameAndTypeCPI extends CPInfo {
	public String name;
	public String descriptor;
	private int nameIndex;
	private int descriptorIndex;

	public NameAndTypeCPI(TrackableDataInputStream tis) throws IOException {
		nameIndex = tis.readUnsignedShort();
		descriptorIndex = tis.readUnsignedShort();
	}

	public void postProcess(CPInfo[] info) {
		name = info[nameIndex-1].toString();
		descriptor = info[descriptorIndex-1].toString();
	}

	public String toString() {
		if(name!=null)
			return "NameAndTypeCPI, name "+name+", desc "+descriptor;
		else
			return "NameAndTypeCPI, name "+nameIndex+", desc "+descriptorIndex;
	}
}