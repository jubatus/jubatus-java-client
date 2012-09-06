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

// This file is auto-generated from ./jubatus/jubatus.github/src/server//recommender.idl
// *** DO NOT EDIT ***


package us.jubat.recommender;

import java.util.Map;
import java.util.List;
import org.msgpack.rpc.Client;
import org.msgpack.rpc.loop.EventLoop;

public class RecommenderClient {
  public RecommenderClient(String host, int port, double timeout_sec) throws Exception {
    EventLoop loop = EventLoop.defaultEventLoop();
    c_ = new Client(host, port, loop);
    iface_ = c_.proxy(RPCInterface.class);
  }

  public static interface RPCInterface {
    boolean set_config(String name, ConfigData  c);
    ConfigData  get_config(String name);
    boolean clear_row(String name, String id);
    boolean update_row(String name, String id, Datum  d);
    boolean clear(String name);
    Datum  complete_row_from_id(String name, String id);
    Datum  complete_row_from_data(String name, Datum  d);
    List<TupleStringFloat > similar_row_from_id(String name, String id, int size);
    List<TupleStringFloat > similar_row_from_data(String name, Datum  data, int size);
    Datum  decode_row(String name, String id);
    List<String > get_all_rows(String name);
    float similarity(String name, Datum  lhs, Datum  rhs);
    float l2norm(String name, Datum  d);
    boolean save(String name, String id);
    boolean load(String name, String id);
    Map<String, Map<String, String > > get_status(String name);

  }


  public boolean set_config(String name, ConfigData  c) {
    return iface_.set_config(name, c);
  }

  public ConfigData  get_config(String name) {
    return iface_.get_config(name);
  }

  public boolean clear_row(String name, String id) {
    return iface_.clear_row(name, id);
  }

  public boolean update_row(String name, String id, Datum  d) {
    return iface_.update_row(name, id, d);
  }

  public boolean clear(String name) {
    return iface_.clear(name);
  }

  public Datum  complete_row_from_id(String name, String id) {
    return iface_.complete_row_from_id(name, id);
  }

  public Datum  complete_row_from_data(String name, Datum  d) {
    return iface_.complete_row_from_data(name, d);
  }

  public List<TupleStringFloat > similar_row_from_id(String name, String id, int size) {
    return iface_.similar_row_from_id(name, id, size);
  }

  public List<TupleStringFloat > similar_row_from_data(String name, Datum  data, int size) {
    return iface_.similar_row_from_data(name, data, size);
  }

  public Datum  decode_row(String name, String id) {
    return iface_.decode_row(name, id);
  }

  public List<String > get_all_rows(String name) {
    return iface_.get_all_rows(name);
  }

  public float similarity(String name, Datum  lhs, Datum  rhs) {
    return iface_.similarity(name, lhs, rhs);
  }

  public float l2norm(String name, Datum  d) {
    return iface_.l2norm(name, d);
  }

  public boolean save(String name, String id) {
    return iface_.save(name, id);
  }

  public boolean load(String name, String id) {
    return iface_.load(name, id);
  }

  public Map<String, Map<String, String > > get_status(String name) {
    return iface_.get_status(name);
  }

  private Client c_;
  private RPCInterface iface_;
};


