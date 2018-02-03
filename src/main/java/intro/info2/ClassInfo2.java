package intro.info2;

import intro.Servlet1;
import intro.info.ClassInfo;
import intro.info.attribute.AbstractAttribute;
import intro.info.attribute.ExceptionsAttribute;
import intro.info.member.FieldInfo;
import intro.info.member.Member;
import intro.info.pool.CPInfo;
import intro.info.pool.ClassCPI;
import intro.info.pool.IntCPI;
import intro.info.pool.StringCPI;

import java.io.Serializable;
import java.util.*;

public class ClassInfo2 implements Comparable, Serializable {
	static final long serialVersionUID = -4806030188890504773L;

	private static int listsCounter = 0;

//	public ClassInfo source;

	public int minorVersion;
	public int majorVersion;

	public boolean deprecated;
	public int accessFlags;
	public String thisClass;
	public String superClass;
	public String[] interfaces;
	public Field[] fields;
	public Method[] methods;

	public transient ClassLists lists;
	public int listsID;

	public ClassInfo2(ClassInfo source, Map xref) {
//		this.source = source;

		listsID = getListsID();

		minorVersion = source.minorVersion;
		majorVersion = source.majorVersion;

		accessFlags = source.accessFlags;
		thisClass = source.thisClass;
		superClass = source.superClass;
		interfaces = source.interfaces;

		if(superClass == null)
			System.out.println("no superclass, "+thisClass);

		deprecated = source.hasDeprecatedAttribute(source.attributes);

		fields = new Field[source.fields.length];

		for(int i=0;i<fields.length;i++)
			fields[i] = new Field(source,source.fields[i], xref);

		Arrays.sort(fields);

		methods = new Method[source.methods.length];

		for(int i=0;i<methods.length;i++)
			methods[i] = new Method(source.methods[i], xref);

		Arrays.sort(methods);

		lists = new ClassLists();
		xref.put(new Integer(listsID),lists);
	}

	public String toString() {
		return thisClass;
	}

	public void sortReferences() {
		lists.sort();
		int i;
		for(i=0;i<fields.length;i++)
			Collections.sort(fields[i].usedBy);
		for(i=0;i<methods.length;i++)
			methods[i].lists.sort();
	}

	public boolean isInteface() {
		return (accessFlags&0x0200)!=0;
	}

	public String getAccessFlagsAsString() {
		StringBuffer buffer = new StringBuffer();

		if((accessFlags&0x0001)!=0)
			buffer.append("public ");
		if((accessFlags&0x0010)!=0)
			buffer.append("final ");
		if((accessFlags&0x0400)!=0)
			buffer.append("abstract ");
		// also 0x0020 (super), 0x0200 (interface)

		return buffer.toString().trim().intern();
	}

	public int compareTo(Object other) {
		return thisClass.compareTo(((ClassInfo2)other).thisClass);
	}

	public void restore(Map xref) {
		for(int i=0;i<fields.length;i++)
			fields[i].restore(xref);

		for(int i=0;i<methods.length;i++)
			methods[i].restore(xref);

		lists = (ClassLists)xref.get(new Integer(listsID));
	}

	public static synchronized int getListsID() {
		return listsCounter++;
	}

	public void dumpFields() {
		for(int i=0;i<fields.length;i++)
			System.out.println("f "+thisClass+"."+fields[i].name+" "+fields[i].descriptor);
	}

	public void dumpMethods() {
		for(int i=0;i<methods.length;i++)
			System.out.println("m "+thisClass+"."+methods[i].name+" "+methods[i].descriptor);
	}

	public Field getField(String name, String desc) {
		for(int i=0;i<fields.length;i++)
			if(fields[i].name.equals(name) && fields[i].descriptor.equals(desc))
				return fields[i];

		return null;
	}

	public Method getMethod(String name, String desc) {
		for(int i=0;i<methods.length;i++)
			if(methods[i].name.equals(name) && methods[i].descriptor.equals(desc))
				return methods[i];

		return null;
	}

