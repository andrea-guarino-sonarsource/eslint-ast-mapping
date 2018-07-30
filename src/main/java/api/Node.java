package api;

public interface Node {
  String type();
  SourceLocation loc();
  int[] range();
}
