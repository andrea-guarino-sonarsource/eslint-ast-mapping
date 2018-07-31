package impl;

import api.SourceLocation;
import api.Token;

public class TokenImpl implements Token {
  String type;
  String value;
  int start;
  int end;
  SourceLocation loc;
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

  public SourceLocation loc() {
    return loc;
  }

  public int[] range() {
    return range;
  }
}
