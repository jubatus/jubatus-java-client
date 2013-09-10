package us.jubat.common.type;

import java.util.List;

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

}
