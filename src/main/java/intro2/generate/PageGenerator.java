package intro2.generate;

import intro2.ui.taglib.*;
import lahaina.compile.SourceFactory;
import lahaina.tag.Include;

/**
 * This program is used to create the Java source from Lahaina pages. It is
 * never invoked in a user's envionment
 */
public final class PageGenerator {
	private PageGenerator() {}

	public static void main(String[] args) {
		String current = null;
		try {
			String[] fn = getFilenames();
			String prefix = "d:\\projects\\intro\\dynamic\\";
			String dir = "d:\\projects\\intro\\";
			Class[] tags = getTagClasses();

			for(int i=0;i<fn.length;i++) {
				current = fn[i];
				SourceFactory.createSource(prefix+fn[i], dir, tags);
			}
		} catch (Exception ex) {
			System.out.println("Exception processing "+current);
			ex.printStackTrace();
		}
	}

	private static String[] getFilenames() {
		return new String[] { "index.html",
							  "header.html",
							  "footer.html",
							  "navbar.html",
							  "stylesheet.css",
							  "key.html",
							  "module.html",
							  "package.html",
							  "class.html",
							  "nr.html",
							  "method.html",
							  "module_uses.html",
							  "module_usedby.html",
							  "inner_class.html",
							  "package_uses.html",
							  "package_usedby.html" };
	}

	private static Class[] getTagClasses() {
		return new Class[] { Include.class, Date.class, Exists.class,
							HyperLink.class, Dots.class, NavbarInit.class,
							ModuleTree.class, LibraryList.class,
							Loop.class, NotExists.class, And.class,
							Or.class, Else.class, ModuleList.class,
							PackageList.class, PackageTree.class,
							ModuleUsageSummaryList.class,
							PackageUsageSummaryList.class,
							HyperLink2.class, Ignore.class,
							ClassList.class, BAnd.class, BOr.class,
							Not.class, Is.class, Equals.class,
							Assign.class, ClassTree.class,
							GetClass.class, FieldList.class,
							MethodList.class, MethodArgList.class,
							ModuleUsageList.class, LookupAssign.class,
							InnerClassListInit.class,
							PackageUsageList.class };
	}
}