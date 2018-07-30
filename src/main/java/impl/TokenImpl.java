package impl;

public class TokenImpl {
  String type;
  String value;
  int start;
  int end;
  SourceLocationImpl loc;
  int [] range;

  public String type() {
    return type;
  }

  public String value() {
    return value;
  }

  public int start() {
    return start;
  }

  public int end() {
    return end;
  }

  public SourceLocationImpl loc() {
    return loc;
  }

  public int[] range() {
    return range;
  }
}
