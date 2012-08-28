/*
 * 
 * Copyright (c) 2012 Preferred Infrastructure, inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */

// This file is auto-generated from ./jubatus/jubatus.github/src/server//graph.idl
// *** DO NOT EDIT ***


package us.jubat.graph;

import java.util.Map;
import java.util.List;
import org.msgpack.rpc.Client;
import org.msgpack.rpc.loop.EventLoop;

public class GraphClient {
  public GraphClient(String host, int port, double timeout_sec) throws Exception {
    EventLoop loop = EventLoop.defaultEventLoop();
    c_ = new Client(host, port, loop);
    iface_ = c_.proxy(RPCInterface.class);
  }

  public static interface RPCInterface {
    String create_node(String name);
    int remove_node(String name, String nid);
    int update_node(String name, String nid, Map<String, String > p);
    long create_edge(String name, String nid, EdgeInfo  ei);
    int update_edge(String name, String nid, long eid, EdgeInfo  ei);
    int remove_edge(String name, String nid, long e);
    double centrality(String name, String nid, int ct, PresetQuery  q);
    boolean add_centrality_query(String name, PresetQuery  q);
    boolean add_shortest_path_query(String name, PresetQuery  q);
    boolean remove_centrality_query(String name, PresetQuery  q);
    boolean remove_shortest_path_query(String name, PresetQuery  q);
    List<String > shortest_path(String name, ShortestPathReq  r);
    int update_index(String name);
    int clear(String name);
    NodeInfo  get_node(String name, String nid);
    EdgeInfo  get_edge(String name, String nid, long e);
    boolean save(String name, String arg1);
    boolean load(String name, String arg1);
    Map<String, Map<String, String > > get_status(String name);
    int create_node_here(String name, String nid);
    int create_global_node(String name, String nid);
    int remove_global_node(String name, String nid);
    int create_edge_here(String name, long eid, EdgeInfo  ei);

  }


  public String create_node(String name) {
    return iface_.create_node(name);
  }

  public int remove_node(String name, String nid) {
    return iface_.remove_node(name, nid);
  }

  public int update_node(String name, String nid, Map<String, String > p) {
    return iface_.update_node(name, nid, p);
  }

  public long create_edge(String name, String nid, EdgeInfo  ei) {
    return iface_.create_edge(name, nid, ei);
  }

  public int update_edge(String name, String nid, long eid, EdgeInfo  ei) {
    return iface_.update_edge(name, nid, eid, ei);
  }

  public int remove_edge(String name, String nid, long e) {
    return iface_.remove_edge(name, nid, e);
  }

  public double centrality(String name, String nid, int ct, PresetQuery  q) {
    return iface_.centrality(name, nid, ct, q);
  }

  public boolean add_centrality_query(String name, PresetQuery  q) {
    return iface_.add_centrality_query(name, q);
  }

  public boolean add_shortest_path_query(String name, PresetQuery  q) {
    return iface_.add_shortest_path_query(name, q);
  }

  public boolean remove_centrality_query(String name, PresetQuery  q) {
    return iface_.remove_centrality_query(name, q);
  }

  public boolean remove_shortest_path_query(String name, PresetQuery  q) {
    return iface_.remove_shortest_path_query(name, q);
  }

  public List<String > shortest_path(String name, ShortestPathReq  r) {
    return iface_.shortest_path(name, r);
  }

  public int update_index(String name) {
    return iface_.update_index(name);
  }

  public int clear(String name) {
    return iface_.clear(name);
  }

  public NodeInfo  get_node(String name, String nid) {
    return iface_.get_node(name, nid);
  }

  public EdgeInfo  get_edge(String name, String nid, long e) {
    return iface_.get_edge(name, nid, e);
  }

  public boolean save(String name, String arg1) {
    return iface_.save(name, arg1);
  }

  public boolean load(String name, String arg1) {
    return iface_.load(name, arg1);
  }

  public Map<String, Map<String, String > > get_status(String name) {
    return iface_.get_status(name);
  }

  public int create_node_here(String name, String nid) {
    return iface_.create_node_here(name, nid);
  }

  public int create_global_node(String name, String nid) {
    return iface_.create_global_node(name, nid);
  }

  public int remove_global_node(String name, String nid) {
    return iface_.remove_global_node(name, nid);
  }

  public int create_edge_here(String name, long eid, EdgeInfo  ei) {
    return iface_.create_edge_here(name, eid, ei);
  }

  private Client c_;
  private RPCInterface iface_;
};


