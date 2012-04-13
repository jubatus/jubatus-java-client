
package us.jubat.regression;

import java.util.HashMap;
import java.util.ArrayList;

public class ConverterConfig  {

  public HashMap<String, HashMap<String, String > > string_filter_types;
  public ArrayList<FilterRule  > string_filter_rules;
  public HashMap<String, HashMap<String, String > > num_filter_types;
  public ArrayList<FilterRule  > num_filter_rules;
  public HashMap<String, HashMap<String, String > > string_types;
  public ArrayList<StringRule  > string_rules;
  public HashMap<String, HashMap<String, String > > num_types;
  public ArrayList<NumRule  > num_rules;

  public ConverterConfig() {
  
  }
};
