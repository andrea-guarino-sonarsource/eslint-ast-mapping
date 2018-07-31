package impl;

import api.BlockStatement;
import api.FunctionDeclaration;
import api.Identifier;
import api.Pattern;
import api.SourceLocation;
import java.util.List;

public class FunctionDeclarationImpl extends NodeImpl implements FunctionDeclaration {
  Identifier id;
  List<Pattern> params;
  BlockStatement body;
  boolean generator;
  boolean expression;

  public FunctionDeclarationImpl(String type, SourceLocation loc, int[] range, Identifier id, List<Pattern> params, BlockStatement body, boolean generator) {
    super(type, loc, range);
    this.id = id;
    this.params = params;
    this.body = body;
    this.generator = generator;
  }

  @Override
  public Identifier id() {
    return id;
  }

  @Override
  public List<Pattern> params() {
    return params;
  }

  @Override
  public BlockStatement body() {
    return body;
  }

  @Override
  public boolean generator() {
    return generator;
  }

  @Override
  public boolean expression() {
    return expression;
  }
}
