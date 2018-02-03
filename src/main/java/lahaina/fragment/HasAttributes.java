package lahaina.fragment;

import lahaina.runtime.LahainaException;

abstract class HasAttributes extends Fragment {
	abstract void addAttribute(String name, String value);

	protected void check(String name, String value) {
		if(name == null)
			throw new LahainaException("null attribute name");

		if(value == null)
			throw new LahainaException("null attribute value, name is: "+name);
	}
}