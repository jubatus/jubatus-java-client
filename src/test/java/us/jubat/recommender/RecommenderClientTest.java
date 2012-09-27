package us.jubat.recommender;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import us.jubat.testutil.JubaServer;
import us.jubat.testutil.JubaServer.Engine;

public class RecommenderClientTest {

	private static final String HOST = "localhost";
	public static final String NAME = JubaServer.NAME;
	private static final double TIMEOUT_SEC = 10;

	private static final String METHOD = "minhash";
	private static final String CONVERTER = "{"
			+ "\"string_filter_types\" : {}," + "\"string_filter_rules\" : [],"
			+ "\"num_filter_types\" : {}," + "\"num_filter_rules\" : [],"
			+ "\"string_types\" : {}," + "\"string_rules\" : [" + "{"
			+ "\"key\" : \"*\"," + "\"type\" : \"str\","
			+ "\"sample_weight\" : \"bin\"," + "\"global_weight\" : \"bin\""
			+ "}" + "]," + "\"num_types\" : {}," + "\"num_rules\" : [" + "{"
			+ "\"key\" : \"*\"," + "\"type\" : \"num\"" + "}" + "]" + "}";

	private RecommenderClient client;
	private JubaServer server;

	@Before
	public void setUp() throws Exception {
		server = new JubaServer(Engine.recommender);
		server.start();
		client = new RecommenderClient(HOST, JubaServer.PORT, TIMEOUT_SEC);
		ConfigData config_data = new ConfigData();
		config_data.method = METHOD;
		config_data.converter = CONVERTER;
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
		assertThat(config_data.converter, is(CONVERTER));
	}

        @Test
        public void testDecode_row() {
                update_row(1);
                Datum datum = client.decode_row(NAME, Integer.toString(1));
                assertDatum(generateDatum(), datum);
        }

	@Test
	public void testClear_row() {
		update_row(1);
		// FIXME: return false
		assertThat(client.clear_row(NAME, Integer.toString(1)), is(false));

		Datum datum = client.decode_row(NAME, Integer.toString(1));
		assertThat(datum.string_values.size(), is(0));
		assertThat(datum.num_values.size(), is(0));
	}

	@Test
	public void testClear_and_Get_all_rows() {
		update_row(3);
		// FIXME: return false
		assertThat(client.clear(NAME), is(false));

		List<String> rows = client.get_all_rows(NAME);
		assertThat(rows.size(), is(0));
	}

	@Test
	public void testComplete_row_from_id() {
		update_row(1);
		Datum result = client.complete_row_from_id(NAME, Integer.toString(1));

		Datum row = client.decode_row(NAME, Integer.toString(1));
		assertDatum(row, result);
	}

	@Test
	public void testComplete_row_from_data() {
		update_row(1);
		Datum result = client.complete_row_from_data(NAME, generateDatum());

		Datum row = client.decode_row(NAME, Integer.toString(1));
		assertDatum(row, result);
	}

	@Test
	public void testSimilar_row_from_id() {
		update_row(1);
		List<TupleStringFloat> result = client.similar_row_from_id(NAME,
				Integer.toString(1), 1);
		assertThat(result, is(notNullValue()));
		assertThat(result.size(), is(1));
		assertThat(result.get(0).first, is("1"));
		assertThat(result.get(0).second, is(1f));
	}

	@Test
	public void testSimilar_row_from_data() {
		update_row(1);
		List<TupleStringFloat> result = client.similar_row_from_data(NAME,
				generateDatum(), 1);
		assertThat(result, is(notNullValue()));
		assertThat(result.size(), is(1));
		assertThat(result.get(0).first, is("1"));
		assertThat(result.get(0).second, is(1f));
	}

	@Test
	public void testSimilarity() {
		assertThat(client.similarity(NAME, generateDatum(), generateDatum()),
				is(1f));
	}

	@Test
	public void testL2norm() {
		double norm = client.l2norm(NAME, generateDatum());
                assertThat(norm, is(closeTo(19.874607, 0.00001)));
	}

	@Test
	public void testSave_and_Load() {
		String id = "recommender.test_java-client.model";
		assertThat(client.save(NAME, id), is(true));
		assertThat(client.load(NAME, id), is(true));
	}

	@Test
	public void testGet_status() {
		Map<String, Map<String, String>> status = client.get_status(NAME);
		assertThat(status, is(notNullValue()));
		assertThat(status.size(), is(1));
	}

	private Datum generateDatum() {
		Datum datum = new Datum();

		ArrayList<TupleStringString> string_values = new ArrayList<TupleStringString>();
		for (int i = 1; i <= 10; i++) {
			TupleStringString string_value = new TupleStringString();
			string_value.first = "key/str" + Integer.toString(i);
			string_value.second = "val/str" + Integer.toString(i);
			string_values.add(string_value);
		}
		datum.string_values = string_values;

		ArrayList<TupleStringDouble> num_values = new ArrayList<TupleStringDouble>();
		for (int i = 1; i <= 10; i++) {
			TupleStringDouble num_value = new TupleStringDouble();
			num_value.first = "key/num" + Integer.toString(i);
			num_value.second = i;
			num_values.add(num_value);
		}
		datum.num_values = num_values;

		return datum;
	}

	private void update_row(int number) {
		for (int i = 1; i <= number; i++) {
			client.update_row(NAME, Integer.toString(i), generateDatum());
		}
	}

	private void assertDatum(Datum actual, Datum expected) {
		List<String> expected_string_value_1 = new ArrayList<String>();
		List<String> expected_string_value_2 = new ArrayList<String>();
		for (TupleStringString string_value : expected.string_values) {
			expected_string_value_1.add(string_value.first);
			expected_string_value_2.add(string_value.second);
		}

		List<String> expected_num_value_1 = new ArrayList<String>();
		List<Double> expected_num_value_2 = new ArrayList<Double>();
		for (TupleStringDouble num_value : expected.num_values) {
			expected_num_value_1.add(num_value.first);
			expected_num_value_2.add(num_value.second);
		}

		assertThat(actual.string_values.size(),
				is(expected.string_values.size()));
		for (TupleStringString string_value : actual.string_values) {
			assertThat(string_value.first, isIn(expected_string_value_1));
			assertThat(string_value.second, isIn(expected_string_value_2));
		}

		assertThat(actual.num_values.size(), is(expected.num_values.size()));
		for (TupleStringDouble num_value : actual.num_values) {
			assertThat(num_value.first, isIn(expected_num_value_1));
			assertThat(num_value.second, isIn(expected_num_value_2));
		}
	}

}
