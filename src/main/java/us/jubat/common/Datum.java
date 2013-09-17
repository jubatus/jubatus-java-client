package us.jubat.common;

import java.util.ArrayList;
import java.util.List;

import org.msgpack.annotation.Message;
import org.msgpack.annotation.NotNullable;

import us.jubat.common.type.TRaw;
import us.jubat.common.type.TString;

@Message
public class Datum {
	@Message
	public static class NumValue {
		public String key;
		public double value;

		public NumValue() {
		}

		public NumValue(String key, double value) {
			this.key = key;
			this.value = value;
		}

		public void check() {
			TString.instance.check(this.key);
		}
	}

	@Message
	public static class StringValue {
		public String key;
		public String value;

		public StringValue() {
		}

		public StringValue(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public void check() {
			TString.instance.check(this.key);
			TString.instance.check(this.value);
		}
	}

	@Message
	public static class BinaryValue {
		public String key;
		public byte[] value;

		public BinaryValue() {
		}

		public BinaryValue(String key, byte[] value) {
			this.key = key;
			this.value = value;
		}

		public void check() {
			TString.instance.check(this.key);
			TRaw.instance.check(this.value);
		}
	}

	@NotNullable
	public List<StringValue> stringValues = new ArrayList<StringValue>();
	@NotNullable
	public List<NumValue> numValues = new ArrayList<NumValue>();
	@NotNullable
	public List<BinaryValue> binaryValues = new ArrayList<BinaryValue>();

	public Datum addNumber(String key, double value) {
		this.numValues.add(new NumValue(key, value));
		return this;
	}

	public Datum addString(String key, String value) {
		this.stringValues.add(new StringValue(key, value));
		return this;
	}

	public Datum addBinary(String key, byte[] value) {
		this.binaryValues.add(new BinaryValue(key, value));
		return this;
	}

	public Iterable<StringValue> getStringValues() {
		return this.stringValues;
	}

	public Iterable<NumValue> getNumValues() {
		return this.numValues;
	}

	public Iterable<BinaryValue> getBinaryValues() {
		return this.binaryValues;
	}

	public void check() {
		for (StringValue v : this.stringValues) {
			v.check();
		}
		for (NumValue v : this.numValues) {
			v.check();
		}
		for (BinaryValue v : this.binaryValues) {
			v.check();
		}
	}
}