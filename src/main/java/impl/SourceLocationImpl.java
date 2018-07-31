package impl;

import api.Position;
import api.SourceLocation;

public class SourceLocationImpl implements SourceLocation{
  String source;
  Position start;
  Position end;

  public String source() {
    return source;
  }

  public Position start() {
    return start;
  }

  public Position end() {
    return end;
  }

  public class PositionImpl implements Position {
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
