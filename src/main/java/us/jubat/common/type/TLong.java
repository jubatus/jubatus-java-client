package us.jubat.common.type;

import org.msgpack.type.Value;

import us.jubat.common.TypeMismatch;

public class TLong extends TPrimitive<Long> {
	public static final TLong instance = new TLong();

	public Long revert(Value value) {
		if (value.isIntegerValue()) {
			return value.asIntegerValue().getLong();
		} else {
			throw new TypeMismatch();
		}
	}
}
