// This file is auto-generated from burst.idl(0.6.1-34-gb64049d) with jenerator version 0.5.4-224-g49229fa/master
// *** DO NOT EDIT ***

package us.jubat.burst;

import org.msgpack.annotation.Message;
import org.msgpack.type.ArrayValue;
import us.jubat.common.MessageStringGenerator;
import us.jubat.common.UserDefinedMessage;
import us.jubat.common.type.*;

@Message
public class Document implements UserDefinedMessage {
  public double pos = 0;
  public String text = "";

  public Document() {
  }

  public Document(double pos, String text) {
    this.pos = pos;
    this.text = text;
  }

  public void check() {
    TString.instance.check(text);
  }

  public String toString() {
    MessageStringGenerator gen = new MessageStringGenerator();
    gen.open("document");
    gen.add("pos", pos);
    gen.add("text", text);
    gen.close();
    return gen.toString();
  }

  public static class Type extends TUserDef<Document> {
    public static Type instance = new Type();

    private Type() {
      super(2);
    }

    @Override
    public Document create(ArrayValue value) {
      return new Document(TDouble.instance.revert(value.get(0)),
          TString.instance.revert(value.get(1)));
    }
  }
};
