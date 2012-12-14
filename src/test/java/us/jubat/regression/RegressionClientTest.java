package us.jubat.regression;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import us.jubat.testutil.JubaServer;
import us.jubat.testutil.JubaServer.Engine;

public class RegressionClientTest {

	private static final String HOST = "localhost";
	public static final String NAME = JubaServer.NAME;
	private static final double TIMEOUT_SEC = 10;

	private static final String METHOD = "PA";
	private static final String CONFIG = "{" + "\"string_filter_types\" : {},"
			+ "\"string_filter_rules\" : []," + "\"num_filter_types\" : {},"
			+ "\"num_filter_rules\" : []," + "\"string_types\" : {},"
			+ "\"string_rules\" : [" + "{" + "\"key\" : \"*\","
			+ "\"type\" : \"space\"," + "\"sample_weight\" : \"bin\","
			+ "\"global_weight\" : \"bin\"" + "}" + "],"
			+ "\"num_types\" : {}," + "\"num_rules\" : [" + "{"
			+ "\"key\" : \"*\"," + "\"type\" : \"num\"" + "}" + "]" + "}";

	private RegressionClient client;
	private JubaServer server;

	@Before
	public void setUp() throws Exception {
		server = new JubaServer(Engine.regression);
		server.start();
		client = new RegressionClient(HOST, Engine.regression.getPort(), TIMEOUT_SEC);
		ConfigData config_data = new ConfigData();
		config_data.method = METHOD;
		config_data.config = CONFIG;
		client.set_config(NAME, config_data);
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	@Test
	public void testGet_config() {
		ConfigData config_data = client.get_config(NAME);
		assertThat(config_data.method, is(METHOD));
		assertThat(config_data.config, is(CONFIG));
	}

	@Test
	public void testTrain_and_Estimate() {
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

		TupleFloatDatum train_datum = new TupleFloatDatum();
		train_datum.first = 1f;
		train_datum.second = datum;

		List<TupleFloatDatum> train_data = new ArrayList<TupleFloatDatum>();
		train_data.add(train_datum);

		for (int i = 1; i <= 100; i++) {
			assertThat(client.train(NAME, train_data), is(1));
		}

		List<Datum> estimate_data = new ArrayList<Datum>();
		estimate_data.add(datum);
		List<Float> result = client.estimate(NAME, estimate_data);

		assertThat(result, is(notNullValue()));
		assertThat(result.size(), is(1));
		assertThat(result.get(0).doubleValue(), is(closeTo(1.0, 0.00001)));
	}

	@Test
	public void testSave_and_Load() {
		String id = "regression.test_java-client.model";
		assertThat(client.save(NAME, id), is(true));
		assertThat(client.load(NAME, id), is(true));
	}

	@Test
	public void testGet_status() {
		Map<String, Map<String, String>> status = client.get_status(NAME);
		assertThat(status, is(notNullValue()));
		assertThat(status.size(), is(1));
	}
}
