
// This file is auto-generated from ../src/server/classifier.idl
// *** DO NOT EDIT ***



package us.jubat.classifier;
import java.util.HashMap;
import java.util.ArrayList;

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
    int train(String name, ArrayList<Tuple<String, Datum  > > data);
    ArrayList<ArrayList<EstimateResult  > > classify(String name, ArrayList<Datum  > data);
    boolean save(String name, String arg1);
    boolean load(String name, String arg1);
    HashMap<String, HashMap<String, String > > get_status(String name);

  }


  public boolean set_config(String name, ConfigData  c) {
    return iface_.set_config(name, c);
  }

  public ConfigData  get_config(String name) {
    return iface_.get_config(name);
  }

  public int train(String name, ArrayList<Tuple<String, Datum  > > data) {
    return iface_.train(name, data);
  }

  public ArrayList<ArrayList<EstimateResult  > > classify(String name, ArrayList<Datum  > data) {
    return iface_.classify(name, data);
  }

  public boolean save(String name, String arg1) {
    return iface_.save(name, arg1);
  }

  public boolean load(String name, String arg1) {
    return iface_.load(name, arg1);
  }

  public HashMap<String, HashMap<String, String > > get_status(String name) {
    return iface_.get_status(name);
  }

  private Client c_;
  private RPCInterface iface_;
};


