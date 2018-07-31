package impl;

import api.SourceLocation;

public class ExpressionImpl extends NodeImpl {
  public ExpressionImpl(String type, SourceLocation loc, int[] range) {
    super(type, loc, range);
  }
}
