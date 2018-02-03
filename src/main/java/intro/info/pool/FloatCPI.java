package intro.info.pool;

import intro.io.TrackableDataInputStream;

import java.io.IOException;

public class FloatCPI extends CPInfo {
	public float value;

	public FloatCPI(TrackableDataInputStream tis) throws IOException {
		value = tis.readFloat();
	}

	public String toString() {
		return String.valueOf(value);
	}
}