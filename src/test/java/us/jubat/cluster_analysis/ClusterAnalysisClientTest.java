package us.jubat.cluster_analysis;

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
import us.jubat.clustering.ClusteringClient;

public class ClusterAnalysisClientTest extends JubatusClientTest {
	private ClusterAnalysisClient client;
	private ClusteringClient client_clustering;
	private JubaServer server_clustering;

    private final int port_clustering = 21998;
  private final String name_clustering = "clustering";

	public ClusterAnalysisClientTest() {
		super(JubaServer.cluster_analysis);
    server_clustering = JubaServer.clustering;
	}

	@Before
	public void setUp() throws Exception {
		server_clustering.start(server_clustering.getConfigPath());
		server.start(server.getConfigPath());
		client_clustering = new ClusteringClient(server_clustering.getHost(), port_clustering, name_clustering,
				TIMEOUT_SEC);
		client = new ClusterAnalysisClient(server.getHost(), server.getPort(), NAME,
				TIMEOUT_SEC);
	}

	@After
	public void tearDown() throws Exception {
    server_clustering.stop();
		server.stop();
	}

	@Test
	public void testGet_config() throws IOException {
		String config = client.getConfig();
		assertThat(formatAsJson(config),
				is(formatAsJson(server.getConfigData())));
	}

	@Test
	public void testSave_and_Load() {
		String id = "cluster_analysis.test_java-client.model";
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
