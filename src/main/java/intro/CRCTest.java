package intro;

import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class CRCTest {
	public static void main(String[] args) {
		String s = "test string";
		byte[] b = s.getBytes();

		showResults(b, new Adler32());
		showResults(b, new Adler32());
		showResults(b, new CRC32());
		showResults(b, new CRC32());
	}

	private static void showResults(byte[] b, Checksum c) {
		System.out.println("init "+c.getValue());
		c.update(b,0,b.length);
		System.out.println("v1 "+c.getValue());
		c.update(b,0,b.length);
		System.out.println("v2 "+c.getValue());
	}
}