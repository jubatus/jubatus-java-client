package us.jubat.common.type;

import java.util.List;

import org.msgpack.type.ArrayValue;

import us.jubat.common.Datum;

public class TDatum extends TUserDef<Datum> {
	public static final TDatum instance = new TDatum();

	private TDatum() {
		super(3);
	}

	public void check(Datum value) {
		if (value == null)
			throw new NullPointerException();
		else
			value.check();
	}

	public Datum create(ArrayValue value) {
		List<Datum.StringValue> stringValues = new TList<Datum.StringValue>(
				Datum.StringValue.Type.instance).revert(value.get(0));
		List<Datum.NumValue> numValues = new TList<Datum.NumValue>(
				Datum.NumValue.Type.instance).revert(value.get(1));
		List<Datum.BinaryValue> binaryValues = new TList<Datum.BinaryValue>(
				Datum.BinaryValue.Type.instance).revert(value.get(2));
		return new Datum(stringValues, numValues, binaryValues);
	}
}
