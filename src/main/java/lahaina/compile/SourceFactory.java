package lahaina.compile;

import lahaina.fragment.Fragment;
import lahaina.fragment.FragmentFactory;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SourceFactory {
	private SourceFactory() {}

	public static synchronized void createSource(String pageFileName, String sourceDirectoryName, Class[] tags) throws IOException {
		String data = loadString(pageFileName);

		Fragment first = FragmentFactory.buildTree(data, tags);

		Source s = new Source(first);

		saveData(s.getClassName(), s.getPackage(), sourceDirectoryName, s.toString());
	}

	private static void saveData(String cn, String p, String dir, String src) throws IOException {
		if(p != null) {
			Pattern pattern = Pattern.compile("\\x2e"); // 0x2e = '.'
			Matcher matcher = pattern.matcher(p);

			p = matcher.replaceAll("/");
		}

		if(p != null)
			dir += "/" + p;

		new File(dir).mkdirs();

		File file = new File(dir, cn + ".java");

		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write(src);
		bw.close();
	}

	private static String loadString(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		StringBuffer sb = new StringBuffer();

		String line = null;
		while((line = br.readLine()) != null) {
			sb.append(line);
			sb.append('\n');
		}
		br.close();

		return sb.toString();
	}
}