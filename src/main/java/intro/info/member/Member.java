package intro.info.member;

import intro.info.ClassInfo;
import intro.info.attribute.AbstractAttribute;
import intro.info.attribute.AttributeFactory;
import intro.io.TrackableDataInputStream;

import java.io.IOException;
import java.io.Serializable;

public class Member implements Serializable {
	public int flags;
	public String name;
	public String descriptor;
	public AbstractAttribute[] attributes;

	public Member(TrackableDataInputStream tis, ClassInfo ci) throws IOException {
		flags = tis.readUnsignedShort();
		name = ci.getName(tis.readUnsignedShort());
		descriptor = ci.getName(tis.readUnsignedShort());
		int count = tis.readUnsignedShort();
//		System.out.println("//member "+name+" desc "+descriptor+" attribute count "+count);
		attributes = AttributeFactory.createAttributes(tis,count,ci);
//		System.out.println("// end member");
	}
}