// This file is auto-generated from burst.idl(0.6.1-34-gb64049d) with jenerator version 0.5.4-224-g49229fa/master
// *** DO NOT EDIT ***

package us.jubat.burst;

import java.util.Map;
import java.util.List;

import java.net.UnknownHostException;
import us.jubat.common.ClientBase;
import us.jubat.common.type.*;

public class BurstClient extends ClientBase {

  public BurstClient(String host, int port, String name,
      int timeoutSec) throws UnknownHostException {
    super(host, port, name, timeoutSec);
  }

  public int addDocuments(List<Document> data) {
    new TList<Document>(Document.Type.instance).check(data);
    return this.call("add_documents", TInt.instance, data);
  }

  public Window getResult(String keyword) {
    TString.instance.check(keyword);
    return this.call("get_result", Window.Type.instance, keyword);
  }

  public Window getResultAt(String keyword, double pos) {
    TString.instance.check(keyword);
    return this.call("get_result_at", Window.Type.instance, keyword, pos);
  }

  public Map<String, Window> getAllBurstedResults() {
    return this.call("get_all_bursted_results", new TMap<String, Window>(
        TString.instance, Window.Type.instance));
  }

  public Map<String, Window> getAllBurstedResultsAt(double pos) {
    return this.call("get_all_bursted_results_at", new TMap<String, Window>(
        TString.instance, Window.Type.instance), pos);
  }

  public List<KeywordWithParams> getAllKeywords() {
    return this.call("get_all_keywords", new TList<KeywordWithParams>(
        KeywordWithParams.Type.instance));
  }

  public boolean addKeyword(KeywordWithParams keyword) {
    KeywordWithParams.Type.instance.check(keyword);
    return this.call("add_keyword", TBool.instance, keyword);
  }

  public boolean removeKeyword(String keyword) {
    TString.instance.check(keyword);
    return this.call("remove_keyword", TBool.instance, keyword);
  }

  public boolean removeAllKeywords() {
    return this.call("remove_all_keywords", TBool.instance);
  }

}
