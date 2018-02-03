package intro.analysis;

import intro.info.ClassInfo;
import intro.info.attribute.AbstractAttribute;
import intro.info.attribute.CodeAttribute;
import intro.info.pool.CPInfo;
import intro.info.pool.ClassCPI;
import intro.info.pool.RefCPI;
import intro.info2.ClassInfo2;
import intro.io.TrackableDataInputStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;

public class Database implements Serializable {
	static final long serialVersionUID = 329014559070632592L;

	private transient HashMap map1;
	public HashMap map2;
	private HashMap xref;
	public List packages;
	public HashMap packagesToClasses;

	public Database() {
		map2 = new HashMap();
		xref = new HashMap();
		packages = new ArrayList();
		packagesToClasses = new HashMap();
	}

	private void restore() {
		System.out.println("restoring cross references");
		Iterator it = map2.values().iterator();

		ClassInfo2 i2;
		while(it.hasNext()) {
			i2 = (ClassInfo2)it.next();
			i2.restore(xref);
		}
		System.out.println("finished restoring cross references");
	}

	public void readClass(TrackableDataInputStream tis) throws IOException {
		if(map1==null)
			map1 = new HashMap();

		ClassInfo ci = new ClassInfo(tis);

		Object result = map1.put(ci.thisClass, ci);
		if(result!=null)
			throw new RuntimeException("duplicate v1");

		String pkg = ci.thisClass.substring(0,ci.thisClass.lastIndexOf('/'));
		if(!packages.contains(pkg))
			packages.add(pkg);

		List list = (List)packagesToClasses.get(pkg);
		if(list==null) {
			list = new ArrayList();
			packagesToClasses.put(pkg,list);
		}

		ClassInfo2 ci2 = new ClassInfo2(ci,xref);
		list.add(ci2);

		result = map2.put(ci.thisClass, ci2);
		if(result!=null)
			throw new RuntimeException("duplicate v2");
	}

	public void postProcess() {
		findSubclassing();

		populateCrossReferences();

		sortReferences();

		map1 = null;

		Collections.sort(packages);

		List classList;
		for(int i=0;i<packages.size();i++) {
			classList = (List)packagesToClasses.get(packages.get(i));
			Collections.sort(classList);
		}

		System.out.println("Got final data structure");
	}

	private void sortReferences() {
		Iterator it = map2.values().iterator();

		ClassInfo2 i2;
		while(it.hasNext()) {
			i2 = (ClassInfo2)it.next();
			i2.sortReferences();
		}
	}

	// Here we look up super classes of each class, adding to it's
	// sub-classed Set
	private void findSubclassing() {
		Iterator it = map1.values().iterator();

		ClassInfo one;
		ClassInfo2 two,intf;
		int i;

		while( it.hasNext() ) {
			one = (ClassInfo)it.next();

			two = (ClassInfo2)map2.get(one.superClass);
			if(two!=null) {
//				System.out.println(one.thisClass+" is a subclass of "+two.thisClass);
				two.lists.subClasses.add(one.thisClass);

				for(i=0;i<one.interfaces.length;i++) {
					intf = (ClassInfo2)map2.get(one.interfaces[i]);
					if(intf!=null)
						intf.lists.implementedBy.add(one.thisClass);
					else
						System.out.println("interface not found: "+one.interfaces[i]);
				}
			}// else
//				if(one.superClass!=null)
//					if(!one.superClass.equals("java/lang/Object"))
//						System.out.println("WARNING, can't find v2 for "+one.superClass);
		}
	}

	// Here we look through each class (old version) at all the
	// references, and add Strings to the reference
	// Sets in other classes (new version).
	private void populateCrossReferences() {
		ClassInfo source;
		ClassInfo2 source2;
		ClassInfo2.Method method;
		AbstractAttribute attr;
		CodeAttribute code;
		Iterator references;

		Iterator classes = map1.values().iterator();

		while( classes.hasNext() ) {
			source = (ClassInfo)classes.next();

			for(int i=0;i<source.methods.length;i++) {
				for(int j=0;j<source.methods[i].attributes.length;j++) {

					attr = source.methods[i].attributes[j];

					if(attr instanceof CodeAttribute) {

						code = (CodeAttribute)attr;

						source2 = (ClassInfo2)map2.get(source.thisClass);

						method = source2.getMethod(source.methods[i].name,source.methods[i].descriptor);

						references = code.refs[0].iterator(); // method
						findMethodReferences(source, source2, method, references);

						references = code.refs[1].iterator(); // use
						findUsageReferences(source, source2, method, references);

						references = code.refs[2].iterator(); // create
						findCreationReferences(source, source2, method, references);

						references = code.refs[3].iterator(); // field
						findFieldReferences(source, source2, method, references);
					}
				}
			}
		}
	}

	private void findMethodReferences(ClassInfo source, ClassInfo2 source2, ClassInfo2.Method method, Iterator references) {
		ClassInfo2 target;
		CPInfo cpi;
		RefCPI rcpi;
		Object ref;
		ClassInfo2.UnresolvedReference uref;

		while(references.hasNext()) {
			cpi = source.getCPI((Integer)references.next());
			if(cpi instanceof RefCPI) {
				rcpi = (RefCPI)cpi;

				target = (ClassInfo2)map2.get(rcpi.classCPI.name);

				if(target!=null)
					ref = target.addMethodReference(target, source2, method, rcpi.natCPI.name, rcpi.natCPI.descriptor, map2);
				else
					ref = null;

				if(ref==null) {
					uref = new ClassInfo2.UnresolvedReference();

					uref.sourceClass = source2;
					uref.sourceMethod = method;
					uref.destinationClassName = rcpi.classCPI.name;
					uref.destinationMethodName = rcpi.natCPI.name;
					uref.destinationDescriptor = rcpi.natCPI.descriptor;

					method.lists.methodsCalled.add(uref);

//					System.out.println("class "+source.thisClass+" method: "+method.name+" "+method.descriptor);
//					System.out.println("**invalid method reference");
				}
			} else {
				System.out.println("class "+source.thisClass+" method: "+method.name+" "+method.descriptor);
				System.out.println("(call)Wrong CPI "+cpi.getClass().getName());
			}
		}
	}

