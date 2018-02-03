package intro2.database.member;

import intro2.database.ByteArrayAccessor;
import intro2.database.ClassInfo;
import intro2.database.Database.DataProcessingException;
import intro2.database.attribute.CodeAttribute;
import intro2.database.pool.ClassCPI;
import intro2.database.pool.UTF8CPI;
import intro2.database.reference.ReferenceFactory;
import intro2.output.Type;

import java.util.ArrayList;

public final class Method extends Member {
	private static final String CLINIT = "[class initializer &lt;clinit&gt;]";

	private ClassInfo ci;
	private int codeAttributeOffset;
	private CodeAttribute code;
	private String[] exceptions;

	private boolean isConstructor;
	private String constructorName;

	private Type returnType;
	private Type[] args;
	private boolean gotArgs = false;

	public Method(int classID, String className, int offset, ByteArrayAccessor acc, ClassInfo ci) throws DataProcessingException {
		super(classID, offset, acc, ci);
		this.ci = ci;

		if(name.equals("<init>")) {
			constructorName = className;
			isConstructor = true;
		}

		if(name.equals("<clinit>")) {
			constructorName = CLINIT;
			isConstructor = true;
		}

		int attrCount = acc.getUnsignedShort(offset+6);
		offset+=8;

		codeAttributeOffset = -1;
		String attrName;
		UTF8CPI u;

		for(int n=0;n<attrCount;n++) {
			u = (UTF8CPI)ci.getCPI(acc.getUnsignedShort(offset));
			attrName = u.getString();

			if(attrName.equals("Code"))
				codeAttributeOffset = offset;

			if(attrName.equals("Exceptions")) { // thrown Exceptions
				int j = acc.getUnsignedShort(offset+6);
				exceptions = new String[j];
				ClassCPI c;
				for(int i=0;i<j;i++) {
					c = (ClassCPI)ci.getCPI(acc.getUnsignedShort(offset+8+(2*i)));
					exceptions[i] = c.getName();
				}
			}

			checkSyntheticDeprecated(attrName);

			offset += acc.getUnsignedInt(offset+2) + 6;
		}
	}

	public CodeAttribute getCode() throws DataProcessingException {
		if((code == null) && (codeAttributeOffset != -1)) {
			code = new CodeAttribute(codeAttributeOffset, ci);
		}
		return code;
	}

	public void addReferences() throws DataProcessingException {
		if(exceptions != null)
			ReferenceFactory.addThrownExceptionReferences(ci.getID(), this, exceptions);

		CodeAttribute c = getCode();
		if(c != null)
			c.addReferences(memberOffset);
	}

	public String[] getExceptions() {
		return exceptions;
	}

	public boolean throwsExceptions() {
		return exceptions != null;
	}

	public boolean isPublic() {
		return (accessFlags & 0x01) != 0;
	}

	public boolean isPrivate() {
		return (accessFlags & 0x02) != 0;
	}

	public boolean isProtected() {
		return (accessFlags & 0x04) != 0;
	}

	public boolean isStatic() {
		return (accessFlags & 0x08) != 0;
	}

	public boolean isFinal() {
		return (accessFlags & 0x010) != 0;
	}

	public boolean isSynchronized() {
		return (accessFlags & 0x020) != 0;
	}

	public boolean isNative() {
		return (accessFlags & 0x0100) != 0;
	}

	public boolean isAbstract() {
		return (accessFlags & 0x0400) != 0;
	}

	public boolean isStrict() {
		return (accessFlags & 0x0800) != 0;
	}

	public boolean isConstructor() {
		return isConstructor;
	}

	public Type getReturnType() {
		if(returnType == null)
			returnType = new Type(descriptor, descriptor.indexOf(')') + 1);

		return returnType;
	}

	public Type[] getArgs() {
		if(!gotArgs) {
			gotArgs = true;

			int last = descriptor.indexOf(')');
			if(last == 1)
				return null;

			int pos = 1;
			Type type;
			ArrayList list = new ArrayList();

			while(pos < last) {
				type = new Type(descriptor, pos);
				list.add(type);
				pos += type.getLengthInDescriptor();
			}

			args = new Type[list.size()];
			list.toArray(args);
		}

		return args;
	}

	public int compareTo(Object other) {
		Method mOther = (Method)other;

		if(isConstructor() && !mOther.isConstructor())
			return -1;

		if(!isConstructor() && mOther.isConstructor())
			return 1;

		return super.compareTo(other);
	}

	public String getDisplayName() {
		if(constructorName != null)
			return constructorName;
		else
			return name;
	}
}