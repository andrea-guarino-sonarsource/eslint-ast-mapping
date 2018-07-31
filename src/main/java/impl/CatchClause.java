package impl;

import api.BlockStatement;
import api.Identifier;
import api.SourceLocation;

public class CatchClause extends NodeImpl {
  Identifier param;
  BlockStatement body;

  public CatchClause(String type, SourceLocation loc, int[] range, Identifier param, BlockStatement body) {
    super(type, loc, range);
    this.param = param;
    this.body = body;
  }

  public Identifier param() {
    return param;
  }

  public BlockStatement body() {
    return body;
  }
}
