package intro2.database.reference;

import intro2.database.*;
import intro2.database.Database.DataProcessingException;
import intro2.database.Package;
import intro2.database.attribute.CodeAttribute;
import intro2.database.attribute.InnerClassAttribute;
import intro2.database.member.Field;
import intro2.database.member.Member;
import intro2.database.member.Method;
import intro2.database.pool.*;
import intro2.util.I2OMap;
import intro2.util.IList;
import intro2.util.ISet;

import java.util.*;

public final class ReferenceFactory {
	private static final int CREATE = 0;
	private static final int USE = 1;
	private static final int USE_FIELD = 2;
	private static final int USE_METHOD = 3;
	private static final int USE_INTERFACE_METHOD = 4;

	private ReferenceFactory() {}

	public static void processClassRelationships(int[] classIDs) throws DataProcessingException {
		int superClassID;
		int[] interfaceIDs;

		ClassInfo ci, ci2;

		for(int i=0;i<classIDs.length;i++) {
			ci = Database.getClassInfo(classIDs[i]);

			superClassID = ci.getSuperClassID();
			if(superClassID != Integer.MIN_VALUE) {
				ci2 = Database.getClassInfo(superClassID);
				if(ci2 != null)
					setIsSubclassedBy(ci2, superClassID);
			}

			interfaceIDs = ci.getInterfaceIDs();
			if(interfaceIDs != null)
				for(int n=0;n<interfaceIDs.length;n++) {
					ci2 = Database.getClassInfo(interfaceIDs[n]);
					if(ci2 != null)
						setIsImplementedBy(ci2, classIDs[i]);
				}
		}
	}

	private static void setIsImplementedBy(ClassInfo ci, int classID) {
		IList list = ci.getImplementedBy();

		if(list == null) {
			list = new IList();
			ci.setImplementedBy(list);
		}

		list.add(classID);
	}

	private static void setIsSubclassedBy(ClassInfo ci, int subClassID) {
		IList list = ci.getSubclassedBy();

		if(list == null) {
			list = new IList();
			ci.setSubclassedBy(list);
		}

		list.add(subClassID);
	}

	public static void processMemberRelationships(int[] classIDs) throws DataProcessingException {
		ClassInfo ci;
		for(int i=0;i<classIDs.length;i++) {
			ci = Database.getClassInfo(classIDs[i]);

			if(ci != null)
				if(!ci.isInterface())
					processMemberRelationships(ci);
		}
	}

	public static void processMemberRelationships(ClassInfo ci) throws DataProcessingException {
		List members = new ArrayList();
		ISet interfacesCovered = new ISet();

		addInterfaceMembers(ci, members, interfacesCovered);
		addSuperClassMembers(ci, members, interfacesCovered);

		interfacesCovered = null;

		addFieldHidingReferences(ci, members);
		addMethodOverridingImplementingReferences(ci, members);
	}

	private static void addFieldHidingReferences(ClassInfo ci, List members) throws DataProcessingException {
		int[] fieldOffsets = ci.getFieldOffsets();

		if(fieldOffsets != null) {
			for(int n=0;n<fieldOffsets.length;n++)
				addFieldHidingReferences(ci, ci.getField(fieldOffsets[n]), members);
		}
	}

	private static void addFieldHidingReferences(ClassInfo ci, Field f1, List members) throws DataProcessingException {
		if(f1.isPrivate())
			return;

		int id;
		Integer iid;
		Reference ref;
		Member member;
		Field f2;
		IList refIDs = ci.getRefIDs();
		ClassInfo ci2;

		for(int n=0;n<members.size();n++) {
			member = (Member)members.get(n); // none are private

			if(member instanceof Field) {
				f2 = (Field)member;

				if(f1.getName().equals(f2.getName())) {
					id = Cache.getNewReferenceID();

					ref = new HidesFieldReference(id, f1, f2);

					refIDs.add(id);
					Cache.addReference(ref);

					ci2 = Database.getClassInfo(f2.getClassID()); // must exist
					ci2.getReverseRefIDs().add(id);
				}
			}
		}
	}

