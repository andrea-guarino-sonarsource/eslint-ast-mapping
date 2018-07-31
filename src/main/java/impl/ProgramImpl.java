package impl;

import api.Program;
import api.SourceLocation;
import api.Statement;
import api.Token;
import java.util.List;

public class ProgramImpl extends NodeImpl implements Program {
  String sourceType;
  List<Statement> body;
  List<Token> tokens;

  public ProgramImpl(String type, SourceLocation loc, int[] range, String sourceType, List<Statement> body, List<Token> tokens) {
    super(type, loc, range);
    this.sourceType = sourceType;
    this.body = body;
    this.tokens = tokens;
  }

  public List<Token> tokens() {
    return tokens;
  }

  public List<Statement> body() {
    return body;
  }

  public String sourceType() {
    return sourceType;
  }


}
