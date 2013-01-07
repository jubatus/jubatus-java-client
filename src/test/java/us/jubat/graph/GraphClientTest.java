package us.jubat.graph;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
		client = new GraphClient(server.getHost(), server.getPort(),
				TIMEOUT_SEC);
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	@Test
	public void testNode() {
		// create
		String nid = client.create_node(NAME);
		assertThat(nid, is("0"));

		// update
		Map<String, String> property = new HashMap<String, String>();
		for (int i = 1; i <= 10; i++) {
			String key = "key" + Integer.toString(i);
			String value = "value" + Integer.toString(i);
			property.put(key, value);
		}
		assertThat(client.update_node(NAME, nid, property), is(true));

		// get
		Node node = client.get_node(NAME, nid);
		assertThat(node.in_edges, is(notNullValue()));
		assertThat(node.in_edges.size(), is(0));
		assertThat(node.out_edges, is(notNullValue()));
		assertThat(node.out_edges.size(), is(0));
		assertThat(node.property, is(property));

		// create here
		assertThat(client.create_node_here(NAME, nid + 1), is(true));

		// remove
		assertThat(client.remove_node(NAME, nid), is(true));
	}

	@Test
	public void testEdge() {
		// create
		String src = client.create_node(NAME);
		String tgt = client.create_node(NAME);
		Map<String, String> property = new HashMap<String, String>();
		for (int i = 1; i <= 10; i++) {
			String key = "key" + Integer.toString(i);
			String value = "value" + Integer.toString(i);
			property.put(key, value);
		}
		client.update_node(NAME, src, property);
		client.update_node(NAME, tgt, property);
		Edge e = new Edge();
		e.source = src;
		e.target = tgt;
		e.property = property;
		long eid = client.create_edge(NAME, src, e);
		assertThat(Long.valueOf(eid), is(2L));

		// update
		assertThat(client.update_edge(NAME, src, eid, e), is(true));

		// get
		Edge edge = client.get_edge(NAME, tgt, eid);
		assertThat(edge.source, is(src));
		assertThat(edge.target, is(tgt));
		assertThat(edge.property, is(property));

		// create here
		assertThat(client.create_edge_here(NAME, eid + 1, e), is(true));

		// remove
		assertThat(client.remove_edge(NAME, src, eid), is(true));
	}

	@Test
	public void testCentrality() {
		PresetQuery q = new PresetQuery();
		q.edge_query = new ArrayList<TupleStringString>();
		q.node_query = new ArrayList<TupleStringString>();

		client.add_centrality_query(NAME, q);
		String nid = client.create_node(NAME);
		client.update_index(NAME);

		assertThat(client.centrality(NAME, nid, 0, q), is(1.0));
	}

	@Test
	public void testAdd_centrality_query() {
		TupleStringString s = new TupleStringString();
		s.first = "";
		s.second = "";
		List<TupleStringString> query = new ArrayList<TupleStringString>();
		query.add(s);
		PresetQuery q = new PresetQuery();
		q.node_query = query;
		q.edge_query = query;
		assertThat(client.add_centrality_query(NAME, q), is(true));
	}

	@Test
	public void testAdd_shortest_path_query() {
		PresetQuery q = new PresetQuery();
		q.node_query = new ArrayList<TupleStringString>();
		q.edge_query = new ArrayList<TupleStringString>();
		assertThat(client.add_shortest_path_query(NAME, q), is(true));
	}

	@Test
	public void testRemove_centrality_query() {
		PresetQuery q = new PresetQuery();
		q.node_query = new ArrayList<TupleStringString>();
		q.edge_query = new ArrayList<TupleStringString>();
		assertThat(client.remove_centrality_query(NAME, q), is(true));
	}

	@Test
	public void testRemove_shortest_path_query() {
		PresetQuery q = new PresetQuery();
		q.node_query = new ArrayList<TupleStringString>();
		q.edge_query = new ArrayList<TupleStringString>();
		assertThat(client.remove_shortest_path_query(NAME, q), is(true));
	}

	@Test
	public void testShortest_path() {
		PresetQuery q = new PresetQuery();
		q.node_query = new ArrayList<TupleStringString>();
		q.edge_query = new ArrayList<TupleStringString>();

		client.add_shortest_path_query(NAME, q);

		String src = client.create_node(NAME);
		String tgt = client.create_node(NAME);
		ShortestPathQuery r = new ShortestPathQuery();
		r.source = src;
		r.target = tgt;
		r.query = q;
		r.max_hop = 0;

		List<String> result = client.get_shortest_path(NAME, r);
		assertThat(result, is(notNullValue()));
		assertThat(result.size(), is(0));
	}

	@Test
	public void testUpdate_index() {
		assertThat(client.update_index(NAME), is(true));
	}

	@Test
	public void testClear() {
		assertThat(client.clear(NAME), is(true));
	}

	@Test
	public void testSave_and_Load() {
		String id = "graph.test_java-client.model";
		assertThat(client.save(NAME, id), is(true));
		assertThat(client.load(NAME, id), is(true));
	}

	@Test
	public void testGet_status() {
		Map<String, Map<String, String>> status = client.get_status(NAME);

		assertThat(status, is(not(nullValue())));
		assertThat(status.size(), is(1));
	}

	@Test
	public void testGlobal_node() {
		String nid = client.create_node(NAME);
		assertThat(client.remove_global_node(NAME, nid), is(true));
	}

}
