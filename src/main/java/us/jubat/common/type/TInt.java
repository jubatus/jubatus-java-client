package us.jubat.common.type;

import org.msgpack.type.Value;

import us.jubat.common.TypeMismatch;

public class TInt extends TPrimitive<Integer> {
	public static final TInt instance = new TInt();

	public Integer revert(Value value) {
		if (value.isIntegerValue()) {
			return value.asIntegerValue().getInt();
		} else {
			throw new TypeMismatch();
		}
	}
}
