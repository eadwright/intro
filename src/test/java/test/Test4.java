package test;

import lahaina.fragment.FragmentFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Test4 {
	public static void main(String[] argv) {
		new Test4().go();
	}

	public Test4() {}

	public void go() {
		try {
			String rawData = loadString();

			lahaina.fragment.Fragment f = FragmentFactory.buildTree(rawData, getSpecialTags());

			System.out.println(f.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Class[] getSpecialTags() {
		return new Class[] { Tag3.class };
	}

	private String loadString() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("test2.lp"));
		StringBuffer sb = new StringBuffer();

		String line = null;
		while((line = br.readLine()) != null) {
			sb.append(line);
//			if(line.charAt(line.length()-1)!='>')
				sb.append('\n');
		}
		br.close();

		return sb.toString();
	}

}