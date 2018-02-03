package intro2.output;

import intro2.database.ClassInfo;
import intro2.database.Database;

public final class Type {
	private static final String[] arrayBrackets;

	public static final String INT = "int";
	public static final String SHORT = "short";
	public static final String BOOLEAN = "boolean";
	public static final String LONG = "long";
	public static final String FLOAT = "float";
	public static final String DOUBLE = "double";
	public static final String CHAR = "char";
	public static final String BYTE = "byte";
	public static final String VOID = "void";

	static {
		arrayBrackets = new String[256];

		StringBuffer sb = new StringBuffer();
		for(int i=1;i<arrayBrackets.length;i++) {
			sb.append("[]");
			arrayBrackets[i] = sb.toString();
		}
	}

	private String type;
	private String displayType;
	private String arrayInfo;
	private boolean isPrimative = true;
	private int length;

	public Type(String descriptor, int pos) {
		int initialPos = pos;

		int count = 0;
		while(descriptor.charAt(pos) == '[') {
			count++;
			pos++;
		}

		length = pos - initialPos + 1;

		arrayInfo = arrayBrackets[count];

		char c = descriptor.charAt(pos);
		switch(c) {
			case 'B':
				type = BYTE;
				break;
			case 'C':
				type = CHAR;
				break;
			case 'D':
				type = DOUBLE;
				break;
			case 'F':
				type = FLOAT;
				break;
			case 'I':
				type = INT;
				break;
			case 'J':
				type = LONG;
				break;
			case 'S':
				type = SHORT;
				break;
			case 'Z':
				type = BOOLEAN;
				break;
			case 'V':
				type = VOID;
				break;
			case 'L':
				int i = descriptor.indexOf(';', pos);
				type = descriptor.substring(pos+1, i);
				displayType = ClassInfo.getDisplayName(type);
				isPrimative = false;
				length = i - initialPos + 1;
				break;
			default:
				throw new RuntimeException("Invalid descriptor in "+descriptor+" pos "+initialPos);
		}
	}

	public String getType() {
		return type;
	}

	public String getDisplayType() {
		if(displayType != null)
			return displayType;
		else
			return type;
	}

	public String getArrayInfo() {
		return arrayInfo;
	}

	public boolean isPrimative() {
		return isPrimative;
	}

	public String getHref() {
		if(isPrimative)
			return null;

		ClassInfo ci = Database.getClassInfo(type);
		if(ci != null)
			return ci.getOutputFilename();
		else
			return null;
	}

	public int getLengthInDescriptor() {
		return length;
	}
}