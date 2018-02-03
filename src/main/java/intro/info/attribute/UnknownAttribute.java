package intro.info.attribute;

import intro.io.TrackableDataInputStream;

import java.io.IOException;

public class UnknownAttribute extends AbstractAttribute {
	public UnknownAttribute(String name, TrackableDataInputStream tis) throws IOException {
		super(name,tis);
		tis.skipBytes(length);
	}
}