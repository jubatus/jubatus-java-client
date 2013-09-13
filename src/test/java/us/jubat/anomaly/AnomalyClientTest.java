package us.jubat.anomaly;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.msgpack.rpc.Client;

import us.jubat.common.Datum;
import us.jubat.testutil.JubaServer;
import us.jubat.testutil.JubatusClientTest;

public class AnomalyClientTest extends JubatusClientTest {
	private AnomalyClient client;

	public AnomalyClientTest() {
		super(JubaServer.anomaly);
	}

	@Before
	public void setUp() throws Exception {
		server.start(server.getConfigPath());
		client = new AnomalyClient(server.getHost(), server.getPort(), NAME,
				TIMEOUT_SEC);
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	@Test
	public void testClear_row() {
		String key = addTestDatum(1).id;
		assertThat(client.clearRow(key), is(true));
		assertThat(client.getAllRows().size(), is(0));
	}

	@Test
	public void testAdd() {
		Datum d = generateDatum();
		IdWithScore result = client.add(d);
		assertThat(result.id, is("0"));
		assertThat(result.score, is(Float.POSITIVE_INFINITY)); // Is it good to
																// be INF ?
	}

	@Test
	public void testUpdate() {
		Datum d = generateDatum();
		IdWithScore added = client.add(d);
		assertThat(client.update(added.id, generateDatum()),
				is(Float.POSITIVE_INFINITY)); // Is it good to be INF ?
	}

	@Test
	public void testCalc_score() {
		assertThat(client.calcScore(generateDatum()),
				is(Float.POSITIVE_INFINITY)); // Is it good to be INF ?
	}

	@Test
	public void testClear_and_Get_all_rows() {
		addTestDatum(3);
		assertThat(client.getAllRows().size(), is(3));
		assertThat(client.clear(), is(true));
		assertThat(client.getAllRows().size(), is(0));
	}

	@Test
	public void testGet_config() throws IOException {
		String config = client.getConfig();
		assertThat(formatAsJson(config),
				is(formatAsJson(server.getConfigData())));
	}

	@Test
	public void testSave_and_Load() {
		String id = "anomaly.test_java-client.model";
		assertThat(client.save(id), is(true));
		assertThat(client.load(id), is(true));
	}

	@Test
	public void testGet_status() {
		Map<String, Map<String, String>> status = client.getStatus();
		assertThat(status, is(notNullValue()));
		assertThat(status.size(), is(1));
	}

	@Test
	public void testGet_client() {
		assertThat(client.getClient(), is(instanceOf(Client.class)));
		assertThat(client.getClient(), is(notNullValue()));
	}

	/**
	 * Call "add" method with a generated test datum.
	 * 
	 * @param count
	 *            number of times to call "add", must be greater than 0
	 * @return the last result of "add" RPC call
	 */
	private IdWithScore addTestDatum(int count) {
		IdWithScore result = null;
		for (int i = 1; i <= count; i++) {
			result = client.add(generateDatum());
		}
		return result;
	}

	private Datum generateDatum() {
		Datum datum = new Datum();

		for (int i = 1; i <= 10; i++) {
			datum.addString("key/str" + Integer.toString(i), "val/str"
					+ Integer.toString(i));
		}

		for (int i = 1; i <= 10; i++) {
			datum.addNumber("key/num" + Integer.toString(i), i);
		}

		return datum;
	}
}
