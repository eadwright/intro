package intro.info.attribute;

import intro.info.ClassInfo;
import intro.info.pool.ClassCPI;
import intro.io.TrackableDataInputStream;
import intro.io.Util;

import java.io.IOException;

public class ExceptionsAttribute extends AbstractAttribute {
	public ClassCPI[] offsets;

	public ExceptionsAttribute(String name, TrackableDataInputStream tis, ClassInfo ci) throws IOException {
		super(name,tis);

		short num = tis.readShort();

//		System.out.println("Exceptions attribute, "+num+" exceptions");

		offsets = new ClassCPI[num];
		for(short n=0;n<num;n++) {
			offsets[n] = (ClassCPI)ci.getCPI(tis.readUnsignedShort());
			Util.debug("throws "+offsets[n].toString());
		}
	}
}