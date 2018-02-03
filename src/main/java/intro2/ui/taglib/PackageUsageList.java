package intro2.ui.taglib;

import intro2.database.ClassInfo;
import intro2.database.Database;
import intro2.database.Package;
import intro2.database.reference.Reference;
import intro2.util.UniqueIList;
import lahaina.runtime.State;

import java.util.ArrayList;
import java.util.List;

public final class PackageUsageList extends AbstractList {
	public static final String THIS = "this";
	public static final String OTHER = "other";
	public static final String OUTWARD = "outward";
	public static final String LEFT = "left";
	public static final String RIGHT = "right";

	public static final String CLASS = "ci";
	public static final String NAME = "name";
	public static final String DISPLAY_NAME = "displayName";

	public PackageUsageList(State state) {
		super(state);

		list = process();
		position = 0;
	}

	private List process() {
		Package pkg = (Package)state.getAttribute(THIS);
		Package other = (Package)state.getAttribute(OTHER);
		boolean outward = state.getBooleanAttribute(OUTWARD);

		UniqueIList refs = null;

		if(outward)
			refs = pkg.getReferencesByDestPackage(other);
		else
			refs = other.getReferencesBySrcPackage(pkg);

		List clzList = new ArrayList();

		// sort somehow?
		Reference ref;
		int source, target, actualTarget;
		for(int i=0;i<refs.getSize();i++) {
			ref = Database.getReference(refs.get(i));

			if(outward) {
				source = ref.getSourceClassID();
				target = ref.getTargetClassID();
				actualTarget = ref.getActualTargetClassID();

				if(target != Integer.MIN_VALUE)
					clzList.add(new int[] { source, target });

				if((actualTarget != Integer.MIN_VALUE) && (actualTarget != target))
					clzList.add(new int[] { source, actualTarget });
			}
		}

		return clzList;
	}

	protected void setCurrentState(Object current) {
		int[] c = (int[])current;

		state.setAttribute("left", Database.getClassInfo(c[0])); // check for null?
		state.setAttribute("right", Database.getClassInfo(c[1]));

//		if(outward)
			state.setAttribute(HyperLink.HREF, ClassInfo.getOutputFilename(Database.getClassName(c[0])));
//		else
//			state.setAttribute(HyperLink.HREF, ClassInfo.getOutputFilename(Database.getClassName(c[1])));

/*		if(outward)
			state.setAttribute(HyperLink.HREF, Package.getUsesFilename(p[0], p[1]));
		else
			state.setAttribute(HyperLink.HREF, Package.getUsedByFilename(p[0], p[1]));*/

	}
}