package impl;

public class SourceLocationImpl {
  String source;
  PositionImpl start;
  PositionImpl end;

  public String source() {
    return source;
  }

  public PositionImpl start() {
    return start;
  }

  public PositionImpl end() {
    return end;
  }

  public class PositionImpl {
    int line;
    int column;
    public Integer line() {
      return line;
    }

    public Integer column() {
      return column;
    }
  }
}
