package impl;

import api.Node;
import api.SourceLocation;

public class NodeImpl implements Node {
  String type;
  SourceLocation loc;
  int [] range;

  public NodeImpl(String type, SourceLocation loc, int[] range) {
    this.type = type;
    this.loc = loc;
    this.range = range;
  }

  public String type() {
    return type;
  }

  public SourceLocation loc() {
    return loc;
  }

  public int [] range() {
    return range;
  }

}
