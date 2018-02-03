package intro2.database.reference;

final class ByteCode {
	static final int UNUSED = 0;
	static final int NOSKIP_1 = 1;
	static final int SKIP1_2 = 2;
	static final int SKIP2_3 = 3;
	static final int SKIP4_5 = 5;
	static final int NEW = 187;
	static final int USAGE_3 = 4;
	static final int FIELD_3 = 7;
	static final int METHOD_3 = 6;
	static final int MULTIANEWARRAY = 197;
	static final int INVOKEINTERFACE = 185;
	static final int LOOKUPSWITCH = 171;
	static final int TABLESWITCH = 170;
	static final int WIDE = 196;

	static final int IINC = 132;

	static final int refControlCodes[] = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
											1, 1, 1, 1, 1, 1, 2, 3, 2, 3,
											3, 2, 2, 2, 2, 2, 1, 1, 1, 1,
											1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
											1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
											1, 1, 1, 1, 2, 2, 2, 2, 2, 1,
											1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
											1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
											1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
											1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
											1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
											1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
											1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
											1, 1, 3, 1, 1, 1, 1, 1, 1, 1,
											1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
											1, 1, 1, 3, 3, 3, 3, 3, 3, 3,
											3, 3, 3, 3, 3, 3, 3, 3, 3, 2,
											170, 171, 1, 1, 1, 1, 1, 1, 7, 7,
											7, 7, 6, 6, 6, 185, 0, 187, 2, 4,
											1, 1, 4, 4, 1, 1, 196, 197, 3, 3,
											5, 5, 0, 0, 0, 0, 0, 0, 0, 0,
											0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
											0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
											0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
											0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
											0, 0, 0, 0, 0, 0 };
}