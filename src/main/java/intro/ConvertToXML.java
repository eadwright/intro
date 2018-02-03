package intro;

import intro.analysis.Database;

import java.beans.XMLEncoder;
import java.io.FileOutputStream;

public class ConvertToXML {
	public static void main(String[] args) {
		Database db = LoadJarAnalysis.loadDatabase();

		System.out.println("begin writing XML data");
		long time = System.currentTimeMillis();
		try {
			FileOutputStream fos = new FileOutputStream("d:/projects/Ideas/WEB-INF/database.xml");
//			GZIPOutputStream zos = new GZIPOutputStream(fos,1024*1024);
			XMLEncoder xe = new XMLEncoder(fos);

			xe.writeObject(db);

			xe.close();
			fos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("done, "+(System.currentTimeMillis()-time)/1000+"secs");
	}
}