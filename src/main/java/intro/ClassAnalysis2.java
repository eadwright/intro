package intro;

import java.lang.reflect.Member;

public class ClassAnalysis2 {
	public static void main(String[] argv) {
		try {
			Class c = Class.forName("Subject1");

		    listClasses("classes",c.getClasses());
		    listClasses("decl classes",c.getDeclaredClasses());
		    listClasses("interfaces",c.getInterfaces());
			listMembers("fields",c.getDeclaredFields());
			listMembers("constructors",c.getConstructors());
			listMembers("methods",c.getDeclaredMethods());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void listMembers(String title,Member[] members) {
		System.out.println(title+":");
		for(int count = 0; count<members.length;count++)
			System.out.println(count+": "+members[count].getName());
	}

	private static void listClasses(String title,Class[] members) {
		System.out.println(title+":");
		for(int count = 0; count<members.length;count++)
			System.out.println(count+": "+members[count].getName());
	}

}