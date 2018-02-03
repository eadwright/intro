package intro2.database;

import intro2.database.Database.DataProcessingException;
import intro2.database.attribute.AttributeUtil;
import intro2.database.attribute.InnerClassAttribute;
import intro2.database.member.Field;
import intro2.database.member.MemberUtil;
import intro2.database.member.Method;
import intro2.database.pool.ClassCPI;
import intro2.database.pool.ConstantPoolInfo;
import intro2.database.pool.ConstantPoolSupplier;
import intro2.database.pool.ConstantPoolUtil;
import intro2.database.reference.InnerClassReference;
import intro2.util.I2IMap;
import intro2.util.IList;

public final class ClassInfo implements ConstantPoolSupplier, java.io.Serializable {
	private int accessFlagsOffset;

	private int[] fieldOffsets;
	private int[] methodOffsets;

	private int id;
	private int jarID;
	private int superClassID;
	private boolean deprecated = false;
	private int accessFlags;
	private boolean usesReflection = false;
	private boolean usesNative = false;

	private IList refIDs;
	private IList reverseRefIDs;
	private IList subclassedBy;
	private IList implementedBy;

	private int attributeCountOffset;

	private transient boolean gotInterfaces = false;
	private transient int[] interfaceIDs;

	// All this is inner-class stuff
	private transient boolean gotInnerClassRawData = false;
	private transient InnerClassAttribute.InnerRef[] innerClassRawData;

	private int innerClassSelfReferenceID = -1;

	// old stuff, to-go
/*	private transient boolean processedInnerClasses = false;
	private transient int outerClassID = Integer.MIN_VALUE;
	private transient String outerClassName;
	private transient String declaredName;
	private transient int declaredFlags;*/

//	private transient DatabaseAccessor da;

	private transient String displayName, displayPackage, pkg, shortName;
	private transient boolean filenameValueSet;
	private transient long filenameValue;
	private transient I2IMap keyMap;

	public ClassInfo() {};

	public ClassInfo(int id, int jarID) throws DataProcessingException {
		this.id = id;
		this.jarID = jarID;

		refIDs = new IList();
		reverseRefIDs = new IList();

//		da = new DatabaseAccessor(id, jarID);

		init();
	}

	// called during db creation only
//	public DatabaseAccessor getDatabaseAccessor() {
//		return da;
//	}

	public String getName() {
		return Database.getClassName(id);
	}

	public String getDisplayName() {
		if(displayName == null)
			displayName = getDisplayName(getName());

		return displayName;
	}

	public String getDisplayPackage() {
		if(displayPackage == null) {
			String s = getName();
			int i = s.lastIndexOf('/');

			if(i == -1)
				displayPackage = ".";
			else
				displayPackage = getDisplayName(s.substring(0,i));
		}

		return displayPackage;
	}

	public String getPackage() {
		if(pkg == null) {
			String s = getName();
			int i = s.lastIndexOf('/');

			if(i == -1)
				pkg = ".";
			else
				pkg = s.substring(0,i);
		}

		return pkg;
	}

	public String getShortName() {
		if(shortName == null)
			shortName = getShortName(getName());

		return shortName;
	}

	public static String getDisplayName(String name) {
		return name.replaceAll("[\\/]","."); // needs Java 1.4
//		return name.replaceAll("[\\/\\$]","."); // needs Java 1.4
	}

	public static String getShortName(String name) {
		int i = name.lastIndexOf('/');
		if(i == -1)
			return name;
		else
			return name.substring(i+1);
	}

	public String getOutputFilename() {
		return getOutputFilenamePostfix(null);
	}

	public String getOutputFilenamePostfix(String ending) {
		if(!filenameValueSet) {
			filenameValueSet = true;

			filenameValue = Cache.getUniqueFilenameValue(getName());
		}

		return getOutputFilename(filenameValue, ending);
	}

	public static String getOutputFilename(String name) {
		if(name == null)
			return null;

		long value = Cache.getUniqueFilenameValue(name);

		return getOutputFilename(value, null);
	}

