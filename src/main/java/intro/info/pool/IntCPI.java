package intro.info.pool;

import intro.io.TrackableDataInputStream;

import java.io.IOException;

public class IntCPI extends CPInfo {
	public int bytes;

	public IntCPI(TrackableDataInputStream tis) throws IOException {
		bytes = tis.readInt();
	}

	public String toString() {
		return String.valueOf(bytes);
	}
}