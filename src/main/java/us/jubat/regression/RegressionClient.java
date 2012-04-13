
// This file is auto-generated from ../src/server/regression.idl
// *** DO NOT EDIT ***



package us.jubat.regression;
import java.util.HashMap;
import java.util.ArrayList;
import org.msgpack.rpc.Client;
import org.msgpack.rpc.loop.EventLoop;

public class RegressionClient {
  public RegressionClient(String host, int port, double timeout_sec) throws Exception {
    EventLoop loop = EventLoop.defaultEventLoop();
    c_ = new Client(host, port, loop);
    iface_ = c_.proxy(RPCInterface.class);
  }

  public static interface RPCInterface {
    boolean set_config(String name, ConfigData  c);
    ConfigData  get_config(String name);
    int train(String arg0, ArrayList<Tuple<Float, Datum  > > arg1);
    ArrayList<Float > estimate(String arg0, ArrayList<Datum  > arg1);
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

  public int train(String arg0, ArrayList<Tuple<Float, Datum  > > arg1) {
    return iface_.train(arg0, arg1);
  }

  public ArrayList<Float > estimate(String arg0, ArrayList<Datum  > arg1) {
    return iface_.estimate(arg0, arg1);
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


