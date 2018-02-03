package intro;

import intro.analysis.Database;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPOutputStream;

public class SaveJarAnalysis {
	public static void main(String[] args) {
		System.out.println("begin creating db");
		long time = System.currentTimeMillis();

		Database db = JarAnalysis2.createDatabase();

		System.out.println("begin writing data");
		try {
			FileOutputStream fos = new FileOutputStream("d:/projects/intro/WEB-INF/database.ser.gz");
			GZIPOutputStream zos = new GZIPOutputStream(fos,1024*1024);
			ObjectOutputStream oos = new ObjectOutputStream(zos);

			oos.writeObject(db);

			zos.close();
			fos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("done, total time "+(System.currentTimeMillis()-time)/1000+"secs");
	}
}