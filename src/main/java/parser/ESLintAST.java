package parser;

import api.BinaryExpression;
import api.BlockStatement;
import api.Expression;
import api.ExpressionStatement;
import api.FunctionDeclaration;
import api.Identifier;
import api.Node;
import api.Pattern;
import api.Position;
import api.Program;
import api.ReturnStatement;
import api.SourceLocation;
import api.Statement;
import api.Token;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import extension.NativeKindImpl;
import extension.NativeTreeImpl;
import impl.AssignmentPattern;
import impl.BinaryExpressionImpl;
import impl.BlockStatamentImpl;
import impl.CatchClause;
import impl.ExpressionStatementImpl;
import impl.FunctionDeclarationImpl;
import impl.IdentifierImpl;
import impl.Literal;
import impl.ObjectPatternImpl;
import impl.ProgramImpl;
import impl.ReturnStatementImpl;
import impl.SourceLocationImpl;
import impl.TokenImpl;
import impl.TryStatementImpl;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.sonar.javascript.tree.impl.SeparatedListImpl;
import org.sonar.javascript.tree.impl.declaration.FunctionDeclarationTreeImpl;
import org.sonar.javascript.tree.impl.declaration.InitializedBindingElementTreeImpl;
import org.sonar.javascript.tree.impl.declaration.ModuleTreeImpl;
import org.sonar.javascript.tree.impl.declaration.ObjectBindingPatternTreeImpl;
import org.sonar.javascript.tree.impl.declaration.ParameterListTreeImpl;
import org.sonar.javascript.tree.impl.declaration.ScriptTreeImpl;
import org.sonar.javascript.tree.impl.expression.BinaryExpressionTreeImpl;
import org.sonar.javascript.tree.impl.expression.IdentifierTreeImpl;
import org.sonar.javascript.tree.impl.expression.LiteralTreeImpl;
import org.sonar.javascript.tree.impl.lexical.InternalSyntaxToken;
import org.sonar.javascript.tree.impl.lexical.InternalSyntaxTrivia;
import org.sonar.javascript.tree.impl.statement.BlockTreeImpl;
import org.sonar.javascript.tree.impl.statement.CatchBlockTreeImpl;
import org.sonar.javascript.tree.impl.statement.ExpressionStatementTreeImpl;
import org.sonar.javascript.tree.impl.statement.FinallyBlockTreeImpl;
import org.sonar.javascript.tree.impl.statement.ReturnStatementTreeImpl;
import org.sonar.javascript.tree.impl.statement.TryStatementTreeImpl;
import org.sonar.plugins.javascript.api.tree.SeparatedList;
import org.sonar.plugins.javascript.api.tree.Tree;
import org.sonar.plugins.javascript.api.tree.declaration.BindingElementTree;
import org.sonar.plugins.javascript.api.tree.declaration.ParameterListTree;
import org.sonar.plugins.javascript.api.tree.expression.ExpressionTree;
import org.sonar.plugins.javascript.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.javascript.api.tree.lexical.SyntaxTrivia;
import org.sonar.plugins.javascript.api.tree.statement.BlockTree;
import org.sonar.plugins.javascript.api.tree.statement.CatchBlockTree;
import org.sonar.plugins.javascript.api.tree.statement.FinallyBlockTree;
import org.sonar.plugins.javascript.api.tree.statement.StatementTree;

public class ESLintAST {

  private List<Token> tokens;

  public Tree parse(String jsonText) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(SourceLocation.class, new MyDeserializer(SourceLocationImpl.class));
    builder.registerTypeAdapter(Position.class, new MyDeserializer(SourceLocationImpl.PositionImpl.class));
    builder.registerTypeAdapter(Identifier.class, new MyDeserializer(IdentifierImpl.class));
    builder.registerTypeAdapter(BlockStatement.class, new MyDeserializer(BlockStatamentImpl.class));
    builder.registerTypeAdapter(Token.class, new MyDeserializer(TokenImpl.class));

    RuntimeTypeAdapterFactory<Statement> typeAdapterFactoryStatement = RuntimeTypeAdapterFactory
      .of(Statement.class, "type")
      .registerSubtype(ReturnStatementImpl.class, "ReturnStatement")
      .registerSubtype(BlockStatamentImpl.class, "BlockStatement")
      .registerSubtype(ExpressionStatementImpl.class, "ExpressionStatement")
      .registerSubtype(FunctionDeclarationImpl.class, "FunctionDeclaration")
      .registerSubtype(TryStatementImpl.class, "TryStatement");

    RuntimeTypeAdapterFactory<Expression> typeAdapterFactoryExpression = RuntimeTypeAdapterFactory
      .of(Expression.class, "type")
      .registerSubtype(IdentifierImpl.class, "Identifier")
      .registerSubtype(BinaryExpressionImpl.class, "BinaryExpression")
      .registerSubtype(Literal.class, "Literal");

