package intro2.database.pool;

import intro2.database.ByteArrayAccessor;
import intro2.database.Database.DataProcessingException;

public abstract class ConstantPoolUtil {
	public static ConstantPoolInfo getConstantPoolInfo(int num, ByteArrayAccessor acc, ConstantPoolSupplier cps) throws DataProcessingException {
		int max = acc.getUnsignedShort(8); // constant_pool_count;
		if(num>max || num==0)
			return null; // error

		int index = skipConstantPoolEntries(num, acc);

		int tag = acc.getUnsignedByte(index);

		int len;
		switch(tag) { // should create something here
			case ConstantPoolInfo.CONSTANT_Class:
				return new ClassCPI(index, acc, cps);
			case ConstantPoolInfo.CONSTANT_String:
				return new StringCPI(index, acc, cps);
			case ConstantPoolInfo.CONSTANT_Fieldref:
				return new FieldRefCPI(index, acc, cps);
			case ConstantPoolInfo.CONSTANT_Methodref:
				return new MethodRefCPI(index, acc, cps);
			case ConstantPoolInfo.CONSTANT_InterfaceMethodref:
				return new InterfaceMethodRefCPI(index, acc, cps);
			case ConstantPoolInfo.CONSTANT_Integer:
				return new IntegerCPI(index, acc, cps);
			case ConstantPoolInfo.CONSTANT_Float:
				return new FloatCPI(index, acc, cps);
			case ConstantPoolInfo.CONSTANT_NameAndType:
				return new NameAndTypeCPI(index, acc, cps);
			case ConstantPoolInfo.CONSTANT_Long:
				return new LongCPI(index, acc, cps);
			case ConstantPoolInfo.CONSTANT_Double:
				return new DoubleCPI(index, acc, cps);
			case ConstantPoolInfo.CONSTANT_Utf8:
				return new UTF8CPI(index, acc, cps);
			default:
				throw new DataProcessingException("Unknown constant pool tag "+tag);
		}
	}

	public static int skipConstantPoolEntries(int num, ByteArrayAccessor acc) {
		int index = 10; // start of constant pool entries
		int count = 1;

		int tag,len;
		while(count<num) {
			tag = acc.getUnsignedByte(index);
			switch(tag) {
				case ConstantPoolInfo.CONSTANT_Class:
				case ConstantPoolInfo.CONSTANT_String:
					index+=3;
					break;
				case ConstantPoolInfo.CONSTANT_Fieldref:
				case ConstantPoolInfo.CONSTANT_Methodref:
				case ConstantPoolInfo.CONSTANT_InterfaceMethodref:
				case ConstantPoolInfo.CONSTANT_Integer:
				case ConstantPoolInfo.CONSTANT_Float:
				case ConstantPoolInfo.CONSTANT_NameAndType:
					index+=5;
					break;
				case ConstantPoolInfo.CONSTANT_Long:
				case ConstantPoolInfo.CONSTANT_Double:
					count++;
					index+=9;
					break;
				case ConstantPoolInfo.CONSTANT_Utf8:
					len = acc.getUnsignedShort(index+1);
//					System.out.println("utf "+new String(acc.getData(),index+3,len));
					index+=3+len;
					break;
				default:
					throw new RuntimeException("unknown cp tag "+tag);
			}

//			System.out.println("cp #"+count+" tag "+tag+" skipping to "+index);
			count++;
		}

		return index;
	}
}