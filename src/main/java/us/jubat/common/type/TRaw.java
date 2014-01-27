package us.jubat.common.type;

import org.msgpack.type.Value;

import us.jubat.common.TypeMismatch;

public class TRaw extends TPrimitive<byte[]> {
	public static final TRaw instance = new TRaw();

	public byte[] revert(Value value) {
		if (value.isRawValue()) {
			return value.asRawValue().getByteArray();
		} else {
			throw new TypeMismatch();
		}
	}
}
