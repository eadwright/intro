package test;

import lahaina.compile.SourceFactory;

public class Test5 {
	public static void main(String[] args) {
		try {
			SourceFactory.createSource("test2.lp","src", new Class[] { Tag3.class });
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}