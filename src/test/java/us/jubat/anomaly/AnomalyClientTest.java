package us.jubat.anomaly;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.msgpack.rpc.Client;

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
		client = new AnomalyClient(server.getHost(), server.getPort(),
				TIMEOUT_SEC);
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	@Test
	public void testClear_row() {
		String key = addTestDatum(1).first;
		assertThat(client.clear_row(NAME, key), is(true));
		assertThat(client.get_all_rows(NAME).size(), is(0));
	}

	@Test
	public void testAdd() {
		Datum d = generateDatum();
		TupleStringFloat result = client.add(NAME, d);
		assertThat(result.first, is("0"));
		assertThat(result.second, is(Float.POSITIVE_INFINITY)); // Is it good to be INF ?
	}

	@Test
	public void testUpdate() {
		Datum d = generateDatum();
		TupleStringFloat added = client.add(NAME, d);
		assertThat(client.update(NAME, added.first, generateDatum()),
				is(Float.POSITIVE_INFINITY)); // Is it good to be INF ?
	}

	@Test
	public void testCalc_score() {
		Datum d = generateDatum();
		assertThat(client.calc_score(NAME, generateDatum()),
				is(Float.POSITIVE_INFINITY)); // Is it good to be INF ?
	}

	@Test
	public void testClear_and_Get_all_rows() {
		addTestDatum(3);
		assertThat(client.get_all_rows(NAME).size(), is(3));
		assertThat(client.clear(NAME), is(true));
		assertThat(client.get_all_rows(NAME).size(), is(0));
	}

	@Test
	public void testGet_config() throws IOException {
		String config = client.get_config(NAME);
		assertThat(formatAsJson(config),
				is(formatAsJson(server.getConfigData())));
	}

	@Test
	public void testSave_and_Load() {
		String id = "anomaly.test_java-client.model";
		assertThat(client.save(NAME, id), is(true));
		assertThat(client.load(NAME, id), is(true));
	}

	@Test
	public void testGet_status() {
		Map<String, Map<String, String>> status = client.get_status(NAME);
		assertThat(status, is(notNullValue()));
		assertThat(status.size(), is(1));
	}

	@Test
	public void testGet_client() {
		assertThat(client.get_client(), is(instanceOf(Client.class)));
		assertThat(client.get_client(), is(notNullValue()));
	}

	/**
	 * Call "add" method with a generated test datum.
	 * 
	 * @param count
	 *            number of times to call "add", must be greater than 0
	 * @return the last result of "add" RPC call
	 */
	private TupleStringFloat addTestDatum(int count) {
		TupleStringFloat result = null;
		for (int i = 1; i <= count; i++) {
			result = client.add(NAME, generateDatum());
		}
		return result;
	}

	private Datum generateDatum() {
		Datum datum = new Datum();

		List<TupleStringString> string_values = new ArrayList<TupleStringString>();
		for (int i = 1; i <= 10; i++) {
			TupleStringString string_value = new TupleStringString();
			string_value.first = "key/str" + Integer.toString(i);
			string_value.second = "val/str" + Integer.toString(i);
			string_values.add(string_value);
		}
		datum.string_values = string_values;

		List<TupleStringDouble> num_values = new ArrayList<TupleStringDouble>();
		for (int i = 1; i <= 10; i++) {
			TupleStringDouble num_value = new TupleStringDouble();
			num_value.first = "key/num" + Integer.toString(i);
			num_value.second = i;
			num_values.add(num_value);
		}
		datum.num_values = num_values;

		return datum;
	}
}