	private static void addMethodOverridingImplementingReferences(ClassInfo ci, List members) throws DataProcessingException {
		int[] methodOffsets = ci.getMethodOffsets();

		if(methodOffsets != null) {
			for(int n=0;n<methodOffsets.length;n++)
				addMethodOverridingImplementingReferences(ci, ci.getMethod(methodOffsets[n]), members);
		}
	}

	private static void addMethodOverridingImplementingReferences(ClassInfo ci, Method m1, List members) throws DataProcessingException {
		if(m1.isPrivate())
			return;

		int id;
		Integer iid;
		Reference ref;
		Member member;
		Method m2;
		IList refIDs = ci.getRefIDs();
		ClassInfo ci2;

		boolean foundOverride = false;

		for(int n=0;n<members.size();n++) {
			member = (Member)members.get(n); // none are private

			if(member instanceof Method) {
				m2 = (Method)member;

				if((!foundOverride) || m2.isAbstract() || (m1.isStatic() && m2.isStatic()))
					if(nameAndDescriptorMatch(m1, m2)) {
						id = Cache.getNewReferenceID();

						if(m2.isAbstract())
							ref = new ImplementsMethodReference(id, m1, m2);
						else {
							if(m1.isStatic() && m2.isStatic())
								ref = new HidesMethodReference(id, m1, m2);
							else {
								ref = new OverridesMethodReference(id, m1, m2);
								foundOverride = true;
							}
						}

						refIDs.add(id);
						Cache.addReference(ref);

						ci2 = Database.getClassInfo(m2.getClassID()); // must exist
						ci2.getReverseRefIDs().add(id);
					}
			}
		}
	}

	private static boolean nameAndDescriptorMatch(Member m1, Member m2) {
		if(!m1.getName().equals(m2.getName()))
			return false;

		return m1.getDescriptor().equals(m2.getDescriptor());
	}

	private static void addSuperClassMembers(ClassInfo ci, List members, ISet interfacesCovered) throws DataProcessingException {
		int sid = ci.getSuperClassID();
		if(sid < 0)
			return; // no super class i.e. java.lang.Object, or not found

		ci = Database.getClassInfo(sid);
		if(ci == null)
			return;

		addMembersToList(ci, members);

		addInterfaceMembers(ci, members, interfacesCovered);
		addSuperClassMembers(ci, members, interfacesCovered);
	}

	private static void addInterfaceMembers(ClassInfo ci, List interfaceMembers, ISet interfacesCovered) throws DataProcessingException {
		int[] ids = ci.getInterfaceIDs();

		if(ids == null)
			return;

		int[] offsets;

		for(int n=0;n<ids.length;n++) {
			if(!interfacesCovered.contains(ids[n])) {
				interfacesCovered.add(ids[n]);

				ci = Database.getClassInfo(ids[n]);
				if(ci != null)
					addMembersToList(ci, interfaceMembers);
			}
		}
	}

	private static void addMembersToList(ClassInfo ci, List list) throws DataProcessingException {
		int[] offsets = ci.getFieldOffsets();
		int n;
		Field f;
		Method m;

		if(offsets != null)
			for(n=0;n<offsets.length;n++) {
				f = ci.getField(offsets[n]);
				if(!f.isPrivate())
					list.add(f);
			}

		offsets = ci.getMethodOffsets();

		if(offsets != null)
			for(n=0;n<offsets.length;n++) {
				m = ci.getMethod(offsets[n]);

				if(!m.isPrivate())
					list.add(m);
			}
	}

	public static void addThrownExceptionReferences(int classID, Method method, String[] exceptions) throws DataProcessingException {
		int id;
		int offset = method.getOffset();

		for(int i=0;i<exceptions.length;i++) {
			id = Cache.getNewReferenceID();
			Cache.addReference(createThrowsReference(id, classID, exceptions[i], offset));
		}
	}

