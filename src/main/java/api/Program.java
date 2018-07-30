package api;

import java.util.List;

public interface Program extends Node {
  List<Statement> body();
  String sourceType();
}
