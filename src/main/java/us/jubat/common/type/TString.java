package us.jubat.common.type;

import org.msgpack.type.Value;

import us.jubat.common.TypeMismatch;

public class TString extends TPrimitive<String> {
	public static final TString instance = new TString();

	public String revert(Value value) {
		if (value.isRawValue()) {
			return value.asRawValue().getString();
		} else {
			throw new TypeMismatch();
		}
	}
}
