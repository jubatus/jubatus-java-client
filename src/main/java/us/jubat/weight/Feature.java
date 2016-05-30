// This file is auto-generated from weight.idl(0.9.0-24-gda61383) with jenerator version 0.8.5-6-g5a2c923/master
// *** DO NOT EDIT ***

package us.jubat.weight;

import org.msgpack.annotation.Message;
import org.msgpack.type.ArrayValue;
import us.jubat.common.MessageStringGenerator;
import us.jubat.common.UserDefinedMessage;
import us.jubat.common.type.*;

@Message
public class Feature implements UserDefinedMessage {
  public String key = "";
  public float value = 0;

  public Feature() {
  }

  public Feature(String key, float value) {
    this.key = key;
    this.value = value;
  }

  public void check() {
    TString.instance.check(key);
  }

  public String toString() {
    MessageStringGenerator gen = new MessageStringGenerator();
    gen.open("feature");
    gen.add("key", key);
    gen.add("value", value);
    gen.close();
    return gen.toString();
  }

  public static class Type extends TUserDef<Feature> {
    public static Type instance = new Type();

    private Type() {
      super(2);
    }

    @Override
    public Feature create(ArrayValue value) {
      return new Feature(TString.instance.revert(value.get(0)),
          TFloat.instance.revert(value.get(1)));
    }
  }
};