    RuntimeTypeAdapterFactory<Pattern> typeAdapterFactoryPattern = RuntimeTypeAdapterFactory
      .of(Pattern.class, "type")
      .registerSubtype(IdentifierImpl.class, "Identifier")
      .registerSubtype(ObjectPatternImpl.class, "ObjectPattern")
      .registerSubtype(AssignmentPattern.class, "AssignmentPattern");

    builder.registerTypeAdapterFactory(typeAdapterFactoryStatement);
    builder.registerTypeAdapterFactory(typeAdapterFactoryExpression);
    builder.registerTypeAdapterFactory(typeAdapterFactoryPattern);
    Gson gson = builder.create();
    ProgramImpl program = gson.fromJson(jsonText, ProgramImpl.class);

    this.tokens = program.tokens();
    return convert(program);
  }

  public Tree convert(Node node) {
    if (node instanceof Program) {
      List<Tree> items = ((Program) node).body().stream()
        .map(this::convert)
        .collect(Collectors.toList());
      return new ScriptTreeImpl(null, new ModuleTreeImpl(items), null);
    } else if (node instanceof Identifier) {
      return createIdentifier((Identifier) node, Tree.Kind.IDENTIFIER_REFERENCE);
    } else if (node instanceof ExpressionStatement) {
      ExpressionTree expression = (ExpressionTree) convert(((ExpressionStatement) node).expression());
      InternalSyntaxToken semicolomnToken = findTokens(";", node.range()).get(0);
      return new ExpressionStatementTreeImpl(expression, semicolomnToken);
    } else if (node instanceof FunctionDeclaration) {
      return createFunctionDeclaration((FunctionDeclaration) node);
    } else if (node instanceof BlockStatement) {
      return createBlockStatement((BlockStatement) node);
    } else if (node instanceof ReturnStatement) {
      return createReturnStatement((ReturnStatement) node);
    } else if (node instanceof BinaryExpression) {
      return createBinaryExpression((BinaryExpression) node);
    } else if (node instanceof TryStatementImpl) {
      return createTryStatement((TryStatementImpl) node);
    } else if (node instanceof ObjectPatternImpl) {
      return createObjectPattern((ObjectPatternImpl) node);
    } else if (node instanceof Literal) {
      try {
        double d = Double.parseDouble(((Literal) node).value());
      } catch (NumberFormatException e) {
        return new LiteralTreeImpl(Tree.Kind.REGULAR_EXPRESSION_LITERAL, findToken(((Literal) node).value(), node.range()));
      }
      return new LiteralTreeImpl(Tree.Kind.NUMERIC_LITERAL, findToken(((Literal) node).value(), node.range()));

    } else {
      return createNative(node);
    }
  }

  private Tree createObjectPattern(ObjectPatternImpl node) {
    InternalSyntaxToken openParens = findTokens("{", node.range()).get(0);
    List<InternalSyntaxToken> tokens = findTokens("}", node.range());
    InternalSyntaxToken closeParens = tokens.get(tokens.size() - 1);
    List<InitializedBindingElementTreeImpl> bindingElements = node.properties().stream()
      .map(prop -> {
        InternalSyntaxToken equal = findTokens("=", prop.range()).get(0);
        return new InitializedBindingElementTreeImpl(
          createIdentifier(prop.value().left(), Tree.Kind.BINDING_IDENTIFIER), equal, (ExpressionTree) convert(prop.value().right()));
      }).collect(Collectors.toList());
    SeparatedList properties = new SeparatedListImpl(bindingElements, findTokens(",", node.range()));
    return new ObjectBindingPatternTreeImpl(openParens, properties, closeParens);
  }

  private Tree createTryStatement(TryStatementImpl node) {
    InternalSyntaxToken tryKeyword = findTokens("try", node.range()).get(0);
    CatchBlockTree catchBlock = createCatchBlock(node.handler());
    if (catchBlock == null) {
      return createNative(node);
    }
    return new TryStatementTreeImpl(tryKeyword,
      (BlockTree) convert(node.block()),
      catchBlock,
      createFinallyBlock(node.finalizer()));
  }

  private CatchBlockTree createCatchBlock(CatchClause node) {
    if (node.param() == null) {
      return null;
    }
    InternalSyntaxToken catchKeyword = findTokens("catch", node.range()).get(0);
    InternalSyntaxToken openParens = findTokens("(", node.range()).get(0);
    InternalSyntaxToken closeParens = findTokens(")", node.range()).get(0);

    return new CatchBlockTreeImpl(catchKeyword, openParens, createIdentifier(node.param(), Tree.Kind.BINDING_IDENTIFIER), closeParens, (BlockTree) convert(node.body()));
  }

  private FinallyBlockTree createFinallyBlock(CatchClause node) {
    if (node == null) {
      return null;
    }
    InternalSyntaxToken finallyKeyword = findTokens("finally", node.range()).get(0);
    return new FinallyBlockTreeImpl(finallyKeyword, (BlockTree) convert(node.body()));
  }

  private Tree createNative(Node node) {
    List<Tree> children = node.children().stream().map(this::convert).collect(Collectors.toList());
    return new NativeTreeImpl(children, new NativeKindImpl(node));
  }

  private Tree createBinaryExpression(BinaryExpression node) {
    if (node.operator().equals("+")) {
      InternalSyntaxToken plus = findTokens("+", node.range()).get(0);
      return new BinaryExpressionTreeImpl(Tree.Kind.PLUS, (ExpressionTree) convert(node.left()), plus, (ExpressionTree) convert(node.right()));
    }
    return null;
  }

  private Tree createReturnStatement(ReturnStatement node) {
    InternalSyntaxToken returnKeyword = findTokens("return", node.range()).get(0);
    InternalSyntaxToken semicolomnToken = findTokens(";", node.range()).get(0);
    return new ReturnStatementTreeImpl(returnKeyword, (ExpressionTree) convert(node.argument()), semicolomnToken);
  }

  private Tree createBlockStatement(BlockStatement node) {
    InternalSyntaxToken openParens = findTokens("{", node.range()).get(0);
    List<InternalSyntaxToken> tokens = findTokens("}", node.range());
    InternalSyntaxToken closeParens = tokens.get(tokens.size() - 1);
    List<StatementTree> statementTrees = node.body()
      .stream()
      .map(stmt -> (StatementTree) this.convert(stmt))
      .collect(Collectors.toList());
    return new BlockTreeImpl(openParens, statementTrees, closeParens);
  }

  private Tree createFunctionDeclaration(FunctionDeclaration node) {
    InternalSyntaxToken functionKeyword = findTokens("function", node.range()).get(0);
    return FunctionDeclarationTreeImpl.create(
      null, functionKeyword, createIdentifier(node.id(), Tree.Kind.BINDING_IDENTIFIER),
      null, createParameters(node), null, (BlockTree) convert(node.body()));
  }

  private ParameterListTree createParameters(FunctionDeclaration node) {
    List<Pattern> params = node.params();
    int[] range = {node.id().range()[0], node.body().range()[0]};
    InternalSyntaxToken openParens = findTokens("(", range).get(0);
    InternalSyntaxToken closeParens = findTokens(")", range).get(0);
    List<BindingElementTree> parameters = node
      .params().stream()
      .map(this::createParameter)
      .collect(Collectors.toList());
    // TODO: find a better way to find separators
    List<InternalSyntaxToken> tokens = parameters.size() > 1 ? findTokens(",", range) : Collections.emptyList();
    SeparatedListImpl<BindingElementTree> bindingElementTrees = new SeparatedListImpl<>(parameters, tokens);
    return new ParameterListTreeImpl(openParens, bindingElementTrees, closeParens);
  }

  private BindingElementTree createParameter(Pattern pattern) {
    if (pattern instanceof Identifier) {
      return createIdentifier((Identifier) pattern, Tree.Kind.BINDING_IDENTIFIER);
    } else if (pattern instanceof ObjectPatternImpl) {
      return (BindingElementTree) createObjectPattern((ObjectPatternImpl) pattern);
    }
    return null;
  }

  private IdentifierTreeImpl createIdentifier(Identifier node, Tree.Kind kind) {
    return new IdentifierTreeImpl(kind, toJSToken(node));
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

  private InternalSyntaxToken findToken(String name, int[] range) {
    return this.tokens.stream()
      .filter(token -> {
        int start = token.range()[0];
        int end = token.range()[1];
        return range[0] <= start && range[1] >= end && token.value().equals(name);
      })
      .map(token -> new InternalSyntaxToken(
        token.loc().start().line(), token.loc().start().column(), token.value(), Collections.emptyList(), token.range()[0], false))
      .findFirst()
      .orElse(null);
  }

  private List<InternalSyntaxToken> findTokens(String name, int[] range) {
    return this.tokens.stream()
      .filter(token -> {
        int start = token.range()[0];
        int end = token.range()[1];
        return range[0] <= start && range[1] >= end && token.value().equals(name);
      })
      .map(token -> new InternalSyntaxToken(
        token.loc().start().line(), token.loc().start().column(), token.value(), Collections.emptyList(), token.range()[0], false))
      .collect(Collectors.toList());
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
