package impl;

import api.Identifier;
import api.SourceLocation;

public class Property extends NodeImpl {
  boolean method;
  boolean shorthand;
  boolean computed;
  Identifier key;
  String kind;
  AssignmentPattern value;

  public Property(String type, SourceLocation loc, int[] range, boolean method,
    boolean shorthand, boolean computed, Identifier key, String kind,
    AssignmentPattern value) {

    super(type, loc, range);
    this.method = method;
    this.shorthand = shorthand;
    this.computed = computed;
    this.key = key;
    this.kind = kind;
    this.value = value;
  }

  public boolean method() {
    return method;
  }

  public boolean shorthand() {
    return shorthand;
  }

  public boolean computed() {
    return computed;
  }

  public Identifier key() {
    return key;
  }

  public String kind() {
    return kind;
  }

  public AssignmentPattern value() {
    return value;
  }
}
