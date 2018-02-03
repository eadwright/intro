package intro.info.attribute;

import intro.io.TrackableDataInputStream;

import java.io.IOException;
import java.io.Serializable;

public abstract class AbstractAttribute implements Serializable {
	public String name;
	int length;

	public AbstractAttribute(String name, TrackableDataInputStream tis) throws IOException {
		this.name = name;

		length = tis.readInt();
	}
}