	public static void addReferences(int classID, CodeAttribute.ExceptionTableEntry[] et, int methodOffset) throws DataProcessingException {
		String exceptionName;
		int id;
		Reference ref;

		for(int i=0;i<et.length;i++)
			if(!et[i].isFinally()) {
				exceptionName = et[i].getCatchType();
				id = Cache.getNewReferenceID();
				Cache.addReference(new UsesClassReference(id, classID, methodOffset, exceptionName));
			}
	}

	public static void addReferences(ClassInfo ci, int byteCodeOffset, int methodOffset) throws DataProcessingException {
		ByteArrayAccessor acc = ci.getClassBytes();

		int length = (int)acc.getUnsignedInt(byteCodeOffset);
		int offset = byteCodeOffset + 4;
		int start = offset;
		int end = byteCodeOffset + length +4;

		int opCode, opCode2, controlCode, n, index;

		I2OMap map = new I2OMap();

		while(offset < end) {
			opCode = acc.getUnsignedByte(offset++);
			controlCode = ByteCode.refControlCodes[opCode];

			switch(controlCode) {
				case ByteCode.UNUSED:
					throw new DataProcessingException("Encountered unused opcode "+opCode);

				case ByteCode.NOSKIP_1:
					break; // 1 byte, no skip

				case ByteCode.SKIP1_2:
					offset++;
					break; // 2 bytes

				case ByteCode.SKIP2_3:
					offset += 2;
					break; // 3 bytes

				case ByteCode.SKIP4_5:
					offset += 4;
					break; // 5 bytes

				case ByteCode.NEW:
					index = acc.getUnsignedShort(offset);
					addToMap(CREATE, index, ci, map);
					offset += 2;
					break; // 3 bytes

				case ByteCode.USAGE_3:
					index = acc.getUnsignedShort(offset);
					addToMap(USE, index, ci, map);
					offset += 2;
					break; // 3 bytes

				case ByteCode.METHOD_3:
					index = acc.getUnsignedShort(offset);
					addToMap(USE_METHOD, index, ci, map);
					offset += 2;
					break; // 3 bytes

				case ByteCode.FIELD_3:
					index = acc.getUnsignedShort(offset);
					addToMap(USE_FIELD, index, ci, map);
					offset += 2;
					break; // 3 bytes

				case ByteCode.MULTIANEWARRAY:
					index = acc.getUnsignedShort(offset);
					addToMap(USE, index, ci, map);
					offset += 3;
					break; // 4 bytes

				case ByteCode.INVOKEINTERFACE:
					index = acc.getUnsignedShort(offset);
					addToMap(USE_INTERFACE_METHOD, index, ci, map);
					offset += 4;
					break;

				case ByteCode.LOOKUPSWITCH:
					offset += getSpare(start, offset);

					n = acc.getInt(offset + 4);
					offset += 8 + (8*n);
					break;

				case ByteCode.TABLESWITCH:
					offset += getSpare(start, offset);

					int low = acc.getInt(offset + 4);
					int high = acc.getInt(offset + 8);
					offset += 12 + (4*(high-low+1)); // default,low,high
					break;

				case ByteCode.WIDE:
					opCode2 = acc.getUnsignedByte(offset+1);

					if(opCode2 == ByteCode.IINC)
						offset += 5;
					else
						offset += 3;

					break;

				default:
					throw new DataProcessingException("Unknown control code "+controlCode);
			}
		}

		buildReferences(ci, map, methodOffset);
	}

	private static int getSpare(int start, int now) {
		return ((now-start-1) & 0x03) ^ 0x03;
	}

	private static void addToMap(int type, int num, ClassInfo ci, I2OMap map) throws DataProcessingException {
		Set set = (Set)map.get(type);
		if(set == null) {
			set = new HashSet();
			map.put(type, set);
		}

		set.add(ci.getCPI(num));
	}

	private static void buildReferences(ClassInfo ci, I2OMap map, int methodOffset) throws DataProcessingException {
		if(!map.isEmpty()) {
			List direct = addDirectReferences(ci, map, methodOffset);
			addReverseReferences(direct, methodOffset);
		}
	}

