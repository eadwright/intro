package intro.info.attribute;

import intro.io.TrackableDataInputStream;

import java.io.IOException;

public class DeprecatedAttribute extends AbstractAttribute {
	public DeprecatedAttribute(String name, TrackableDataInputStream tis) throws IOException {
		super(name,tis);
		tis.skipBytes(length);
	}
}