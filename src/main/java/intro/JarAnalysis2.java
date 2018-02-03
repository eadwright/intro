package intro;

import intro.analysis.Database;
import intro.io.TrackableDataInputStream;
import intro.io.Util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class JarAnalysis2 {
	public static void main(String[] args) {
		createDatabase();
	}

	public static Database createDatabase() {
		Database db = new Database();

		try {
//			JarFile jf = new JarFile("classes/test.jar");
			JarFile jf = new JarFile("rt.jar");
//			JarFile jf = new JarFile("tools.jar");

			Enumeration en = jf.entries();

			ZipEntry ze;
			BufferedInputStream bis;
			TrackableDataInputStream tis;

			while(en.hasMoreElements()) {
				ze = (ZipEntry)en.nextElement();

				if(ze.getName().indexOf(".class")!=-1) {
//				if(ze.getName().indexOf("PrinterCapabilities")!=-1) {
//					System.out.println(ze.getName());

					bis = new BufferedInputStream(jf.getInputStream(ze));
					tis = new TrackableDataInputStream(bis);

					try {
						db.readClass(tis);
					} catch (Exception ex) {
						Util.hexDump(new DataInputStream(new BufferedInputStream(jf.getInputStream(ze))),tis.getPosition());
						ex.printStackTrace();
						System.exit(1);
					} finally {
						tis.close();
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		db.postProcess();

		return db;
	}
}