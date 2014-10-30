// This file is auto-generated from burst.idl(0.6.1-34-gb64049d) with jenerator version 0.5.4-224-g49229fa/master
// *** DO NOT EDIT ***

package us.jubat.burst;

import java.util.List;
import java.util.ArrayList;
import org.msgpack.annotation.Message;
import org.msgpack.type.ArrayValue;
import us.jubat.common.MessageStringGenerator;
import us.jubat.common.UserDefinedMessage;
import us.jubat.common.type.*;

@Message
public class Window implements UserDefinedMessage {
  public double startPos = 0;
  public List<Batch> batches = new ArrayList<Batch>();

  public Window() {
  }

  public Window(double startPos, List<Batch> batches) {
    this.startPos = startPos;
    this.batches = batches;
  }

  public void check() {
    new TList<Batch>(Batch.Type.instance).check(batches);
  }

  public String toString() {
    MessageStringGenerator gen = new MessageStringGenerator();
    gen.open("window");
    gen.add("start_pos", startPos);
    gen.add("batches", batches);
    gen.close();
    return gen.toString();
  }

  public static class Type extends TUserDef<Window> {
    public static Type instance = new Type();

    private Type() {
      super(2);
    }

    @Override
    public Window create(ArrayValue value) {
      return new Window(TDouble.instance.revert(value.get(0)), new TList<Batch>(
          Batch.Type.instance).revert(value.get(1)));
    }
  }
};
