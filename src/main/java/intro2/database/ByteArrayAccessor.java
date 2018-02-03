package intro2.database;

import java.io.UnsupportedEncodingException;

public final class ByteArrayAccessor {
	private byte[] data;

	public ByteArrayAccessor(byte[] data) throws Database.DataProcessingException {
		this.data = data;

		long magic = getUnsignedInt(0);
		if(magic != 0xCAFEBABEL)
			throw new Database.DataProcessingException("Class file magic number wrong");
	}

	public int getLength() {
		return data.length;
	}

	public byte[] getData() {
		return data;
	}

	public String getString(int offset, int length) throws Database.DataProcessingException {
		try {
			return new String(data, offset, length, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			throw new Database.DataProcessingException("Invalid UTF8 constant pool entry");
		}
	}

	public void dump() {
		int a=0,b;

		String s;
		while(a<data.length) {
			b=0;
			System.out.print(a+": ");
			while(b<10 && a<data.length) {
				s = Integer.toHexString(getUnsignedByte(a++));
				if(s.length()==1)
					System.out.print("0");
				System.out.print(s+" ");
				b++;
			}
			System.out.println();
		}
	}

	public byte getByte(int offset) {
		return data[offset];
	}

	public int getUnsignedByte(int offset) {
		return ((int)data[offset]) & 0xFF;
	}

	public int getUnsignedShort(int offset) {
		int ch1 = getUnsignedByte(offset);
		int ch2 = getUnsignedByte(offset+1);

		return ((ch1 << 8) + (ch2 << 0));
	}

	public short getShort(int offset) {
		int ch1 = getUnsignedByte(offset);
		int ch2 = getUnsignedByte(offset+1);

		return (short)((ch1 << 8) + (ch2 << 0));
	}

	public int getInt(int offset) {
		int ch1 = getUnsignedByte(offset);
		int ch2 = getUnsignedByte(offset+1);
		int ch3 = getUnsignedByte(offset+2);
		int ch4 = getUnsignedByte(offset+3);

		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}

	public long getUnsignedInt(int offset) {
		return ((long)(getInt(offset)) & 0xFFFFFFFFL);
	}

	public long getLong(int offset) {
		return ((long)(getInt(offset)) << 32) + (getInt(offset+4) & 0xFFFFFFFFL);
	}

	public double getDouble(int offset) {
		return Double.longBitsToDouble(getLong(offset));
	}

	public float getFloat(int offset) {
		return Float.intBitsToFloat(getInt(offset));
	}
}