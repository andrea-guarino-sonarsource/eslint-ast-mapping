package impl;

import api.BlockStatement;
import api.SourceLocation;
import api.Statement;
import java.util.List;

public class BlockStatamentImpl extends NodeImpl implements BlockStatement {
  List<Statement> body;

  public BlockStatamentImpl(String type, SourceLocation loc, int[] range, List<Statement> body) {
    super(type, loc, range);
    this.body = body;
  }

  @Override
  public List<Statement> body() {
    return body;
  }
}