	private void findUsageReferences(ClassInfo source, ClassInfo2 source2, ClassInfo2.Method method, Iterator references) {
		ClassInfo2 target;
		CPInfo cpi;
		RefCPI rcpi;
		ClassInfo2.Reference ref;
		ClassInfo2.UnresolvedReference uref;
		ClassCPI ccpi;
		String name;
		int lastBracket;
		int type;

		while(references.hasNext()) {
			cpi = source.getCPI((Integer)references.next());
			if(cpi instanceof ClassCPI) {
				ccpi = (ClassCPI)cpi;

				lastBracket = ccpi.name.lastIndexOf('[');

				if(lastBracket!=-1)
					type = lastBracket+1;
				else
					type = 0;

				if(ccpi.name.charAt(type)=='L') {
					name = ccpi.name.substring(type+1,ccpi.name.indexOf(';'));
//					System.out.println("  converted "+ccpi.name+" to "+name);

					target = (ClassInfo2)map2.get(name);

					if(target!=null) {
						ref = new ClassInfo2.Reference();

						ref.sourceClass = source2;
						ref.sourceMethod = method;
						ref.actualDestinationClass = target;

						method.lists.classesUsed.add(ref);
						target.lists.usedBy.add(new ClassInfo2.InverseReference(ref));

//						System.out.println("  --> "+source2.thisClass+" uses "+target.thisClass);
					} else {
						uref = new ClassInfo2.UnresolvedReference();

						uref.sourceClass = source2;
						uref.sourceMethod = method;
						uref.destinationClassName = name;

						method.lists.classesUsed.add(uref);

//										if(name.indexOf("java")!=0) {
//						System.out.println("class "+source.thisClass+" method: "+method.name+" "+method.descriptor);
//						System.out.println("(use)Can't find "+ccpi.name);
					}
				}
			} else {
				System.out.println("class "+source.thisClass+" method: "+method.name+" "+method.descriptor);
				System.out.println("(use)Wrong CPI "+cpi.getClass().getName());
			}
		}
	}

	private void findCreationReferences(ClassInfo source, ClassInfo2 source2, ClassInfo2.Method method, Iterator references) {
		ClassInfo2 target;
		CPInfo cpi;
		RefCPI rcpi;
		ClassInfo2.Reference ref;
		ClassInfo2.UnresolvedReference uref;
		ClassCPI ccpi;

		while(references.hasNext()) {
			cpi = source.getCPI((Integer)references.next());
			if(cpi instanceof ClassCPI) {
				ccpi = (ClassCPI)cpi;

				target = (ClassInfo2)map2.get(ccpi.name);

				if(target!=null) {
					ref = new ClassInfo2.Reference();

					ref.sourceClass = source2;
					ref.sourceMethod = method;
					ref.actualDestinationClass = target;

					method.lists.classesCreated.add(ref);
					target.lists.createdBy.add(new ClassInfo2.InverseReference(ref));

//						System.out.println("  --> uses "+two.thisClass);
				} else {
					uref = new ClassInfo2.UnresolvedReference();

					uref.sourceClass = source2;
					uref.sourceMethod = method;
					uref.destinationClassName = ccpi.name;

					method.lists.classesCreated.add(uref);

//										if(name.indexOf("java")!=0) {
//					System.out.println("class "+source.thisClass+" method: "+method.name+" "+method.descriptor);
//					System.out.println("(create)Can't find "+ccpi.name);
				}
			} else {
				System.out.println("class "+source.thisClass+" method: "+method.name+" "+method.descriptor);
				System.out.println("(create)Wrong CPI "+cpi.getClass().getName());
			}
		}
	}

	private void findFieldReferences(ClassInfo source, ClassInfo2 source2, ClassInfo2.Method method, Iterator references) {
		ClassInfo2 target;
		CPInfo cpi;
		RefCPI rcpi;
		ClassInfo2.Reference ref;
		ClassInfo2.UnresolvedReference uref;
		ClassInfo2.Field field;

		while(references.hasNext()) {
			cpi = source.getCPI((Integer)references.next());
			if(cpi instanceof RefCPI) {
				rcpi = (RefCPI)cpi;

				target = (ClassInfo2)map2.get(rcpi.classCPI.name);

				if(target!=null)
					ref = target.addFieldReference(target, source2, method, rcpi.natCPI.name, rcpi.natCPI.descriptor, map2);
				else
					ref = null;

				if(ref==null) {
					uref = new ClassInfo2.UnresolvedReference();

					uref.sourceClass = source2;
					uref.sourceMethod = method;
					uref.destinationClassName = rcpi.classCPI.name;
					uref.destinationFieldName = rcpi.natCPI.name;
					uref.destinationDescriptor = rcpi.natCPI.descriptor;

					method.lists.fieldsUsed.add(uref);

//					System.out.println("class "+source.thisClass+" method: "+method.name+" "+method.descriptor);
//					System.out.println("**invalid field reference");
				}
			} else {
				System.out.println("class "+source.thisClass+" method: "+method.name+" "+method.descriptor);
				System.out.println("(call)Wrong CPI "+cpi.getClass().getName());
			}
		}
	}

	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();

		restore();
	}
}