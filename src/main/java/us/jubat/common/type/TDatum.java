package us.jubat.common.type;

import us.jubat.common.Datum;

public class TDatum implements TType<Datum> {
	public static final TDatum instance = new TDatum();

	public void check(Datum value) {
		if (value == null)
			throw new NullPointerException();
		else
			value.check();
	}

}