	public Reference addMethodReference(ClassInfo2 targetClass, ClassInfo2 callingClass,
									Method callingMethod, String name, String desc,
									Map map2) {

		Method m = null;
		for(int i=0;i<methods.length;i++)
			if(methods[i].name.equals(name) && methods[i].descriptor.equals(desc)) {
				m = methods[i];
				break;
			}

		Reference ref = null;
		if(m!=null) {
			ref = new Reference();
			ref.sourceClass = callingClass;
			ref.sourceMethod = callingMethod;
			ref.targetDestinationClass = targetClass;
			ref.actualDestinationClass = this;
			ref.destinationMethod = m;

			m.lists.calledBy.add(ref);
			callingMethod.lists.methodsCalled.add(new InverseReference(ref));

			return ref;
		} else {
			if(superClass!=null) {
				ClassInfo2 sc = (ClassInfo2)map2.get(superClass);
				if(sc!=null) // do we want this line?
					ref = sc.addMethodReference(targetClass,callingClass,callingMethod,
																			name,desc,map2);
			}

			if(ref!=null) {
//				System.out.println("--> clz "+ref.sourceClass.thisClass+" calls "+ref.actualDestinationClass.thisClass+" target "+ref.targetDestinationClass.thisClass);
				lists.superClassMethodCalls.add(ref);
			}

			return ref;
		}
	}

	public Reference addFieldReference(ClassInfo2 targetClass, ClassInfo2 callingClass,
									Method callingMethod, String name, String desc,
									Map map) {

		Field f = null;
		for(int i=0;i<fields.length;i++)
			if(fields[i].name.equals(name) && fields[i].descriptor.equals(desc)) {
				f = fields[i];
				break;
			}

		Reference ref = null;
		if(f!=null) {
			ref = new Reference();
			ref.sourceClass = callingClass;
			ref.sourceMethod = callingMethod;
			ref.targetDestinationClass = targetClass;
			ref.actualDestinationClass = this;
			ref.destinationField = f;

			f.usedBy.add(ref);
			callingMethod.lists.fieldsUsed.add(new InverseReference(ref));

			return ref;
		} else {
			if(superClass!=null) {
				ClassInfo2 sc = (ClassInfo2)map.get(superClass);
				if(sc!=null) // do we want this line?
					ref = sc.addFieldReference(targetClass,callingClass,callingMethod,
																			name,desc,map);
			}

			if(ref!=null) {
//				System.out.println("--> clz "+ref.sourceClass.thisClass+" uses "+ref.actualDestinationClass.thisClass+" target "+ref.targetDestinationClass.thisClass);
				lists.superClassFieldUsage.add(ref);
			}

			return ref;
		}
	}

	public static class Field implements Comparable,Serializable {
		static final long serialVersionUID = 5376385762903112876L;

		int listsID;

		public boolean deprecated;
		public int flags;
		public String flagsString;

		public String name;
		public String descriptor;
		public String constantValue;

		public transient List usedBy;

		private Field(ClassInfo orig,FieldInfo source, Map xref) {
			listsID = getListsID();

			deprecated = ClassInfo.hasDeprecatedAttribute(source.attributes);
			flags = source.flags;
			flagsString = getAccessFlagsAsString();

			name = source.name;
			descriptor = source.descriptor;

			usedBy = new UniqueList();
			xref.put(new Integer(listsID),usedBy);

			if(source.value!=null)
				setValue(orig,source.value);
		}

