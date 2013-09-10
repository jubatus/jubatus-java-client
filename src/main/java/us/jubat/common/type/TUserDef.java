package us.jubat.common.type;

import us.jubat.common.UserDefinedMessage;

public class TUserDef<T extends UserDefinedMessage> implements TType<T> {
	public void check(T value) {
		if (value == null) {
			throw new NullPointerException();
		} else {
			value.check();
		}
	}
}
