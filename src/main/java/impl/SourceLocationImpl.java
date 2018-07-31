package impl;

import api.Position;
import api.SourceLocation;

public class SourceLocationImpl implements SourceLocation{
  String source;
  Position start;
  Position end;

  public SourceLocationImpl(String source, Position start, Position end) {
    this.source = source;
    this.start = start;
    this.end = end;
  }

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

    public PositionImpl(int line, int column) {
      this.line = line;
      this.column = column;
    }

    public Integer line() {
      return line;
    }

    public Integer column() {
      return column;
    }
  }
}
