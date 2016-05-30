// This file is auto-generated from weight.idl(0.9.0-24-gda61383) with jenerator version 0.8.5-6-g5a2c923/master
// *** DO NOT EDIT ***

package us.jubat.weight;

import java.util.List;
import us.jubat.common.Datum;

import java.net.UnknownHostException;
import us.jubat.common.ClientBase;
import us.jubat.common.type.*;

public class WeightClient extends ClientBase {

  public WeightClient(String host, int port, String name,
      int timeoutSec) throws UnknownHostException {
    super(host, port, name, timeoutSec);
  }

  public List<Feature> update(Datum d) {
    TDatum.instance.check(d);
    return this.call("update", new TList<Feature>(Feature.Type.instance), d);
  }

  public List<Feature> calcWeight(Datum d) {
    TDatum.instance.check(d);
    return this.call("calc_weight", new TList<Feature>(Feature.Type.instance),
        d);
  }

  public boolean clear() {
    return this.call("clear", TBool.instance);
  }

}
