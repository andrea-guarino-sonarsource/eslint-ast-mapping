package api;

public interface BinaryExpression extends Expression {
  Expression left();
  String operator();
  Expression right();
}
