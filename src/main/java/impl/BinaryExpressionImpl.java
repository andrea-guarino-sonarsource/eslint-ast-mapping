package impl;

import api.BinaryExpression;
import api.Expression;
import api.SourceLocation;

public class BinaryExpressionImpl extends NodeImpl implements BinaryExpression {
  Expression left;

  public BinaryExpressionImpl(String type, SourceLocation loc, int[] range, Expression left, Expression right, String operator) {
    super(type, loc, range);
    this.left = left;
    this.right = right;
    this.operator = operator;
  }

  Expression right;
  String operator;

  @Override
  public Expression left() {
    return left;
  }

  @Override
  public String operator() {
    return operator;
  }

  @Override
  public Expression right() {
    return right;
  }
}
