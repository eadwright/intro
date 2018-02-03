package intro.info.attribute;

import intro.info.ClassInfo;
import intro.info.bytecode.ByteCode2;
import intro.info.pool.CPInfo;
import intro.info.pool.ClassCPI;
import intro.io.TrackableDataInputStream;
import intro.io.Util;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

public class CodeAttribute extends AbstractAttribute {
	public ClassCPI[] exceptionCatchTypes;
	public AbstractAttribute[] attributes;
	public Set[] refs;

	public CodeAttribute(String name, TrackableDataInputStream tis, ClassInfo ci) throws IOException {
		super(name,tis);

		tis.skipBytes(4); // max stack, max locals

		refs = ByteCode2.getReferences(tis,tis.readInt());

//		showRefs(refs[0],ci,"method ref: ");
//		showRefs(refs[1],ci,"usage ref: ");
//		showRefs(refs[2],ci,"creation ref: ");

		short etec = tis.readShort(); // exception table entry count
		exceptionCatchTypes = new ClassCPI[etec];
		for(short n = 0;n<etec;n++) {
			tis.skipBytes(6); // start/end pc, handler pc
			exceptionCatchTypes[n] = (ClassCPI)ci.getCPI(tis.readShort());
		}

		short ac = tis.readShort();
//		Util.debug("// begin "+ac+" sub-attributes for code");
		attributes = AttributeFactory.createAttributes(tis,ac,ci);
//		Util.debug("// end sub attributes");
	}

	private void showRefs(Set set, ClassInfo ci, String label) {
		Iterator it = set.iterator();

		CPInfo cpi;
		while(it.hasNext()) {
			cpi = ci.getCPI((Integer)it.next());
			if(cpi!=null)
				Util.debug(label+cpi.toString());
		}
	}
}