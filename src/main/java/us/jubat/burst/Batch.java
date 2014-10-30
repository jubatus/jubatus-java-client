// This file is auto-generated from burst.idl(0.6.1-34-gb64049d) with jenerator version 0.5.4-224-g49229fa/master
// *** DO NOT EDIT ***

package us.jubat.burst;

import org.msgpack.annotation.Message;
import org.msgpack.type.ArrayValue;
import us.jubat.common.MessageStringGenerator;
import us.jubat.common.UserDefinedMessage;
import us.jubat.common.type.*;

@Message
public class Batch implements UserDefinedMessage {
  public int allDataCount = 0;
  public int relevantDataCount = 0;
  public double burstWeight = 0;

  public Batch() {
  }

  public Batch(int allDataCount, int relevantDataCount, double burstWeight) {
    this.allDataCount = allDataCount;
    this.relevantDataCount = relevantDataCount;
    this.burstWeight = burstWeight;
  }

  public void check() {
  }

  public String toString() {
    MessageStringGenerator gen = new MessageStringGenerator();
    gen.open("batch");
    gen.add("all_data_count", allDataCount);
    gen.add("relevant_data_count", relevantDataCount);
    gen.add("burst_weight", burstWeight);
    gen.close();
    return gen.toString();
  }

  public static class Type extends TUserDef<Batch> {
    public static Type instance = new Type();

    private Type() {
      super(3);
    }

    @Override
    public Batch create(ArrayValue value) {
      return new Batch(TInt.instance.revert(value.get(0)), TInt.instance.revert(
          value.get(1)), TDouble.instance.revert(value.get(2)));
    }
  }
};
