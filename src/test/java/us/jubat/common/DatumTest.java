package us.jubat.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.msgpack.MessagePack;
import org.msgpack.packer.Packer;

public class DatumTest {
	private MessagePack msgpack = new MessagePack();

	@Test
	public void testEmpty() throws IOException {
		Datum d = new Datum();
		byte[] actual = msgpack.write(d);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Packer packer = msgpack.createPacker(out);
		packer.writeArrayBegin(3);
		packer.writeArrayBegin(0).writeArrayEnd(); // string values
		packer.writeArrayBegin(0).writeArrayEnd(); // num values
		packer.writeArrayBegin(0).writeArrayEnd(); // binary values
		packer.writeArrayEnd();
		byte[] expect = out.toByteArray();

		Assert.assertArrayEquals(expect, actual);
	}

	@Test
	public void testPack() throws IOException {
		Datum d = new Datum();
		d.addString("name", "Taro");
		d.addNumber("age", 20);
		d.addBinary("img", new byte[] { 0, 1, 0, 1 });
		byte[] actual = msgpack.write(d);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Packer packer = msgpack.createPacker(out);
		packer.writeArrayBegin(3);
		// string values
		packer.writeArrayBegin(1);
		packer.writeArrayBegin(2).write("name").write("Taro");
		packer.writeArrayEnd();
		// num values
		packer.writeArrayBegin(1);
		packer.writeArrayBegin(2).write("age").write(20.0);
		packer.writeArrayEnd();
		// binary values
		packer.writeArrayBegin(1);
		packer.writeArrayBegin(2).write("img").write(new byte[] { 0, 1, 0, 1 });
		packer.writeArrayEnd();
		packer.writeArrayEnd();
		byte[] expect = out.toByteArray();

		Assert.assertArrayEquals(expect, actual);
	}

	@Test
	public void testUnpack() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Packer packer = msgpack.createPacker(out);
		packer.writeArrayBegin(3);
		// string values
		packer.writeArrayBegin(1);
		packer.writeArrayBegin(2).write("name").write("Taro");
		packer.writeArrayEnd();
		// num values
		packer.writeArrayBegin(1);
		packer.writeArrayBegin(2).write("age").write(20.0);
		packer.writeArrayEnd();
		// binary values
		packer.writeArrayBegin(1);
		packer.writeArrayBegin(2).write("img").write(new byte[] { 0, 1, 0, 1 });
		packer.writeArrayEnd();
		packer.writeArrayEnd();
		byte[] packed = out.toByteArray();

		Datum d = msgpack.read(packed, Datum.class);

		Assert.assertEquals(1, d.stringValues.size());
		Assert.assertEquals("name", d.stringValues.get(0).key);
		Assert.assertEquals("Taro", d.stringValues.get(0).value);
		Assert.assertEquals(1, d.numValues.size());
		Assert.assertEquals("age", d.numValues.get(0).key);
		Assert.assertEquals(20.0, d.numValues.get(0).value, 0.001);
		Assert.assertEquals(1, d.binaryValues.size());
		Assert.assertEquals("img", d.binaryValues.get(0).key);
		Assert.assertArrayEquals(new byte[] { 0, 1, 0, 1 },
				d.binaryValues.get(0).value);
	}

	@Test(expected = NullPointerException.class)
	public void testNullStringValue() {
		Datum d = new Datum();
		d.addString("", null);
		d.check();
	}

	@Test(expected = NullPointerException.class)
	public void testNullBinaryValue() {
		Datum d = new Datum();
		d.addBinary("", null);
		d.check();
	}

}
