package intro.info.bytecode;

import intro.io.TrackableDataInputStream;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class ByteCode2 {
	private static final int UNUSED = 0;
	private static final int NOSKIP_1 = 1;
	private static final int SKIP1_2 = 2;
	private static final int SKIP2_3 = 3;
	private static final int SKIP4_5 = 5;
	private static final int NEW = 187;
	private static final int USAGE_3 = 4;
	private static final int FIELD_3 = 7;
	private static final int METHOD_3 = 6;
	private static final int MULTIANEWARRAY = 197;
	private static final int INVOKEINTERFACE = 185;
	private static final int LOOKUPSWITCH = 171;
	private static final int TABLESWITCH = 170;
	private static final int WIDE = 196;

	private static final int IINC = 132;

	private static Instruction[] instructions;

	static {
		try {
			FileReader fr = new FileReader("instructions.csv");
			BufferedReader br = new BufferedReader(fr);

			instructions = new Instruction[256];

			int first,second;
			String line;

			for(int count = 0;count<256;count++) {
				line = br.readLine();
				first = line.indexOf(',');
				second = line.lastIndexOf(',');

				instructions[count] = new Instruction(line.substring(first+1,second),
												Integer.parseInt(line.substring(second+1)));
			}

			br.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Failure to read instructions.csv");
		}
	}

	private static class Instruction {
		public String name;
		public int controlCode;

		public Instruction(String name, int controlCode) {
			this.name = name;
			this.controlCode = controlCode;
		}
	}

	/**
	 * Returns an array of Sets, 0 = methods, 1 = usage, 2 = creation
	 */
	public static Set[] getReferences(TrackableDataInputStream tis, int codeLength) throws IOException {
		HashSet methodRefs = new HashSet();
		HashSet usageRefs = new HashSet();
		HashSet createRefs = new HashSet();
		HashSet fieldRefs = new HashSet();

		int pos = tis.getPosition();
		while(tis.getPosition()-pos<codeLength) {
			findRef(tis,methodRefs,usageRefs,createRefs, fieldRefs, pos);
		}

		Iterator it = methodRefs.iterator();
		while(it.hasNext())
			usageRefs.remove(it.next());

		Set[] results = new Set[4];

		results[0] = methodRefs;
		results[1] = usageRefs;
		results[2] = createRefs;
		results[3] = fieldRefs;

		return results;
	}

	private static void findRef(TrackableDataInputStream tis, Set methodRefs, Set usageRefs, Set createRefs, Set fieldRefs, int pos) throws IOException {
		int here = tis.getPosition();
		int opcode = tis.readUnsignedByte();

		Instruction i = instructions[opcode];
//		System.out.println(here+" "+i.name+" ("+opcode+")");

		Integer offset;

		switch(i.controlCode) {
			case UNUSED:
				throw new IOException("Encountered unused opcode "+opcode);

			case NOSKIP_1:
				break; // 1 byte, no skip

			case SKIP1_2:
				tis.skipBytes(1);
				break; // 2 bytes

			case SKIP2_3:
				tis.skipBytes(2);
				break; // 3 bytes

			case SKIP4_5:
				tis.skipBytes(4);
				break; // 5 bytes

			case NEW:
				offset = new Integer(tis.readUnsignedShort());
				createRefs.add(offset);
//				System.out.println("  create ref "+offset);
				break; // 3 bytes

			case USAGE_3:
				offset = new Integer(tis.readUnsignedShort());
				usageRefs.add(offset);
//				System.out.println("  usage ref "+offset);
				break; // 3 bytes

			case METHOD_3:
				offset = new Integer(tis.readUnsignedShort());
				methodRefs.add(offset);
//				System.out.println("  method ref "+offset);
				break; // 3 bytes

			case FIELD_3:
				offset = new Integer(tis.readUnsignedShort());
				fieldRefs.add(offset);
//				System.out.println("  field ref "+offset);
				break; // 3 bytes

			case MULTIANEWARRAY:
				offset = new Integer(tis.readUnsignedShort());
				usageRefs.add(offset);
//				System.out.println("  usage ref "+offset);
				tis.skipBytes(1);
				break; // 4 bytes

			case INVOKEINTERFACE:
				offset = new Integer(tis.readUnsignedShort());
				methodRefs.add(offset);
//				System.out.println("  method ref "+offset);
				tis.skipBytes(2);
				break;

			case LOOKUPSWITCH:
//				System.out.println("uh oh, lookupswitch!");

				int spare = getSpare(tis,pos);
				tis.skipBytes(spare);

				tis.skipBytes(4); // default
				int num = tis.readInt();
//				System.out.println("skipping "+num+" dwords");
				tis.skipBytes(8*num);
				break;

			case TABLESWITCH:
//				System.out.println("uh oh, tableswitch!");

				spare = getSpare(tis,pos);
				tis.skipBytes(spare);

				tis.skipBytes(4);
				int low = tis.readInt();
				int high = tis.readInt();
				tis.skipBytes(4*(high-low+1)); // default,low,high
				break;

			case WIDE:
				int opcode2 = tis.readUnsignedByte();

				if(opcode2 == IINC)
					tis.skipBytes(4);
				else
					tis.skipBytes(2);

				break;

			default:
				throw new IOException("Unknown control code "+i.controlCode);
		}
	}

	private static int getSpare(TrackableDataInputStream tis, int start) {
		int spare = ((tis.getPosition()-start-1) & 0x03) ^ 0x03;
//		System.out.println("pos "+tis.getPosition()+" offset "+(tis.getPosition()-start)+" spare "+spare);
		return spare;
	}
}