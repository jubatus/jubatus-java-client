package us.jubat.common.type;

import org.msgpack.type.Value;

import us.jubat.common.TypeMismatch;

public class TDouble extends TPrimitive<Double> {
	public static final TDouble instance = new TDouble();

	public Double revert(Value value) {
		if (value.isFloatValue()) {
			return value.asFloatValue().getDouble();
		} else {
			throw new TypeMismatch();
		}
	}
}
