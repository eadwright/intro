package intro.info.pool;

import intro.io.TrackableDataInputStream;

import java.io.IOException;

public class Utf8CPI extends CPInfo {
    public int length;
    public byte[] bytes;

    public Utf8CPI(TrackableDataInputStream tis) throws IOException {
        length = tis.readUnsignedShort();

//		int start = tis.getPosition();

		bytes = new byte[length];
		int read = tis.read(bytes,0,bytes.length);

//        String str = new String(bytes);
//        System.out.println("UTF: "+str+" length "+length+" read "+read+" start "+start+" final pos "+tis.getPosition());
    }

	public String toString() {
		return new String(bytes);
	}
}