		private void setValue(ClassInfo orig,CPInfo cpi) {
			int i=0;
			char type;
			do {
				type = descriptor.charAt(i);
			} while (descriptor.charAt(i++)=='[');

			switch(type) {
				case 'B':
					constantValue = new Byte((byte)(((IntCPI)cpi).bytes)).toString();
					break;
				case 'C':
					constantValue = String.valueOf((char)(((IntCPI)cpi).bytes&0x0ffff));
					break;
				case 'D':
				case 'F':
				case 'I':
				case 'J':
					constantValue = cpi.toString();
					break;
				case 'S':
					constantValue = String.valueOf((short)(((IntCPI)cpi).bytes));
					break;
				case 'Z':
					if(((IntCPI)cpi).bytes == 0)
						constantValue = "false";
					else
						constantValue = "true";
					break;

				case 'L':
					constantValue = orig.getCPI(((StringCPI)cpi).stringIndex).toString();
					break;
				default:
					throw new RuntimeException("invalid field descriptor for a constant "+descriptor+" type "+type);
			}
		}

		public int compareTo(Object other) {
			return name.compareTo(((Field)other).name);
		}

		public void restore(Map xref) {
			usedBy = (UniqueList)xref.get(new Integer(listsID));
		}

		public String toString() {
			StringBuffer buffer = new StringBuffer(getSignature());

			if(usedBy.size()>0) {
				buffer.append("<br>");
				buffer.append("Used by: <ul>");

				Object obj;
				Reference ref;
				for(int i=0;i<usedBy.size();i++) {
					obj = usedBy.get(i);
					if(obj instanceof Reference) {
						ref = (Reference)obj;
						buffer.append("<li>Class ");
						buffer.append(Servlet1.asRef(ref.sourceClass));
						buffer.append(" method ");
						buffer.append(ref.sourceMethod.getSignature());
					}
					if(obj instanceof UnresolvedReference) {
						buffer.append("<li>unresolved reference"); // add more info here
					}
				}

				buffer.append("</ul>");
			}

			return buffer.toString();
		}

		public String getSignature() {
			StringBuffer buffer = new StringBuffer(getAccessFlagsAsString());
			buffer.append(' ');
			buffer.append(decodeType(descriptor));
			buffer.append(' ');
			buffer.append(name);

			if(constantValue!=null) {
				buffer.append(" = ");
				buffer.append(constantValue);
			}

			if(deprecated)
				buffer.append(" (DEPRECATED)");

			return buffer.toString();
		}

		private String getAccessFlagsAsString() {
			StringBuffer buffer = new StringBuffer();

			if((flags&0x0001)!=0)
				buffer.append("public ");
			if((flags&0x0002)!=0)
				buffer.append("private ");
			if((flags&0x0004)!=0)
				buffer.append("protected ");
			if((flags&0x0008)!=0)
				buffer.append("static ");
			if((flags&0x0010)!=0)
				buffer.append("final ");
			if((flags&0x0040)!=0)
				buffer.append("volatile ");
			if((flags&0x0080)!=0)
				buffer.append("transient ");

			return buffer.toString().trim().intern();
		}
	}

	public static class Method implements Comparable,Serializable {
		static final long serialVersionUID = -1027472034726277002L;

		public transient MethodLists lists;
		public int listsID;

		public boolean deprecated;
		public int flags;
		public String flagsString;

		public String name;
		public String descriptor;

		public String exceptions[];

		private Method(Member source, Map xref) {
			listsID = getListsID();

			deprecated = ClassInfo.hasDeprecatedAttribute(source.attributes);
			flags = source.flags;
			flagsString = getAccessFlagsAsString();

			name = source.name;
			descriptor = source.descriptor;

			AbstractAttribute aa;
			ExceptionsAttribute ea;
			ClassCPI cpi;

			for(int i=0;i<source.attributes.length;i++) {
				aa = (AbstractAttribute)source.attributes[i];

				if(aa instanceof ExceptionsAttribute) {
					ea = (ExceptionsAttribute)aa;
//					System.out.println("got ExceptionsAttribute with "+ea.offlists.length+" exceptions");

					exceptions = new String[ea.offsets.length];

					for(int j=0;j<ea.offsets.length;j++) {
//						System.out.println(name+" does throw "+ea.offsets[j]);
						exceptions[j] = ea.offsets[j].name;
					}

					break;
				}
			}

			lists = new MethodLists();
			xref.put(new Integer(listsID),lists);
		}