	private static String getOutputFilename(long value, String ending) {
		long part1 = value % 100L;
		long part2 = value / 100L;

		StringBuffer sb = new StringBuffer("class/");
		sb.append(Long.toString(part1, 32));
		sb.append('/');
		sb.append(Long.toString(part2, 32));

		if(ending != null)
			sb.append(ending);

		sb.append(".html");
		return sb.toString();
	}

	private void init() throws DataProcessingException {
		ByteArrayAccessor acc = getClassBytes();

		int count = acc.getUnsignedShort(8); // # constant pool entries
		accessFlagsOffset = ConstantPoolUtil.skipConstantPoolEntries(count, acc);

		count = acc.getUnsignedShort(accessFlagsOffset + 6); // # interfaces
		int fieldCountOffset = accessFlagsOffset + 8 + (2*count);

		fieldOffsets = MemberUtil.getMemberOffsets(fieldCountOffset, acc);

		int methodCountOffset;
		if(fieldOffsets == null)
			methodCountOffset = fieldCountOffset + 2;
		else
			methodCountOffset = MemberUtil.skipLast(fieldOffsets, acc);

		methodOffsets = MemberUtil.getMemberOffsets(methodCountOffset, acc);

		if(methodOffsets == null)
			attributeCountOffset = methodCountOffset + 2;
		else
			attributeCountOffset = MemberUtil.skipLast(methodOffsets, acc);

		superClassID = -1;

		accessFlags = acc.getUnsignedShort(accessFlagsOffset);
		deprecated = AttributeUtil.isDeprecated(attributeCountOffset, this);
	}

//	void post() { // called after loading ClassInfo from disk
//		da = new DatabaseAccessor(id, jarID);
//	}

	public ByteArrayAccessor getClassBytes() throws DataProcessingException {
		DBData data = Database.getDBData();
		String jarName = (String)data.jarIDsToJarNames.get(jarID);
		return Cache.getClassBytes(getName(), jarName);
	}

	public ConstantPoolInfo getCPI(int num) throws DataProcessingException {
		int cacheKey = Integer.MIN_VALUE;
		ConstantPoolInfo cpi = null;

		if(keyMap != null) {
			cacheKey = keyMap.get(-num); // -ve keys for CPI
			if(cacheKey != Integer.MIN_VALUE)
				cpi = (ConstantPoolInfo)Cache.getObject(cacheKey);
		} else
			keyMap = new I2IMap();

		if(cacheKey == Integer.MIN_VALUE) { // i.e. not found
			cacheKey = Cache.getNewKey();
			keyMap.put(-num, cacheKey);
		}

		if(cpi == null) {
			ByteArrayAccessor acc = getClassBytes();
			cpi = ConstantPoolUtil.getConstantPoolInfo(num, acc, this);

			Cache.putObject(cacheKey, cpi);
		}

		return cpi;
	}

	public Field getField(int offset) throws DataProcessingException {
		int cacheKey = Integer.MIN_VALUE;
		Field f = null;

		if(keyMap != null) {
			cacheKey = keyMap.get(offset);
			if(cacheKey != Integer.MIN_VALUE)
				f = (Field)Cache.getObject(cacheKey);
		} else
			keyMap = new I2IMap();

		if(cacheKey == Integer.MIN_VALUE) {
			cacheKey = Cache.getNewKey();
			keyMap.put(offset, cacheKey);
		}

		if(f == null) {
			ByteArrayAccessor acc = getClassBytes();
			f = new Field(id, offset, acc, this);

			Cache.putObject(cacheKey, f);
		}

		return f;
	}

	public Method getMethod(int offset) throws DataProcessingException {
		String className = getShortName();
		int cacheKey = Integer.MIN_VALUE;
		Method m = null;

		if(keyMap != null) {
			cacheKey = keyMap.get(offset);
			if(cacheKey != Integer.MIN_VALUE)
				m = (Method)Cache.getObject(cacheKey);
		} else
			keyMap = new I2IMap();

		if(cacheKey == Integer.MIN_VALUE) {
			cacheKey = Cache.getNewKey();
			keyMap.put(offset, cacheKey);
		}

		if(m == null) {
			ByteArrayAccessor acc = getClassBytes();
			m = new Method(id, className, offset, acc, this);

			Cache.putObject(cacheKey, m);
		}

		return m;
	}

