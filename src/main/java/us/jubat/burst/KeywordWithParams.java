// This file is auto-generated from burst.idl(0.6.1-34-gb64049d) with jenerator version 0.5.4-224-g49229fa/master
// *** DO NOT EDIT ***

package us.jubat.burst;

import org.msgpack.annotation.Message;
import org.msgpack.type.ArrayValue;
import us.jubat.common.MessageStringGenerator;
import us.jubat.common.UserDefinedMessage;
import us.jubat.common.type.*;

@Message
public class KeywordWithParams implements UserDefinedMessage {
  public String keyword = "";
  public double scalingParam = 0;
  public double gamma = 0;

  public KeywordWithParams() {
  }

  public KeywordWithParams(String keyword, double scalingParam, double gamma) {
    this.keyword = keyword;
    this.scalingParam = scalingParam;
    this.gamma = gamma;
  }

  public void check() {
    TString.instance.check(keyword);
  }

  public String toString() {
    MessageStringGenerator gen = new MessageStringGenerator();
    gen.open("keyword_with_params");
    gen.add("keyword", keyword);
    gen.add("scaling_param", scalingParam);
    gen.add("gamma", gamma);
    gen.close();
    return gen.toString();
  }

  public static class Type extends TUserDef<KeywordWithParams> {
    public static Type instance = new Type();

    private Type() {
      super(3);
    }

    @Override
    public KeywordWithParams create(ArrayValue value) {
      return new KeywordWithParams(TString.instance.revert(value.get(0)),
          TDouble.instance.revert(value.get(1)), TDouble.instance.revert(
          value.get(2)));
    }
  }
};