		public int compareTo(Object other) {
			return name.compareTo(((Method)other).name);
		}

		public void restore(Map xref) {
			lists = (MethodLists)xref.get(new Integer(listsID));
		}

		public String toString() {
			StringBuffer buffer = new StringBuffer(getSignature());

			Object obj;
			Reference ref;
			UnresolvedReference uref;

			buffer.append("<br>");
			if(lists.fieldsUsed.size()>0) {
				buffer.append("<br>");
				buffer.append("Uses fields: <ul>");

				for(int i=0;i<lists.fieldsUsed.size();i++) {
					obj = lists.fieldsUsed.get(i);
					if(obj instanceof Reference) {
						ref = (Reference)obj;
						buffer.append("<li>In ");
						buffer.append(Servlet1.asRef(ref.actualDestinationClass));
						buffer.append(' ');
						buffer.append(ref.destinationField.getSignature());
						if(ref.actualDestinationClass!=ref.targetDestinationClass) {
							buffer.append(" target class was ");
							buffer.append(Servlet1.asRef(ref.targetDestinationClass));
						}
					}
					if(obj instanceof UnresolvedReference) {
						uref = (UnresolvedReference)obj;
						buffer.append("<li>unresolved reference to class "+uref.destinationClassName+" field "+uref.destinationFieldName);
					}
				}

				buffer.append("</ul>");
			}

			if(lists.methodsCalled.size()>0) {
				buffer.append("<br>");
				buffer.append("Calls: <ul>");

				for(int i=0;i<lists.methodsCalled.size();i++) {
					obj = lists.methodsCalled.get(i);
					if(obj instanceof Reference) {
						ref = (Reference)obj;
						buffer.append("<li>On ");
						buffer.append(Servlet1.asRef(ref.actualDestinationClass));
						buffer.append(' ');
						buffer.append(ref.destinationMethod.getSignature());
						if(ref.actualDestinationClass!=ref.targetDestinationClass) {
							buffer.append(" target class was ");
							buffer.append(Servlet1.asRef(ref.targetDestinationClass));
						}
					}
					if(obj instanceof UnresolvedReference) {
						uref = (UnresolvedReference)obj;
						buffer.append("<li>unresolved reference to class "+uref.destinationClassName+" method "+uref.destinationMethodName);
					}
				}

				buffer.append("</ul>");
			}

			if(lists.classesCreated.size()>0) {
				buffer.append("<br>");
				buffer.append("Creates: <ul>");

				for(int i=0;i<lists.classesCreated.size();i++) {
					obj = lists.classesCreated.get(i);
					if(obj instanceof Reference) {
						ref = (Reference)obj;
						buffer.append("<li>");
						buffer.append(Servlet1.asRef(ref.actualDestinationClass));
					}
					if(obj instanceof UnresolvedReference) {
						buffer.append("<li>unresolved reference"); // add more info here
					}
				}

				buffer.append("</ul>");
			}

			if(lists.classesUsed.size()>0) {
				buffer.append("<br>");
				buffer.append("Uses: <ul>");

				for(int i=0;i<lists.classesUsed.size();i++) {
					obj = lists.classesUsed.get(i);
					if(obj instanceof Reference) {
						ref = (Reference)obj;
						buffer.append("<li>");
						buffer.append(Servlet1.asRef(ref.actualDestinationClass));
					}
					if(obj instanceof UnresolvedReference) {
						buffer.append("<li>unresolved reference"); // add more info here
					}
				}

				buffer.append("</ul>");
			}

			if(lists.calledBy.size()>0) {
				buffer.append("<br>");
				buffer.append("Called by: <ul>");

				for(int i=0;i<lists.calledBy.size();i++) {
					obj = lists.calledBy.get(i);
					if(obj instanceof Reference) {
						ref = (Reference)obj;
						buffer.append("<li>Class ");
						buffer.append(Servlet1.asRef(ref.sourceClass));
						buffer.append(" method ");
						buffer.append(ref.sourceMethod.getSignature());
					}
					if(obj instanceof UnresolvedReference) {
						buffer.append("<li>unresolved reference"); // add more info here
					}
				}

				buffer.append("</ul>");
			}

			return buffer.toString();
		}

