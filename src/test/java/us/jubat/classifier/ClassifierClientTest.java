package us.jubat.classifier;

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

public class ClassifierClientTest {

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

	private ClassifierClient client;
	private JubaServer server;

	@Before
	public void setUp() throws Exception {
		server = new JubaServer(Engine.classifier);
		server.start();
		client = new ClassifierClient(HOST, Engine.classifier.getPort(), TIMEOUT_SEC);
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
	public void testTrain_and_Classify() {
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

		TupleStringDatum train_datum = new TupleStringDatum();
		train_datum.first = "label";
		train_datum.second = datum;

		List<TupleStringDatum> train_data = new ArrayList<TupleStringDatum>();
		train_data.add(train_datum);

		for (int i = 1; i <= 100; i++) {
			assertThat(client.train(NAME, train_data), is(1));
		}

		List<Datum> test_data = new ArrayList<Datum>();
		test_data.add(datum);
		List<List<EstimateResult>> result = client.classify(NAME, test_data);

		assertThat(result, is(notNullValue()));
		assertThat(result.size(), is(1));
		assertThat(result.get(0), is(notNullValue()));
		assertThat(result.get(0).size(), is(1));
		assertThat(result.get(0).get(0).label, is("label"));
		assertThat(result.get(0).get(0).prob, is(closeTo(1.0, 0.00001)));
	}

	@Test
	public void testSave_and_Load() {
		String id = "classifier.test_java-client.model";
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
