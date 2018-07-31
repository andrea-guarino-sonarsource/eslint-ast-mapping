package api;

import java.util.List;

public interface FunctionDeclaration extends Statement {
  Identifier id();
  List<Pattern> params();
  BlockStatement body();
  boolean generator();
  boolean expression();
}
