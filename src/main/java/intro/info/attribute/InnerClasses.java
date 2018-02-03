package intro.info.attribute;

import intro.info.ClassInfo;
import intro.info.pool.CPInfo;
import intro.info.pool.ClassCPI;
import intro.io.TrackableDataInputStream;
import intro.io.Util;

import java.io.IOException;
import java.io.Serializable;

public class InnerClasses extends AbstractAttribute {
	public InnerRef[] refs;

	public InnerClasses(String name, TrackableDataInputStream tis, ClassInfo ci) throws IOException {
		super(name,tis);

		short num = tis.readShort();
		refs = new InnerRef[num];

		for(short n=0;n<num;n++) {
			refs[n] = new InnerRef(tis,ci);
			Util.debug("inner class: "+refs[n].toString());
		}
	}

	public class InnerRef implements Serializable {
		public ClassCPI innerRef;
		public ClassCPI outerRef;
		public String innerName;
		public AccessFlags accessFlags;

		InnerRef(TrackableDataInputStream tis, ClassInfo ci) throws IOException {
			CPInfo cpi;

			innerRef = (ClassCPI)ci.getCPI(tis.readUnsignedShort());
			outerRef = (ClassCPI)ci.getCPI(tis.readUnsignedShort());

			cpi = ci.getCPI(tis.readShort());

			if(cpi != null)
				innerName = cpi.toString();
			else
				innerName = "";

			accessFlags = new AccessFlags(tis.readUnsignedShort());
		}

		public String toString() {
			return innerName;
		}
	}

	public class AccessFlags implements Serializable {
		public boolean isPublic;
		public boolean isPrivate;
		public boolean isProtected;
		public boolean isStatic;
		public boolean isFinal;
		public boolean isInterface;
		public boolean isAbstract;

		public AccessFlags(int input) {
			isPublic = ((input&0x0001)!=0);
			isPrivate = ((input&0x0002)!=0);
			isProtected = ((input&0x0004)!=0);
			isStatic = ((input&0x0008)!=0);
			isFinal = ((input&0x0010)!=0);
			isInterface = ((input&0x0200)!=0);
			isAbstract = ((input&0x0400)!=0);
		}
	}
}