package intro.info.member;

import intro.info.ClassInfo;
import intro.info.attribute.ConstantValueAttribute;
import intro.info.pool.CPInfo;
import intro.io.TrackableDataInputStream;

import java.io.IOException;

public class FieldInfo extends Member {
	public CPInfo value;

	public FieldInfo(TrackableDataInputStream tis, ClassInfo ci) throws IOException {
		super(tis,ci);

		for(int i=0;i<attributes.length;i++)
			if(attributes[i] instanceof ConstantValueAttribute) {
				value = ci.getCPI(((ConstantValueAttribute)attributes[i]).constantValueIndex);
				break;
			}
	}
}
