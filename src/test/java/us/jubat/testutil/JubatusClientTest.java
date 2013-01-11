package us.jubat.testutil;

import org.json.simple.JSONValue;

public abstract class JubatusClientTest {
	public final String NAME = "";
	public final double TIMEOUT_SEC = 10;

	public final JubaServer server;

	public JubatusClientTest(JubaServer server) {
		this.server = server;
	}

	public String formatAsJson(String text) {
		return JSONValue.parse(text).toString();
	}
}
