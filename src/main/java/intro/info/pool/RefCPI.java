package intro.info.pool;

import intro.io.TrackableDataInputStream;

import java.io.IOException;

public class RefCPI extends CPInfo {
	public ClassCPI classCPI;
	public NameAndTypeCPI natCPI;
	private int classIndex;
	private int nameAndTypeIndex;

	public RefCPI(TrackableDataInputStream tis) throws IOException {
		classIndex = tis.readUnsignedShort();
		nameAndTypeIndex = tis.readUnsignedShort();
	}

	public void postProcess(CPInfo[] info) {
		classCPI = (ClassCPI)info[classIndex-1];
		natCPI = (NameAndTypeCPI)info[nameAndTypeIndex-1];
	}

	public String toString() {
		if(classCPI!=null)
			return "RefCPI, class "+classIndex+" "+classCPI.toString()+" "+nameAndTypeIndex+" "+natCPI.toString();
		else
			return "RefCPI, class "+classIndex+" "+nameAndTypeIndex;
	}
}