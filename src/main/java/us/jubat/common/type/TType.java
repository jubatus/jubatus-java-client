package us.jubat.common.type;

import org.msgpack.type.Value;

public interface TType<T> {
	public void check(T value);

	public T revert(Value value);
}
