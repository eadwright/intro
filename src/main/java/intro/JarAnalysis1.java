package intro;

import intro.info.ClassInfo;
import intro.io.TrackableDataInputStream;
import intro.io.Util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class JarAnalysis1 {
	public static void main(String[] args) {
		new JarAnalysis1();
	}

	public JarAnalysis1() {
		try {
//			HashMap map = new HashMap();

//			JarFile jf = new JarFile("classes/test.jar");
			JarFile jf = new JarFile("classes/rt.jar");

			Enumeration en = jf.entries();

			ZipEntry ze;
			BufferedInputStream bis;
			TrackableDataInputStream tis;
			ClassInfo ci;

			while(en.hasMoreElements()) {
				ze = (ZipEntry)en.nextElement();

				if(ze.getName().indexOf(".class")!=-1) {
//				if(ze.getName().equals("java/awt/event/KeyEvent.class")) {

					System.out.println(ze.getName());

					bis = new BufferedInputStream(jf.getInputStream(ze));
					tis = new TrackableDataInputStream(bis);

					try {
						ci = new ClassInfo(tis);
					} catch (Exception ex) {
						Util.hexDump(new DataInputStream(new BufferedInputStream(jf.getInputStream(ze))),tis.getPosition());
						ex.printStackTrace();
						System.exit(1);
					}

					tis.close();

//					map.put(ci.thisClass,ci);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}