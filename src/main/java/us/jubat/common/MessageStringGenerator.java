/*
 * Copyright (c) 2013 NTT Corporation & Preferred Networks.
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

import java.lang.StringBuilder;

public class MessageStringGenerator {
	public static final String OPEN = "{";
	public static final String CLOSE = "}";
	public static final String DELIMITER = ", ";
	public static final String SPLITTER = ": ";
	public static final String NULL_STRING = "null";

	private StringBuilder builder = new StringBuilder();
	private boolean first = true;

	public void open(String name) {
		builder.append(name);
		builder.append(OPEN);
	}

	public void close() {
		builder.append(CLOSE);
	}

	public void add(String key, Object value) {
		if (first) {
			first = false;
		} else {
			builder.append(DELIMITER);
		}
		builder.append(key);
		builder.append(SPLITTER);
		if (value == null) {
			builder.append(NULL_STRING);
		} else {
			builder.append(value.toString());
		}
	}

	public String toString() {
		return builder.toString();
	}
}
