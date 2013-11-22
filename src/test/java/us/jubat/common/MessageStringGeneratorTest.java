/*
 * Copyright (c) 2013 NTT Corporation & Preferred Infrastructure.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.jubat.common;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MessageStringGeneratorTest {
	@Test
	public void testEmpty() {
		MessageStringGenerator gen = new MessageStringGenerator();
		gen.open("test");
		gen.close();
		assertEquals("test{}", gen.toString());
	}

	@Test
	public void testOne() {
		MessageStringGenerator gen = new MessageStringGenerator();
		gen.open("test");
		gen.add("var", "val");
		gen.close();
		assertEquals("test{var: val}", gen.toString());
	}

	@Test
	public void testTwo() {
		MessageStringGenerator gen = new MessageStringGenerator();
		gen.open("test");
		gen.add("var1", "val1");
		gen.add("var2", "val2");
		gen.close();
		assertEquals("test{var1: val1, var2: val2}", gen.toString());
	}

	@Test
	public void testMany() {
		MessageStringGenerator gen = new MessageStringGenerator();
		gen.open("test");
		gen.add("var1", "val1");
		gen.add("var2", "val2");
		gen.add("var3", "val3");
		gen.add("var4", "val4");
		gen.close();
		assertEquals("test{var1: val1, var2: val2, var3: val3, var4: val4}",
				gen.toString());
	}

	@Test
	public void testNull() {
		MessageStringGenerator gen = new MessageStringGenerator();
		gen.open("test");
		gen.add("var", null);
		gen.close();
		assertEquals("test{var: null}", gen.toString());
	}
}