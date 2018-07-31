package impl;

import api.BlockStatement;
import api.Node;
import api.SourceLocation;
import api.Statement;
import java.util.List;

public class TryStatementImpl extends NodeImpl implements Statement {
  BlockStatement block;
  CatchClause handler;
  CatchClause finalizer;

  public BlockStatement block() {
    return block;
  }

  public CatchClause handler() {
    return handler;
  }

  public CatchClause finalizer() {
    return finalizer;
  }

  public TryStatementImpl(String type, SourceLocation loc, int[] range, BlockStatement block, CatchClause handler, CatchClause finalizer) {
    super(type, loc, range);
    this.block = block;
    this.handler = handler;
    this.finalizer = finalizer;
  }

  @Override
  public List<? extends Node> children() {
    return block.body();
  }
}
