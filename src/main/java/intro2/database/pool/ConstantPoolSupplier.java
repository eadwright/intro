package intro2.database.pool;

import intro2.database.Database.DataProcessingException;

public interface ConstantPoolSupplier {
	public ConstantPoolInfo getCPI(int num) throws DataProcessingException;
}