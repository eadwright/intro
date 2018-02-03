package intro.info.attribute;

import intro.io.TrackableDataInputStream;

import java.io.IOException;

public class ConstantValueAttribute extends AbstractAttribute {
	public int constantValueIndex;

	public ConstantValueAttribute(String name, TrackableDataInputStream tis) throws IOException {
		super(name, tis);

		constantValueIndex = tis.readUnsignedShort();
	}
}