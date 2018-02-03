package intro.info.member;

import intro.info.ClassInfo;
import intro.io.TrackableDataInputStream;

import java.io.IOException;

public class MethodInfo extends Member {
	public MethodInfo(TrackableDataInputStream tis, ClassInfo ci) throws IOException {
		super(tis,ci);
	}
}