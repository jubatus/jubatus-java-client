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

// This file is auto-generated from ./jubatus/jubatus.github/src/server//classifier.idl
// *** DO NOT EDIT ***


package us.jubat.classifier;

import java.util.Map;
import java.util.List;
import org.msgpack.rpc.Client;
import org.msgpack.rpc.loop.EventLoop;

public class ClassifierClient {
  public ClassifierClient(String host, int port, double timeout_sec) throws Exception {
    EventLoop loop = EventLoop.defaultEventLoop();
    c_ = new Client(host, port, loop);
    iface_ = c_.proxy(RPCInterface.class);
  }

  public static interface RPCInterface {
    boolean set_config(String name, ConfigData  c);
    ConfigData  get_config(String name);
    int train(String name, List<TupleStringDatum > data);
    List<List<EstimateResult  > > classify(String name, List<Datum  > data);
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

  public int train(String name, List<TupleStringDatum > data) {
    return iface_.train(name, data);
  }

  public List<List<EstimateResult  > > classify(String name, List<Datum  > data) {
    return iface_.classify(name, data);
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


