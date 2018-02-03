package intro2.ui.taglib;

import intro2.database.ClassInfo;
import intro2.database.Database;
import intro2.database.member.Method;
import intro2.output.Type;
import lahaina.runtime.State;

import java.util.TreeSet;

public final class MethodList extends AbstractList {
	public static final String RETURN_TYPE = "returnType";
	public static final String ARGS = "args";

	private ClassInfo ci;

	public MethodList(State state) throws Database.DataProcessingException {
		super(state);

		ci = (ClassInfo)state.getAttribute("this");

		int[] offsets = ci.getMethodOffsets();

		if(offsets == null)
			return;

		TreeSet methods = new TreeSet();
		for(int i=0;i<offsets.length;i++)
			methods.add(ci.getMethod(offsets[i]));

		list = methods;
	}

	// n.b. throws clause is tmp
	protected void setCurrentState(Object arg) throws Database.DataProcessingException {
		Method method = (Method)arg;

		Type returnType = method.getReturnType();

		state.setAttribute(CURRENT, method);
		state.setAttribute(RETURN_TYPE, returnType);
		state.setAttribute(ARGS, method.getArgs());

		state.conditionalSetAttribute(!returnType.isPrimative(), HyperLink.HREF, returnType.getHref());

		// tmp test
//		if(intro2.output.ReferenceUtil.isMethodUsed(ci, method.getOffset(), false))
//			System.err.println(ci.getName()+"."+method.getName()+"() externally used");
	}
}