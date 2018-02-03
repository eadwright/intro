package intro.io;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

public abstract class Util {
	public static final boolean DO_DEBUG = false;

	public static void debug(String msg) {
		if(DO_DEBUG)
			System.out.println(msg);
	}

	public static void hexDump(DataInputStream is, int pos) {
		int offset = 0;

		try {
			int skip = 0;

			skip = pos - 50;
			if(skip>0) {
				is.skip(skip);
				offset = skip;
			}

			int count = (pos+50-skip)/5;

			for(int n = 0;n<count;n++)
				printLine(is,skip+(5*n),pos);
		} catch (EOFException eof) {
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private static void printLine(DataInputStream is, int offset, int pos) throws IOException {
		System.out.print(offset);

		int b;

		StringBuffer buffer = new StringBuffer();

		for(int n=0;n<5;n++) {
			b = is.readUnsignedByte();

			if(b > 31 && b < 128)
				buffer.append((char)b);
			else
				buffer.append('_');

			System.out.print(" "+b);
		}
		System.out.print("  ");

		System.out.println(buffer.toString());
	}
}