package api;

import java.util.List;

public interface BlockStatement extends Statement {
  List<Statement> body();
}
