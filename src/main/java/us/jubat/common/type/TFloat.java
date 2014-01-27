package us.jubat.common.type;

import org.msgpack.type.Value;

import us.jubat.common.TypeMismatch;

public class TFloat extends TPrimitive<Float> {
	public static final TFloat instance = new TFloat();

	public Float revert(Value value) {
		if (value.isFloatValue()) {
			return value.asFloatValue().getFloat();
		} else {
			throw new TypeMismatch();
		}
	}
}
