package us.jubat.common;

public class TUserDef<T extends UserDefinedMessage> implements TType<T> {
	public void check(T value) {
		if (value == null) {
			throw new NullPointerException();
		} else {
			value.check();
		}
	}
}
