package us.jubat.graph;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.msgpack.rpc.Client;

import us.jubat.testutil.JubaServer;
import us.jubat.testutil.JubatusClientTest;

public class GraphClientTest extends JubatusClientTest {
	private GraphClient client;

	public GraphClientTest() {
		super(JubaServer.graph);
	}

	@Before
	public void setUp() throws Exception {
		server.start(server.getConfigPath());
		client = new GraphClient(server.getHost(), server.getPort(), NAME,
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
	public void testNode() {
		// create
		String nid = client.createNode();
		assertThat(nid, is("0"));

		// update
		Map<String, String> property = new HashMap<String, String>();
		for (int i = 1; i <= 10; i++) {
			String key = "key" + Integer.toString(i);
			String value = "value" + Integer.toString(i);
			property.put(key, value);
		}
		assertThat(client.updateNode(nid, property), is(true));

		// get
		Node node = client.getNode(nid);
		assertThat(node.inEdges, is(notNullValue()));
		assertThat(node.inEdges.size(), is(0));
		assertThat(node.outEdges, is(notNullValue()));
		assertThat(node.outEdges.size(), is(0));
		assertThat(node.property, is(property));

		// create here
		assertThat(client.createNodeHere(nid + 1), is(true));

		// remove
		assertThat(client.removeNode(nid), is(true));
	}

	@Test
	public void testEdge() {
		// create
		String src = client.createNode();
		String tgt = client.createNode();
		Map<String, String> property = new HashMap<String, String>();
		for (int i = 1; i <= 10; i++) {
			String key = "key" + Integer.toString(i);
			String value = "value" + Integer.toString(i);
			property.put(key, value);
		}
		client.updateNode(src, property);
		client.updateNode(tgt, property);
		Edge e = new Edge(property, src, tgt);
		long eid = client.createEdge(src, e);
		assertThat(Long.valueOf(eid), is(2L));

		// update
		assertThat(client.updateEdge(src, eid, e), is(true));

		// get
		Edge edge = client.getEdge(tgt, eid);
		assertThat(edge.source, is(src));
		assertThat(edge.target, is(tgt));
		assertThat(edge.property, is(property));

		// create here
		assertThat(client.createEdgeHere(eid + 1, e), is(true));

		// remove
		assertThat(client.removeEdge(src, eid), is(true));
	}

	@Test
	public void testGet_centrality() {
		PresetQuery q = new PresetQuery();

		client.addCentralityQuery(q);
		String nid = client.createNode();
		client.updateIndex();

		assertThat(client.getCentrality(nid, 0, q), is(1.0));
	}

	@Test
	public void testAdd_centrality_query() {
		Query s = new Query("", "");
		PresetQuery q = new PresetQuery();
		q.nodeQuery.add(s);
		q.edgeQuery.add(s);
		assertThat(client.addCentralityQuery(q), is(true));
	}

	@Test
	public void testAdd_shortest_path_query() {
		PresetQuery q = new PresetQuery();
		assertThat(client.addShortestPathQuery(q), is(true));
	}

	@Test
	public void testRemove_centrality_query() {
		PresetQuery q = new PresetQuery();
		assertThat(client.removeCentralityQuery(q), is(true));
	}

	@Test
	public void testRemove_shortest_path_query() {
		PresetQuery q = new PresetQuery();
		assertThat(client.removeShortestPathQuery(q), is(true));
	}

	@Test
	public void testGet_shortest_path() {
		PresetQuery q = new PresetQuery();

		client.addShortestPathQuery(q);

		String src = client.createNode();
		String tgt = client.createNode();
		ShortestPathQuery r = new ShortestPathQuery(src, tgt, 0, q);

		List<String> result = client.getShortestPath(r);
		assertThat(result, is(notNullValue()));
		assertThat(result.size(), is(0));
	}

	@Test
	public void testUpdate_index() {
		assertThat(client.updateIndex(), is(true));
	}

	@Test
	public void testClear() {
		assertThat(client.clear(), is(true));
	}

	@Test
	public void testSave_and_Load() {
		String id = "graph.test_java-client.model";
		Map<String, String> saveResult = client.save(id);
		assertThat(saveResult, is(notNullValue()));
		assertThat(saveResult.size(), is(1));
		assertThat(client.load(id), is(true));
	}

	@Test
	public void testGet_status() {
		Map<String, Map<String, String>> status = client.getStatus();

		assertThat(status, is(not(nullValue())));
		assertThat(status.size(), is(1));
	}

	@Test
	public void testGlobal_node() {
		String nid = client.createNode();
		assertThat(client.removeGlobalNode(nid), is(true));
	}

	@Test
	public void testGet_client() {
		assertThat(client.getClient(), is(instanceOf(Client.class)));
		assertThat(client.getClient(), is(notNullValue()));
	}

	@Test
	public void testToString() {
		Node node = new Node();
		assertThat(node.toString(),
				is("node{property: {}, in_edges: [], out_edges: []}"));

		PresetQuery query = new PresetQuery();
		assertThat(query.toString(),
				is("preset_query{edge_query: [], node_query: []}"));

		Edge edge = new Edge();
		edge.source = "src";
		edge.target = "tgt";
		assertThat(edge.toString(),
				is("edge{property: {}, source: src, target: tgt}"));

		ShortestPathQuery path = new ShortestPathQuery("src", "tgt", 10, query);
		assertThat(
				path.toString(),
				is("shortest_path_query{source: src, target: tgt, max_hop: 10, query: preset_query{edge_query: [], node_query: []}}"));
	}
}
