package intro;

import intro.analysis.Database;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.zip.GZIPInputStream;

public class LoadJarAnalysis {
	public static void main(String[] args) {
		loadDatabase();
	}

	public static Database loadDatabase() {
		Database db = null;

		System.out.println("begin reading data");
		try {
			FileInputStream fis = new FileInputStream("d:/projects/intro/WEB-INF/database.ser.gz");
			GZIPInputStream zis = new GZIPInputStream(fis,1024*1024);
			ObjectInputStream ois = new ObjectInputStream(zis);

			db = (Database)ois.readObject();

			zis.close();
			fis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
/*			try {
				File f = new File("findme");
				f.createNewFile();
			} catch (IOException e) {}*/
		}
		System.out.println("done");

		return db;
	}
}