		public String getSignature() {
			StringBuffer buffer = new StringBuffer(getAccessFlagsAsString());
			buffer.append(' ');
			buffer.append(getReturnType());
			buffer.append(' ');

			buffer.append(name);

			buffer.append('(');
			buffer.append(getParameters());
			buffer.append(')');

			if(exceptions!=null && exceptions.length>0) {
				buffer.append(" throws ");
				for(int i=0;i<exceptions.length;i++) {
					buffer.append(Servlet1.asRef(exceptions[i]));
					if(i<exceptions.length-1)
						buffer.append(", ");
				}
			}

			if(deprecated)
				buffer.append(" (DEPRECATED)");

			return buffer.toString();
		}

		private String getReturnType() {
			int i=descriptor.lastIndexOf(')');

			return decodeType(descriptor.substring(i+1));
		}

		private String getParameters() {
			StringBuffer result = new StringBuffer();

			String middle = descriptor.substring(1,descriptor.lastIndexOf(')'));
			String type;
			int len;

			while(middle.length()>0) {
				type = decodeType(middle);
				len = getTypeLength(middle);
				middle = middle.substring(len);
				result.append(type);
				if(middle.length()>0)
					result.append(", ");
			}

			return result.toString();
		}

		private String getAccessFlagsAsString() {
			StringBuffer buffer = new StringBuffer();

			if((flags&0x0001)!=0)
				buffer.append("public ");
			if((flags&0x0002)!=0)
				buffer.append("private ");
			if((flags&0x0004)!=0)
				buffer.append("protected ");
			if((flags&0x0008)!=0)
				buffer.append("static ");
			if((flags&0x0010)!=0)
				buffer.append("final ");
			if((flags&0x0020)!=0)
				buffer.append("synchronized ");
			if((flags&0x0100)!=0)
				buffer.append("native ");
			if((flags&0x0200)!=0)
				buffer.append("abstract ");
			if((flags&0x0200)!=0)
				buffer.append("strict ");

			return buffer.toString().trim().intern();
		}
	}

	private static String decodeType(String type) {
		int dim = 0;
		while(type.charAt(dim)=='[')
			dim++;

		char t=type.charAt(dim);
		String clz = null;

		switch(t) {
			case 'V':
				clz = "void";
				break;
			case 'B':
				clz = "byte";
				break;
			case 'C':
				clz = "char";
				break;
			case 'D':
				clz = "double";
				break;
			case 'F':
				clz = "float";
				break;
			case 'I':
				clz = "int";
				break;
			case 'J':
				clz = "long";
				break;
			case 'S':
				clz = "short";
				break;
			case 'Z':
				clz = "boolean";
				break;
			case 'L':
				clz = Servlet1.asRef(type.substring(dim+1,type.indexOf(';')));
				break;
			default:
				clz = "??"+t;
				break;
		}

		if(dim==0)
			return clz;
		else {
			StringBuffer result = new StringBuffer(clz);
			for(int i=0;i<dim;i++)
				result.append("[]");

			return result.toString();
		}
	}

	private static int getTypeLength(String type) {
		int count=0;
		while(type.charAt(count)=='[')
			count++;
		if(type.charAt(count)!='L')
			return ++count;
		else
			return type.indexOf(';')+1;
	}

	public static class Reference implements Comparable,Serializable {
		public ClassInfo2 sourceClass;
		public Method sourceMethod;
		public Field sourceField;