	private static List addDirectReferences(ClassInfo ci, I2OMap map, int methodOffset) throws DataProcessingException {
		int[] keys = map.keys();

		int key;
		Iterator it2;
		ConstantPoolInfo cpi;
		Reference ref;
		int id = 0;

		IList refIDList = ci.getRefIDs();
		List refs = new ArrayList();

		for(int i=0;i<keys.length;i++) {
			key = keys[i];

			it2 = ((Set)map.get(key)).iterator();

			while(it2.hasNext()) {
				ref = null;

				cpi = (ConstantPoolInfo)it2.next();

				switch(key) {
					case CREATE:
						if(isReferenceToClass((ClassCPI)cpi)) {
							id = Cache.getNewReferenceID();
							ref = new CreatesClassReference(id, ci.getID(), methodOffset, (ClassCPI)cpi);
						}
						break;
					case USE:
						if(isReferenceToClass((ClassCPI)cpi)) {
							id = Cache.getNewReferenceID();
							ref = new UsesClassReference(id, ci.getID(), methodOffset, (ClassCPI)cpi);
						}
						break;
					case USE_FIELD:
						id = Cache.getNewReferenceID();
						ref = createUsesFieldReference(id, ci.getID(), methodOffset, (FieldRefCPI)cpi);
						break;
					case USE_METHOD:
						id = Cache.getNewReferenceID();
						ref = createUsesMethodReference(id, ci.getID(), methodOffset, (MethodRefCPI)cpi);
						break;
					case USE_INTERFACE_METHOD:
						id = Cache.getNewReferenceID();
						ref = createUsesInterfaceMethodReference(id, ci.getID(), methodOffset, (InterfaceMethodRefCPI)cpi);
						break;
					default:
						throw new RuntimeException("invalid reference type");
				}

				if(ref != null) {
					refIDList.add(id);
					refs.add(ref);
					Cache.addReference(ref);
				}
			}
		}

		return refs;
	}

	private static Reference createThrowsReference(int id, int classID, String exception, int methodOffset) throws DataProcessingException {
		int targetID = Database.getClassID(exception);

		if(targetID != Integer.MIN_VALUE)
			return new ThrowsReference(id, classID, methodOffset, targetID);
		else
			return new UnresolvedThrowsReference(id, classID, methodOffset, exception);
	}

	private static Reference createUsesFieldReference(int id, int classID, int methodOffset, FieldRefCPI cpi) throws DataProcessingException {
		Field f = Resolver.findField(cpi);

		if(f != null)
			return new UsesFieldReference(id, classID, Database.getClassID(cpi.getClassName()), methodOffset, f);
		else
			return new UnresolvedUsesFieldReference(id, classID, methodOffset, cpi);
	}

	private static Reference createUsesMethodReference(int id, int classID, int methodOffset, MethodRefCPI cpi) throws DataProcessingException {
		Method m = Resolver.findMethod(cpi);

		if(m != null)
			return new UsesMethodReference(id, classID, Database.getClassID(cpi.getClassName()), methodOffset, m);
		else
			return new UnresolvedUsesMethodReference(id, classID, methodOffset, cpi);
	}

	private static Reference createUsesInterfaceMethodReference(int id, int classID, int methodOffset, InterfaceMethodRefCPI cpi) throws DataProcessingException {
		Method m = Resolver.findMethod(cpi);

		if(m != null)
			return new UsesInterfaceMethodReference(id, classID, Database.getClassID(cpi.getInterfaceName()), methodOffset, m);
		else
			return new UnresolvedUsesInterfaceMethodReference(id, classID, methodOffset, cpi);
	}

	private static boolean isReferenceToClass(ClassCPI cpi) {
		String c = cpi.getName();
		return (c.indexOf(';')!=-1);
	}

	private static void addReverseReferences(List refs, int methodOffset) throws DataProcessingException {
		int size = refs.size();

		Object obj;
		OutwardReference ref;
		int id;
		int currentTarget, actualTarget;

		for(int n=0;n<size;n++) {
			obj = refs.get(n);

			if(obj instanceof OutwardReference) {
				ref = (OutwardReference)obj;
				id = ref.getID();

				currentTarget = ref.getTargetClassID();
				actualTarget = ref.getActualTargetClassID();

				fillInReverse(id, currentTarget, actualTarget);
//				boolean tmp = fillInReverse(id, currentTarget, actualTarget, da);
//				if(tmp)
//					System.out.println(ref.getClass().toString());
			}
		}
	}

