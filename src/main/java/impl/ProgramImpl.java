package impl;

import api.ExpressionStatement;
import api.Program;

import java.util.List;

public class ProgramImpl extends NodeImpl implements Program {
  String sourceType;
  List<ExpressionStatement> body;

  public List<TokenImpl> tokens() {
    return tokens;
  }

  List<TokenImpl> tokens;


  public List<ExpressionStatement> body() {
    return body;
  }

  public String sourceType() {
    return sourceType;
  }


}
