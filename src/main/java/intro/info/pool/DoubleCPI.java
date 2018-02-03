package intro.info.pool;

import intro.io.TrackableDataInputStream;

import java.io.IOException;

public class DoubleCPI extends CPInfo {
	public double value;

	public DoubleCPI(TrackableDataInputStream tis) throws IOException {
		value = tis.readDouble();
	}

	public String toString() {
		return String.valueOf(value);
	}
}