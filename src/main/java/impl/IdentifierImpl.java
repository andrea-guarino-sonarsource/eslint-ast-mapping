package impl;

import api.Identifier;
import api.Pattern;
import api.SourceLocation;

public class IdentifierImpl extends ExpressionImpl implements Identifier, Pattern {
  String name;

  public IdentifierImpl(String type, SourceLocation loc, int[] range, String name) {
    super(type, loc, range);
    this.name = name;
  }

  public String name() {
    return name;
  }
}
