package impl;

import api.Expression;
import api.Identifier;
import api.Pattern;
import api.SourceLocation;

public class AssignmentPattern extends NodeImpl implements Pattern {
  Identifier left;
  Expression right;

  public AssignmentPattern(String type, SourceLocation loc, int[] range, Identifier left, Expression right) {
    super(type, loc, range);
    this.left = left;
    this.right = right;
  }

  public Identifier left() {
    return left;
  }

  public Expression right() {
    return right;
  }
}
