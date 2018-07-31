package impl;

import api.Expression;
import api.ExpressionStatement;
import api.SourceLocation;
import api.Statement;

public class ExpressionStatementImpl extends NodeImpl implements ExpressionStatement, Statement {
  Expression expression;

  public ExpressionStatementImpl(String type, SourceLocation loc, int[] range, Expression expression) {
    super(type, loc, range);
    this.expression = expression;
  }

  public Expression expression() {
    return expression;
  }
}
