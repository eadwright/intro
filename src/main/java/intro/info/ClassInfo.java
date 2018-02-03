package intro.info;

import intro.info.attribute.AbstractAttribute;
import intro.info.attribute.AttributeFactory;
import intro.info.attribute.DeprecatedAttribute;
import intro.info.member.FieldInfo;
import intro.info.member.MethodInfo;
import intro.info.pool.CPFactory;
import intro.info.pool.CPInfo;
import intro.io.TrackableDataInputStream;
import intro.io.Util;

import java.io.IOException;
import java.io.Serializable;

public class ClassInfo implements Serializable {
	public int minorVersion;
	public int majorVersion;
	private CPInfo[] constantPool; // constant_pool_count-1, was static
//	private ClassAccessFlags accessFlags;
	public int accessFlags;
	public String thisClass;
	public String superClass;
	public String[] interfaces;
	public FieldInfo[] fields; // fields_count
	public MethodInfo[] methods; // methods_count
	public AbstractAttribute[] attributes; // attributes_count

	public ClassInfo(TrackableDataInputStream tis) throws IOException {

		tis.skipBytes(4);

		minorVersion = tis.readUnsignedShort();
		majorVersion = tis.readUnsignedShort();

		int count = tis.readUnsignedShort();
		Util.debug("constant pool entries: "+count);
//		System.out.println("constant pool entries: "+count);
		constantPool = new CPInfo[count-1];
		for(int n=0;n<count-1;n++) {
			constantPool[n] = CPFactory.create(tis,n);
			if(CPFactory.isSpecialAdd(constantPool[n]))
				n++;
		}

//		dumpCPI();

		CPFactory.postProcess(constantPool);

//		accessFlags = new ClassAccessFlags(tis.readUnsignedShort());
		accessFlags = tis.readUnsignedShort();
		thisClass = getName(tis.readUnsignedShort());
		superClass = getName(tis.readUnsignedShort());

		Util.debug("this "+thisClass);
		Util.debug("super "+superClass);

		count = tis.readUnsignedShort();
		Util.debug("interface entries: "+count);
		interfaces = new String[count];
		for(short n=0;n<count;n++)
			interfaces[n] = getName(tis.readUnsignedShort());

		count = tis.readUnsignedShort();
		Util.debug("field entries: "+count);
		fields = new FieldInfo[count];
		for(short n=0;n<count;n++)
			fields[n] = new FieldInfo(tis, this);

		count = tis.readUnsignedShort();
		Util.debug("method entries: "+count);
		methods = new MethodInfo[count];
		for(short n=0;n<count;n++)
			methods[n] = new MethodInfo(tis, this);

		count = tis.readUnsignedShort();
		attributes = AttributeFactory.createAttributes(tis,count,this);
	}

	public String getName(int index) {
//		try {
		if(index > 0)
			return constantPool[index-1].toString();
		else
			return null;
//		} catch (ArrayIndexOutOfBoundsException ex) {
//			System.out.println("ERROR, index "+index+", len "+constantPool.length);
//			throw ex;
//		}
	}

	public String getName(Integer index) {
		if(index.intValue() > 0)
			return constantPool[index.intValue()-1].toString();
		else
			return null;
	}

	public CPInfo getCPI(int index) {
		if(index>0)
			return constantPool[index-1];
		else
			return null;
	}

	public CPInfo getCPI(Integer index) {
		if(index.intValue()>0)
			return constantPool[index.intValue()-1];
		else
			return null;
	}

	public void dumpCPI() {
		CPInfo cpi;
		for(int i=0;i<constantPool.length;i++) {
			cpi = constantPool[i];
			if(cpi==null)
				System.out.println("cpi "+(i+1)+" is null!");
			else
				System.out.println("cpi "+(i+1)+"("+constantPool[i].getClass().getName()+"): "+constantPool[i].toString());
		}
	}

	public static boolean hasDeprecatedAttribute(AbstractAttribute[] attrs) {
		for(int i=0;i<attrs.length;i++)
			if(attrs[i] instanceof DeprecatedAttribute)
				return true;

		return false;
	}
}