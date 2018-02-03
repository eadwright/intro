package intro2.ui.taglib;

import intro2.database.ClassInfo;
import intro2.database.Database;
import intro2.database.reference.InnerClassReference;
import intro2.util.IList;
import intro2.util.ISet;
import lahaina.runtime.State;
import lahaina.runtime.Writer;
import lahaina.tag.NonEmptyTag;

public final class ClassList implements NonEmptyTag {
	public static final String LISTNAME = "listName";
	public static final String CLASS = "ci";
	public static final String NAME = "name";
	public static final String DISPLAY_NAME = "displayName";
	public static final String COMMA = "comma";
	public static final String INNER_REF = "innerref";

	private State state;
	private Object[] data;
	private int position;

	public ClassList(State state) {
		this.state = state;

		String listName = state.getStringAttribute(LISTNAME);

		Object list = state.getAttribute(listName);
		if(list == null)
			return;

		if(list instanceof ISet)
			list = ((ISet)list).keys();

		if(list instanceof int[]) {
			int[] intList = (int[])list;
			data = new String[intList.length];

			for(int i=0;i<intList.length;i++)
				data[i] = Database.getClassName(intList[i]);
		}

		if(list instanceof IList) {
			IList iList = (IList)list;

			data = new String[iList.getSize()];
			for(int i=0;i<iList.getSize();i++)
				data[i] = Database.getClassName(iList.get(i));
		}

		if(list instanceof Object[])
			data = (Object[])list;

		position = 0;
	}

	public boolean process(Writer out) {
		if(data == null)
			return false;

		if(position >= data.length)
			return false;

		state.conditionalSetAttribute((position < data.length-1), COMMA, ", ");

		Object obj = data[position++];

		String name = null;
		ClassInfo ci = null;

		if(obj instanceof String) {
			name = (String)obj;
			ci = Database.getClassInfo(name);
		}

		if(obj instanceof InnerClassReference) {
			InnerClassReference innerRef = (InnerClassReference)obj;
			ci = Database.getClassInfo(innerRef.getInnerClassID());
			name = Database.getClassName(innerRef.getInnerClassID());
			state.setAttribute(INNER_REF, innerRef);
		}

		String displayName = ClassInfo.getDisplayName(name);

		state.setAttribute(NAME, name);
		state.setAttribute(DISPLAY_NAME, displayName);
		state.setAttribute(CLASS, ci);

		if(ci != null)
			state.setAttribute(HyperLink.HREF, ci.getOutputFilename());
		else
			state.remove(HyperLink.HREF);

		return true;
	}
}