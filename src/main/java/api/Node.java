package api;

import java.util.Collections;
import java.util.List;

public interface Node {
  String type();
  SourceLocation loc();
  int[] range();

  default List<? extends Node> children() { return Collections.emptyList(); }
}
