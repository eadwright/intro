package intro.info.pool;

import intro.io.TrackableDataInputStream;

import java.io.IOException;

public class LongCPI extends CPInfo {
	public long value;

	public LongCPI(TrackableDataInputStream tis) throws IOException {
		value = tis.readLong();
	}

	public String toString() {
		return String.valueOf(value);
	}
}