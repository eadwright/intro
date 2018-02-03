package intro.info.pool;

import intro.io.TrackableDataInputStream;

import java.io.IOException;

public abstract class CPFactory {
	public static CPInfo create(TrackableDataInputStream tis, int n) throws IOException {
		CPInfo cpi = null;

		int tag = tis.readUnsignedByte();
//        System.out.println("No "+n+" tag "+tag);
		switch(tag) {
			case CPInfo.CONSTANT_Class:
				cpi = new ClassCPI(tis);
				break;
			case CPInfo.CONSTANT_Integer:
				cpi = new IntCPI(tis);
				break;
			case CPInfo.CONSTANT_Float:
				cpi = new FloatCPI(tis);
				break;
			case CPInfo.CONSTANT_Fieldref:
			case CPInfo.CONSTANT_Methodref:
			case CPInfo.CONSTANT_InterfaceMethodref:
				cpi = new RefCPI(tis);
				break;
			case CPInfo.CONSTANT_Long:
				cpi = new LongCPI(tis);
				break;
			case CPInfo.CONSTANT_Double:
				cpi = new DoubleCPI(tis);
				break;
			case CPInfo.CONSTANT_NameAndType:
				cpi = new NameAndTypeCPI(tis);
				break;
			case CPInfo.CONSTANT_String:
				cpi = new StringCPI(tis);
				break;
			case CPInfo.CONSTANT_Utf8:
				cpi = new Utf8CPI(tis);
				break;
			default:
				throw new IOException("Bad tag "+tag+" position "+tis.getPosition()+" no "+n);
		}

		if(cpi!=null)
			cpi.tag = tag;

//		System.out.println("No "+n+" cpi: "+cpi.toString());


		return cpi;
	}

	public static boolean isSpecialAdd(CPInfo cpi) {
		return (cpi instanceof LongCPI) || (cpi instanceof DoubleCPI); // these take two constant pool entries;
	}

	public static void postProcess(CPInfo[] info) {
		for(int n=0;n<info.length;n++)
			if(info[n]!=null)
				info[n].postProcess(info);

//		for(int n=0;n<info.length;n++)
//		    System.out.println(n+" "+info[n].toString());
	}
}