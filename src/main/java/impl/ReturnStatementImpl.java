package impl;

import api.Expression;
import api.Node;
import api.ReturnStatement;
import api.SourceLocation;
import java.util.Collections;
import java.util.List;

public class ReturnStatementImpl extends NodeImpl implements ReturnStatement {
  Expression argument;

  public ReturnStatementImpl(String type, SourceLocation loc, int[] range, Expression argument) {
    super(type, loc, range);
    this.argument = argument;
  }

  @Override
  public Expression argument() {
    return argument;
  }

  @Override
  public List<Node> children() {
    return Collections.singletonList(argument);
  }
}
