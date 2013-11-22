package us.jubat.common.type;

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

}
