package us.jubat.common.type;

import java.util.ArrayList;
import java.util.List;

import org.msgpack.type.ArrayValue;
import org.msgpack.type.Value;

import us.jubat.common.TypeMismatch;

public class TList<T> implements TType<List<T>> {
	private final TType<T> type;

	public TList(TType<T> type) {
		this.type = type;
	}

	public void check(List<T> value) {
		if (value == null)
			throw new NullPointerException();
		for (T v : value) {
			type.check(v);
		}
	}

	public List<T> revert(Value value) {
		if (value.isArrayValue()) {
			ArrayValue array = value.asArrayValue();
			List<T> list = new ArrayList<T>();
			for (Value v : array) {
				list.add(this.type.revert(v));
			}
			return list;
		} else {
			throw new TypeMismatch();
		}
	}

}
