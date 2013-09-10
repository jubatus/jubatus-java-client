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
		String config = client.get_config();
		assertThat(formatAsJson(config),
				is(formatAsJson(server.getConfigData())));
	}

	@Test
	public void testNode() {
		// create
		String nid = client.create_node();
		assertThat(nid, is("0"));

		// update
		Map<String, String> property = new HashMap<String, String>();
		for (int i = 1; i <= 10; i++) {
			String key = "key" + Integer.toString(i);
			String value = "value" + Integer.toString(i);
			property.put(key, value);
		}
		assertThat(client.update_node(nid, property), is(true));

		// get
		Node node = client.get_node(nid);
		assertThat(node.in_edges, is(notNullValue()));
		assertThat(node.in_edges.size(), is(0));
		assertThat(node.out_edges, is(notNullValue()));
		assertThat(node.out_edges.size(), is(0));
		assertThat(node.property, is(property));

		// create here
		assertThat(client.create_node_here(nid + 1), is(true));

		// remove
		assertThat(client.remove_node(nid), is(true));
	}

	@Test
	public void testEdge() {
		// create
		String src = client.create_node();
		String tgt = client.create_node();
		Map<String, String> property = new HashMap<String, String>();
		for (int i = 1; i <= 10; i++) {
			String key = "key" + Integer.toString(i);
			String value = "value" + Integer.toString(i);
			property.put(key, value);
		}
		client.update_node(src, property);
		client.update_node(tgt, property);
		Edge e = new Edge(property, src, tgt);
		long eid = client.create_edge(src, e);
		assertThat(Long.valueOf(eid), is(2L));

		// update
		assertThat(client.update_edge(src, eid, e), is(true));

		// get
		Edge edge = client.get_edge(tgt, eid);
		assertThat(edge.source, is(src));
		assertThat(edge.target, is(tgt));
		assertThat(edge.property, is(property));

		// create here
		assertThat(client.create_edge_here(eid + 1, e), is(true));

		// remove
		assertThat(client.remove_edge(src, eid), is(true));
	}

	@Test
	public void testGet_centrality() {
		PresetQuery q = new PresetQuery();

		client.add_centrality_query(q);
		String nid = client.create_node();
		client.update_index();

		assertThat(client.get_centrality(nid, 0, q), is(1.0));
	}

	@Test
	public void testAdd_centrality_query() {
		Query s = new Query("", "");
		PresetQuery q = new PresetQuery();
		q.node_query.add(s);
		q.edge_query.add(s);
		assertThat(client.add_centrality_query(q), is(true));
	}

	@Test
	public void testAdd_shortest_path_query() {
		PresetQuery q = new PresetQuery();
		assertThat(client.add_shortest_path_query(q), is(true));
	}

	@Test
	public void testRemove_centrality_query() {
		PresetQuery q = new PresetQuery();
		assertThat(client.remove_centrality_query(q), is(true));
	}

	@Test
	public void testRemove_shortest_path_query() {
		PresetQuery q = new PresetQuery();
		assertThat(client.remove_shortest_path_query(q), is(true));
	}

	@Test
	public void testGet_shortest_path() {
		PresetQuery q = new PresetQuery();

		client.add_shortest_path_query(q);

		String src = client.create_node();
		String tgt = client.create_node();
		ShortestPathQuery r = new ShortestPathQuery(src, tgt, 0, q);

		List<String> result = client.get_shortest_path(r);
		assertThat(result, is(notNullValue()));
		assertThat(result.size(), is(0));
	}

	@Test
	public void testUpdate_index() {
		assertThat(client.update_index(), is(true));
	}

	@Test
	public void testClear() {
		assertThat(client.clear(), is(true));
	}

	@Test
	public void testSave_and_Load() {
		String id = "graph.test_java-client.model";
		assertThat(client.save(id), is(true));
		assertThat(client.load(id), is(true));
	}

	@Test
	public void testGet_status() {
		Map<String, Map<String, String>> status = client.get_status();

		assertThat(status, is(not(nullValue())));
		assertThat(status.size(), is(1));
	}

	@Test
	public void testGlobal_node() {
		String nid = client.create_node();
		assertThat(client.remove_global_node(nid), is(true));
	}

	@Test
	public void testGet_client() {
		assertThat(client.get_client(), is(instanceOf(Client.class)));
		assertThat(client.get_client(), is(notNullValue()));
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
