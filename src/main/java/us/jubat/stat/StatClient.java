
// This file is auto-generated from ../src/server/stat.idl
// *** DO NOT EDIT ***



package us.jubat.stat;
import java.util.HashMap;
import java.util.ArrayList;
import org.msgpack.rpc.Client;
import org.msgpack.rpc.loop.EventLoop;

public class StatClient {
  public StatClient(String host, int port, double timeout_sec) throws Exception {
    EventLoop loop = EventLoop.defaultEventLoop();
    c_ = new Client(host, port, loop);
    iface_ = c_.proxy(RPCInterface.class);
  }

  public static interface RPCInterface {
    boolean set_config(String name, ConfigData  c);
    ConfigData  get_config(String name);
    boolean push(String arg0, String arg1, double arg2);
    double sum(String arg0, String arg1);
    double stddev(String arg0, String arg1);
    double max(String arg0, String arg1);
    double min(String arg0, String arg1);
    double entropy(String arg0, String arg1);
    double moment(String arg0, String arg1, int n, double c);
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

  public boolean push(String arg0, String arg1, double arg2) {
    return iface_.push(arg0, arg1, arg2);
  }

  public double sum(String arg0, String arg1) {
    return iface_.sum(arg0, arg1);
  }

  public double stddev(String arg0, String arg1) {
    return iface_.stddev(arg0, arg1);
  }

  public double max(String arg0, String arg1) {
    return iface_.max(arg0, arg1);
  }

  public double min(String arg0, String arg1) {
    return iface_.min(arg0, arg1);
  }

  public double entropy(String arg0, String arg1) {
    return iface_.entropy(arg0, arg1);
  }

  public double moment(String arg0, String arg1, int n, double c) {
    return iface_.moment(arg0, arg1, n, c);
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


