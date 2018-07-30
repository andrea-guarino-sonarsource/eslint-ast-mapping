package impl;

import java.util.List;

public class ProgramImpl extends NodeImpl {
  String sourceType;
  List<ExpressionStatementImpl> body;

  public List<TokenImpl> tokens() {
    return tokens;
  }

  List<TokenImpl> tokens;


  public List<ExpressionStatementImpl> body() {
    return body;
  }

  public String sourceType() {
    return sourceType;
  }


}
