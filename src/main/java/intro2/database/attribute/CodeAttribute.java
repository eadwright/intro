package intro2.database.attribute;

import intro2.database.ByteArrayAccessor;
import intro2.database.ClassInfo;
import intro2.database.Database.DataProcessingException;
import intro2.database.pool.ClassCPI;
import intro2.database.pool.UTF8CPI;
import intro2.database.reference.ReferenceFactory;
import intro2.util.I2IMap;

public final class CodeAttribute {
	private int maxStack;
	private int maxLocals;

	private int byteCodeOffset;
	private ExceptionTableEntry[] et;
	private int etOffset;

	private I2IMap lineNumberTable;
	private ClassInfo ci;

	public CodeAttribute(int offset, ClassInfo ci) throws DataProcessingException {
		this.ci = ci;
		ByteArrayAccessor acc = ci.getClassBytes();

//		acc.dump();
//		System.out.println("CodeAttrib offset "+offset);

		maxStack = acc.getUnsignedShort(offset+6);
		maxLocals = acc.getUnsignedShort(offset+8);

		offset += 10;
		byteCodeOffset = offset;

		offset += 4 + acc.getUnsignedInt(byteCodeOffset);

		etOffset = offset;
//		System.out.println("ex table offset "+offset);

		offset += 2 + (acc.getUnsignedShort(offset)*8);

//		System.out.println("read attr count from "+offset);

		int attrCount = acc.getUnsignedShort(offset);

		offset +=2;

		String attrName;
		UTF8CPI u;

		int n,num,i,start,line,loffset;

		for(n=0;n<attrCount;n++) {
			u = (UTF8CPI)ci.getCPI(acc.getUnsignedShort(offset));
			attrName = u.getString();

			if(attrName.equals("LineNumberTable")) {
				if(lineNumberTable == null)
					lineNumberTable = new I2IMap();

				num = acc.getUnsignedShort(offset+6);
				loffset = offset+8;

				for(i=0;i<num;i++) {
					start = acc.getUnsignedShort(loffset);
					line = acc.getUnsignedShort(loffset+2);

					lineNumberTable.put(start, line);

					loffset += 4;
				}
			}

			// LocalVariableTable ignored

			offset += acc.getUnsignedInt(offset+2) + 6;
		}
	}

	public int getMaxStack() {
		return maxStack;
	}

	public int getMaxLocals() {
		return maxLocals;
	}

	// Exception handlers
	public ExceptionTableEntry[] getExceptionTable() throws DataProcessingException {
		if(et == null)
			createExceptionTable();

		return et;
	}

	public void addReferences(int methodOffset) throws DataProcessingException {
		ReferenceFactory.addReferences(ci, byteCodeOffset, methodOffset);
		ReferenceFactory.addReferences(ci.getID(), getExceptionTable(), methodOffset);
	}

	private void createExceptionTable() throws DataProcessingException {
		int offset = etOffset;

		ByteArrayAccessor acc = ci.getClassBytes();
		et = new ExceptionTableEntry[acc.getUnsignedShort(offset)];
		offset += 2;
		for(int i=0;i<et.length;i++) {
			et[i] = new ExceptionTableEntry(offset, acc);
			offset += 8;
		}
	}

	public final class ExceptionTableEntry {
		private int start_pc;
		private int end_pc;
		private int handler_pc;
		private int catchTypeIndex;

		private ExceptionTableEntry(int offset, ByteArrayAccessor acc) {
			start_pc = acc.getUnsignedShort(offset);
			end_pc = acc.getUnsignedShort(offset+2);
			handler_pc = acc.getUnsignedShort(offset+4);
			catchTypeIndex = acc.getUnsignedShort(offset+6);
		}

		public int getStartPC() {
			return start_pc;
		}

		public int getEndPC() {
			return end_pc;
		}

		public int getHandlerPC() {
			return handler_pc;
		}

		public boolean isFinally() {
			return catchTypeIndex == 0;
		}

		public String getCatchType() throws DataProcessingException {
			if(isFinally())
				return null;

			return ((ClassCPI)ci.getCPI(catchTypeIndex)).getName();
		}
	}
}
