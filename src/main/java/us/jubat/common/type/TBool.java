package us.jubat.common.type;

import org.msgpack.type.Value;

import us.jubat.common.TypeMismatch;

public class TBool extends TPrimitive<Boolean> {
	public static final TBool instance = new TBool();

	public Boolean revert(Value value) {
		if (value.isBooleanValue()) {
			return value.asBooleanValue().getBoolean();
		} else {
			throw new TypeMismatch();
		}
	}
}
