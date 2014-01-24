package us.jubat.common.type;

import org.msgpack.type.Value;

import us.jubat.common.TypeMismatch;

public class TNull implements TType<Object> {
	public static TNull instance = new TNull();
	
	private TNull() {
	}

	public void check(Object value) {
		if (value != null) {
			throw new TypeMismatch();
		}
	}

	public Object revert(Value value) {
		if (value.isNilValue()) {
			return null;
		} else {
			throw new TypeMismatch();
		}
	}

}
