package impl;

public abstract class NodeImpl {
  String type;
  SourceLocationImpl loc;
  int [] range;

  public String type() {
    return type;
  }

  public SourceLocationImpl loc() {
    return loc;
  }

  public int [] range() {
    return range;
  }

}
