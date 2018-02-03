package intro;

import java.beans.XMLEncoder;
import java.util.ArrayList;
import java.util.List;

public class XMLTest {
	public static void main(String[] args) {
		new XMLTest();
	}

	public XMLTest() {
		Outer o = new Outer();
		o.init();

		try {
			XMLEncoder xe = new XMLEncoder(System.out);

			xe.writeObject(o);

			xe.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static class Outer {
		private String os;
		private Inner i;

		public void init() {
			os = "outer string";
			i = new Inner();
			i.i = 7;
			i.is = "inner string";

			i.list = new ArrayList();
			i.list.add(new Integer(1));
			i.list.add(new Integer(2));
			i.list.add(new Integer(3));
		}

		public String getOS() {
			return os;
		}

		public void setOS(String s) {
			os = s;
		}

		public Inner getI() {
			return i;
		}

		public void setI(Inner i) {
			this.i = i;
		}
	}

	public static class Inner {
		public int i;
		public String is;
		public List list;

		public List getList() {
			return list;
		}

		public void setList(List l) {
			list = l;
		}

		public int getI() {
			return i;
		}

		public void setI(int z) {
			i = z;
		}

		public String getIS() {
			return is;
		}

		public void setIS(String s) {
			is = s;
		}
	}
}