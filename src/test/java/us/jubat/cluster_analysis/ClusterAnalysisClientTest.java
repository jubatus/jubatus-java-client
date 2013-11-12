package us.jubat.cluster_analysis;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.msgpack.rpc.Client;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import us.jubat.common.Datum;
import us.jubat.testutil.JubaServer;
import us.jubat.testutil.JubatusClientTest;
import us.jubat.clustering.ClusteringClient;

public class ClusterAnalysisClientTest extends JubatusClientTest {
	private ClusterAnalysisClient client;
	private ClusteringClient client_clustering;
	private JubaServer server_clustering;
  private String temporal_config_path;  

	public ClusterAnalysisClientTest() {
		super(JubaServer.cluster_analysis);
    server_clustering = JubaServer.clustering;
	}

	@Before
	public void setUp() throws Exception {
		server_clustering.start(server_clustering.getConfigPath());

    // Change config based on clustering server setting
    BufferedReader br = new BufferedReader(new InputStreamReader(
      new FileInputStream(server.getConfigPath())));
    JSONObject json = (JSONObject) JSONValue.parse(br);
    json.put("host", server_clustering.getHost());
    json.put("port", server_clustering.getPort());
    json.put("name", NAME);
    temporal_config_path = server.getConfigPath() + ".temp.json";
    try {
      FileOutputStream os = new FileOutputStream(temporal_config_path);
      OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
      writer.write(json.toString());
      writer.flush();
      writer.close();
      os.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
		server.start(temporal_config_path);

		client_clustering = new ClusteringClient(server_clustering.getHost(), server_clustering.getPort(), NAME,
				TIMEOUT_SEC);
		client = new ClusterAnalysisClient(server.getHost(), server.getPort(), NAME,
				TIMEOUT_SEC);
    for (int i = 0; i < 100; i++) {
      List<Datum> data_list = new ArrayList<Datum>();
      data_list.add(generateDatum());
      client_clustering.push(data_list);
    }
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
	public void testSave_and_load() {
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

	@Test
	public void testAdd_snapshot() {
		assertThat(client.addSnapshot("snap"), is(true));
	}

	@Test
	public void testGet_history() {
    client.addSnapshot("snap1");
    client.addSnapshot("snap2");
    client.addSnapshot("snap3");
    List<ChangeGraph> history = client.getHistory();
    assertThat(history.size(), is(2));
    assertThat(history.get(0), instanceOf(ChangeGraph.class));
    assertThat(history.get(0).snapshotName1, is("snap1"));
    assertThat(history.get(0).snapshotName2, is("snap2"));
    assertThat(history.get(1).snapshotName1, is("snap2"));
    assertThat(history.get(1).snapshotName2, is("snap3"));
	}

	@Test
	public void testGet_snapshots() {
    client.addSnapshot("snap1");
    client.addSnapshot("snap2");
    List<ClusteringSnapshot> shots = client.getSnapshots();
    assertThat(shots.size(), is(2));
    assertThat(shots.get(0), instanceOf(ClusteringSnapshot.class));
    assertThat(shots.get(0).name, is("snap1"));
    assertThat(shots.get(1).name, is("snap2"));
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
