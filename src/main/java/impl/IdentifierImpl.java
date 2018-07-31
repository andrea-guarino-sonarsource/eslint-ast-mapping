package impl;

import api.Identifier;

public class IdentifierImpl extends ExpressionImpl implements Identifier{
  String name;
  public String name() {
    return name;
  }
}
