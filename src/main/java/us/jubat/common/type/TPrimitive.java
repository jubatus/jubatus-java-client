package us.jubat.common.type;

public abstract class TPrimitive<T> implements TType<T> {

	public final void check(T value) {
		if (value == null)
			throw new NullPointerException();
	}

}