	private static void fillInReverse(int id, int currentTargetClass, int actualTargetClass) throws DataProcessingException {
		ClassInfo ci = Database.getClassInfo(currentTargetClass);
		if(ci != null) {
			ci.getReverseRefIDs().add(id);

			if(currentTargetClass != actualTargetClass) {
				int superClass = ci.getSuperClassID();
				if(superClass != Integer.MIN_VALUE) // i.e. found
					fillInReverse(id, superClass, actualTargetClass);
			}
		} else
			System.out.println("Can't find class "+Database.getClassName(currentTargetClass)+" id "+currentTargetClass);
	}

	public static void processPackageRelationships(List refs, int[] classIDs) {
		int num = refs.size(); // all previous refs are class level

		Reference ref;

		int id;
		String srcPkg, targetPkg, actualPkg;
		Package ps, pt, pa;

		for(int i=0;i<num;i++) {
			ref = (Reference)refs.get(i);

			id = ref.getID();

			srcPkg = NamingUtil.getPackage(Database.getClassName(ref.getSourceClassID()));
			targetPkg = NamingUtil.getPackage(Database.getClassName(ref.getTargetClassID()));
			actualPkg = NamingUtil.getPackage(Database.getClassName(ref.getActualTargetClassID()));

			if(compare(srcPkg, targetPkg) && ((actualPkg == null) || compare(srcPkg, actualPkg)))
				continue; // all same packages, so boring

			ps = Database.getPackage(srcPkg);
			pt = Database.getPackage(targetPkg);

			ps.addOutwardReference(pt, id);
			pt.addInwardReference(ps, id);

			if(actualPkg != null) {
				pa = Database.getPackage(actualPkg);
				ps.addOutwardReference(pa, id);
				pa.addInwardReference(ps, id);
			}
		}
	}

	private static boolean compare(String s1, String s2) {
		if((s1 == null) || (s2 == null))
			return false;

		return s1.equals(s2);
	}

	public static void processInnerClassRelationships(int[] classIDs) throws DataProcessingException {
		ClassInfo ci;
		int innerID, outerID;
		String className;
		InnerClassAttribute.InnerRef[] innerRefs;
		int i,j,id;

		Reference ref;
		IList refIDs;

		boolean member, anonymous, weAreParent, weAreIt, unresolved;

		for(i=0;i<classIDs.length;i++) {
			ci = Database.getClassInfo(classIDs[i]);

			innerRefs = ci.getInnerClassRawData();
			if(innerRefs != null) {
				refIDs = ci.getRefIDs();
				className = ci.getName();

				for(j=0;j<innerRefs.length;j++) {
					member = innerRefs[j].getOuterClass() != null;
					anonymous = innerRefs[j].getName() == null;
					weAreParent = compare(innerRefs[j].getOuterClass(), className);
					weAreIt = compare(innerRefs[j].getInnerClass(), className);

					if(weAreIt || weAreParent || anonymous) {

						if(member)
							outerID = Database.getClassID(innerRefs[j].getOuterClass());
						else {
							if(weAreParent || anonymous)
								outerID = classIDs[i];
							else
								outerID = Database.getClassID(className.substring(0, className.lastIndexOf('$')));
						}

						innerID = Database.getClassID(innerRefs[j].getInnerClass());

						id = Cache.getNewReferenceID();

						ref = new InnerClassReference(id, innerID, outerID,
										innerRefs[j].getName(), innerRefs[j].getAccessFlags(),
										weAreParent, member);

						Cache.addReference(ref);

						if(weAreIt)
							ci.setInnerClassSelfReferenceID(id);

						if(weAreParent || anonymous)
							refIDs.add(id);
					}
				}
			}
		}
	}
}