package intro.info.bytecode;

import intro.io.TrackableDataInputStream;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class ByteCode {
	private static final int AALOAD = 50;
	private static final int ALOAD = 25;
	private static final int ALOAD_0 = 42;
	private static final int ALOAD_1 = 43;
	private static final int ALOAD_2 = 44;
	private static final int ALOAD_3 = 45;
	private static final int ASTORE = 58;
	private static final int ASTORE_0 = 75;
	private static final int ASTORE_1 = 76;
	private static final int ASTORE_2 = 77;
	private static final int ASTORE_3 = 78;
	private static final int AASTORE = 83;
	private static final int ATHROW = 191;
	private static final int ACONST_NULL = 1;
	private static final int ARETURN = 176;
	private static final int ARRAYLENGTH = 190;
	private static final int BALOAD = 51;
	private static final int BASTORE = 84;
	private static final int BIPUSH = 16;
	private static final int CALOAD = 52;
	private static final int CASTORE = 85;
	private static final int D2F = 144;
	private static final int D2I = 142;
	private static final int D2L = 143;
	private static final int DADD = 99;
	private static final int DALOAD = 49;
	private static final int DASTORE = 82;
	private static final int DCMPG = 152;
	private static final int DCMPL = 151;
	private static final int DCONST_0 = 14;
	private static final int DCONST_1 = 15;
	private static final int DDIV = 111;
	private static final int DLOAD = 24;
	private static final int DLOAD_0 = 38;
	private static final int DLOAD_1 = 39;
	private static final int DLOAD_2 = 40;
	private static final int DLOAD_3 = 41;
	private static final int DMUL = 107;
	private static final int DNEG = 119;
	private static final int DREM = 115;
	private static final int DRETURN = 175;
	private static final int DSTORE = 57;
	private static final int DSTORE_0 = 71;
	private static final int DSTORE_1 = 72;
	private static final int DSTORE_2 = 73;
	private static final int DSTORE_3 = 74;
	private static final int DSUB = 103;
	private static final int DUP = 89;
	private static final int DUP_X1 = 90;
	private static final int DUP_X2 = 91;
	private static final int DUP2 = 92;
	private static final int DUP2_X1 = 93;
	private static final int DUP2_X2 = 94;
	private static final int F2D = 141;
	private static final int F2I = 139;
	private static final int F2L = 140;
	private static final int FADD = 98;
	private static final int FALOAD = 48;
	private static final int FASTORE = 81;
	private static final int FCMPG = 150;
	private static final int FCMPL = 149;
	private static final int FCONST_0 = 11;
	private static final int FCONST_1 = 12;
	private static final int FCONST_2 = 13;
	private static final int FDIV = 110;
	private static final int FLOAD = 23;
	private static final int FLOAD_0 = 34;
	private static final int FLOAD_1 = 35;
	private static final int FLOAD_2 = 36;
	private static final int FLOAD_3 = 37;
	private static final int FMUL = 106;
	private static final int FNEG = 118;
	private static final int FREM = 114;
	private static final int FRETURN = 174;
	private static final int FSTORE = 56;
	private static final int FSTORE_0 = 67;
	private static final int FSTORE_1 = 68;
	private static final int FSTORE_2 = 69;
	private static final int FSTORE_3 = 70;
	private static final int FSUB = 102;
	private static final int ILOAD = 26;
	private static final int ILOAD_1 = 27;
	private static final int ILOAD_2 = 28;
	private static final int ILOAD_3 = 29;
	private static final int ISTORE =54 ;
	private static final int LDC = 18;
	private static final int LLOAD = 22;
	private static final int LSTORE = 55;
	private static final int NEWARRAY = 188;
	private static final int RET = 169;
	private static final int ANEWARRAY = 189;
	private static final int CHECKCAST = 192;
	private static final int GETFIELD = 180;
	private static final int GETSTATIC = 178;
	private static final int GOTO = 167;
	private static final int I2B = 145;
	private static final int I2C = 146;
	private static final int I2D = 135;
	private static final int I2F = 134;
	private static final int I2L = 133;
	private static final int I2S = 147;
	private static final int IADD = 96;
	private static final int IALOAD = 46;
	private static final int IAND = 126;
	private static final int IASTORE = 79;
	private static final int ICONST_ML = 2;
	private static final int ICONST_0 = 3;
	private static final int ICONST_1 = 4;
	private static final int ICONST_2 = 5;
	private static final int ICONST_3 = 6;
	private static final int ICONST_4 = 7;
	private static final int ICONST_5 = 8;
	private static final int IDIV = 108;
	private static final int IF_ACMPEQ = 165;
	private static final int IF_ACMPNE = 166;
	private static final int IF_ICMPEQ = 159;
	private static final int IF_ICMPNE = 160;
	private static final int IF_ICMPLT = 161;
	private static final int IF_ICMPLE = 162;
	private static final int IF_ICMPGT = 163;
	private static final int IF_ICMPGE = 164;
	private static final int IFEQ = 153;
	private static final int IFNE = 154;
	private static final int IFLT = 155;
	private static final int IFLE = 156;
	private static final int IFGT = 157;
	private static final int IFGE = 158;
	private static final int IFNONNULL = 199;
	private static final int IFNULL = 198;
	private static final int IINC = 132;
	private static final int INSTANCEOF = 193;
	private static final int INVOKESPECIAL = 183;
	private static final int INVOKESTATIC = 184;
	private static final int INVOKEVIRTUAL = 182;
	private static final int JSR = 168;
	private static final int LDC_W = 19;
	private static final int LDC2_W = 20;
	private static final int NEW = 187;
	private static final int PUTFIELD = 181;
	private static final int PUTSTATIC = 179;
	private static final int SIPUSH = 17;
	private static final int MULTILANEARRAY = 197;
	private static final int GOTO_W = 200;
	private static final int INVOKEINTERFACE = 185;
	private static final int JSR_W = 201;
	private static final int LOOKUPSWITCH = 171;
	private static final int TABLESWITCH = 170;
	private static final int WIDE = 196;


	/**
	 * Returns an array of Sets, 0 = methods, 1 = usage, 2 = creation
	 */
	public static Set[] getReferences(TrackableDataInputStream tis, int codeLength) throws IOException {
		HashSet methodRefs = new HashSet();
		HashSet usageRefs = new HashSet();
		HashSet createRefs = new HashSet();

		int pos = tis.getPosition();
		while(tis.getPosition()-pos<codeLength) {
			findRef(tis,methodRefs,usageRefs,createRefs, pos);
		}

		Iterator it = methodRefs.iterator();
		while(it.hasNext())
			usageRefs.remove(it.next());

		Set[] results = new Set[3];

		results[0] = methodRefs;
		results[1] = usageRefs;
		results[2] = createRefs;

		return results;
	}

	private static void findRef(TrackableDataInputStream tis, Set methodRefs, Set usageRefs, Set createRefs, int pos) throws IOException {
		int opcode = tis.readUnsignedByte();
//		System.out.println("opcode "+opcode);

		Integer offset;

		switch(opcode) {
			case AALOAD:
			case ALOAD_0:
			case ALOAD_1:
			case ALOAD_2:
			case ALOAD_3:
			case ACONST_NULL:
			case ARRAYLENGTH:
			case ASTORE_0:
			case ASTORE_1:
			case ASTORE_2:
			case ASTORE_3:
			case ATHROW:
			case BALOAD:
			case BASTORE:
			case CALOAD:
			case CASTORE:
			case D2F:
			case D2I:
			case D2L:
			case DADD:
			case DALOAD:
			case DASTORE:
			case DCMPG:
			case DCMPL:
			case DCONST_0:
			case DCONST_1:
			case DDIV:
			case DLOAD_0:
			case DLOAD_1:
			case DLOAD_2:
			case DLOAD_3:
			case DMUL:
			case DNEG:
			case DREM:
			case DRETURN:
			case DSTORE_0:
			case DSTORE_1:
			case DSTORE_2:
			case DSTORE_3:
			case DSUB:
			case DUP:
			case DUP_X1:
			case DUP_X2:
			case DUP2:
			case DUP2_X1:
			case DUP2_X2:
			case F2D:
			case F2I:
			case F2L:
			case FADD:
			case FCMPG:
			case FCMPL:
			case FCONST_0:
			case FCONST_1:
			case FCONST_2:
			case FDIV:
			case FLOAD_0:
			case FLOAD_1:
			case FLOAD_2:
			case FLOAD_3:
			case FMUL:
			case FNEG:
			case FREM:
			case FRETURN:
			case FSTORE_0:
			case FSTORE_1:
			case FSTORE_2:
			case FSTORE_3:
				break; // 1 byte, no skip

			case FLOAD:
			case FSTORE:
			case ILOAD:
			case ISTORE:
			case LDC:
			case LLOAD:
			case LSTORE:
			case NEWARRAY:
			case RET:
//				System.out.println("skip 1");
				tis.skipBytes(1);
				break; // 2 bytes

			case ALOAD:
			case ASTORE:
			case BIPUSH:
			case DLOAD:
			case DSTORE:
			case GOTO:
			case IF_ACMPEQ:
			case IF_ACMPNE:
			case IF_ICMPEQ:
			case IF_ICMPNE:
			case IF_ICMPLT:
			case IF_ICMPLE:
			case IF_ICMPGT:
			case IF_ICMPGE:
			case IFEQ:
			case IFNE:
			case IFGE:
			case IFGT:
			case IFLE:
			case IFLT:
			case IFNONNULL:
			case IFNULL:
			case IINC:
			case JSR:
			case LDC_W:
			case LDC2_W:
			case SIPUSH:
//				System.out.println("skip 2");
				tis.skipBytes(2);
				break; // 3 bytes

			case NEW:
//				System.out.println("read 2");
				offset = new Integer(tis.readUnsignedShort());
				createRefs.add(offset);
				break; // 3 bytes

			case ANEWARRAY:
			case CHECKCAST:
			case GETFIELD:
			case GETSTATIC:
			case PUTFIELD:
			case PUTSTATIC:
			case INSTANCEOF:
//				System.out.println("read 2");
				offset = new Integer(tis.readUnsignedShort());
				usageRefs.add(offset);
				break; // 3 bytes

			case INVOKESPECIAL:
			case INVOKESTATIC:
			case INVOKEVIRTUAL:
//				System.out.println("read 2");
				offset = new Integer(tis.readUnsignedShort());
				methodRefs.add(offset);
				break; // 3 bytes

			case MULTILANEARRAY:
//				System.out.println("read 3");
				offset = new Integer(tis.readUnsignedShort());
				usageRefs.add(offset);
				tis.skipBytes(1);
				break; // 4 bytes

			case INVOKEINTERFACE:
//				System.out.println("read 4");
				offset = new Integer(tis.readUnsignedShort());
				methodRefs.add(offset);
				tis.skipBytes(2);
				break;

			case GOTO_W:
			case JSR_W:
//				System.out.println("skip 4");
				tis.skipBytes(4);
				break; // 5 bytes

			case LOOKUPSWITCH:
//				System.out.println("skip n");
				System.out.println("uh oh, lookupswitch!");
				int spare = getSpare(tis,pos);
				tis.skipBytes(spare);
				tis.skipBytes(4); // default
				int num = tis.readInt();
				tis.skipBytes(4*num);
				break;

			case TABLESWITCH:
//				System.out.println("skip n");
				System.out.println("uh oh, tableswitch!");
//				tis.skip(tis.available());
//				spare = (4 - ((tis.getPosition()-1) & 0x03)) & 0x03;

				spare = getSpare(tis,pos);

				tis.skipBytes(spare);
				tis.skipBytes(4);
				int low = tis.readInt();
				int high = tis.readInt();
				tis.skipBytes(4*(high-low)); // default,low,high
				break;

			case WIDE:
//				System.out.println("skip 4 or 2");
				int opcode2 = tis.readUnsignedByte();

				if(opcode2 == IINC)
					tis.skipBytes(4);
				else
					tis.skipBytes(2);

				break;

			default:
				throw new IOException("Unknown opcode "+opcode);
		}
	}

	private static int getSpare(TrackableDataInputStream tis, int start) {
		int spare = ((tis.getPosition()-start-1) & 0x03) ^ 0x03;
//		System.out.println("pos "+(tis.getPosition()-start)+" spare "+spare);
		return spare;
	}
}