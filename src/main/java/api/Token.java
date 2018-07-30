package api;

public interface Token {
  String type();
  String value();
  int start();
  int end();
  SourceLocation loc();
  int[] range();
}
