package intro2.ui.taglib;

public interface Constants {
	public final static int ALL_MODULES = 0; // = "all modules" too
	public final static int MODULE = 1;
	public final static int MODULE_USES_OUTWARD = 2;
	public final static int MODULE_USES_INWARD = 3;
	public final static int PACKAGE = 4;
	public final static int PACKAGE_USES_OUTWARD = 5;
	public final static int PACKAGE_USES_INWARD = 6;
	public final static int PACKAGE_USES_OUTWARD_COMPLETE = 7;
	public final static int PACKAGE_USES_INWARD_COMPLETE = 8;
	public final static int CLASS = 9;
	public final static int CLASS_USES_OUTWARD = 10;
	public final static int CLASS_USES_INWARD = 11;
	public final static int CLASS_USES_OUTWARD_SELFREF = 12;
	public final static int CLASS_USES_INWARD_SELFREF = 13;
	public final static int BYTECODE = 14;
	public final static int ALL_PACKAGES = 15;
	public final static int ALL_CLASSES = 16;

	public final static String REFLECTION = "reflection";
	public final static String NATIVE = "native";

	public final static String TITLE = "title";

	public final static String INCLUDESELF = "includeSelf";

	public final static String USES = "uses";
	public final static String USEDBY = "usedBy";

	public final static String PROJECTNAME = "projectName";
}