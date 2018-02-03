package test;

import java.io.BufferedReader;
import java.io.FileReader;

public class Test2 {
	public static void main(String[] argv) {
		new Test2().go();
	}

	public Test2() {}

	public void go() {
		try {
			Fragment f = Fragment.getInitial(new BufferedReader(new FileReader("test2.lp")));

			showFragment(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showFragment(Fragment f) throws Exception {
		if(f == null)
			return;

		String name = f.getName();
		if(name != null)
			System.out.println("<"+f.getName()+">");
		else
			System.out.println("raw: "+f.getContent());

		if(f.isCode())
			System.out.println("code: "+f.getContent());

		if(f.containsContent()) {
//			System.out.println("child of "+f.getName());
			showFragment(f.getSubFragment());
		}

		if(name != null)
			System.out.println("</"+f.getName()+">");

		if(f.hasNext()) {
//			System.out.println("next (after "+f.getName()+")");
			showFragment(f.getNext());
		}

	}

}