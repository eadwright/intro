package intro2.ui.taglib;

import intro2.util.IList;
import lahaina.runtime.State;
import lahaina.runtime.Writer;
import lahaina.tag.NonEmptyTag;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

abstract class AbstractList implements NonEmptyTag {
	public final static String CURRENT = "current";
	public final static String TARGET = "target";

	protected State state;
	protected Object list;
	protected int position;
	private boolean first = true;

	protected AbstractList(State state) {
		this.state = state;
	}

	protected void setCurrentState(Object current) throws Exception {}
	protected void setCurrentState(int current) throws Exception {}

	protected boolean hasNext() {
		if(list == null)
			return false;

		if(list instanceof Set)
			list = ((Set)list).iterator();

		if(list instanceof Iterator)
			return ((Iterator)list).hasNext();

		if(list instanceof Object[])
			return position <((Object[])list).length;

		if(list instanceof List)
			return position < ((List)list).size();

		if(list instanceof IList)
			return position < ((IList)list).getSize();

		throw new RuntimeException("unrecognised list type");
	}

	protected Object getNext() {
		if(list instanceof Iterator)
			return ((Iterator)list).next();
		else if(list instanceof Object[])
			return ((Object[])list)[position++];
		else
			return ((List)list).get(position++);
	}

	protected int getNextInt() {
		return ((IList)list).get(position++);
	}

	public final boolean process(Writer out) throws Exception {
		if(!hasNext())
			return false;

		if(list instanceof IList)
			setCurrentState(getNextInt());
		else
			setCurrentState(getNext());

		return true;
	}
}