		public ClassInfo2 targetDestinationClass;
		public ClassInfo2 actualDestinationClass;
		public Method destinationMethod;
		public Field destinationField;

		public boolean equals(Object other) {
			if(other==null)
				return false;
			else
				return hashCode()==other.hashCode();

/*			if(!(other instanceof Reference)) {
				return false;
			}

			Reference two = (Reference)other;

			if(sourceClass!=two.sourceClass) {
				return false;
			}

			if(sourceMethod!=two.sourceMethod) {
				return false;
			}

			if(sourceField!=two.sourceField) {
				return false;
			}

			if(targetDestinationClass!=two.targetDestinationClass) {
				return false;
			}

			if(actualDestinationClass!=two.actualDestinationClass) {
				return false;
			}

			if(destinationMethod!=two.destinationMethod) {
				return false;
			}

			if(destinationField!=two.destinationField) {
				return false;
			}

			return true;*/
		}

		public int compareTo(Object obj) {
			if(obj instanceof UnresolvedReference)
				return -1;

			if(!(obj instanceof Reference))
				throw new RuntimeException("invalid comparison, not a Reference");

			Reference other = (Reference)obj;

			int result = compareDestination(other);
			if(result==0)
				return compareSource(other);
			else
				return result;
		}

		protected int compareSource(Reference other) {
			int result;
			if(sourceClass!=null && other.sourceClass!=null) {
				result = sourceClass.thisClass.compareTo(other.sourceClass.thisClass);
				if(result!=0)
					return result;
			}

			if(sourceMethod!=null && other.sourceMethod!=null) {
				result = sourceMethod.name.compareTo(other.sourceMethod.name);
				if(result!=0)
					return result;
			}

			if(sourceField!=null && other.sourceField!=null) {
				result = sourceField.name.compareTo(other.sourceField.name);
				if(result!=0)
					return result;
			}

			return 0;
		}

		protected int compareDestination(Reference other) {
			int result;
			if(actualDestinationClass!=null && other.actualDestinationClass!=null) {
				result = actualDestinationClass.thisClass.compareTo(other.actualDestinationClass.thisClass);
				if(result!=0)
					return result;
			}

			if(targetDestinationClass!=null && other.targetDestinationClass!=null) {
				result = targetDestinationClass.thisClass.compareTo(other.targetDestinationClass.thisClass);
				if(result!=0)
					return result;
			}

			if(destinationMethod!=null && other.destinationMethod!=null) {
				result = destinationMethod.name.compareTo(other.destinationMethod.name);
				if(result!=0)
					return result;
			}

			if(destinationField!=null && other.destinationField!=null) {
				result = destinationField.name.compareTo(other.destinationField.name);
				if(result!=0)
					return result;
			}

			return 0;
		}

		public int hashCode() {
			int code=0;
			if(sourceClass!=null)
				code^=sourceClass.hashCode();
			if(sourceMethod!=null)
				code^=sourceMethod.hashCode();
			if(sourceField!=null)
				code^=sourceField.hashCode();
			if(targetDestinationClass!=null)
				code^=targetDestinationClass.hashCode();
			if(actualDestinationClass!=null)
				code^=actualDestinationClass.hashCode();
			if(destinationMethod!=null)
				code^=destinationMethod.hashCode();
			if(destinationField!=null)
				code^=destinationField.hashCode();

			return code;
		}

		public String toString() {
			StringBuffer buffer = new StringBuffer("src class: ");
			buffer.append(displayObject(sourceClass));
			buffer.append("\nsrc method: ");
			buffer.append(displayObject(sourceMethod));
			buffer.append("\nsrc field: ");
			buffer.append(displayObject(sourceField));
			buffer.append("\ntarget class: ");
			buffer.append(displayObject(targetDestinationClass));
			buffer.append("\nactual class: ");
			buffer.append(displayObject(actualDestinationClass));
			buffer.append("\ndestination method: ");
			buffer.append(displayObject(destinationMethod));
			buffer.append("\ndestination field: ");
			buffer.append(displayObject(destinationField));
			buffer.append("\n\n");

			return buffer.toString();
		}

