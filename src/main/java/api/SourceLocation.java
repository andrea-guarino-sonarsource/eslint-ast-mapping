package api;

public interface SourceLocation {
  String source();
  Position start();
  Position end();
}
