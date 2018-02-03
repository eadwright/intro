package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;

public class Test3 {
	public static void main(String[] argv) {
		new Test3().go();
	}

	public Test3() {}

	public void go() {
		try {
			Fragment2 f = Fragment2.getInitial(new BufferedReader(new FileReader("test2.lp")), getSpecialTagNames());

			showFragment(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String[] getSpecialTagNames() {
		return new String[] { "tag3" };
	}

	private void showFragment(Fragment2 f) throws Exception {
		if(f == null)
			return;

		String name = f.getName();
		if(name != null)
			System.out.println("[<"+f.getName()+">]");
		else
			System.out.println("[raw]");

		if(f.getAttributes()!=null) {
			Map a = f.getAttributes();
			System.out.println("[ "+a.size()+" attributes ]");
			Iterator it = a.keySet().iterator();
			String key, value;
			while(it.hasNext()) {
				key = (String)it.next();
				value = (String)a.get(key);
				System.out.println("[ "+key+" = "+value+" ]");
			}
		}

		if(f.getContent()!=null)
			System.out.println(f.getContent());

		if(f.hasChildren()) {
//			System.out.println("child of "+f.getName());
			showFragment(f.getChild());
		}

		if(name != null)
			System.out.println("[</"+f.getName()+">]");

		if(f.hasNext()) {
//			System.out.println("next (after "+f.getName()+")");
			showFragment(f.getNext());
		}

	}

}