package parser;

import api.*;
import com.google.gson.*;
import impl.*;
import org.sonar.javascript.tree.impl.JavaScriptTree;
import org.sonar.javascript.tree.impl.declaration.ModuleTreeImpl;
import org.sonar.javascript.tree.impl.declaration.ScriptTreeImpl;
import org.sonar.javascript.tree.impl.expression.IdentifierTreeImpl;
import org.sonar.javascript.tree.impl.lexical.InternalSyntaxToken;
import org.sonar.javascript.tree.impl.lexical.InternalSyntaxTrivia;
import org.sonar.javascript.tree.impl.statement.ExpressionStatementTreeImpl;
import org.sonar.plugins.javascript.api.tree.Tree;
import org.sonar.plugins.javascript.api.tree.expression.ExpressionTree;
import org.sonar.plugins.javascript.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.javascript.api.tree.lexical.SyntaxTrivia;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class ESLintAST {

  private List<TokenImpl> tokens;

  public Tree parse(String jsonText) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(SourceLocation.class, new MyDeserializer(SourceLocationImpl.class));
    builder.registerTypeAdapter(Position.class, new MyDeserializer(SourceLocationImpl.PositionImpl.class));
    builder.registerTypeAdapter(Statement.class, new MyDeserializer(StatementImpl.class));
    builder.registerTypeAdapter(ExpressionStatement.class, new MyDeserializer(ExpressionStatementImpl.class));
    builder.registerTypeAdapter(Expression.class, new MyDeserializer(IdentifierTreeImpl.class));
    Gson gson = builder.create();
    ProgramImpl program = gson.fromJson(jsonText, ProgramImpl.class);

    this.tokens = program.tokens();
    return convert(program);
  }

  public Tree convert(Node node) {
    JavaScriptTree root = null;
    if (node instanceof ProgramImpl) {
      List<Tree> items = ((Program) node).body().stream()
        .map(this::convert)
        .collect(Collectors.toList());
      root = new ScriptTreeImpl(null, new ModuleTreeImpl(items), null);
    } else if (node instanceof IdentifierImpl) {
      return new IdentifierTreeImpl(Tree.Kind.IDENTIFIER_REFERENCE, toJSToken(node));
    } else if (node instanceof ExpressionStatementImpl) {
      ExpressionTree expression = (ExpressionTree) convert(((ExpressionStatementImpl) node).expression());
      SyntaxTrivia semicolomnToken = toJSToken(node).trivias().stream()
              .filter(syntaxTrivia -> syntaxTrivia.text().equals(";"))
              .findFirst()
              .orElse(null);
      return new ExpressionStatementTreeImpl(expression, semicolomnToken);
    }
    return root;
  }

  private InternalSyntaxToken toJSToken(Node node) {
    List<SyntaxTrivia> trivias = tokens.stream()
      .filter(token -> {
        int start = token.range()[0];
        int end = token.range()[1];
        return node.range()[0] <= start && node.range()[1] >= end;
      })
      .map(token -> new InternalSyntaxTrivia(token.value(), token.loc().start().line(), token.loc().start().column()))
      .collect(Collectors.toList());
    String text = trivias.stream().map(SyntaxToken::text).collect(Collectors.joining());
    return new InternalSyntaxToken(
      node.loc().start().line(),
      node.loc().start().column(),
      text,
      trivias,
      node.range()[0],
      false
    );
  }

  public static class MyDeserializer implements JsonDeserializer {
    private Class<?> implClass;

    public MyDeserializer(Class<?> c) {
      implClass = c;
    }

    @Override
    public Object deserialize(JsonElement json, Type typeOfT,
                              JsonDeserializationContext context) throws JsonParseException {
      return context.deserialize(json, implClass);
    }
  }
}
