package intro.info.bytecode;

public abstract class Descriptor {
	public static String getMethodReturnType(String signature) {
		return decodeType(extractType(signature,1));
	}

	public static String getMethodParameters(String signature) {
		int pointer = signature.indexOf(')')+1;

		if(signature.charAt(pointer)=='V')
			return "";

		StringBuffer results = new StringBuffer();
		String encoded;
		while(pointer<signature.length()) {
			encoded = extractType(signature,pointer);
		    results.append(decodeType(encoded));
			pointer+=encoded.length();
			if(pointer<signature.length())
				results.append(", ");
		}

		return results.toString();
	}

	public static String getMethodSignature(String name, String signature, String accessFlags, String className) {
		boolean constructor = name.equals("<init>");
		if(constructor)
			name = className;

		StringBuffer results = new StringBuffer();

		results.append(accessFlags.toString());
		results.append(' ');

		if(!constructor) {
			results.append(getMethodReturnType(signature));
			results.append(' ');
		}

		results.append(name);
		results.append("(");
		results.append(getMethodParameters(signature));
		results.append(");");

		return results.toString();
	}

	public static String getFieldSignature(String name, String signature, String accessFlags) {
		StringBuffer results = new StringBuffer();

		results.append(accessFlags.toString());
		results.append(' ');
		results.append(decodeType(signature));
		results.append(' ');
		results.append(name);
		results.append(';');

		return results.toString();
	}

	private static String extractType(String signature, int startPos) {
//		System.out.println("extracting type from "+signature+" start pos "+startPos);

		if(signature.charAt(startPos)==')')
			return "V";

		int pointer = startPos;

		char c;
		boolean flag = true;
		while(flag) {
			c = signature.charAt(pointer++);
			if(isBaseType(c))
				flag = false;

			if(c == 'L') {
				pointer = signature.indexOf(';',startPos)+1;
				flag = false;
			}
		}

		return signature.substring(startPos,pointer);
	}

	private static boolean isBaseType(char c) {
		switch(c) {
			case 'B' :
			case 'C' :
			case 'D' :
			case 'F' :
			case 'I' :
			case 'J' :
			case 'S' :
			case 'Z' :
			case 'V' :
				return true;
			default:
				return false;
		}
	}

	private static String decodeType(String signature) {
//		System.out.println("extracting type from "+signature);

		if(signature.length()==0)
			return "";

		int arrayCount = 0;
		int pointer = 0;

		while(signature.charAt(pointer) == '[') {
		    pointer++;
			arrayCount++;
		}

		String clz = null;
		switch(signature.charAt(pointer)) {
			case 'B' :
				clz = "byte";
				break;
			case 'C' :
				clz = "char";
				break;
			case 'D' :
				clz = "double";
				break;
			case 'F' :
				clz = "float";
				break;
			case 'I' :
				clz = "int";
				break;
			case 'J' :
				clz = "long";
				break;
			case 'S' :
				clz = "short";
				break;
			case 'Z' :
				clz = "boolean";
				break;
			case 'V' :
				clz = "void";
				break;
			case 'L' :
				clz = signature.substring(pointer+1,signature.indexOf(';'));
				break;
		}

		StringBuffer results = new StringBuffer(clz);
		for(int i=0;i<arrayCount;i++)
			results.append("[]");

		return results.toString();
	}
}