	public int getID() {
		return id;
	}

	void setUsesReflection() {
		usesReflection = true;
	}

	public boolean usesReflection() {
		return usesReflection;
	}

	void setUsesNative() {
		usesNative = true;
	}

	public boolean usesNative() {
		return usesNative;
	}

	public int getAccessFlagsOffset() {
		return accessFlagsOffset;
	}

	public int[] getFieldOffsets() {
		return fieldOffsets;
	}

	public int[] getMethodOffsets() {
		return methodOffsets;
	}

	public IList getRefIDs() {
		return refIDs;
	}

	public IList getReverseRefIDs() {
		return reverseRefIDs;
	}

	public IList getSubclassedBy() {
		return subclassedBy;
	}

	public void setSubclassedBy(IList i) {
		subclassedBy = i;
	}

	public IList getImplementedBy() {
		return implementedBy;
	}

	public void setImplementedBy(IList i) {
		implementedBy = i;
	}

	public int getSuperClassID() throws DataProcessingException {
		if(superClassID == -1) {
			ByteArrayAccessor acc = getClassBytes();

			int sc = acc.getUnsignedShort(accessFlagsOffset+4);
			if(sc != 0)
				superClassID = Database.getClassID(((ClassCPI)getCPI(sc)).getName());
			else
				superClassID = Integer.MIN_VALUE; // none
		}

		return superClassID;
	}

	public boolean isInnerClass() {
		return getName().indexOf('$') != -1;
	}

	public InnerClassAttribute.InnerRef[] getInnerClassRawData() throws DataProcessingException {
		if(!gotInnerClassRawData) {
			innerClassRawData = AttributeUtil.getInnerClassAttributeData(attributeCountOffset, this);
			gotInnerClassRawData = true;
		}

		return innerClassRawData;
	}

	public void setInnerClassSelfReferenceID(int id) {
		innerClassSelfReferenceID = id;
	}

	public InnerClassReference getInnerClassSelfReference() {
		if(innerClassSelfReferenceID != -1)
			return (InnerClassReference)Database.getReference(innerClassSelfReferenceID);
		else
			return null;
	}

	public boolean hasInnerClasses() {
		Object ref;
		InnerClassReference inner;

		for(int i=0;i<refIDs.getSize();i++) {
			ref = Database.getReference(refIDs.get(i));
			if(ref instanceof InnerClassReference) {
				inner = (InnerClassReference)ref;
				if(inner.getOuterClassID() == id)
					return true;
			}
		}

		return false;
	}

	public boolean implementsInterfaces() throws DataProcessingException {
		return getInterfaceIDs() != null;
	}

	public int[] getInterfaceIDs() throws DataProcessingException {
		if(!gotInterfaces)
			findInterfaces();

		return interfaceIDs;
	}

	private void findInterfaces() throws DataProcessingException {
		ByteArrayAccessor acc = getClassBytes();

		gotInterfaces = true;

		int intCount = acc.getUnsignedShort(accessFlagsOffset + 6);

		if(intCount > 0) {
			int offset = accessFlagsOffset + 8;

			interfaceIDs = new int[intCount];

			for(int n=0;n<intCount;n++) {
				interfaceIDs[n] = Database.getClassID(((ClassCPI)getCPI(acc.getUnsignedShort(offset))).getName());
				offset += 2;
			}
		}
	}

	public boolean isPublic() {
		return (accessFlags & 0x01) != 0;
	}

	public boolean isFinal() {
		return (accessFlags & 0x010) != 0;
	}

	public boolean isInterface() {
		return (accessFlags & 0x0200) != 0;
	}

	public boolean isAbstract() {
		return (accessFlags & 0x0400) != 0;
	}

	public boolean isDeprecated() {
		return deprecated;
	}

	public int getAccessFlags() {
		return accessFlags;
	}
}