package impl;

import api.Expression;
import api.ExpressionStatement;

public class ExpressionStatementImpl extends StatementImpl implements ExpressionStatement {
  Expression expression;

  public Expression expression() {
    return expression;
  }
}
