package intro.info.pool;

import intro.io.TrackableDataInputStream;

import java.io.IOException;

public class StringCPI extends CPInfo {
    public int stringIndex;

    public StringCPI(TrackableDataInputStream tis) throws IOException {
        stringIndex = tis.readUnsignedShort();
    }
}