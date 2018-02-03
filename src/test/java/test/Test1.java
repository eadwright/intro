package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test1 {
	public static void main(String[] argv) {
		new Test1().go();
	}

	public Test1() {}

	public void go() {
		try {
			String allData = readFile();

			doTest(allData, "tag1");
			doTest(allData, "tag2");
			doTest(allData, "tag3");

//			System.out.println(allData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String readFile() throws IOException {
		FileReader fr = new FileReader("test2.lp");
		BufferedReader br = new BufferedReader(fr);

		String line = null;
		StringBuffer all = new StringBuffer();
		do {
			line = br.readLine();
			if(line != null) {
				all.append(line);
				all.append("\n");
			}
		} while(line != null);
		br.close();

		return all.toString();
	}

/*	private void doTest(String all) {
		Pattern pattern = Pattern.compile("(<%)");
		Matcher matcher = pattern.matcher(all);

		Pattern pattern2 = Pattern.compile("(/>)");
		Matcher matcher2;

		int start,end=0;
		while(matcher.find(end)) {
			start = matcher.start();
			matcher2 = pattern2.matcher(all.substring(start));
			if(!matcher2.find())
				System.out.println("Can't find end!");
			else {
				end = matcher2.start();
				System.out.println(all.substring(start, start+end+2));
				end+=start+2;
			}
		}
	}*/

	private void doTest(String all, String tagName) {
		Pattern pattern = Pattern.compile("<"+tagName+"[^>]*", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(all);

		Pattern pattern2 = Pattern.compile(">");
		Pattern pattern3 = Pattern.compile("</"+tagName+">", Pattern.CASE_INSENSITIVE);
		Matcher matcher2;

		int start,end=0;
		String group;
		while(matcher.find(end)) {
			start = matcher.start();
			group = matcher.group();

//			System.out.println("Group "+group);

			if(group.charAt(group.length()-1)=='/')
				matcher2 = pattern2.matcher(all.substring(start));
			else
				matcher2 = pattern3.matcher(all.substring(start));

			if(!matcher2.find())
				System.out.println("Can't find end!");
			else {
				end = matcher2.start();
				System.out.println("got: "+all.substring(start, start+matcher2.end()));
				System.out.println();
				end+=start+2;
			}
		}
	}
}