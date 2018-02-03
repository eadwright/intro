package lahaina.runtime;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public final class State {
	private static Map methodMap = new HashMap();
	private static Set checked = new HashSet();

	private Map data;

	public State() {
		data = new HashMap();
	}

	public void setAttribute(String name, int value) {
		data.put(name, new Integer(value));
	}

	public void setAttribute(String name, short value) {
		data.put(name, new Integer(value));
	}

	public void setAttribute(String name, boolean value) {
		data.put(name, new Boolean(value));
	}

	public void setAttribute(String name, char value) {
		data.put(name, new Character(value));
	}

	public void setAttribute(String name, long value) {
		data.put(name, new Long(value));
	}

	public void setAttribute(String name, float value) {
		data.put(name, new Float(value));
	}

	public void setAttribute(String name, double value) {
		data.put(name, new Double(value));
	}

	public void setAttribute(String name, byte value) {
		data.put(name, new Byte(value));
	}

	public void setAttribute(String name, Object value) {
		if(value == null) {
			data.remove(name);
			return;
		}

		boolean exists = data.containsKey(name);

		if(exists && (value instanceof String)) {
			String s = (String)value;

			Object ov = data.get(name);
			Class oc = ov.getClass();

			try {
				if(oc.equals(Integer.class)) {
					data.put(name, new Integer(s));
					return;
				}
				if(oc.equals(Short.class)) {
					data.put(name, new Short(s));
					return;
				}
				if(oc.equals(Long.class)) {
					data.put(name, new Long(s));
					return;
				}
				if(oc.equals(Boolean.class)) {
					data.put(name, new Boolean(s));
					return;
				}
				if(oc.equals(Float.class)) {
					data.put(name, new Float(s));
					return;
				}
				if(oc.equals(Double.class)) {
					data.put(name, new Double(s));
					return;
				}
				if(oc.equals(Byte.class)) {
					data.put(name, new Byte(s));
					return;
				}
			} catch (NumberFormatException ex) {}
			data.put(name, value);
		} else
			data.put(name, value);
	}

	public void conditionalSetAttribute(boolean condition, String name, int value) {
		if(condition)
			setAttribute(name, value);
		else
			remove(name);
	}

	public void conditionalSetAttribute(boolean condition, String name, short value) {
		if(condition)
			setAttribute(name, value);
		else
			remove(name);
	}

	public void conditionalSetAttribute(boolean condition, String name, boolean value) {
		if(condition)
			setAttribute(name, value);
		else
			remove(name);
	}

	public void conditionalSetAttribute(boolean condition, String name, char value) {
		if(condition)
			setAttribute(name, value);
		else
			remove(name);
	}

	public void conditionalSetAttribute(boolean condition, String name, long value) {
		if(condition)
			setAttribute(name, value);
		else
			remove(name);
	}

	public void conditionalSetAttribute(boolean condition, String name, float value) {
		if(condition)
			setAttribute(name, value);
		else
			remove(name);
	}

	public void conditionalSetAttribute(boolean condition, String name, double value) {
		if(condition)
			setAttribute(name, value);
		else
			remove(name);
	}

	public void conditionalSetAttribute(boolean condition, String name, byte value) {
		if(condition)
			setAttribute(name, value);
		else
			remove(name);
	}

	public void conditionalSetAttribute(boolean condition, String name, Object value) {
		if(condition)
			setAttribute(name, value);
		else
			remove(name);
	}

	public boolean exists(String name) {
		if(name == null)
			return false;

		int i = name.indexOf('.');
		if(i == -1)
			return data.containsKey(name);

		String base = name.substring(0, i);

		Object obj = data.get(base);
		if(obj == null)
			return false;

		String param = name.substring(i+1);

		return getValue(obj, param) != null;
	}

	public void remove(String name) {
		data.remove(name);
	}

	public Object getAttribute(String name) {
		return getObject(name);
	}

	public String getStringAttribute(String name) {
		Object value = getObject(name);
		if(value == null)
			return null;

		if(value instanceof String)
			return (String)value;

		return value.toString();
	}

	public int getIntAttribute(String name) {
		Object value = getObject(name);
		if(value instanceof String)
			return Integer.parseInt((String)value);
		else
			return ((Integer)value).intValue();
	}

	public short getShortAttribute(String name) {
		Object value = getObject(name);
		if(value instanceof String)
			return Short.parseShort((String)value);
		else
			return ((Short)value).shortValue();
	}

	public long getLongAttribute(String name) {
		Object value = getObject(name);
		if(value instanceof String)
			return Long.parseLong((String)value);
		else
			return ((Long)value).longValue();
	}

	public boolean getBooleanAttribute(String name) {
		Object value = getObject(name);
		if(value == null) // special case, we'll take null as false
			return false;
		if(value instanceof String)
			return Boolean.valueOf((String)value).booleanValue();
		else
			return ((Boolean)value).booleanValue();
	}

	public float getFloatAttribute(String name) {
		Object value = getObject(name);
		if(value instanceof String)
			return Float.parseFloat((String)value);
		else
			return ((Float)value).floatValue();
	}

	public double getDoubleAttribute(String name) {
		Object value = getObject(name);
		if(value instanceof String)
			return Double.parseDouble((String)value);
		else
			return ((Double)value).doubleValue();
	}

	public byte getByteAttribute(String name) {
		Object value = getObject(name);
		if(value instanceof String)
			return Byte.parseByte((String)value);
		else
			return ((Long)value).byteValue();
	}

	public char getCharAttribute(String name) {
		Object value = getObject(name);
		if(value instanceof String) {
			if(((String)value).length() >0)
				return ((String)value).charAt(0);
			else
				return (char)0;
		} else
			return ((Character)value).charValue();
	}

	private Object getObject(String name) {
		if(name == null)
			return null;

		int i = name.indexOf('.');

		if(i == -1)
			return data.get(name);

		Object obj = data.get(name.substring(0,i));

		if((obj != null) && (i < name.length()-1))
			return doGetObject(obj, name.substring(i+1));
		else
			return obj;
	}

	private Object doGetObject(Object obj, String name) {
		int i = name.indexOf('.');

		String param;
		if(i != -1)
			param = name.substring(0, i);
		else
			param = name;

		obj = getValue(obj, param);

		if((obj != null) && (i > 0) && (i < name.length() - 1))
			return doGetObject(obj, name.substring(i+1));
		else
			return obj;
	}

	private static synchronized Object getValue(Object obj, String param) {
		Class clz = obj.getClass();

		StringBuffer buffer = new StringBuffer(clz.getName());
		buffer.append(':');
		buffer.append(param);
		String key = buffer.toString();

		try {
			Method method = (Method)methodMap.get(key);

			if(method == null) {
				if(checked.contains(key)) // don't check twice
					return null;

				method = getReflectedMethod(clz, param);

				if(method == null)
					method = getBeanMethod(clz, param);

				if(method != null) {
					methodMap.put(key, method);
					checked.add(key);
				} else {
					System.err.println("Warning, "+clz.getName()+" param "+param+" not found");
					return null;
//					throw new RuntimeException(clz.getName()+" param "+param+" not found");
				}
			}

			return method.invoke(obj, new Object[0]);
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
			return null;
		} catch (IllegalAccessException ex2) {
			ex2.printStackTrace();
			return null;
		} catch (IntrospectionException ex3) {
			ex3.printStackTrace();
			return null;
		}
	}

/*	private static synchronized boolean hasParameter(Object obj, String param) {
		Class clz = obj.getClass();

		StringBuffer buffer = new StringBuffer(clz.getName());
		buffer.append(':');
		buffer.append(param);
		String key = buffer.toString();

		if(methodMap.containsKey(key))
			return true;

		if(checked.contains(key)) // don't check twice
			return false;

		try {
			Method method = getReflectedMethod(clz, param);

			if(method == null)
				method = getBeanMethod(clz, param);

			if(method != null) {
				methodMap.put(key, method);
				return true;
			} else
				return false;

		} catch (IntrospectionException ex) {
			ex.printStackTrace();
			return false;
		}
	}*/

	private static Method getReflectedMethod(Class clz, String param) {
		Method[] m = clz.getMethods();

		for(int i=0;i<m.length;i++) {
			if(m[i].getName().equalsIgnoreCase(param)
					&& (m[i].getParameterTypes().length == 0))
				return m[i];
		}

		return null;
	}

	private static Method getBeanMethod(Class clz, String param) throws IntrospectionException {
		BeanInfo bi = Introspector.getBeanInfo(clz);
		PropertyDescriptor[] pd = bi.getPropertyDescriptors();

		for(int i=0;i<pd.length;i++) {
			if(pd[i].getName().equalsIgnoreCase(param))
				return pd[i].getReadMethod();
		}

		return null;
	}

	public Set getKeys() {
		return Collections.unmodifiableSet(data.keySet());
	}
}