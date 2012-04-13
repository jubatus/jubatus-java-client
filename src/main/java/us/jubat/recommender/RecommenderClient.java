
// This file is auto-generated from ../src/server/recommender.idl
// *** DO NOT EDIT ***



package us.jubat.recommender;

import java.util.HashMap;
import java.util.ArrayList;
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
    boolean update_row(String name, String id, Datum  arg2);
    boolean clear(String name);
    Datum  complete_row_from_id(String name, String id);
    Datum  complete_row_from_data(String name, Datum  arg1);
    ArrayList<Tuple<String, Float > > similar_row_from_id(String name, String id, int size);
    ArrayList<Tuple<String, Float > > similar_row_from_data(String name, Datum  data, int size);
    Datum  decode_row(String name, String id);
    ArrayList<String > get_all_rows(String name);
    float similarity(String name, Datum  lhs, Datum  rhs);
    float l2norm(String name, Datum  arg1);
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

  public boolean clear_row(String name, String id) {
    return iface_.clear_row(name, id);
  }

  public boolean update_row(String name, String id, Datum  arg2) {
    return iface_.update_row(name, id, arg2);
  }

  public boolean clear(String name) {
    return iface_.clear(name);
  }

  public Datum  complete_row_from_id(String name, String id) {
    return iface_.complete_row_from_id(name, id);
  }

  public Datum  complete_row_from_data(String name, Datum  arg1) {
    return iface_.complete_row_from_data(name, arg1);
  }

  public ArrayList<Tuple<String, Float > > similar_row_from_id(String name, String id, int size) {
    return iface_.similar_row_from_id(name, id, size);
  }

  public ArrayList<Tuple<String, Float > > similar_row_from_data(String name, Datum  data, int size) {
    return iface_.similar_row_from_data(name, data, size);
  }

  public Datum  decode_row(String name, String id) {
    return iface_.decode_row(name, id);
  }

  public ArrayList<String > get_all_rows(String name) {
    return iface_.get_all_rows(name);
  }

  public float similarity(String name, Datum  lhs, Datum  rhs) {
    return iface_.similarity(name, lhs, rhs);
  }

  public float l2norm(String name, Datum  arg1) {
    return iface_.l2norm(name, arg1);
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


