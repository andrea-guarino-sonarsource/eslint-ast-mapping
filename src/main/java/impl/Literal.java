package impl;

import api.Expression;
import api.SourceLocation;

public class Literal extends NodeImpl implements Expression {
  String value;
  String raw;

  public Literal(String type, SourceLocation loc, int[] range, String value, String raw) {
    super(type, loc, range);
    this.value = value;
    this.raw = raw;
  }

  public String value() {
    return value;
  }

  public String raw() {
    return raw;
  }
}
