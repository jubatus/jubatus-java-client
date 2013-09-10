package us.jubat.recommender;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
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

public class RecommenderClientTest extends JubatusClientTest {
	private RecommenderClient client;

	public RecommenderClientTest() {
		super(JubaServer.recommender);
	}

	@Before
	public void setUp() throws Exception {
		server.start(server.getConfigPath());
		client = new RecommenderClient(server.getHost(), server.getPort(), NAME,
				TIMEOUT_SEC);
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	@Test
	public void testGet_config() throws IOException {
		String config = client.get_config();
		assertThat(formatAsJson(config),
				is(formatAsJson(server.getConfigData())));
	}

	@Test
	public void testUpdate_row() {
		assertThat(
				client.update_row(Integer.toString(1), generateDatum()),
				is(true));
	}

	@Test
	public void testDecode_row() {
		update_row(1);
		Datum datum = client.decode_row(Integer.toString(1));
		assertDatum(generateDatum(), datum);
	}

	@Test
	public void testClear_row() {
		update_row(1);
		assertThat(client.clear_row(Integer.toString(1)), is(true));

		Datum datum = client.decode_row(Integer.toString(1));
		assertThat(datum.stringValues.size(), is(0));
		assertThat(datum.numValues.size(), is(0));
	}

	@Test
	public void testClear_and_Get_all_rows() {
		update_row(3);
		assertThat(client.get_all_rows().size(), is(3));
		assertThat(client.clear(), is(true));
		assertThat(client.get_all_rows().size(), is(0));
	}

	@Test
	public void testComplete_row_from_id() {
		update_row(1);
		Datum result = client.complete_row_from_id(Integer.toString(1));

		Datum row = client.decode_row(Integer.toString(1));
		assertDatum(row, result);
	}

	@Test
	public void testComplete_row_from_datum() {
		update_row(1);
		Datum result = client.complete_row_from_datum(generateDatum());

		Datum row = client.decode_row(Integer.toString(1));
		assertDatum(row, result);
	}

	@Test
	public void testSimilar_row_from_id() {
		update_row(1);
		List<IdWithScore> result = client.similar_row_from_id(
				Integer.toString(1), 1);
		assertThat(result, is(notNullValue()));
		assertThat(result.size(), is(1));
		assertThat(result.get(0).id, is("1"));
		assertThat(result.get(0).score, is(1f));
	}

	@Test
	public void testSimilar_row_from_datum() {
		update_row(1);
		List<IdWithScore> result = client.similar_row_from_datum(
				generateDatum(), 1);
		assertThat(result, is(notNullValue()));
		assertThat(result.size(), is(1));
		assertThat(result.get(0).id, is("1"));
		assertThat(result.get(0).score, is(1f));
	}

	@Test
	public void testCalc_Similarity() {
		assertThat(
				client.calc_similarity(generateDatum(), generateDatum()),
				is(1f));
	}

	@Test
	public void testCalc_L2norm() {
		double norm = client.calc_l2norm(generateDatum());
		assertThat(norm, is(closeTo(19.874607, 0.00001)));
	}

	@Test
	public void testSave_and_Load() {
		String id = "recommender.test_java-client.model";
		assertThat(client.save(id), is(true));
		assertThat(client.load(id), is(true));
	}

	@Test
	public void testGet_status() {
		Map<String, Map<String, String>> status = client.get_status();
		assertThat(status, is(notNullValue()));
		assertThat(status.size(), is(1));
	}

	@Test
	public void testGet_client() {
		assertThat(client.get_client(), is(instanceOf(Client.class)));
		assertThat(client.get_client(), is(notNullValue()));
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

	private void update_row(int number) {
		for (int i = 1; i <= number; i++) {
			client.update_row(Integer.toString(i), generateDatum());
		}
	}

	private void assertDatum(Datum actual, Datum expected) {
		List<String> expected_string_value_1 = new ArrayList<String>();
		List<String> expected_string_value_2 = new ArrayList<String>();
		for (Datum.StringValue string_value : expected.getStringValues()) {
			expected_string_value_1.add(string_value.key);
			expected_string_value_2.add(string_value.value);
		}

		List<String> expected_num_value_1 = new ArrayList<String>();
		List<Double> expected_num_value_2 = new ArrayList<Double>();
		for (Datum.NumValue num_value : expected.numValues) {
			expected_num_value_1.add(num_value.key);
			expected_num_value_2.add(num_value.value);
		}

		assertThat(actual.stringValues.size(),
				is(expected.stringValues.size()));
		for (Datum.StringValue string_value : actual.getStringValues()) {
			assertThat(string_value.key, isIn(expected_string_value_1));
			assertThat(string_value.value, isIn(expected_string_value_2));
		}

		assertThat(actual.numValues.size(), is(expected.numValues.size()));
		for (Datum.NumValue num_value : actual.getNumValues()) {
			assertThat(num_value.key, isIn(expected_num_value_1));
			assertThat(num_value.value, isIn(expected_num_value_2));
		}
	}
}
