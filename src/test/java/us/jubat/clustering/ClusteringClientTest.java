package us.jubat.clustering;

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

import us.jubat.common.Datum;
import us.jubat.testutil.JubaServer;
import us.jubat.testutil.JubatusClientTest;

public class ClusteringClientTest extends JubatusClientTest {
	private ClusteringClient client;

	public ClusteringClientTest() {
		super(JubaServer.clustering);
	}

	@Before
	public void setUp() throws Exception {
		server.start(server.getConfigPath());
		client = new ClusteringClient(server.getHost(), server.getPort(), NAME,
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
	public void testSave_and_load() {
		String id = "clustering.test_java-client.model";
		Map<String, String> saveResult = client.save(id);
		assertThat(saveResult, is(notNullValue()));
		assertThat(saveResult.size(), is(1));
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

	@Test
	public void testPush() {
		List<IndexedPoint> data_list = new ArrayList<IndexedPoint>();
		Datum datum = new Datum();
		data_list.add(new IndexedPoint("test", datum));
		assertThat(client.push(data_list), is(true));
	}

	@Test
	public void testGet_revision() {
		assertThat(client.getRevision(), is(instanceOf(Integer.class)));
	}

	@Test
	public void testGet_core_members() {
		for (int i = 0; i < 100; i++) {
			List<IndexedPoint> data_list = new ArrayList<IndexedPoint>();
			data_list.add(new IndexedPoint(String.valueOf(i), generateDatum(i)));
			client.push(data_list);
		}
		List<List<WeightedDatum>> members = client.getCoreMembers();
		assertThat(members.size(), is(10));
		assertThat(members.get(0).get(0), instanceOf(WeightedDatum.class));
	}

	@Test
	public void testGet_core_members_light() {
		for (int i = 0; i < 100; i++) {
			List<IndexedPoint> data_list = new ArrayList<IndexedPoint>();
			data_list.add(new IndexedPoint(String.valueOf(i), generateDatum(i)));
			client.push(data_list);
		}
		List<List<WeightedIndex>> members = client.getCoreMembersLight();
		assertThat(members.size(), is(10));
		assertThat(members.get(0).get(0), instanceOf(WeightedIndex.class));
	}

	@Test
	public void testGet_k_center() {
		for (int i = 0; i < 100; i++) {
			List<IndexedPoint> data_list = new ArrayList<IndexedPoint>();
			data_list.add(new IndexedPoint(String.valueOf(i), generateDatum(i)));
			client.push(data_list);
		}
		List<Datum> centers = client.getKCenter();
		assertThat(centers.size(), is(10));
		assertThat(centers.get(0), instanceOf(Datum.class));
	}

	@Test
	public void testGet_nearest_center() {
		for (int i = 0; i < 100; i++) {
			List<IndexedPoint> data_list = new ArrayList<IndexedPoint>();
			data_list.add(new IndexedPoint(String.valueOf(i), generateDatum(i)));
			client.push(data_list);
		}

		Datum datum = new Datum();
		datum.addNumber("nkey1", 2.0);
		datum.addNumber("nkey2", 1.0);

		Datum center = client.getNearestCenter(datum);
		assertThat(center, instanceOf(Datum.class));
	}

	@Test
	public void testGet_nearest_members() {
		for (int i = 0; i < 100; i++) {
			List<IndexedPoint> data_list = new ArrayList<IndexedPoint>();
			data_list.add(new IndexedPoint(String.valueOf(i), generateDatum(i)));
			client.push(data_list);
		}

		Datum datum = new Datum();
		datum.addNumber("nkey1", 2.0);
		datum.addNumber("nkey2", 1.0);

		List<WeightedDatum> members = client.getNearestMembers(datum);
		assertThat(members.get(0), instanceOf(WeightedDatum.class));
	}

	@Test
	public void testGet_nearest_members_light() {
		for (int i = 0; i < 100; i++) {
			List<IndexedPoint> data_list = new ArrayList<IndexedPoint>();
			data_list.add(new IndexedPoint(String.valueOf(i), generateDatum(i)));
			client.push(data_list);
		}

		Datum datum = new Datum();
		datum.addNumber("nkey1", 2.0);
		datum.addNumber("nkey2", 1.0);

		List<WeightedIndex> members = client.getNearestMembersLight(datum);
		assertThat(members.get(0), instanceOf(WeightedIndex.class));
	}

	private Datum generateDatum(int i) {
		Datum datum = new Datum();

		datum.addNumber("nkey1", i);
		datum.addNumber("nkey2", -1.0 * i);

		return datum;
	}
}
