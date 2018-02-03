package intro2.ui.taglib;

import intro2.database.ClassInfo;
import intro2.database.Database;
import intro2.database.member.Field;
import intro2.output.Type;
import lahaina.runtime.State;

import java.util.TreeSet;

public final class FieldList extends AbstractList {
	public static final String TYPE = "type";

	private ClassInfo ci;

	public FieldList(State state) throws Database.DataProcessingException {
		super(state);

		ci = (ClassInfo)state.getAttribute("this");

		int[] offsets = ci.getFieldOffsets();

		if(offsets == null)
			return;

		TreeSet fields = new TreeSet();
		for(int i=0;i<offsets.length;i++)
			fields.add(ci.getField(offsets[i]));

		list = fields;
	}

	// n.b. throws clause is tmp
	protected void setCurrentState(Object arg) throws Database.DataProcessingException {
		Field field = (Field)arg;
		Type type = field.getType();

		state.setAttribute(CURRENT, field);
		state.setAttribute(TYPE, type);

		state.conditionalSetAttribute(!type.isPrimative(), HyperLink.HREF, type.getHref());

		// tmp test
//		if(intro2.output.ReferenceUtil.isFieldUsed(ci, field.getOffset(), false))
//			System.err.println(ci.getName()+"."+field.getName()+" externally used");
	}
}