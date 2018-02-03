package intro.info.attribute;

import intro.info.ClassInfo;
import intro.io.TrackableDataInputStream;

import java.io.IOException;

public class AttributeFactory {
	public static AbstractAttribute createAttribute(TrackableDataInputStream tis,ClassInfo ci) throws IOException {
		int nameIndex = tis.readUnsignedShort();

		String name = ci.getName(nameIndex);
//		Util.debug("attribute name "+name);

		if(name.equals("ConstantValue"))
			return new ConstantValueAttribute(name,tis);

		if(name.equals("Code"))
			return new CodeAttribute(name,tis,ci);

		if(name.equals("Exceptions"))
			return new ExceptionsAttribute(name,tis,ci);

		if(name.equals("InnerClasses"))
			return new InnerClasses(name,tis,ci);

/*		if(name.equals("Synthetic"))
			return new SyntheticAttribute(name,tis);

		if(name.equals("SourceFile"))
			return new SourceFileAttribute(name,tis);

		if(name.equals("LineNumberTable"))
			return new LineNumberTableAttribute(name,tis);

		if(name.equals("LocalVariableTable"))
			return new LocalVariableTableAttribute(name,tis);*/

		if(name.equals("Deprecated"))
			return new DeprecatedAttribute(name,tis);

		return new UnknownAttribute(name,tis);
	}

	public static AbstractAttribute[] createAttributes(TrackableDataInputStream tis, int num, ClassInfo ci) throws IOException {
		AbstractAttribute[] attributes = new AbstractAttribute[num];
		for(int count = 0;count<num;count++) {
			attributes[count] = createAttribute(tis, ci);
//		    Util.debug("attribute: "+attributes[count].toString());
		}

		return attributes;
	}
}
