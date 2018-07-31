package impl;

import api.Node;
import api.SourceLocation;

public class NodeImpl implements Node {
  String type;
  SourceLocation loc;
  int [] range;

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
