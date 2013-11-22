package us.jubat.nearest_neighbor;

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

public class NearestNeighborClientTest extends JubatusClientTest {
	private NearestNeighborClient client;

	public NearestNeighborClientTest() {
		super(JubaServer.nearest_neighbor);
	}

	@Before
	public void setUp() throws Exception {
		server.start(server.getConfigPath());
		client = new NearestNeighborClient(server.getHost(), server.getPort(),
				NAME, TIMEOUT_SEC);
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
	public void testNeighbor_row() {
		client.clear();
		Datum d = new Datum().addString("skey1", "val1")
				.addString("skey2", "val2").addNumber("nkey1", 1.0)
				.addNumber("nkey2", 2.0);
		client.setRow("neighbor_row", d);
		client.neighborRowFromId("neighbor_row", 10);
		client.neighborRowFromData(d, 10);
	}

	@Test
	public void testSimilar_row() {
		client.clear();
		Datum d = new Datum().addString("skey1", "val1")
				.addString("skey2", "val2").addNumber("nkey1", 1.0)
				.addNumber("nkey2", 2.0);
		client.setRow("similar_row", d);
		client.similarRowFromId("similar_row", 10);
		client.similarRowFromData(d, 10);
	}

	@Test
	public void testClaer() {
		client.clear();
	}

	@Test
	public void testSave_and_Load() {
		String id = "nearest_neighbor.test_java-client.model";
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
}
