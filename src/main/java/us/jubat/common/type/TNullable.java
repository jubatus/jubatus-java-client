package us.jubat.common.type;

import org.msgpack.type.Value;

public class TNullable<T> implements TType<T> {
	private TType<T> type;

	public TNullable(TType<T> type) {
		this.type = type;
	}

	public void check(T value) {
		if (value != null) {
			this.type.check(value);
		}
	}

	public T revert(Value value) {
		if (value.isNilValue()) {
			return null;
		} else {
			return this.type.revert(value);
		}
	}
}
