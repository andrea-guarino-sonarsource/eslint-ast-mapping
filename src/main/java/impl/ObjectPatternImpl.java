package impl;

import api.Pattern;
import api.SourceLocation;
import java.util.List;

public class ObjectPatternImpl extends NodeImpl implements Pattern {
  List<Property> properties;

  public ObjectPatternImpl(String type, SourceLocation loc, int[] range, List<Property> properties) {
    super(type, loc, range);
    this.properties = properties;
  }

  public List<Property> properties() {
    return properties;
  }
}