		private String displayObject(ClassInfo2 obj) {
			if(obj==null)
				return "null";
			else
				return obj.thisClass;
		}

		private String displayObject(Field obj) {
			if(obj==null)
				return "null";
			else
				return obj.name;
		}

		private String displayObject(Method obj) {
			if(obj==null)
				return "null";
			else
				return obj.name;
		}
	}

	public static class UnresolvedReference implements Comparable,Serializable {
		public ClassInfo2 sourceClass;
		public Method sourceMethod;
		public Field sourceField;

		public String destinationClassName;
		public String destinationMethodName;
		public String destinationFieldName;
		public String destinationDescriptor;

		public int compareTo(Object obj) {
			if(obj instanceof Reference)
				return 1;

			if(!(obj instanceof UnresolvedReference))
				throw new RuntimeException("invalid comparison, not a Reference");

			UnresolvedReference other = (UnresolvedReference)obj;

			int result;
			if(destinationClassName!=null && other.destinationClassName!=null) {
				result = destinationClassName.compareTo(other.destinationClassName);
				if(result!=0)
					return result;
			}

			if(destinationMethodName!=null && other.destinationMethodName!=null) {
				result = destinationMethodName.compareTo(other.destinationMethodName);
				if(result!=0)
					return result;
			}

			if(destinationFieldName!=null && other.destinationFieldName!=null) {
				result = destinationFieldName.compareTo(other.destinationFieldName);
				if(result!=0)
					return result;
			}

			if(destinationDescriptor!=null && other.destinationDescriptor!=null) {
				result = destinationDescriptor.compareTo(other.destinationDescriptor);
				if(result!=0)
					return result;
			}

			return 0;
		}
	}

	public static class InverseReference extends Reference {
		public InverseReference(Reference orig) {
			sourceClass = orig.sourceClass;
			sourceMethod = orig.sourceMethod;
			sourceField = orig.sourceField;
			targetDestinationClass = orig.targetDestinationClass;
			actualDestinationClass = orig.actualDestinationClass;
			destinationMethod = orig.destinationMethod;
			destinationField = orig.destinationField;
		}

		public int compareTo(Object obj) {
			if(obj instanceof UnresolvedReference)
				return -1;

			if(!(obj instanceof Reference))
				throw new RuntimeException("invalid comparison, not a Reference");

			Reference other = (Reference)obj;

			int result = compareSource(other);
			if(result==0)
				return compareDestination(other);
			else
				return result;
		}
	}

	public static class ClassLists implements Serializable {
		public List createdBy = new UniqueList();
		public List usedBy = new UniqueList();
		public List superClassMethodCalls = new UniqueList();
		public List superClassFieldUsage = new UniqueList();
		public List subClasses = new UniqueList();
		public List implementedBy = new UniqueList();

		public void sort() {
			Collections.sort(createdBy);
			Collections.sort(usedBy);
			Collections.sort(superClassMethodCalls);
			Collections.sort(superClassFieldUsage);
			Collections.sort(subClasses);
			Collections.sort(implementedBy);
		}
	}

	public static class MethodLists implements Serializable {
		public List methodsCalled = new UniqueList();
		public List fieldsUsed = new UniqueList();
		public List classesCreated = new UniqueList();
		public List classesUsed = new UniqueList();
		public List calledBy = new UniqueList();

		public void sort() {
			Collections.sort(methodsCalled);
			Collections.sort(fieldsUsed);
			Collections.sort(classesCreated);
			Collections.sort(classesUsed);
			Collections.sort(calledBy);
		}
	}

	public static class UniqueList extends ArrayList implements Serializable {
		public boolean add(Object obj) {
			if(contains(obj))
				return false;
			else
				return super.add(obj);
		}
	}
}
