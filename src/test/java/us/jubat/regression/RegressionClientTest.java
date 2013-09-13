package us.jubat.regression;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
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

import us.jubat.common.Datum;
import us.jubat.testutil.JubaServer;
import us.jubat.testutil.JubatusClientTest;

public class RegressionClientTest extends JubatusClientTest {
	private RegressionClient client;

	public RegressionClientTest() {
		super(JubaServer.regression);
	}

	@Before
	public void setUp() throws Exception {
		server.start(server.getConfigPath());
		client = new RegressionClient(server.getHost(), server.getPort(), NAME,
				TIMEOUT_SEC);
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	@Test
	public void testGet_config() throws IOException {
		String config = client.getConfig();
		assertThat(formatAsJson(config),
				is(formatAsJson(server.getConfigData())));
	}

	@Test
	public void testTrain_and_Estimate() {
		Datum datum = new Datum();

		for (int i = 1; i <= 10; i++) {
			datum.addString("key/str" + Integer.toString(i), "val/str"
					+ Integer.toString(i));
		}

		for (int i = 1; i <= 10; i++) {
			datum.addNumber("key/num" + Integer.toString(i), i);
		}

		ScoredDatum train_datum = new ScoredDatum(1f, datum);

		List<ScoredDatum> train_data = new ArrayList<ScoredDatum>();
		train_data.add(train_datum);

		for (int i = 1; i <= 100; i++) {
			assertThat(client.train(train_data), is(1));
		}

		List<Datum> estimate_data = new ArrayList<Datum>();
		estimate_data.add(datum);
		List<Float> result = client.estimate(estimate_data);

		assertThat(result, is(notNullValue()));
		assertThat(result.size(), is(1));
		assertThat(result.get(0).doubleValue(), is(closeTo(1.0, 0.00001)));
	}

	@Test
	public void testSave_and_Load() {
		String id = "regression.test_java-client.model";
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
	public void testClear() {
		Datum datum = new Datum();
		datum.addString("key/str", "val/str");
		datum.addNumber("key/str", 1);

		ScoredDatum train_datum = new ScoredDatum(1f, datum);

		List<ScoredDatum> train_data = new ArrayList<ScoredDatum>();
		train_data.add(train_datum);

		client.train(train_data);

		Map<String, Map<String, String>> before = client.getStatus();
		String node_name = (String) before.keySet().iterator().next();
		assertThat(before.get(node_name).get("num_classes"), is(not("0")));
		assertThat(before.get(node_name).get("num_features"), is(not("0")));

		client.clear();

		Map<String, Map<String, String>> after = client.getStatus();
		assertThat(after.get(node_name).get("num_classes"), is("0"));
		assertThat(after.get(node_name).get("num_features"), is("0"));
	}

	@Test
	public void testGet_client() {
		assertThat(client.getClient(), is(instanceOf(Client.class)));
		assertThat(client.getClient(), is(notNullValue()));
	}
}
