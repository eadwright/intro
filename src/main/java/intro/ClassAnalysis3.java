package intro;

import intro.info.ClassInfo;
import intro.io.TrackableDataInputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class ClassAnalysis3 {
    public static void main(String[] argv) {
        new ClassAnalysis3();
    }

    public ClassAnalysis3() {
        try {
	        File f = new File("classes/test/Subject1.class");

            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
			TrackableDataInputStream tis = new TrackableDataInputStream(bis);

            ClassInfo ci = new ClassInfo(tis);

	        tis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}