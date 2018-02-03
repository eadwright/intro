package intro.io;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TrackableDataInputStream extends FilterInputStream {
	private InputStream in;
	private int pos;

	public TrackableDataInputStream(InputStream in) {
		super(in);
		this.in = in;
	}

	public int read() throws IOException {
		try {
			pos++;
			return in.read();
		} catch (IOException e) {
			pos = -1;
			throw e;
		}
	}

	public double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}

	public float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}

	public long readLong() throws IOException {
		return ((long)(readInt()) << 32) + (readInt() & 0xFFFFFFFFL);
	}

	public int readUnsignedByte() throws IOException {
		int ch = read();
		if (ch < 0)
			throw new EOFException();
		return ch;
	}

	public final int readUnsignedShort() throws IOException {
		int ch1 = read();
		int ch2 = read();
		if ((ch1 | ch2) < 0)
			 throw new EOFException();
		return (ch1 << 8) + (ch2 << 0);
	}

	public byte readByte() throws IOException {
		int ch = in.read();
		if (ch < 0)
			throw new EOFException();
		return (byte)(ch);
	}

	public short readShort() throws IOException {
		int ch1 = read();
		int ch2 = read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (short)((ch1 << 8) + (ch2 << 0));
	}

	public int readInt() throws IOException {
		int ch1 = read();
		int ch2 = read();
		int ch3 = read();
		int ch4 = read();
		if ((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}

	public int read(byte b[], int off, int len) throws IOException {
		if (b == null) {
			throw new NullPointerException();
		} else if ((off < 0) || (off > b.length) || (len < 0) ||
			   ((off + len) > b.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return 0;
		}

		int c = read();
		if (c == -1)
			return -1;

		b[off] = (byte)c;

		int i = 1;
		try {
			for (; i < len ; i++) {
				c = read();
				if (c == -1)
					break;
				if (b != null)
					b[off + i] = (byte)c;
			}
		} catch (IOException ee) {}
		return i;
	}

	public int skipBytes(int n) throws IOException {
		int total = 0;
		int cur = 0;

		while ((total<n) && ((cur = (int) skip(n-total)) > 0))
			total += cur;

		pos += total;
		return total;
	}

	public int getPosition() {
		return pos;
	}

	/**
	 *
	 *
	 *
	 * Returns true if the current position pointer is on a 32-bit
	 * word boundary. Throws EOFException if all data has been read.
	 */
	public boolean isWordAligned() throws EOFException {
		if(pos==-1)
			throw new EOFException();

		return ((pos & 3) == 0);
	}

	/**
	 * Reads and discards bytes until we're on a 32-bit word boundary
	 */
	public void skipPadding() throws IOException {
		while((pos & 3) != 0)
			read();
	}
}